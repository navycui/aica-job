package aicluster.tsp.api.common.param;

import java.io.Serializable;
import java.util.List;

public class CommonReturn<T> implements Serializable {
    private T value;

    public CommonReturn(T value) {
        this.value = value;
    }

    public void setValue(T value){ this.value = value; }

    public T getValue() { return this.value; }
}