package ru.otus.io.external;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.io.PersonExternalizable;

// Демонстрация java.io.Externalizable.
// Обратите внимание на порядок вызовов в логах: при десериализации вызывается
// public no-arg конструктор, затем readExternal (у Serializable конструктор не вызывается).
// Сравните с DemoSerialization.
public class DemoExternalizable {
    private static final Logger logger = LoggerFactory.getLogger(DemoExternalizable.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var person = new PersonExternalizable(33, "ExternalPersonForArray", "secret");
        byte[] serializedPerson = serializePerson(person);
        deserialize(serializedPerson);
    }

    private static byte[] serializePerson(PersonExternalizable person) throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
                var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            logger.info("Serializing:{}", person);
            objectOutputStream.writeObject(person);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private static void deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            Object person = objectInputStream.readObject();
            logger.info("deSerialized person:{}", person);
        }
    }
}
