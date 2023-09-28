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
public class UsptEvlTrget implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8271522033518573132L;

	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 그리드 선택여부*/
	Boolean check;
	/** 순번 */
	private int rn;

	/** 평가대상ID */
	String  evlTrgetId;
	/** 신청ID */
	String  applyId;
	/** 분과ID */
	String  sectId;
	/** 평가단계ID */
	String  evlStepId;
	/** 발표자료처리구분코드(G:PRESNATN_PROCESS_DIV) */
	String  presnatnProcessDivCd;
	/** 제출일시 */
	Date    presentnDt;
	/** 선정여부 */
	Boolean slctn;
	/** 선정일시 */
	Date    slctnDt;
	/** 첨부파일그룹ID */
	String  attachmentGroupId;
	/** 평가계획ID*/
	String  evlPlanId;

	/** 접수번호(BA + 8자리 순번) */
	String  receiptNo;
	/** 회원ID */
	String  memberId;
	/** 회원명(사업자명) */
	String  memberNm;
	/** 담당자명 */
	String  chargerNm;
	/** 과제명(국문) */
	String  taskNmKo;
	/** 지원분야ID */
	String  applyRealmId;
	/** 지원분야명 */
	String  applyRealmNm;
	/** 공고ID */
	String  pblancId;
	/** 접수차수 */
	Integer rceptOdr;
	/** 회원유형명(G:MEMBER_TYPE) */
	String  memberTypeNm;
	/** 회원유형코드 */
	String 	memberTypeCd;
	 /** 공고명 */
	String  pblancNm;

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
