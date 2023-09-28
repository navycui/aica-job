package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlCmitExtrcTarget implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3331789917369019466L;
	/** 변경 플래그(I,U,D) */
	@JsonIgnore
	String  flag;
	/** 평가위원회ID */
	String  evlCmitId;
	/** 분과ID */
	@JsonIgnore
	String  sectId;
	/** 분과명 */
	String  sectNm;
	/** 평가단계ID */
	@JsonIgnore
	String  evlStepId;
	/** 평가단계명 */
	String  evlStepNm;
	/** 평가표ID */
	@JsonIgnore
	String  evlTableId;
	/** 평가위원회명 */
	String  evlCmitNm;
	/** 위원수 */
	Integer mfcmmCo;
	/** 평가예정일 */
	String  evlPrarnde;
	/** 시작시간 */
	@JsonIgnore
	String  beginHour;
	/** 종료시간 */
	@JsonIgnore
	String  endHour;
	/** 평가장소 */
	String  evlPlace;
	/** 간사ID(CMMT_INSIDER.MEMBER_ID) */
	@JsonIgnore
	String  orgnzrId;
	/** 간사명(CMMT_INSIDER.MEMBER_ID) */
	String  orgnzrNm;
	/** 평가위원장종합의견내용 */
	@JsonIgnore
	String  evlCharmnOpinionCn;
	/** 평가상태코드(G:EVL_ST) */
	@JsonIgnore
	String  evlSttusCd;
	/** 평가상태명(G:EVL_ST) */
	String  evlSttusNm;
	/** 평가상태일시 */
	Date    evlSttusDt;
	/** 통보여부 */
	Boolean dspth;
	/** 통보여부명 */
	String  dspthNm;
	/** 통보일시 */
	Date    dspthDt;


	/** 선정통보여부 */
	Boolean slctnDspth;
	/** 선정통보여부명 */
	String  slctnDspthNm;
	/** 선정통보일시 */
	Date    slctnDspthDt;

	/** 탈락통보여부 */
	Boolean ptrmsnDspth;
	/** 탈락통보여부명 */
	String  ptrmsnDspthNm;
	/** 탈락통보일시 */
	Date    ptrmsnDspthDt;


	/** 온라인여부 */
	Boolean online;
	/** 첨부파일그룹ID */
	String  attachmentGroupId;

	/** 사업년도 */
	String  bsnsYear;
	/** 공고명 */
	String  pblancNm;
	/** 상시모집명 */
	String  ordtmRcritNm;
	/** 평가유형코드(G:EVL_TYPE) */
	String  evlTypeCd;
	/** 평가유형명(G:EVL_TYPE) */
	String  evlTypeNm;
	/** 처리상태명 */
	String  processSttusNm;
	/** 접수차수 */
	String  rceptOdr;
	/** 평가방식코드 */
	String  evlMthdCd;
	/** 사업명 */
	String  bsnsNm;
	/** 최종추출차수 */
	int  maxOdrNo;

	/** 대기건수 */
	int waitCo;
	/** 부재건수 */
	int absnceCo;
	/** 거부건수 */
	int rejectCo;
	/** 승낙건수 */
	int confmCo;
	/** 회피건수 */
	int evasCo;

	/** 순번 */
	private int rn;

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자 ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
         }
		return null;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}

	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}
}
