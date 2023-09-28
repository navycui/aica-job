package aicluster.tsp.api.common.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonReturnList<T> implements Serializable {
    private List<T> list;

    public CommonReturnList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        List<T> list = new ArrayList<>();
        if (this.list != null) {
            list.addAll(this.list);
        }
        return list;
    }
    public void setList(List<T> list) {
        this.list = new ArrayList<>();
        if (list != null) {
            this.list.addAll(list);
        }
    }
}