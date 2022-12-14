package com.raft.Entity;

import com.raft.ProtoBuf.RaftNodeServiceGrpc;
import com.raft.ProtoBuf.RaftRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RaftServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftServer.class);
    private NodeRole nodeRole = NodeRole.FOLLOWER;
    private final RaftConfiguration raftConfiguration = new RaftConfiguration();
    private final Lock lock = new ReentrantLock();
    private final RaftRPC.Server localServer;
    private final RaftRPC.Cluster cluster;

    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture electionScheduledFuture;
    private ConcurrentMap<Integer, RaftRPC.Server> serverMap = new ConcurrentHashMap<>();

    private StateMachine stateMachine;

    private int leaderId;
    private int serverId;
    private int votedCount;

    // Persistent
    private long currentTerm;
    private int votedFor;
    // Volatile
    private volatile long commitIndex;
    private volatile long lastAppliedIndex;

    // Constructor
    public RaftServer(List<RaftRPC.Server> cluster,
                      RaftRPC.Server localServer)
    {
        RaftRPC.Cluster.Builder clusterBuilder = RaftRPC.Cluster.newBuilder();
        for (RaftRPC.Server server : cluster) {
            clusterBuilder.addServers(server);
            this.serverMap.put(server.getServerId(), server);
        }
        this.cluster = clusterBuilder.build();
        this.localServer = localServer;
        this.serverId = localServer.getServerId();
        this.stateMachine = new StateMachine();
        this.votedCount = 0;
        this.leaderId = 0;
        this.votedFor = 0;
        this.currentTerm = 0;
    }

    public void buildRaftServer()
    {
        executorService = new ThreadPoolExecutor(
                raftConfiguration.getThreadPoolSize(),
                raftConfiguration.getThreadPoolSize(),
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        initiateLeaderElection(); // start leader election
    }

    // Multi-threads is applied to issue multiple requestVote RPCs to other raft nodes.
    private void initiateLeaderElection()
    {
        System.out.println("'initiateLeaderElection");
        LOGGER.info("Building cluster >>> Server (ServerId={}, ServerTerm={}) has been started", localServer.getServerId(), getCurrentTerm());
        if (electionScheduledFuture != null && !electionScheduledFuture.isDone()) {
            electionScheduledFuture.cancel(true); // interrupt thread
        }
        electionScheduledFuture = scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                requestVote();
            }
        }, getElectionTimeoutMs(), TimeUnit.MILLISECONDS);
    }

    //
    private void requestVote()
    {
        lock.lock();
        try {
            System.out.println("'requestVote");
            setCurrentTerm(getCurrentTerm() + 1);
            LOGGER.info("Leader Election >>> Candidate (ServerId={}, ServerTerm={}) become candidate and try to canvass ...", localServer.getServerId(), getCurrentTerm());
            setNodeRole(NodeRole.CANDIDATE);
            setLeaderId(0);
            setVotedFor(localServer.getServerId()); // vote for itself
        } finally {
            lock.unlock();
        }
        // issue requestVoteRPCs in parallel to other RaftNodes
        for (RaftRPC.Server server : cluster.getServersList())
        {
            // request vote except itself
            int targetServerId = server.getServerId();
            String targetServerHost = server.getHost();
            int targetServerPort = server.getPort();
            if (targetServerId == localServer.getServerId())
                continue;
            executorService.submit(() -> issueRequestVoteRPC(targetServerId, targetServerHost, targetServerPort));
        }
    }

    private void issueRequestVoteRPC(int targetServerId, String targetServerHost, int targetServerPort)
    {
        LOGGER.info("Leader Election >>> Start issue RequestVoteRPC ...");
        RaftRPC.VoteRequest.Builder requestBuilder = RaftRPC.VoteRequest.newBuilder();
        lock.lock();
        try {
            
            requestBuilder.setCandidateId(localServer.getServerId())
                    .setCandidateTerm(getCurrentTerm())
                    .setLastLogIndex(getStateMachine().getLastLogIndex())
                    .setLastLogTerm(getStateMachine().getLastLogTerm());

            ManagedChannel channel = ManagedChannelBuilder.forAddress(targetServerHost, targetServerPort)
                    .usePlaintext()
                    .build();
            RaftNodeServiceGrpc.RaftNodeServiceBlockingStub blockingStub = RaftNodeServiceGrpc.newBlockingStub(channel);
            RaftRPC.VoteRequest request = requestBuilder.build();
            RaftRPC.VoteReply reply = blockingStub.requestVoteRPC(request);
            long receiverTerm = reply.getCurrentTerm();
            long candidateTerm = getCurrentTerm();
            boolean receiverVoteGranted = reply.getVoteGranted();

            // Candidate steps down whenever this happens.
            if (candidateTerm < receiverTerm)
            {
                LOGGER.info("Leader Election >>> Candidate (ServerId={}, ServerTerm={}) steps down ... because of receiver (ServerId={}, ServerTerm={}) with greater term",
                        localServer.getServerId(),
                        candidateTerm,
                        targetServerId,
                        receiverTerm);
                stepDown(receiverTerm);
            }
            else
            {
                if(receiverVoteGranted)
                {
                    setVotedCount(getVotedCount() + 1);
                    LOGGER.info("Leader Election [Granted] >>> Candidate (ServerId={}, ServerTerm={}) get vote granted from receiver (ServerId={}, ServerTerm={}). Current votedCount={}",
                            localServer.getServerId(),
                            candidateTerm,
                            targetServerId,
                            receiverTerm, getVotedCount());
                    if(getVotedCount() > cluster.getServersCount()/2)
                    {
                        LOGGER.info("Leader Election [Win] >>> Candidate (ServerId={}, ServerTerm={}) gets votes from majority and transitions to leader state!!!",
                                localServer.getServerId(),
                                candidateTerm);
                        setNodeRole(NodeRole.LEADER);
                        setLeaderId(localServer.getServerId());
                        if (electionScheduledFuture != null && !electionScheduledFuture.isDone()) {
                            electionScheduledFuture.cancel(true);
                        }
                    }
                }
                else
                {
                    LOGGER.info("Leader Election [Denied] >>> Candidate (ServerId={}, ServerTerm={}) get vote denied from receiver (ServerId={}, ServerTerm={}). Current votedCount={}",
                            localServer.getServerId(),
                            candidateTerm,
                            targetServerId,
                            receiverTerm, getVotedCount());
                }
            }
        } finally {
            lock.unlock();
        }



    }

    private long getElectionTimeoutMs()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomElectionTimeout = raftConfiguration.getElectionTimeout()
                + random.nextInt(0, raftConfiguration.getElectionTimeout());
        LOGGER.info("This server (ServerId={}, ServerTerm={}) will become candidate and initiate election after {} ms",
                localServer.getServerId(),
                getCurrentTerm(),
                randomElectionTimeout);
        return randomElectionTimeout;
    }

//    private RaftRPC.AppendEntriesReply appendEntriesRequest(RaftRPC.AppendEntriesRequest)
//    {
//
//    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void stepDown(long newTerm)
    {
        LOGGER.info("Step down >>> Candidate/Leader (ServerId={}, ServerTerm={}) starts stepping down ... since receive a RPC returning greater term than itself",
                localServer.getServerId(),
                getCurrentTerm());
        if (currentTerm < newTerm)
        {
            setCurrentTerm(newTerm);
            setLeaderId(0);
            setVotedFor(0);
        }
        setNodeRole(NodeRole.FOLLOWER); // Step down
        // stop heartbeat
//        if (heartbeatScheduledFuture != null && !heartbeatScheduledFuture.isDone()) {
//            heartbeatScheduledFuture.cancel(true);
//        }
        initiateLeaderElection();
    }



    // get lock
    public Lock getLock() {
        return lock;
    }

    public long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public NodeRole getNodeRole() {
        return nodeRole;
    }

    public void setNodeRole(NodeRole nodeRole) {
        this.nodeRole = nodeRole;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public int getVotedFor() {
        return votedFor;
    }

    public int getVotedCount() {
        return votedCount;
    }

    public void setVotedCount(int votedCount) {
        this.votedCount = votedCount;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public void setVotedFor(int votedFor) {
        this.votedFor = votedFor;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
