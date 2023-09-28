package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogtBatchLog implements Serializable {
	private static final long serialVersionUID = -4853734626284011881L;

	private String apiSystemId;
	private String logId;
	private String batchName;
	private String batchMethod;
	private Date beginDt;
	private Long elapsedTime;
	private String batchSt;
	private String resultCn;
	private String errorCode;
	private String errorMsg;

	private Long rowNum;
}
