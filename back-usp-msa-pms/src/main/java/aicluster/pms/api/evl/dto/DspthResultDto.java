package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DspthResultDto implements Serializable {
	private static final long serialVersionUID = 6817635504842435124L;
	private String resultMessage;
	private Integer totalCnt;
	private Integer smsTotalCnt;
	private Integer smsSuccessCnt;
	private Integer smsFailCnt;
	private Integer emailTotalCnt;
	private Integer emailSuccessCnt;
	private Integer emailFailCnt;

}
