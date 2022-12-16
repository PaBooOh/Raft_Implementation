package com.raft.ProtoBuf;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Raft Services
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.1)",
    comments = "Source: RaftRPC.proto")
public final class RaftNodeServiceGrpc {

  private RaftNodeServiceGrpc() {}

  public static final String SERVICE_NAME = "raft.RaftNodeService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRequestVoteRPCMethod()} instead. 
  public static final io.grpc.MethodDescriptor<RaftRPC.VoteRequest,
          RaftRPC.VoteReply> METHOD_REQUEST_VOTE_RPC = getRequestVoteRPCMethod();

  private static volatile io.grpc.MethodDescriptor<RaftRPC.VoteRequest,
          RaftRPC.VoteReply> getRequestVoteRPCMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<RaftRPC.VoteRequest,
          RaftRPC.VoteReply> getRequestVoteRPCMethod() {
    io.grpc.MethodDescriptor<RaftRPC.VoteRequest, RaftRPC.VoteReply> getRequestVoteRPCMethod;
    if ((getRequestVoteRPCMethod = RaftNodeServiceGrpc.getRequestVoteRPCMethod) == null) {
      synchronized (RaftNodeServiceGrpc.class) {
        if ((getRequestVoteRPCMethod = RaftNodeServiceGrpc.getRequestVoteRPCMethod) == null) {
          RaftNodeServiceGrpc.getRequestVoteRPCMethod = getRequestVoteRPCMethod = 
              io.grpc.MethodDescriptor.<RaftRPC.VoteRequest, RaftRPC.VoteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "raft.RaftNodeService", "RequestVoteRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RaftRPC.VoteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RaftRPC.VoteReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftNodeServiceMethodDescriptorSupplier("RequestVoteRPC"))
                  .build();
          }
        }
     }
     return getRequestVoteRPCMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAppendEntriesRPCMethod()} instead. 
  public static final io.grpc.MethodDescriptor<RaftRPC.AppendEntriesRequest,
          RaftRPC.AppendEntriesReply> METHOD_APPEND_ENTRIES_RPC = getAppendEntriesRPCMethod();

  private static volatile io.grpc.MethodDescriptor<RaftRPC.AppendEntriesRequest,
          RaftRPC.AppendEntriesReply> getAppendEntriesRPCMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<RaftRPC.AppendEntriesRequest,
          RaftRPC.AppendEntriesReply> getAppendEntriesRPCMethod() {
    io.grpc.MethodDescriptor<RaftRPC.AppendEntriesRequest, RaftRPC.AppendEntriesReply> getAppendEntriesRPCMethod;
    if ((getAppendEntriesRPCMethod = RaftNodeServiceGrpc.getAppendEntriesRPCMethod) == null) {
      synchronized (RaftNodeServiceGrpc.class) {
        if ((getAppendEntriesRPCMethod = RaftNodeServiceGrpc.getAppendEntriesRPCMethod) == null) {
          RaftNodeServiceGrpc.getAppendEntriesRPCMethod = getAppendEntriesRPCMethod = 
              io.grpc.MethodDescriptor.<RaftRPC.AppendEntriesRequest, RaftRPC.AppendEntriesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "raft.RaftNodeService", "AppendEntriesRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RaftRPC.AppendEntriesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RaftRPC.AppendEntriesReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftNodeServiceMethodDescriptorSupplier("AppendEntriesRPC"))
                  .build();
          }
        }
     }
     return getAppendEntriesRPCMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getClientRequestRPCMethod()} instead. 
  public static final io.grpc.MethodDescriptor<RaftRPC.ClientRequest,
          RaftRPC.ClientReply> METHOD_CLIENT_REQUEST_RPC = getClientRequestRPCMethod();

  private static volatile io.grpc.MethodDescriptor<RaftRPC.ClientRequest,
          RaftRPC.ClientReply> getClientRequestRPCMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<RaftRPC.ClientRequest,
          RaftRPC.ClientReply> getClientRequestRPCMethod() {
    io.grpc.MethodDescriptor<RaftRPC.ClientRequest, RaftRPC.ClientReply> getClientRequestRPCMethod;
    if ((getClientRequestRPCMethod = RaftNodeServiceGrpc.getClientRequestRPCMethod) == null) {
      synchronized (RaftNodeServiceGrpc.class) {
        if ((getClientRequestRPCMethod = RaftNodeServiceGrpc.getClientRequestRPCMethod) == null) {
          RaftNodeServiceGrpc.getClientRequestRPCMethod = getClientRequestRPCMethod = 
              io.grpc.MethodDescriptor.<RaftRPC.ClientRequest, RaftRPC.ClientReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "raft.RaftNodeService", "ClientRequestRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RaftRPC.ClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RaftRPC.ClientReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftNodeServiceMethodDescriptorSupplier("ClientRequestRPC"))
                  .build();
          }
        }
     }
     return getClientRequestRPCMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RaftNodeServiceStub newStub(io.grpc.Channel channel) {
    return new RaftNodeServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RaftNodeServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RaftNodeServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RaftNodeServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RaftNodeServiceFutureStub(channel);
  }

  /**
   * <pre>
   * Raft Services
   * </pre>
   */
  public static abstract class RaftNodeServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void requestVoteRPC(RaftRPC.VoteRequest request,
                               io.grpc.stub.StreamObserver<RaftRPC.VoteReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestVoteRPCMethod(), responseObserver);
    }

    /**
     */
    public void appendEntriesRPC(RaftRPC.AppendEntriesRequest request,
                                 io.grpc.stub.StreamObserver<RaftRPC.AppendEntriesReply> responseObserver) {
      asyncUnimplementedUnaryCall(getAppendEntriesRPCMethod(), responseObserver);
    }

    /**
     */
    public void clientRequestRPC(RaftRPC.ClientRequest request,
                                 io.grpc.stub.StreamObserver<RaftRPC.ClientReply> responseObserver) {
      asyncUnimplementedUnaryCall(getClientRequestRPCMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestVoteRPCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                      RaftRPC.VoteRequest,
                      RaftRPC.VoteReply>(
                  this, METHODID_REQUEST_VOTE_RPC)))
          .addMethod(
            getAppendEntriesRPCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                      RaftRPC.AppendEntriesRequest,
                      RaftRPC.AppendEntriesReply>(
                  this, METHODID_APPEND_ENTRIES_RPC)))
          .addMethod(
            getClientRequestRPCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                      RaftRPC.ClientRequest,
                      RaftRPC.ClientReply>(
                  this, METHODID_CLIENT_REQUEST_RPC)))
          .build();
    }
  }

  /**
   * <pre>
   * Raft Services
   * </pre>
   */
  public static final class RaftNodeServiceStub extends io.grpc.stub.AbstractStub<RaftNodeServiceStub> {
    private RaftNodeServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftNodeServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftNodeServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftNodeServiceStub(channel, callOptions);
    }

    /**
     */
    public void requestVoteRPC(RaftRPC.VoteRequest request,
                               io.grpc.stub.StreamObserver<RaftRPC.VoteReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestVoteRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void appendEntriesRPC(RaftRPC.AppendEntriesRequest request,
                                 io.grpc.stub.StreamObserver<RaftRPC.AppendEntriesReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAppendEntriesRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void clientRequestRPC(RaftRPC.ClientRequest request,
                                 io.grpc.stub.StreamObserver<RaftRPC.ClientReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getClientRequestRPCMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Raft Services
   * </pre>
   */
  public static final class RaftNodeServiceBlockingStub extends io.grpc.stub.AbstractStub<RaftNodeServiceBlockingStub> {
    private RaftNodeServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftNodeServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftNodeServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftNodeServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public RaftRPC.VoteReply requestVoteRPC(RaftRPC.VoteRequest request) {
      return blockingUnaryCall(
          getChannel(), getRequestVoteRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public RaftRPC.AppendEntriesReply appendEntriesRPC(RaftRPC.AppendEntriesRequest request) {
      return blockingUnaryCall(
          getChannel(), getAppendEntriesRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public RaftRPC.ClientReply clientRequestRPC(RaftRPC.ClientRequest request) {
      return blockingUnaryCall(
          getChannel(), getClientRequestRPCMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Raft Services
   * </pre>
   */
  public static final class RaftNodeServiceFutureStub extends io.grpc.stub.AbstractStub<RaftNodeServiceFutureStub> {
    private RaftNodeServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftNodeServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftNodeServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftNodeServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<RaftRPC.VoteReply> requestVoteRPC(
        RaftRPC.VoteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestVoteRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<RaftRPC.AppendEntriesReply> appendEntriesRPC(
        RaftRPC.AppendEntriesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAppendEntriesRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<RaftRPC.ClientReply> clientRequestRPC(
        RaftRPC.ClientRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getClientRequestRPCMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST_VOTE_RPC = 0;
  private static final int METHODID_APPEND_ENTRIES_RPC = 1;
  private static final int METHODID_CLIENT_REQUEST_RPC = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RaftNodeServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RaftNodeServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_VOTE_RPC:
          serviceImpl.requestVoteRPC((RaftRPC.VoteRequest) request,
              (io.grpc.stub.StreamObserver<RaftRPC.VoteReply>) responseObserver);
          break;
        case METHODID_APPEND_ENTRIES_RPC:
          serviceImpl.appendEntriesRPC((RaftRPC.AppendEntriesRequest) request,
              (io.grpc.stub.StreamObserver<RaftRPC.AppendEntriesReply>) responseObserver);
          break;
        case METHODID_CLIENT_REQUEST_RPC:
          serviceImpl.clientRequestRPC((RaftRPC.ClientRequest) request,
              (io.grpc.stub.StreamObserver<RaftRPC.ClientReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RaftNodeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RaftNodeServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RaftRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RaftNodeService");
    }
  }

  private static final class RaftNodeServiceFileDescriptorSupplier
      extends RaftNodeServiceBaseDescriptorSupplier {
    RaftNodeServiceFileDescriptorSupplier() {}
  }

  private static final class RaftNodeServiceMethodDescriptorSupplier
      extends RaftNodeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RaftNodeServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RaftNodeServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RaftNodeServiceFileDescriptorSupplier())
              .addMethod(getRequestVoteRPCMethod())
              .addMethod(getAppendEntriesRPCMethod())
              .addMethod(getClientRequestRPCMethod())
              .build();
        }
      }
    }
    return result;
  }
}
