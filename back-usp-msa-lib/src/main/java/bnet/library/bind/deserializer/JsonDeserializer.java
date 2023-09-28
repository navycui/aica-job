package bnet.library.bind.deserializer;

public abstract class JsonDeserializer<T> extends com.fasterxml.jackson.databind.JsonDeserializer<T> {
    private Class<T> targetClass;
    public Class<T> getTargetClass() {
        return this.targetClass;
    }
    protected JsonDeserializer(Class<T> clazz) {
        this.targetClass = clazz;
    }
}
