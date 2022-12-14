package Entity;

public class RaftConfiguration {

    // Milliseconds
    private int electionTimeout = 5000; // 5s
    private int heartbeatInterval = 500; // 0.5s
    // Thread
    private int threadPoolSize = 20;

    public int getElectionTimeout() {
        return electionTimeout;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setElectionTimeout(int electionTimeout) {
        this.electionTimeout = electionTimeout;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }


}
