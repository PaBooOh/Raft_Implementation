package RaftRPC;

import Entity.ACK.ACKRequestVote;
import Entity.RPC.RPCRequestVote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRaftRPC extends Remote {

    public ACKRequestVote requestVote(RPCRequestVote requestVote) throws RemoteException;
}
