package com.raft.Main;

import com.raft.RaftRPCService.RaftNodeService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    private final Server grpcServer;
    public GrpcServer(int port, RaftNodeService raftNodeService)
    {
        grpcServer = ServerBuilder.forPort(port)
                // 将具体实现的服务添加到gRPC服务中
                .addService(raftNodeService)
                .build();
    }

    public void startGrpcServer() throws IOException
    {
        grpcServer.start();
    }

    public void shutdown()
    {
        grpcServer.shutdown();
    }

    public void blockUntilShutdown() throws InterruptedException
    {
        if (grpcServer != null) {
            grpcServer.awaitTermination();
        }
    }
}
