package RaftRPCService;

import Entity.RaftServer;
import ProtoBuf.RaftNodeServiceGrpc;
import io.grpc.stub.StreamObserver;
import ProtoBuf.RaftRPC.*;
import org.slf4j.*;

public class RaftNodeService extends RaftNodeServiceGrpc.RaftNodeServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftNodeService.class);
    private final RaftServer receiver;
    public RaftNodeService(RaftServer raftServer) {this.receiver = raftServer;} // Constructor

    // Voting
    @Override
    public void requestVoteRPC(VoteRequest candidateRequest, StreamObserver<VoteReply> responseObserver) {
        // lock
        receiver.getLock().lock();
        try {
            VoteReply.Builder responseBuilder = VoteReply.newBuilder();


            long candidateTerm = candidateRequest.getCandidateTerm();
            long receiverTerm = receiver.getCurrentTerm();
            responseBuilder.setCurrentTerm(receiverTerm);
            // If candidate is fake, i.e., not in the Raft cluster
//            if (!ConfigurationUtils.containsServer(raftServer.getConfiguration(), candidateRequest.getCandidateId())) {
//                responseBuilder.setVoteGranted(false);
//                responseObserver.onNext(responseBuilder.build());
//                responseObserver.onCompleted();
//            }
            // If candidate's term < receiver's term
            if (candidateTerm < receiver.getCurrentTerm()) {
                responseBuilder.setVoteGranted(false);
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
            }
            //
            if (candidateRequest.getCandidateTerm() > receiver.getCurrentTerm())
            {
                receiver.stepDown(candidateRequest.getCandidateTerm());
            }

            // Count votes
            long candidateLastLogTerm = candidateRequest.getLastLogTerm();
            long candidateLastLogIndex = candidateRequest.getLastLogIndex();
            long receiverLastLogTerm = receiver.getStateMachine().getLastLogTerm();
            long receiverLastLogIndex = receiver.getStateMachine().getLastLogIndex();
            boolean logMatchingSafety = candidateLastLogTerm > receiverLastLogTerm
                    || (candidateLastLogTerm == receiverLastLogTerm && candidateLastLogIndex >= receiverLastLogIndex);

            if (receiver.getVotedFor() == 0 && logMatchingSafety)
            {
                receiver.stepDown(candidateTerm);
                receiver.setVotedFor(candidateRequest.getCandidateId());
//                raftServer.getRaftLog().updateMetaData(raftServer.getCurrentTerm(), raftServer.getVotedFor(), null, null);
                responseBuilder.setVoteGranted(true);
                responseBuilder.setCurrentTerm(receiver.getCurrentTerm());
            }
            LOGGER.info("Leader Election Reply >>> Candidate (ServerId={}, ServerTerm={}) request a vote from server (ServerId={}, ServerTerm={}) and granted={}",
                    candidateRequest.getCandidateId(), candidateRequest.getCandidateTerm(), receiver.getServerId(),
                    receiver.getCurrentTerm(), responseBuilder.getVoteGranted());
        } finally {
            receiver.getLock().unlock();
        }
    }
}
