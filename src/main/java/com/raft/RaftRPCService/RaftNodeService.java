package com.raft.RaftRPCService;

import com.raft.Entity.NodeRole;
import com.raft.Entity.RaftServer;
import com.raft.ProtoBuf.RaftNodeServiceGrpc;
import com.raft.ProtoBuf.RaftRPC;
import io.grpc.stub.StreamObserver;
import org.slf4j.*;

import java.util.ArrayList;
import java.util.List;

public class RaftNodeService extends RaftNodeServiceGrpc.RaftNodeServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftNodeService.class);
    private final RaftServer receiver;
    public RaftNodeService(RaftServer raftServer) {this.receiver = raftServer;} // Constructor

    // Leader Election
    @Override
    public void requestVoteRPC(RaftRPC.VoteRequest candidateRequest, StreamObserver<RaftRPC.VoteReply> responseObserver)
    {
        // lock
        receiver.getLock().lock();
        try {
            RaftRPC.VoteReply.Builder responseBuilder = RaftRPC.VoteReply.newBuilder();
            long candidateTerm = candidateRequest.getCandidateTerm();
            long receiverTerm = receiver.getCurrentTerm();
            long candidateLastLogTerm = candidateRequest.getLastLogTerm();
            long candidateLastLogIndex = candidateRequest.getLastLogIndex();
            long receiverLastLogTerm = receiver.getStateMachine().getLastLogTerm();
            long receiverLastLogIndex = receiver.getStateMachine().getLastLogIndex();
            int receiverVotedFor = receiver.getVotedFor();
            boolean logMatchingSafety = candidateLastLogTerm > receiverLastLogTerm
                    || (candidateLastLogTerm == receiverLastLogTerm && candidateLastLogIndex >= receiverLastLogIndex);

            responseBuilder.setCurrentTerm(receiverTerm);
            // If candidate's term < receiver's term
            if (candidateTerm < receiverTerm)
            {
                responseBuilder.setVoteGranted(false);
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
            //
            if(candidateTerm > receiverTerm &&
                    (receiver.getNodeRole() == NodeRole.CANDIDATE || receiver.getNodeRole() == NodeRole.LEADER))
            {
                receiver.stepDown(candidateRequest.getCandidateTerm());
            }


            if(receiverVotedFor == 0) // 0 not yet voted, the server(ID) it voted for otherwise
            {
                // Agree to vote
                if(logMatchingSafety)
                {
                    // receiver.stepDown(candidateTerm);
                    receiver.setVotedFor(candidateRequest.getCandidateId());
                    responseBuilder.setVoteGranted(true);

                }
                // Disagree to vote
                else
                {
                    responseBuilder.setVoteGranted(false);
                }
            }
            // Disagree to vote since this receiver has already voted for other servers (or itself)
            else
            {
                responseBuilder.setVoteGranted(false);
            }
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } finally {
            receiver.getLock().unlock();
        }
    }

    @Override
    public void appendEntriesRPC(RaftRPC.AppendEntriesRequest request, StreamObserver<RaftRPC.AppendEntriesReply> responseObserver)
    {
        receiver.getLock().lock();
        try {
            RaftRPC.AppendEntriesReply.Builder responseBuilder = RaftRPC.AppendEntriesReply.newBuilder();
            long leaderTerm = request.getLeaderTerm();
            long receiverTerm = receiver.getCurrentTerm();
            long receiverId = receiver.getServerId();
            long leaderCommit = request.getLeaderCommit();
            NodeRole receiverRole = receiver.getNodeRole();
            boolean isHeartbeat = request.getLogEntriesCount() == 0;
            long leaderPrevLogIndex = request.getPrevLogIndex();
            long receiverLastLogIndex = receiver.getStateMachine().getLastLogIndex();
            String rpcType = isHeartbeat? "ReceiveHeartbeat":"ReceiveEntries";

            // Safety check 01
            // Leader with smaller term is forced to step down
            if(leaderTerm < receiverTerm)
            {
                LOGGER.info("[{}] {}-[SafetyCheck-StepDown] >>> Server (ServerId={}, ServerTerm={}) has greater term than leader's ({})",
                        receiverRole.toString(),
                        rpcType,
                        receiverId,
                        receiverTerm,
                        leaderTerm);
                responseBuilder
                        .setCurrentTerm(receiverTerm)
                        .setSuccess(false)
                        .build();
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
            // Step down if receiver is either candidate or leader, while for followers, they need to step down as well (but just update the info itself)
            else
            {
                if(receiverRole == NodeRole.CANDIDATE || receiverRole == NodeRole.LEADER)
                {
                    LOGGER.info("[{}] {}-[SafetyCheck-StepDown] >>> Server (ServerId={}, ServerTerm={}) is stepping down, since a new leader has arisen.",
                            receiverRole,
                            rpcType,
                            receiverId,
                            receiverTerm);
                }
                receiver.stepDown(leaderTerm);
            }
            // Agree with the leader's RPC
            receiver.setLeaderId(request.getLeaderId());
            // Safety check 02
            if(leaderPrevLogIndex > receiverLastLogIndex)
            {
                LOGGER.info("[{}] {}-[Mismatch] >>> Server (ServerId={}, ServerTerm={}) 's log does not match that of leader.",
                        receiverRole,
                        rpcType,
                        receiverId,
                        receiverTerm);
                responseBuilder
                        .setCurrentTerm(receiverTerm)
                        .setSuccess(false)
                        .build();
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
            // Heartbeat
            if(isHeartbeat)
            {
                LOGGER.info("[{}] {}-[Success] >>> Server (ServerId={}, ServerTerm={}) has received heartbeat. The latest entry's (Index={}, Term={}) Command is <{}> ",
                        receiverRole.toString(),
                        rpcType,
                        receiverId,
                        receiverTerm,
                        receiver.getStateMachine().getLastLogIndex(),
                        receiver.getStateMachine().getLastLogTerm(),
                        receiver.getStateMachine().getLastLogCommand());
            }
            // AppendEntries, added into local LogContainer (stored in the in-memory for simplicity)
            else
            {
                List<RaftRPC.LogEntry> logEntriesFromRequest = request.getLogEntriesList();
                receiver.getStateMachine().getLogContainer().addAll(logEntriesFromRequest);
                LOGGER.info("[{}] {}-[Success] >>> Server (ServerId={}, ServerTerm={}) has added entries provided. The latest entry's (Index={}, Term={}) Command={}> ",
                        receiverRole.toString(),
                        rpcType,
                        receiverId,
                        receiverTerm,
                        receiver.getStateMachine().getLastLogIndex(),
                        receiver.getStateMachine().getLastLogTerm(),
                        receiver.getStateMachine().getLastLogCommand());
            }
            responseBuilder
                    .setCurrentTerm(receiverTerm)
                    .setSuccess(true)
                    .build();
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }finally {
            receiver.getLock().unlock();
        }
    }

    @Override
    public void clientRequestRPC(RaftRPC.ClientRequest request, StreamObserver<RaftRPC.ClientReply> responseObserver) {
        receiver.getLock().lock();
        try {
            RaftRPC.ClientReply.Builder responseBuilder = RaftRPC.ClientReply.newBuilder();
            int receiverServerId = receiver.getServerId();
            int leaderServerId = receiver.getLeaderId();

            responseBuilder.setLeaderId(leaderServerId);

            // if the receiver is not the leader
            if(receiverServerId != leaderServerId)
            {
                responseBuilder
                        .setIsLeader(false)
                        .build();
            }
            // if the receiver is the leader
            else
            {
                RaftRPC.LogEntry logEntry = RaftRPC.LogEntry.newBuilder()
                        .setCommand(request.getCommand())
                        .setTerm(receiver.getCurrentTerm())
                        .setIndex(receiver.getStateMachine().getLastLogIndex() + 1)
                        .build();
                List<RaftRPC.LogEntry> logEntries = new ArrayList<>();
                logEntries.add(logEntry);
                receiver.sendAppendEntriesToOtherServers(logEntries);
                responseBuilder
                        .setIsLeader(true)
                        .build();
            }
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }finally {
            receiver.getLock().unlock();
        }
    }
}
