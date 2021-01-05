package protos;

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
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.13.1)",
    comments = "Source: TaxiRideProto.proto")
public final class TaxiServiceGrpc {

  private TaxiServiceGrpc() {}

  public static final String SERVICE_NAME = "protos.TaxiService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRequest,
      protos.TaxiRideProto.UserRequest> getUserMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRequest,
      protos.TaxiRideProto.UserRequest> getUserMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRequest, protos.TaxiRideProto.UserRequest> getUserMethod;
    if ((getUserMethod = TaxiServiceGrpc.getUserMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getUserMethod = TaxiServiceGrpc.getUserMethod) == null) {
          TaxiServiceGrpc.getUserMethod = getUserMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.UserRequest, protos.TaxiRideProto.UserRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "User"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.UserRequest.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("User"))
                  .build();
          }
        }
     }
     return getUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.RideRequest,
      protos.TaxiRideProto.RideRequest> getRideMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.RideRequest,
      protos.TaxiRideProto.RideRequest> getRideMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.RideRequest, protos.TaxiRideProto.RideRequest> getRideMethod;
    if ((getRideMethod = TaxiServiceGrpc.getRideMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getRideMethod = TaxiServiceGrpc.getRideMethod) == null) {
          TaxiServiceGrpc.getRideMethod = getRideMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.RideRequest, protos.TaxiRideProto.RideRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "Ride"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.RideRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.RideRequest.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("Ride"))
                  .build();
          }
        }
     }
     return getRideMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TaxiServiceStub newStub(io.grpc.Channel channel) {
    return new TaxiServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TaxiServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TaxiServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TaxiServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TaxiServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TaxiServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void user(protos.TaxiRideProto.UserRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getUserMethod(), responseObserver);
    }

    /**
     */
    public void ride(protos.TaxiRideProto.RideRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getRideMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.UserRequest,
                protos.TaxiRideProto.UserRequest>(
                  this, METHODID_USER)))
          .addMethod(
            getRideMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.RideRequest,
                protos.TaxiRideProto.RideRequest>(
                  this, METHODID_RIDE)))
          .build();
    }
  }

  /**
   */
  public static final class TaxiServiceStub extends io.grpc.stub.AbstractStub<TaxiServiceStub> {
    private TaxiServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiServiceStub(channel, callOptions);
    }

    /**
     */
    public void user(protos.TaxiRideProto.UserRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ride(protos.TaxiRideProto.RideRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRideMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TaxiServiceBlockingStub extends io.grpc.stub.AbstractStub<TaxiServiceBlockingStub> {
    private TaxiServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public protos.TaxiRideProto.UserRequest user(protos.TaxiRideProto.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), getUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.RideRequest ride(protos.TaxiRideProto.RideRequest request) {
      return blockingUnaryCall(
          getChannel(), getRideMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TaxiServiceFutureStub extends io.grpc.stub.AbstractStub<TaxiServiceFutureStub> {
    private TaxiServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.UserRequest> user(
        protos.TaxiRideProto.UserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.RideRequest> ride(
        protos.TaxiRideProto.RideRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRideMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_USER = 0;
  private static final int METHODID_RIDE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TaxiServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TaxiServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_USER:
          serviceImpl.user((protos.TaxiRideProto.UserRequest) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserRequest>) responseObserver);
          break;
        case METHODID_RIDE:
          serviceImpl.ride((protos.TaxiRideProto.RideRequest) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRequest>) responseObserver);
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

  private static abstract class TaxiServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TaxiServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.TaxiRideProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TaxiService");
    }
  }

  private static final class TaxiServiceFileDescriptorSupplier
      extends TaxiServiceBaseDescriptorSupplier {
    TaxiServiceFileDescriptorSupplier() {}
  }

  private static final class TaxiServiceMethodDescriptorSupplier
      extends TaxiServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TaxiServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (TaxiServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TaxiServiceFileDescriptorSupplier())
              .addMethod(getUserMethod())
              .addMethod(getRideMethod())
              .build();
        }
      }
    }
    return result;
  }
}
