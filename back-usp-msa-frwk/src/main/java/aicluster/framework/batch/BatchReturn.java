package aicluster.framework.batch;

import java.io.Serializable;

public class BatchReturn implements Serializable {
    /**
     *
     */
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_EXCEPTION = "EXCEPTION";
    public static final String STATUS_SKIP = "SKIP";
    public static final String STATUS_STOP = "STOP";

    private static final long serialVersionUID = -6845972862174587481L;
    private String status;
    private String result;
    private BatchReturn() {
    }

    public static BatchReturn success(String result) {
        BatchReturn br = new BatchReturn();
        br.status = STATUS_SUCCESS;
        br.result = result;
        return br;
    }

    public static BatchReturn exception(String result) {
        BatchReturn br = new BatchReturn();
        br.status = STATUS_EXCEPTION;
        br.result = result;
        return br;
    }

    public static BatchReturn skip(String result) {
        BatchReturn br = new BatchReturn();
        br.status = STATUS_SKIP;
        br.result = result;
        return br;
    }

    public static BatchReturn stop(String result) {
        BatchReturn br = new BatchReturn();
        br.status = STATUS_STOP;
        br.result = result;
        return br;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
