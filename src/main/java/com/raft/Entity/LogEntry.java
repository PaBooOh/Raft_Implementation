package com.raft.Entity;

public class LogEntry {

    private long index;

    private long term;

    private String command;


    LogEntry(long term, long index, String command) {
        this.term = term;
        this.index = index;
        this.command = command;
    }

    public long getIndex() {
        return index;
    }

    public long getTerm() {
        return term;
    }

    public String getCommand() {
        return command;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public void setTerm(long term) {
        this.term = term;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
