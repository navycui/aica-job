package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UsptBsnsPblancApplyDplctAtchmnfl implements Serializable {

	/**
	 * 사업공고지원중복첨부파일
	 */
	private static final long serialVersionUID = 6528329159216165930L;
	/** 문서중보첨부파일ID */
	private String dplctDocAtchmnflId;
	/** 신청ID */
	private String applyId;
	/** 첨부파일설정ID */
	private String atchmnflSetupId;
	/** 첨부파일ID */
	private String atchmnflId;
	/** 문서ID */
	private String docId;
	/** 파일명 */
	private String fileNm;
	/** 동일비율 */
	private Float samenssRate;
	/** 생성자ID */
	private String creatorId;
	/** 생성일시 */
	private Date createdDt;
}
