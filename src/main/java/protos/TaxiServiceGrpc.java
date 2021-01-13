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
      protos.TaxiRideProto.DriveOptions> getUserMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRequest,
      protos.TaxiRideProto.DriveOptions> getUserMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRequest, protos.TaxiRideProto.DriveOptions> getUserMethod;
    if ((getUserMethod = TaxiServiceGrpc.getUserMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getUserMethod = TaxiServiceGrpc.getUserMethod) == null) {
          TaxiServiceGrpc.getUserMethod = getUserMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.UserRequest, protos.TaxiRideProto.DriveOptions>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "User"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveOptions.getDefaultInstance()))
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

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveRequest,
      protos.TaxiRideProto.DriveResponse> getPathMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveRequest,
      protos.TaxiRideProto.DriveResponse> getPathMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveRequest, protos.TaxiRideProto.DriveResponse> getPathMethod;
    if ((getPathMethod = TaxiServiceGrpc.getPathMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getPathMethod = TaxiServiceGrpc.getPathMethod) == null) {
          TaxiServiceGrpc.getPathMethod = getPathMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.DriveRequest, protos.TaxiRideProto.DriveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "Path"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("Path"))
                  .build();
          }
        }
     }
     return getPathMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveResponse,
      protos.TaxiRideProto.DriveResponse> getCancelPathMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveResponse,
      protos.TaxiRideProto.DriveResponse> getCancelPathMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveResponse, protos.TaxiRideProto.DriveResponse> getCancelPathMethod;
    if ((getCancelPathMethod = TaxiServiceGrpc.getCancelPathMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getCancelPathMethod = TaxiServiceGrpc.getCancelPathMethod) == null) {
          TaxiServiceGrpc.getCancelPathMethod = getCancelPathMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.DriveResponse, protos.TaxiRideProto.DriveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "CancelPath"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("CancelPath"))
                  .build();
          }
        }
     }
     return getCancelPathMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRepoRequest,
      protos.TaxiRideProto.UserRepoRequest> getDuplicateUserMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRepoRequest,
      protos.TaxiRideProto.UserRepoRequest> getDuplicateUserMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.UserRepoRequest, protos.TaxiRideProto.UserRepoRequest> getDuplicateUserMethod;
    if ((getDuplicateUserMethod = TaxiServiceGrpc.getDuplicateUserMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getDuplicateUserMethod = TaxiServiceGrpc.getDuplicateUserMethod) == null) {
          TaxiServiceGrpc.getDuplicateUserMethod = getDuplicateUserMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.UserRepoRequest, protos.TaxiRideProto.UserRepoRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "DuplicateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.UserRepoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.UserRepoRequest.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("DuplicateUser"))
                  .build();
          }
        }
     }
     return getDuplicateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.RideRepoRequest,
      protos.TaxiRideProto.RideRepoRequest> getDuplicateRideMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.RideRepoRequest,
      protos.TaxiRideProto.RideRepoRequest> getDuplicateRideMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.RideRepoRequest, protos.TaxiRideProto.RideRepoRequest> getDuplicateRideMethod;
    if ((getDuplicateRideMethod = TaxiServiceGrpc.getDuplicateRideMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getDuplicateRideMethod = TaxiServiceGrpc.getDuplicateRideMethod) == null) {
          TaxiServiceGrpc.getDuplicateRideMethod = getDuplicateRideMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.RideRepoRequest, protos.TaxiRideProto.RideRepoRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "DuplicateRide"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.RideRepoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.RideRepoRequest.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("DuplicateRide"))
                  .build();
          }
        }
     }
     return getDuplicateRideMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveResponse,
      protos.TaxiRideProto.DriveResponse> getCompleteReservationMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveResponse,
      protos.TaxiRideProto.DriveResponse> getCompleteReservationMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.DriveResponse, protos.TaxiRideProto.DriveResponse> getCompleteReservationMethod;
    if ((getCompleteReservationMethod = TaxiServiceGrpc.getCompleteReservationMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getCompleteReservationMethod = TaxiServiceGrpc.getCompleteReservationMethod) == null) {
          TaxiServiceGrpc.getCompleteReservationMethod = getCompleteReservationMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.DriveResponse, protos.TaxiRideProto.DriveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "CompleteReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.DriveResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("CompleteReservation"))
                  .build();
          }
        }
     }
     return getCompleteReservationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.EmptyMessage,
      protos.TaxiRideProto.RideSnapshot> getGetRideSnapshotMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.EmptyMessage,
      protos.TaxiRideProto.RideSnapshot> getGetRideSnapshotMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.EmptyMessage, protos.TaxiRideProto.RideSnapshot> getGetRideSnapshotMethod;
    if ((getGetRideSnapshotMethod = TaxiServiceGrpc.getGetRideSnapshotMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getGetRideSnapshotMethod = TaxiServiceGrpc.getGetRideSnapshotMethod) == null) {
          TaxiServiceGrpc.getGetRideSnapshotMethod = getGetRideSnapshotMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.EmptyMessage, protos.TaxiRideProto.RideSnapshot>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "GetRideSnapshot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.RideSnapshot.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("GetRideSnapshot"))
                  .build();
          }
        }
     }
     return getGetRideSnapshotMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TaxiRideProto.EmptyMessage,
      protos.TaxiRideProto.UserSnapshot> getGetUserSnapshotMethod;

  public static io.grpc.MethodDescriptor<protos.TaxiRideProto.EmptyMessage,
      protos.TaxiRideProto.UserSnapshot> getGetUserSnapshotMethod() {
    io.grpc.MethodDescriptor<protos.TaxiRideProto.EmptyMessage, protos.TaxiRideProto.UserSnapshot> getGetUserSnapshotMethod;
    if ((getGetUserSnapshotMethod = TaxiServiceGrpc.getGetUserSnapshotMethod) == null) {
      synchronized (TaxiServiceGrpc.class) {
        if ((getGetUserSnapshotMethod = TaxiServiceGrpc.getGetUserSnapshotMethod) == null) {
          TaxiServiceGrpc.getGetUserSnapshotMethod = getGetUserSnapshotMethod = 
              io.grpc.MethodDescriptor.<protos.TaxiRideProto.EmptyMessage, protos.TaxiRideProto.UserSnapshot>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.TaxiService", "GetUserSnapshot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TaxiRideProto.UserSnapshot.getDefaultInstance()))
                  .setSchemaDescriptor(new TaxiServiceMethodDescriptorSupplier("GetUserSnapshot"))
                  .build();
          }
        }
     }
     return getGetUserSnapshotMethod;
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
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveOptions> responseObserver) {
      asyncUnimplementedUnaryCall(getUserMethod(), responseObserver);
    }

    /**
     */
    public void ride(protos.TaxiRideProto.RideRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getRideMethod(), responseObserver);
    }

    /**
     */
    public void path(protos.TaxiRideProto.DriveRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPathMethod(), responseObserver);
    }

    /**
     */
    public void cancelPath(protos.TaxiRideProto.DriveResponse request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCancelPathMethod(), responseObserver);
    }

    /**
     */
    public void duplicateUser(protos.TaxiRideProto.UserRepoRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserRepoRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getDuplicateUserMethod(), responseObserver);
    }

    /**
     */
    public void duplicateRide(protos.TaxiRideProto.RideRepoRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRepoRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getDuplicateRideMethod(), responseObserver);
    }

    /**
     */
    public void completeReservation(protos.TaxiRideProto.DriveResponse request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCompleteReservationMethod(), responseObserver);
    }

    /**
     */
    public void getRideSnapshot(protos.TaxiRideProto.EmptyMessage request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideSnapshot> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRideSnapshotMethod(), responseObserver);
    }

    /**
     */
    public void getUserSnapshot(protos.TaxiRideProto.EmptyMessage request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserSnapshot> responseObserver) {
      asyncUnimplementedUnaryCall(getGetUserSnapshotMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.UserRequest,
                protos.TaxiRideProto.DriveOptions>(
                  this, METHODID_USER)))
          .addMethod(
            getRideMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.RideRequest,
                protos.TaxiRideProto.RideRequest>(
                  this, METHODID_RIDE)))
          .addMethod(
            getPathMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.DriveRequest,
                protos.TaxiRideProto.DriveResponse>(
                  this, METHODID_PATH)))
          .addMethod(
            getCancelPathMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.DriveResponse,
                protos.TaxiRideProto.DriveResponse>(
                  this, METHODID_CANCEL_PATH)))
          .addMethod(
            getDuplicateUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.UserRepoRequest,
                protos.TaxiRideProto.UserRepoRequest>(
                  this, METHODID_DUPLICATE_USER)))
          .addMethod(
            getDuplicateRideMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.RideRepoRequest,
                protos.TaxiRideProto.RideRepoRequest>(
                  this, METHODID_DUPLICATE_RIDE)))
          .addMethod(
            getCompleteReservationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.DriveResponse,
                protos.TaxiRideProto.DriveResponse>(
                  this, METHODID_COMPLETE_RESERVATION)))
          .addMethod(
            getGetRideSnapshotMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.EmptyMessage,
                protos.TaxiRideProto.RideSnapshot>(
                  this, METHODID_GET_RIDE_SNAPSHOT)))
          .addMethod(
            getGetUserSnapshotMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TaxiRideProto.EmptyMessage,
                protos.TaxiRideProto.UserSnapshot>(
                  this, METHODID_GET_USER_SNAPSHOT)))
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
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveOptions> responseObserver) {
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

    /**
     */
    public void path(protos.TaxiRideProto.DriveRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPathMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelPath(protos.TaxiRideProto.DriveResponse request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCancelPathMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void duplicateUser(protos.TaxiRideProto.UserRepoRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserRepoRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDuplicateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void duplicateRide(protos.TaxiRideProto.RideRepoRequest request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRepoRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDuplicateRideMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void completeReservation(protos.TaxiRideProto.DriveResponse request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCompleteReservationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRideSnapshot(protos.TaxiRideProto.EmptyMessage request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideSnapshot> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetRideSnapshotMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUserSnapshot(protos.TaxiRideProto.EmptyMessage request,
        io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserSnapshot> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetUserSnapshotMethod(), getCallOptions()), request, responseObserver);
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
    public protos.TaxiRideProto.DriveOptions user(protos.TaxiRideProto.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), getUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.RideRequest ride(protos.TaxiRideProto.RideRequest request) {
      return blockingUnaryCall(
          getChannel(), getRideMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.DriveResponse path(protos.TaxiRideProto.DriveRequest request) {
      return blockingUnaryCall(
          getChannel(), getPathMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.DriveResponse cancelPath(protos.TaxiRideProto.DriveResponse request) {
      return blockingUnaryCall(
          getChannel(), getCancelPathMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.UserRepoRequest duplicateUser(protos.TaxiRideProto.UserRepoRequest request) {
      return blockingUnaryCall(
          getChannel(), getDuplicateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.RideRepoRequest duplicateRide(protos.TaxiRideProto.RideRepoRequest request) {
      return blockingUnaryCall(
          getChannel(), getDuplicateRideMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.DriveResponse completeReservation(protos.TaxiRideProto.DriveResponse request) {
      return blockingUnaryCall(
          getChannel(), getCompleteReservationMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.RideSnapshot getRideSnapshot(protos.TaxiRideProto.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getGetRideSnapshotMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TaxiRideProto.UserSnapshot getUserSnapshot(protos.TaxiRideProto.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getGetUserSnapshotMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.DriveOptions> user(
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

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.DriveResponse> path(
        protos.TaxiRideProto.DriveRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPathMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.DriveResponse> cancelPath(
        protos.TaxiRideProto.DriveResponse request) {
      return futureUnaryCall(
          getChannel().newCall(getCancelPathMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.UserRepoRequest> duplicateUser(
        protos.TaxiRideProto.UserRepoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDuplicateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.RideRepoRequest> duplicateRide(
        protos.TaxiRideProto.RideRepoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDuplicateRideMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.DriveResponse> completeReservation(
        protos.TaxiRideProto.DriveResponse request) {
      return futureUnaryCall(
          getChannel().newCall(getCompleteReservationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.RideSnapshot> getRideSnapshot(
        protos.TaxiRideProto.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getGetRideSnapshotMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TaxiRideProto.UserSnapshot> getUserSnapshot(
        protos.TaxiRideProto.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getGetUserSnapshotMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_USER = 0;
  private static final int METHODID_RIDE = 1;
  private static final int METHODID_PATH = 2;
  private static final int METHODID_CANCEL_PATH = 3;
  private static final int METHODID_DUPLICATE_USER = 4;
  private static final int METHODID_DUPLICATE_RIDE = 5;
  private static final int METHODID_COMPLETE_RESERVATION = 6;
  private static final int METHODID_GET_RIDE_SNAPSHOT = 7;
  private static final int METHODID_GET_USER_SNAPSHOT = 8;

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
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveOptions>) responseObserver);
          break;
        case METHODID_RIDE:
          serviceImpl.ride((protos.TaxiRideProto.RideRequest) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRequest>) responseObserver);
          break;
        case METHODID_PATH:
          serviceImpl.path((protos.TaxiRideProto.DriveRequest) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse>) responseObserver);
          break;
        case METHODID_CANCEL_PATH:
          serviceImpl.cancelPath((protos.TaxiRideProto.DriveResponse) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse>) responseObserver);
          break;
        case METHODID_DUPLICATE_USER:
          serviceImpl.duplicateUser((protos.TaxiRideProto.UserRepoRequest) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserRepoRequest>) responseObserver);
          break;
        case METHODID_DUPLICATE_RIDE:
          serviceImpl.duplicateRide((protos.TaxiRideProto.RideRepoRequest) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideRepoRequest>) responseObserver);
          break;
        case METHODID_COMPLETE_RESERVATION:
          serviceImpl.completeReservation((protos.TaxiRideProto.DriveResponse) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.DriveResponse>) responseObserver);
          break;
        case METHODID_GET_RIDE_SNAPSHOT:
          serviceImpl.getRideSnapshot((protos.TaxiRideProto.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.RideSnapshot>) responseObserver);
          break;
        case METHODID_GET_USER_SNAPSHOT:
          serviceImpl.getUserSnapshot((protos.TaxiRideProto.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<protos.TaxiRideProto.UserSnapshot>) responseObserver);
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
              .addMethod(getPathMethod())
              .addMethod(getCancelPathMethod())
              .addMethod(getDuplicateUserMethod())
              .addMethod(getDuplicateRideMethod())
              .addMethod(getCompleteReservationMethod())
              .addMethod(getGetRideSnapshotMethod())
              .addMethod(getGetUserSnapshotMethod())
              .build();
        }
      }
    }
    return result;
  }
}
