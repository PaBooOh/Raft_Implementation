package com.raft.Main;

import com.raft.Entity.RaftServer;
import com.raft.ProtoBuf.RaftNodeServiceGrpc;
import com.raft.ProtoBuf.RaftRPC;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTest.class);

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.printf("Using maven command like this: CLUSTER COMMAND\n");
            System.exit(-1);
        }

        // parse args
        String clusterString = args[0];
        String[] serversString = clusterString.split(",");
        String command = args[1]; // stored in Entry format

        List<RaftRPC.Server> cluster = new ArrayList<>();
        for (String serverString : serversString)
        {
            // build cluster
            RaftRPC.Server server = ServerTest.getServer(serversString, serverString);
            cluster.add(server);
        }
        // Connect to the cluster by randomly select a server
        Random random = new Random();
        RaftRPC.Server randomServer =  cluster.get(random.nextInt(cluster.size()));
        // Create the channel between client and cluster
        ManagedChannel channel = ManagedChannelBuilder.forAddress(randomServer.getHost(), randomServer.getPort())
                .usePlaintext()
                .build();
        RaftRPC.ClientRequest request = RaftRPC.ClientRequest.newBuilder().setCommand(command).build();

        RaftNodeServiceGrpc.RaftNodeServiceBlockingStub blockingStub = RaftNodeServiceGrpc.newBlockingStub(channel);
        RaftRPC.ClientReply reply = blockingStub.clientRequestRPC(request);

        boolean isLeader = reply.getIsLeader();
        int leaderId = reply.getLeaderId();

        // Redirect, if the server just connected to is not the leader server.
        if(!isLeader && leaderId != 0)
        {
            LOGGER.info("Client request [Redirect]>>> The server belonging to the raft cluster is not leader. Redirecting to the leader...");
            ManagedChannel leaderChannel = ManagedChannelBuilder.forAddress(randomServer.getHost(), randomServer.getPort())
                    .usePlaintext()
                    .build();
            RaftNodeServiceGrpc.RaftNodeServiceBlockingStub newBlockingStub = RaftNodeServiceGrpc.newBlockingStub(leaderChannel);
            RaftRPC.ClientReply newReply = newBlockingStub.clientRequestRPC(request);
            leaderChannel.shutdown();
        }
        if(leaderId == 0)
        {
            LOGGER.info("Client request [Failed]>>> Client is requesting a command to the raft cluster in which there is no leader elected currently. Please try again.");
            return;
        }
        channel.shutdown();
        LOGGER.info("Client request [Success]>>> This command (request) has been stored in servers' log and has been applied to statemachine.");
        System.exit(0);
    }
}
