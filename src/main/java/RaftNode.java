import Entity.NodeRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaftNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftNode.class);
    private final NodeRole nodeRole = NodeRole.FOLLOWER;
    private StateMachine stateMachine;
    private int leaderId;
    // Persistent
    private long currentTerm;
    private int votedFor;
    // Volatile
    private volatile long commitIndex;
    private volatile long lastAppliedIndex;


}
