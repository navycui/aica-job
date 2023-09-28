package aicluster.pms.api.expertReqst.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontExpertReqstCntDto implements Serializable{

	/**
	 * 전문가 신청자 최근 상태
	 */
	private static final long serialVersionUID = -3407333519921632812L;
	/** 신청진행 코드  */
	String expertReqstProcessSttusCd;
}
