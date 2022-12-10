package RaftRPC;

import Entity.ACK.ACKRequestVote;
import Entity.RPC.RPCRequestVote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RaftRPC extends UnicastRemoteObject implements IRaftRPC{

    private int serverID;

    RaftRPC(int _serverID) throws RemoteException
    {
        this.serverID = _serverID;
    }

    public ACKRequestVote requestVote(RPCRequestVote requestVote) throws RemoteException
    {
        long candidateTerm = requestVote.getCandidateTerm();
        ACKRequestVote ackRequestVote = new ACKRequestVote(candidateTerm, true);
        return ackRequestVote;
    }

}
