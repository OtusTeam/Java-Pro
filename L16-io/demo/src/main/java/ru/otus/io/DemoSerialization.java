package ru.otus.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DemoSerialization {
    private static final Logger logger = LoggerFactory.getLogger(DemoSerialization.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        logger.info("SERIALIZABLE");
        var personSer = new PersonSerializable(12, "SerialPersonForArray", "secretSer");
        deserialize(serialize(personSer));
        logger.info("EXTERNALIZABLE");
        var personExt = new PersonExternalizable(33, "ExternalPersonForArray", "secretExt");
        deserialize(serialize(personExt));
    }

    private static byte[] serialize(Object person) throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        ) {
            logger.info("Serializing:{}", person);
            objectOutputStream.writeObject(person);
            byte[] personBin = byteArrayOutputStream.toByteArray();
            return personBin;
        }
    }

    private static void deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            var person = objectInputStream.readObject();
            logger.info("deSerialized person:{}", person);
        }
    }
}
