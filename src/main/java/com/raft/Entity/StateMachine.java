package com.raft.Entity;

import com.raft.ProtoBuf.RaftRPC;

import java.util.ArrayList;
import java.util.List;
//import


public class StateMachine {

    private List<RaftRPC.LogEntry> logContainer = new ArrayList<>();

    public List<RaftRPC.LogEntry> getLogContainer()
    {
        return logContainer;
    }

    public void setLogContainer(List<RaftRPC.LogEntry> logContainer)
    {
        this.logContainer = logContainer;
    }

    public void appendLogEntry(RaftRPC.LogEntry logEntry)
    {
        this.logContainer.add(logEntry);
    }

    public RaftRPC.LogEntry getLastLog()
    {
        return logContainer.get(logContainer.size() - 1);
    }

    public long getLastLogIndex()
    {
        if (!logContainer.isEmpty())
        {
            return getLastLog().getIndex();
        }
        return 0;
    }

    public long getLastLogTerm()
    {
        if (!logContainer.isEmpty())
        {
            return getLastLog().getTerm();
        }
        return 0;
    }

    public long getLogEntryTermByIndex(long entryIndex)
    {
        if(entryIndex == 0L || logContainer.isEmpty())
        {
            return 0L;
        }
        return logContainer.get((int) entryIndex).getTerm();
    }
}
