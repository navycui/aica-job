package aicluster.pms.api.expertReqst.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExpertClIdParntsDto implements Serializable{

	/**
	 *  전문가 부모 분류코드조회
	 */
	private static final long serialVersionUID = 74873828609908889L;

	/** 부모전문가분류ID */
	String  parntsExpertClId;
	/** 전문가분류ID */
	String  expertClId;
	/** 전문가분류명 */
	String  expertClNm;
}
