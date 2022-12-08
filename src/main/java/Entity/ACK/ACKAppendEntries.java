package Entity.ACK;

public class ACKAppendEntries {

    private int term;

    private boolean success;

    public ACKAppendEntries(int term, boolean success) {
        this.term = term;
        this.success = success;
    }

    public int getTerm() {
        return term;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
