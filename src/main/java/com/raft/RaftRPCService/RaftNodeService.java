package com.raft.RaftRPCService;

import com.raft.Entity.NodeRole;
import com.raft.Entity.RaftServer;
import com.raft.ProtoBuf.RaftNodeServiceGrpc;
import com.raft.ProtoBuf.RaftRPC;
import io.grpc.stub.StreamObserver;
import org.slf4j.*;

public class RaftNodeService extends RaftNodeServiceGrpc.RaftNodeServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftNodeService.class);
    private final RaftServer receiver;
    public RaftNodeService(RaftServer raftServer) {this.receiver = raftServer;} // Constructor

    // Voting
    @Override
    public void requestVoteRPC(RaftRPC.VoteRequest candidateRequest, StreamObserver<RaftRPC.VoteReply> responseObserver) {
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
}
