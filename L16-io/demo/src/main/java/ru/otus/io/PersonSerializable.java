package ru.otus.io;

import java.io.Serializable;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ToString
public class PersonSerializable implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PersonSerializable.class);
    //    private static final long serialVersionUID = 1L; // не забывайте про serialVersionUID
    private final int age;
    private final String name;
    private final transient String hidden; // transient - поле будет пропущено при сериализации

    public PersonSerializable() {
        this(0, null, null);
        logger.info("CONSTRUCTOR default: {}", this.getClass().getName());
    }
    // Обратите внимание на то, сколько раз вызывается констурктор и сколько объектов создается
    PersonSerializable(int age, String name, String hidden) {
        logger.info("CONSTRUCTOR: {}", this.getClass().getName());
        this.age = age;
        this.name = name;
        this.hidden = hidden;
    }
}
