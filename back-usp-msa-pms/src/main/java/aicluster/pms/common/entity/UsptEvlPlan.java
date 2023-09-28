package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlPlan implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3438271350798235851L;
	/** 평가계획ID */
	String  evlPlanId;
	/** 공고ID */
	String  pblancId;
	/** 접수차수 */
	Integer rceptOdr;
	/** 접수차수명 */
	String  rceptOdrNm;
	/** 사용여부 */
	Boolean enabled;
	/** 전체평가대상수 */
	Integer totalEvlTrgetCo;

	/** 사업비조정심의여부 */
	Boolean wctMdatDlbrt;
	/** 평가진행상태코드(G:EVL_STTUS) */
	String  evlSttusCd;
	/** 평가진행상태명(G:EVL_STTUS) */
	String  evlSttusNm;
	/** 평가유형코드(G:EVL_TYPE) */
	String  evlTypeCd;
	/** 평가유형명(G:EVL_TYPE) */
	String  evlTypeNm;

	/** 공고명 */
	String  pblancNm;
	/** 공고번호 */
	String  pblancNo;
	/** 상시모집여부 */
	Boolean ordtmRcrit;
	/** 상시모집여부명 */
	String  ordtmRcritNm;
	/** 평가예정일시 */
	String evlPrarnde;
	/** 등록일 */
	String rgsde;

	/** 담당자명 */
	String  chargerNm;
	/** 담당부서명 */
	String  chrgDeptNm;
	/** 담당자ID */
	String  insiderId;
	/** 순번 */
	private int rn;
	/** 사업코드 */
	String bsnsCd;

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

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

	/** 분과리스트 */
	List<UsptSect> usptSectList;

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
