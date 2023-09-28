package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlctnObjcReqstListDto implements Serializable {
	private static final long serialVersionUID = -4252050917597694871L;
	/** 이의신청ID */
	private String objcReqstId;
	/** 회원명 */
	private String memberNm;
	/** 이의처리상태코드 (G:LAST_SLCTN_OBJC_PROCESS_STTUS) */
	private String lastSlctnObjcProcessSttusCd;
	/** 이의처리상태 */
	private String lastSlctnObjcProcessSttus;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	/** 모집유형 */
	private String ordtmRcrit;
	/** 접수차수 */
	private String rceptOdr;
	/** 담당부서 */
	private String deptNm;
	/** 담당자 */
	private String chargerNm;
	/** 접수일 */
	private String rceptDate;
	/** 순번 */
	private int rn;



}
