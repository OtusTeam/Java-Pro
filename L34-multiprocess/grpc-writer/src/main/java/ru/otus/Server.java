package ru.otus;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.PersonOuterClass.Empty;
import ru.otus.PersonOuterClass.Person;
import ru.otus.PersonOuterClass.PersonRequest;
import java.util.concurrent.CompletableFuture;

public class Server extends PersonServiceGrpc.PersonServiceImplBase {
  @Override
  public void getUser(final PersonRequest request, final StreamObserver<Person> responseObserver) {
    var p = Person.newBuilder()
        .setName(request.getName())
        .setFavoriteColor("red")
        .setFavoriteNumber(42)
        .build();
    responseObserver.onNext(p);
    responseObserver.onCompleted();
  }

  @Override
  public void getUsers(final Empty request, final StreamObserver<Person> responseObserver) {
    CompletableFuture.runAsync(() -> asyncSendPersons(responseObserver));
  }

  private void asyncSendPersons(final StreamObserver<Person> responseObserver) {
    try {
      for (int i = 0; i < 100; i++) {
        var p = Person.newBuilder()
            .setName("Stepa")
            .setFavoriteColor("red")
            .setFavoriteNumber(i)
            .build();
        responseObserver.onNext(p);
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    responseObserver.onCompleted();
  }

  public static void main(String[] args) throws Exception {
    var server = ServerBuilder.forPort(8000).addService(new Server()).build();
    server.start();
    server.awaitTermination();
  }
}
