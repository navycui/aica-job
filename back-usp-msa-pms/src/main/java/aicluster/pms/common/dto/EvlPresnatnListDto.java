package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class EvlPresnatnListDto implements Serializable {

	private static final long serialVersionUID = -8476745432042716197L;

	private int rn;					/** 순번 */
	/** 변경 플래그(I,U,D) */
	String  flag;
	 /** 공고명 */
	String  pblancNm;
	 /** 평가계획ID */
	String  evlPlanId;
	 /** 공고ID */
	String  pblancId;
	 /** 접수차수 */
	Integer rceptOdr;
	/** 접수번호(BA + 8자리 순번) */
	String  receiptNo;
	/** 공고번호 */
	String  pblancNo;
	/** 상시모집여부 */
	Boolean ordtmRcrit;
	/** 상시모집여부명 */
	String  ordtmRcritNm;
	/** 사업년도 */
	String  bsnsYear;
	/** 사업명 */
	String  bsnsNm;
	/** 제출일시 */
	Date    presentnDt;
	/** 제출 시작일 */
	Date    presnatnBgnde;
	/** 제출 종료일 */
	Date    presnatnEndde;
	/** 제출일시 */
	String  fmtPresentnDt;
	/** 발표자료처리구분코드(G:PRESNATN_PROCESS_DIV) */
	String  presnatnProcessDivCd;
	/** 발표자료처리구분명 */
	String  presnatnProcessDivNm
	 /** 회원ID */;
	String  memberId;
	/** 회원명 */
	String  memberNm;
	/** 평가대상ID */
	String  evlTrgetId;
	/** 과제명(국문) */
	String  taskNmKo;
	/*프론트용*/
	Boolean isFront;
	/*첨부파일그룹ID*/
	String attachmentGroupId;
	/**첨부파일ID */
	String attachmentId;
	/** 평가단계명 */
	String  evlStepNm;

	@JsonIgnore
	private String encMobileNo;


	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;
	@JsonIgnore
	private Long totalItems;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;

	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		String mobileNo = aes256.decrypt(this.encMobileNo, this.memberId);
		return mobileNo;
	}

	public String getPresnatnBgnde() {
		return date.format(presnatnBgnde, "yyyyMMdd");
	}

	public String getPresnatnEndde() {
		return date.format(presnatnEndde, "yyyyMMdd");
	}

}
