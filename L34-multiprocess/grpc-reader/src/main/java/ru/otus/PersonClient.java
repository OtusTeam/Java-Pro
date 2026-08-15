package ru.otus;

import io.grpc.ManagedChannelBuilder;
import ru.otus.PersonOuterClass.Empty;
import ru.otus.PersonOuterClass.PersonRequest;

public class PersonClient {
  public static void main(String[] args) {
    var channel = ManagedChannelBuilder.forAddress("localhost", 8000).usePlaintext().build();
    var stub = PersonServiceGrpc.newBlockingStub(channel);

    var result = stub.getUser(PersonRequest.newBuilder().setName("X").build());
    System.out.println(result);

    var stream = stub.getUsers(Empty.newBuilder().build());
    stream.forEachRemaining(System.out::println);
  }
}
