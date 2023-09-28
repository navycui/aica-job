package aicluster.framework.log.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BatchLog implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5667510729719840825L;
    private String apiSystemId;
    private String batchName;
    private String batchMethod;
    private Date beginDt;
    private Long elapsedTime;
    private String batchSt;
    private String resultCn;
    private String errorCode;
    private String errorMsg;

}
