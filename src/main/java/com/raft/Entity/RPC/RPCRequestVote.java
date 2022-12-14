package com.raft.Entity.RPC;

public class RPCRequestVote {

    private long candidateTerm;
    private int candidateId;
    private int lastLogIndex;
    private int lastLogTerm;

    public RPCRequestVote(int candidateTerm, int candidateId, int lastLogIndex, int lastLogTerm) {
        this.candidateTerm = candidateTerm;
        this.candidateId = candidateId;
        this.lastLogIndex = lastLogIndex;
        this.lastLogTerm = lastLogTerm;
    }

    public long getCandidateTerm() {
        return candidateTerm;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public int getLastLogIndex() {
        return lastLogIndex;
    }

    public int getLastLogTerm() {
        return lastLogTerm;
    }

    public void setCandidateTerm(int candidateTerm) {
        this.candidateTerm = candidateTerm;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public void setLastLogIndex(int lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public void setLastLogTerm(int lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }
}
