package com.raft.Main;

import com.raft.Entity.RaftServer;
import com.raft.ProtoBuf.RaftRPC;
import com.raft.RaftRPCService.RaftNodeService;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2)
        {
            System.out.print("Usage: ./run_server.sh CLUSTER CURRENT_NODE\n");
            System.exit(-1);
        }

//        String dataPath = args[0];
        // "host1:port1,host2:port2,host3:port3"
        String clusterString = args[0];
        String localServerString = args[1];
        LOGGER.info("Building cluster >>> Starting a server (" + localServerString + ") in cluster(" + clusterString + ")");
        String[] serversString = clusterString.split(",");
        RaftRPC.Server localServer = getServer(serversString, localServerString);

        // Split and parse cluster into separate servers
        List<RaftRPC.Server> cluster = new ArrayList<>();
        List<RaftRPC.Server> peer = new ArrayList<>();
        for (String serverString : serversString)
        {
            // build cluster
            RaftRPC.Server server = getServer(serversString, serverString);
            cluster.add(server);

            // build peer
            if(server.getServerId() == localServer.getServerId())
                continue;
            peer.add(server);
        }

        // Create local server
        RaftServer raftServer = new RaftServer(cluster, localServer);
        RaftNodeService raftNodeService = new RaftNodeService(raftServer); // For leader election and append entry

        // Build RPCServer to provide services
        GrpcServer grpcServer = new GrpcServer(localServer.getPort(), raftNodeService);
        grpcServer.startGrpcServer();
        // Wait for the cluster built completely
        waitPeersAvailable(peer);
        // Build RaftServer as client that communicates with other RaftServers
        raftServer.buildRaftServer();
    }

    private static void waitPeersAvailable(List<RaftRPC.Server> peer) throws InterruptedException {
        for(RaftRPC.Server server: peer)
        {
            String targetServerHost = server.getHost();
            int targetServerPort = server.getPort();
            ManagedChannel channel = ManagedChannelBuilder.forAddress(targetServerHost, targetServerPort)
                    .usePlaintext()
                    .build();

            ConnectivityState channelState = channel.getState(true);
            // Try to reconnect
            while(channelState != ConnectivityState.READY)
            {
                LOGGER.info("Connection State={}, waiting for Server={} joining", channel.getState(true), targetServerHost);
                TimeUnit.SECONDS.sleep(1);
                channelState = channel.getState(true);
            }
            LOGGER.info("Connection State={}, connected to Server={}", channel.getState(true), targetServerHost);
        }
        LOGGER.info("Cluster built successfully!");
    }


    private static RaftRPC.Server getServer(String[] serversString, String serverString) {

        int serverId = getServerId(serversString, serverString);
        if (serverId == 0) {
            System.out.print("Cluster given did not match local server: " + serverString);
            System.exit(-1);
        }
        String[] splitServer = serverString.split(":");
        String host = splitServer[0];
        int port = Integer.parseInt(splitServer[1]);

        RaftRPC.Server.Builder serverBuilder = RaftRPC.Server.newBuilder();
        return serverBuilder
                .setServerId(serverId)
                .setHost(host)
                .setPort(port)
                .build();
    }

    private static int getServerId(String[] serversString, String serverString)
    {
        int serverId = 0;
        for (int i=0; i<serversString.length; i++)
        {
            if(serversString[i].equals(serverString))
            {
                serverId = i+1;
                break;
            }
        }
        return serverId;
    }
}
