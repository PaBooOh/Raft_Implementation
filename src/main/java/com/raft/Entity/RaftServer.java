package com.raft.Entity;

import com.raft.ProtoBuf.RaftNodeServiceGrpc;
import com.raft.ProtoBuf.RaftRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private ScheduledExecutorService scheduledExecutorService; // RPC task that needs to send RPCs to other nodes in parallel
    private ScheduledFuture electionScheduledFuture; // Timer task
    private ScheduledFuture heartbeatScheduledFuture; // Timer task
    private ConcurrentMap<Integer, RaftRPC.Server> serverMap = new ConcurrentHashMap<>();
    // Key: serverId (i.e followerId), Value: lastLogIndex(leader sent a log to this follower previously)
    private ConcurrentMap<Integer, Long> logMatchMap = new ConcurrentHashMap<>();
    private StateMachine stateMachine;
    private int leaderId;
    private int serverId;
    private int votedCount;
    private int ackCount;

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
            this.logMatchMap.put(server.getServerId(), 0L);
        }
        this.cluster = clusterBuilder.build();
        this.localServer = localServer;
        this.serverId = localServer.getServerId();
        this.stateMachine = new StateMachine();
        this.votedCount = 0;
        this.leaderId = 0;
        this.votedFor = 0;
        this.currentTerm = 0;
        this.commitIndex = 0;
        this.lastAppliedIndex = 0;
        this.ackCount = 0;
    }

    public void buildRaftServer()
    {
        LOGGER.info("Building cluster >>> Server (ServerId={}, ServerTerm={}) has been started", localServer.getServerId(), getCurrentTerm());
        executorService = new ThreadPoolExecutor(
                raftConfiguration.getThreadPoolSize(),
                raftConfiguration.getThreadPoolSize(),
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        startElectionTimeout(); // start timeout, if not receive heartbeatRPC in timeout ms, then starts leader election
    }

    // Timer, counting down... Will start election unless it receives heartbeat
    private void startElectionTimeout()
    {
        // interrupt thread
        if (electionScheduledFuture != null && !electionScheduledFuture.isDone())
        {
            electionScheduledFuture.cancel(true);
        }
        electionScheduledFuture = scheduledExecutorService.schedule(this::requestVoteFromOtherServers, getElectionTimeoutMs(), TimeUnit.MILLISECONDS);
    }

    // Multi-threads is applied to issue multiple requestVote RPCs to other raft nodes.
    private void requestVoteFromOtherServers()
    {
        long startTime = System.currentTimeMillis();
        lock.lock();
        try {
            setCurrentTerm(getCurrentTerm() + 1);
            setNodeRole(NodeRole.CANDIDATE);
            setLeaderId(0);
            setVotedFor(localServer.getServerId()); // vote for itself
            LOGGER.info("[{}] Leader Election >>> Server (ServerId={}, ServerTerm={}) become candidate and try to canvass from other servers",
                    getNodeRole().toString(),
                    localServer.getServerId(),
                    getCurrentTerm());
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

        long endTime = System.currentTimeMillis();
        if(getNodeRole() == NodeRole.LEADER)
        {
            long durationTime = endTime - startTime;
            LOGGER.info("[{}] Leader Election >>> Duration of the leader election={} ms",
                    getNodeRole().toString(),
                    durationTime);
        }
    }

    private void issueRequestVoteRPC(int targetServerId, String targetServerHost, int targetServerPort)
    {
//        LOGGER.info("[{}] Leader Election >>> Candidate (ServerId={}, ServerHost={}, ServerPort={}) issued RequestVoteRPC to a server (ServerId={}, ServerHost={}, ServerPort={})",
//                getNodeRole().toString(),
//                localServer.getServerId(),
//                localServer.getHost(),
//                localServer.getPort(),
//                targetServerId,
//                targetServerHost,
//                targetServerPort);
        RaftRPC.VoteRequest.Builder requestBuilder = RaftRPC.VoteRequest.newBuilder();
        RaftRPC.VoteReply reply = null;
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
            reply = blockingStub.requestVoteRPC(request);
            long receiverTerm = reply.getCurrentTerm();
            long candidateTerm = getCurrentTerm();
            boolean receiverVoteGranted = reply.getVoteGranted();

            // Candidate steps down whenever this happens.
            if (candidateTerm < receiverTerm)
            {
                LOGGER.info("[{}] Leader Election-[SafetyCheck-StepDown]>>> Server (ServerId={}, ServerTerm={}) steps down ... because of receiver (ServerId={}, ServerTerm={}) with greater term.",
                        getNodeRole().toString(),
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
                    LOGGER.info("[{}] Leader Election-[Granted] >>> Server (ServerId={}, ServerTerm={}) got vote granted from receiver (ServerId={}, ServerTerm={}). Current votedCount={}.",
                            getNodeRole().toString(),
                            localServer.getServerId(),
                            candidateTerm,
                            targetServerId,
                            receiverTerm, getVotedCount());
                    if(getVotedCount() > cluster.getServersCount()/2)
                    {
                        LOGGER.info("[{}] Leader Election-[Elected] >>> Server (ServerId={}, ServerTerm={}) got votes from majority and is about to transitions to leader state.",
                                getNodeRole().toString(),
                                localServer.getServerId(),
                                candidateTerm);
                        setNodeRole(NodeRole.LEADER);
                        setLeaderId(localServer.getServerId());
                        setVotedCount(0);
                        // No need to request vote because have received the majority of servers, i.e., already become leader
                        if (electionScheduledFuture != null && !electionScheduledFuture.isDone()) {
                            electionScheduledFuture.cancel(true);
                        }
                        sendAppendEntriesToOtherServers(new ArrayList<>()); // Leader starts normal operation (empty entries)
                    }
                }
                else
                {
                    LOGGER.info("[{}] Leader Election-[Denied] >>> Server (ServerId={}, ServerTerm={}) got vote denied from receiver (ServerId={}, ServerTerm={}). Current votedCount={}.",
                            getNodeRole().toString(),
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

    // Only for leader: AppendEntries or Heartbeat
    public void sendAppendEntriesToOtherServers(List<RaftRPC.LogEntry> logEntries)
    {
        // It is the first time that the new leader issue heartbeatRPCs (appendEntriesRPC with empty entry) to its followers
        boolean isHeartbeat = logEntries.isEmpty();

        if(!isHeartbeat)
        {
            setAckCount(0);
        }

        for (RaftRPC.Server server: cluster.getServersList())
        {
            int targetServerId = server.getServerId();
            String targetServerHost = server.getHost();
            int targetServerPort = server.getPort();
            if(targetServerId == localServer.getServerId())
                continue;
            executorService.submit(() -> issueAppendEntriesRPC(targetServerId, targetServerHost, targetServerPort, logEntries));
        }

        if(getAckCount() > cluster.getServersCount()/2 && !isHeartbeat)
        {
            setAckCount(0);
            LOGGER.info("[{}] Statemachine-[Applied] >>> Leader (ServerId={}, ServerTerm={}) got acknowledges from majority so apply this entry to its statemachine.",
                    getNodeRole().toString(),
                    localServer.getServerId(),
                    getCurrentTerm());
        }

        if(isHeartbeat)
        {
            // reissue heartbeatRPCs
            if (heartbeatScheduledFuture != null && !heartbeatScheduledFuture.isDone())
            {
                heartbeatScheduledFuture.cancel(true);
            }
            heartbeatScheduledFuture = scheduledExecutorService.schedule(() -> sendAppendEntriesToOtherServers(new ArrayList<>()), raftConfiguration.getHeartbeatInterval(), TimeUnit.MILLISECONDS);
        }
    }

    // Except for leader, i.e., only for followers
    private long getElectionTimeoutMs()
    {
        Random random = new Random();
        int minElectionTimeout = raftConfiguration.getElectionTimeout();
        int randomElectionTimeout = random.nextInt(2*minElectionTimeout+1) + minElectionTimeout;
        LOGGER.info("[{}] ElectionTimeout ({} ms) >>> Server (ServerId={}, ServerTerm={}) will initiate election if not receive heartbeat",
                getNodeRole().toString(),
                randomElectionTimeout,
                localServer.getServerId(),
                getCurrentTerm());
        return randomElectionTimeout;
    }

    private void issueAppendEntriesRPC(int targetServerId, String targetServerHost, int targetServerPort, List<RaftRPC.LogEntry> logEntries)
    {
        lock.lock();
        try {
            long prevLogIndex = logMatchMap.get(targetServerId);
            long prevLogTerm = getStateMachine().getLogEntryTermByIndex(prevLogIndex);
            boolean isHeartbeat = logEntries.isEmpty();
            RaftRPC.AppendEntriesRequest.Builder requestBuilder = RaftRPC.AppendEntriesRequest.newBuilder()
                    .setLeaderId(localServer.getServerId())
                    .setLeaderTerm(getCurrentTerm())
                    .setPrevLogIndex(prevLogIndex)
                    .setPrevLogTerm(prevLogTerm)
                    .setLeaderCommit(commitIndex);
            // This is not heartbeat but appendEntries
            if(!isHeartbeat)
            {
                for(RaftRPC.LogEntry logEntry: logEntries)
                {
                    requestBuilder.addLogEntries(logEntry);
                }
            }
            RaftRPC.AppendEntriesRequest request = requestBuilder.build();
            // Build the channel between leader and follower
            ManagedChannel channel = ManagedChannelBuilder.forAddress(targetServerHost, targetServerPort)
                    .usePlaintext()
                    .build();
            RaftNodeServiceGrpc.RaftNodeServiceBlockingStub blockingStub = RaftNodeServiceGrpc.newBlockingStub(channel);
            RaftRPC.AppendEntriesReply reply = blockingStub.appendEntriesRPC(request);
            String rpcType = isHeartbeat ? "Heartbeat":"AppendEntries";
//            LOGGER.info("[{}] Normal operation-[{}] >>> Server (ServerId={}, ServerTerm={}) is issuing AppendEntriesRPCs to its followers ...",
//                    getNodeRole().toString(),
//                    rpcType,
//                    localServer.getServerId(),
//                    getCurrentTerm());
            long receiverTerm = reply.getCurrentTerm();
            long leaderTerm = getCurrentTerm();
            boolean logMatchSafety = reply.getSuccess();

            // Check valid: leader maintain its position
            if(leaderTerm >= receiverTerm)
            {
                // Append successfully
                if(logMatchSafety)
                {
                    // 1 below stands for the size of entries[] (but here is a simple implementation that only appends one entry)
                    LOGGER.info("[{}] Normal operation-[{}] >>> Server (ServerId={}, ServerTerm={}) has successfully issued the AppendEntriesRPCs to follower (ServerId={}, ServerTerm={})",
                            getNodeRole().toString(),
                            rpcType,
                            localServer.getServerId(),
                            leaderTerm,
                            targetServerId,
                            receiverTerm);
                    if(!isHeartbeat)
                    {
                        setAckCount(getAckCount()+1);
                    }
//                    logMatchMap.put(targetServerId, logMatchMap.get(targetServerId) + 1);
                }
                // Append unsuccessfully (Log Dis-matching)
                else
                {
                    LOGGER.debug("[{}] Normal operation-[{}] >>> Leader (ServerId={}, ServerTerm={})'s Log does not match with that of a follower (ServerId={}, ServerTerm={})",
                            getNodeRole().toString(),
                            rpcType,
                            localServer.getServerId(),
                            leaderTerm,
                            targetServerId,
                            receiverTerm);
                }
            }
            // Step down
            else
            {
                stepDown(receiverTerm);
            }
        } finally {
            lock.unlock();
        }
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void stepDown(long newTerm)
    {
//        LOGGER.info("Step down >>> Candidate/Leader (ServerId={}, ServerTerm={}) starts stepping down ... since receive a RPC returning greater term than itself",
//                localServer.getServerId(),
//                getCurrentTerm());
        setNodeRole(NodeRole.FOLLOWER); // Step down
        setCurrentTerm(newTerm);
        setLeaderId(0);
        setVotedFor(0);
        setVotedCount(0);
        // If the server that just stepped down is the leader, then stop sending heartbeat RPCs
        if (heartbeatScheduledFuture != null && !heartbeatScheduledFuture.isDone()) {
            heartbeatScheduledFuture.cancel(true);
        }
        // Follower have to use a random timer
        startElectionTimeout();
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

    public long getCommitIndex() {
        return commitIndex;
    }

    public int getAckCount() {
        return ackCount;
    }

    public void setAckCount(int ackCount) {
        this.ackCount = ackCount;
    }
}