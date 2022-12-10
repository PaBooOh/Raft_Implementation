package Entity.ACK;

public class ACKAppendEntries {

    private int term;

    private boolean ack;

    public ACKAppendEntries(int term, boolean ack) {
        this.term = term;
        this.ack = ack;
    }

    public int getTerm() {
        return term;
    }

    public boolean isAck() {
        return ack;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }
}
