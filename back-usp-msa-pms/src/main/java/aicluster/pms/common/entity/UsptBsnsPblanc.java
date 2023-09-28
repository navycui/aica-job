package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsPblanc implements Serializable {
	private static final long serialVersionUID = -7604030731545777300L;
	/** 공고ID */
	private String pblancId;
	/** 사업코드(BS + 8자리 순번) */
	private String bsnsCd;
	/** 공고명 */
	private String pblancNm;
	/** 공고번호 */
	private String pblancNo;
	/** 상시모집여부 */
	private Boolean ordtmRcrit;
	/** 공고일 */
	private String pblancDay;
	/** 사업시작일  */
	private String bsnsBgnde;
	/** 사업종료일  */
	private String bsnsEndde;
	/** 사업규모 */
	private Long bsnsScale;
	/** 선정규모 */
	private int slctnScale;
	/** 공고요약 */
	private String pblancSumry;
	/** 접수시작일  */
	private String rceptBgnde;
	/** 접수종료일  */
	private String rceptEndde;
	/** 접수마감시분 */
	private String rceptClosingHm;
	/** 공고상태코드(G:PBLANC_STTUS) */
	private String pblancSttusCd;
	/** 공고상태 */
	private String pblancSttus;
	/** 게시여부 */
	private Boolean ntce;
	/** 게시상태 */
	private String ntceNm;
	/** 사용여부 */
	private Boolean enabled;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 썸네일파일ID */
	private String thumbnailFileId;
	/** 접수차수 */
	@JsonIgnore
	private Integer rceptOdr;
	@JsonIgnore
	/** 담당자권한코드 */
	private String chargerAuthorCd;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 과제유형명 */
	private String taskTypeNm;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자ID */
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
