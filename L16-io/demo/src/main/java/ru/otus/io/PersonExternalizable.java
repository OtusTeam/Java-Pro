package ru.otus.io;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ToString
public class PersonExternalizable implements Externalizable {
    private static final Logger logger = LoggerFactory.getLogger(PersonExternalizable.class);
    @ToString.Include
    private static final long serialVersionUID = 1L; // как и для Serializable, важно для совместимости версий

    private int age;
    private String name;
    private transient String hidden; // transient здесь НЕ работает - в writeExternal решаем сами, что писать

    public PersonExternalizable() {
        logger.info("CONSTRUCTOR default: {}", this.getClass().getName());
    }

    public PersonExternalizable(int age, String name, String hidden) {
        logger.info("CONSTRUCTOR: {}", this.getClass().getName());
        logger.info("{} {} {}", age, name, hidden);
        this.age = age;
        this.name = name;
        this.hidden = hidden;
    }

    // Записываем состояние вручную. Порядок записи должен точно совпадать с порядком чтения в readExternal!
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        logger.info("writeExternal");
        out.writeInt(age);
        out.writeUTF(name);
        out.writeUTF(hidden); // transient-поле пишем сами - в отличие от Serializable, оно сохранится
    }

    // Читаем в том же порядке, в котором писали
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        logger.info("readExternal");
        this.age = in.readInt();
        this.name = in.readUTF();
        this.hidden = in.readUTF();
    }
}
