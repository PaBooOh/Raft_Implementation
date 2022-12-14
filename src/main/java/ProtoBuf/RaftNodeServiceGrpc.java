package ProtoBuf;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Services
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
  public static final io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.VoteRequest,
      ProtoBuf.RaftRPC.VoteReply> METHOD_REQUEST_VOTE_RPC = getRequestVoteRPCMethod();

  private static volatile io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.VoteRequest,
      ProtoBuf.RaftRPC.VoteReply> getRequestVoteRPCMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.VoteRequest,
      ProtoBuf.RaftRPC.VoteReply> getRequestVoteRPCMethod() {
    io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.VoteRequest, ProtoBuf.RaftRPC.VoteReply> getRequestVoteRPCMethod;
    if ((getRequestVoteRPCMethod = RaftNodeServiceGrpc.getRequestVoteRPCMethod) == null) {
      synchronized (RaftNodeServiceGrpc.class) {
        if ((getRequestVoteRPCMethod = RaftNodeServiceGrpc.getRequestVoteRPCMethod) == null) {
          RaftNodeServiceGrpc.getRequestVoteRPCMethod = getRequestVoteRPCMethod = 
              io.grpc.MethodDescriptor.<ProtoBuf.RaftRPC.VoteRequest, ProtoBuf.RaftRPC.VoteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "raft.RaftNodeService", "RequestVoteRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ProtoBuf.RaftRPC.VoteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ProtoBuf.RaftRPC.VoteReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftNodeServiceMethodDescriptorSupplier("RequestVoteRPC"))
                  .build();
          }
        }
     }
     return getRequestVoteRPCMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAppendEntriesRPCMethod()} instead. 
  public static final io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.AppendEntriesRequest,
      ProtoBuf.RaftRPC.AppendEntriesReply> METHOD_APPEND_ENTRIES_RPC = getAppendEntriesRPCMethod();

  private static volatile io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.AppendEntriesRequest,
      ProtoBuf.RaftRPC.AppendEntriesReply> getAppendEntriesRPCMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.AppendEntriesRequest,
      ProtoBuf.RaftRPC.AppendEntriesReply> getAppendEntriesRPCMethod() {
    io.grpc.MethodDescriptor<ProtoBuf.RaftRPC.AppendEntriesRequest, ProtoBuf.RaftRPC.AppendEntriesReply> getAppendEntriesRPCMethod;
    if ((getAppendEntriesRPCMethod = RaftNodeServiceGrpc.getAppendEntriesRPCMethod) == null) {
      synchronized (RaftNodeServiceGrpc.class) {
        if ((getAppendEntriesRPCMethod = RaftNodeServiceGrpc.getAppendEntriesRPCMethod) == null) {
          RaftNodeServiceGrpc.getAppendEntriesRPCMethod = getAppendEntriesRPCMethod = 
              io.grpc.MethodDescriptor.<ProtoBuf.RaftRPC.AppendEntriesRequest, ProtoBuf.RaftRPC.AppendEntriesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "raft.RaftNodeService", "AppendEntriesRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ProtoBuf.RaftRPC.AppendEntriesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ProtoBuf.RaftRPC.AppendEntriesReply.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftNodeServiceMethodDescriptorSupplier("AppendEntriesRPC"))
                  .build();
          }
        }
     }
     return getAppendEntriesRPCMethod;
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
   * Services
   * </pre>
   */
  public static abstract class RaftNodeServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void requestVoteRPC(ProtoBuf.RaftRPC.VoteRequest request,
        io.grpc.stub.StreamObserver<ProtoBuf.RaftRPC.VoteReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestVoteRPCMethod(), responseObserver);
    }

    /**
     */
    public void appendEntriesRPC(ProtoBuf.RaftRPC.AppendEntriesRequest request,
        io.grpc.stub.StreamObserver<ProtoBuf.RaftRPC.AppendEntriesReply> responseObserver) {
      asyncUnimplementedUnaryCall(getAppendEntriesRPCMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestVoteRPCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ProtoBuf.RaftRPC.VoteRequest,
                ProtoBuf.RaftRPC.VoteReply>(
                  this, METHODID_REQUEST_VOTE_RPC)))
          .addMethod(
            getAppendEntriesRPCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ProtoBuf.RaftRPC.AppendEntriesRequest,
                ProtoBuf.RaftRPC.AppendEntriesReply>(
                  this, METHODID_APPEND_ENTRIES_RPC)))
          .build();
    }
  }

  /**
   * <pre>
   * Services
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
    public void requestVoteRPC(ProtoBuf.RaftRPC.VoteRequest request,
        io.grpc.stub.StreamObserver<ProtoBuf.RaftRPC.VoteReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestVoteRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void appendEntriesRPC(ProtoBuf.RaftRPC.AppendEntriesRequest request,
        io.grpc.stub.StreamObserver<ProtoBuf.RaftRPC.AppendEntriesReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAppendEntriesRPCMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Services
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
    public ProtoBuf.RaftRPC.VoteReply requestVoteRPC(ProtoBuf.RaftRPC.VoteRequest request) {
      return blockingUnaryCall(
          getChannel(), getRequestVoteRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public ProtoBuf.RaftRPC.AppendEntriesReply appendEntriesRPC(ProtoBuf.RaftRPC.AppendEntriesRequest request) {
      return blockingUnaryCall(
          getChannel(), getAppendEntriesRPCMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Services
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
    public com.google.common.util.concurrent.ListenableFuture<ProtoBuf.RaftRPC.VoteReply> requestVoteRPC(
        ProtoBuf.RaftRPC.VoteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestVoteRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ProtoBuf.RaftRPC.AppendEntriesReply> appendEntriesRPC(
        ProtoBuf.RaftRPC.AppendEntriesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAppendEntriesRPCMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST_VOTE_RPC = 0;
  private static final int METHODID_APPEND_ENTRIES_RPC = 1;

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
          serviceImpl.requestVoteRPC((ProtoBuf.RaftRPC.VoteRequest) request,
              (io.grpc.stub.StreamObserver<ProtoBuf.RaftRPC.VoteReply>) responseObserver);
          break;
        case METHODID_APPEND_ENTRIES_RPC:
          serviceImpl.appendEntriesRPC((ProtoBuf.RaftRPC.AppendEntriesRequest) request,
              (io.grpc.stub.StreamObserver<ProtoBuf.RaftRPC.AppendEntriesReply>) responseObserver);
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
      return ProtoBuf.RaftRPC.getDescriptor();
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
              .build();
        }
      }
    }
    return result;
  }
}
