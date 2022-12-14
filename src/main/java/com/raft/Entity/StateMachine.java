package com.raft.Entity;

import com.raft.ProtoBuf.RaftRPC;

import java.util.ArrayList;
import java.util.List;
//import


public class StateMachine {

    private List<RaftRPC.LogEntry> logContainer = new ArrayList<>();

    public List<RaftRPC.LogEntry> getLogContainer() {
        return logContainer;
    }

    public void setLogContainer(List<RaftRPC.LogEntry> logContainer) {
        this.logContainer = logContainer;
    }

    public void appendLogEntry(RaftRPC.LogEntry logEntry)
    {
        logContainer.add(logEntry);
    }

    public long getLastLogIndex()
    {
        if (!logContainer.isEmpty())
        {
            return logContainer.get(logContainer.size() - 1).getIndex();
        }
        return 0;
    }

    public long getLastLogTerm()
    {
        if (!logContainer.isEmpty())
        {
            return logContainer.get(logContainer.size() - 1).getTerm();
        }
        return 0;
    }
}
