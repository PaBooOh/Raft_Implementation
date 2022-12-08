package Entity.ACK;

public class ACKRequestVote {

    private int term;

    private boolean voteGranted;

    public ACKRequestVote(int term, boolean voteGranted) {
        this.term = term;
        this.voteGranted = voteGranted;
    }

    public int getTerm() {
        return term;
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setVoteGranted(boolean voteGranted) {
        this.voteGranted = voteGranted;
    }
}
