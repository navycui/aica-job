package aicluster.batch.common.entity;

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
	private String bsnsCode;
	/** 공고명 */
	private String pblancNm;
	/** 공고번호 */
	private String pblancNo;
	/** 상시모집여부 */
	private Boolean ordtmRcritAt;
	/** 공고일 */
	private String pblancDe;
	/** 사업시작일  */
	private String bsnsBgnde;
	/** 사업종료일  */
	private String bsnsEndde;
	/** 사업규모금액 */
	private Long bsnsScaleAmount;
	/** 선정규모 */
	private int slctnScale;
	/** 공고요약 */
	private String pblancSumry;
	/** 접수시작일  */
	private String rceptBgnde;
	/** 접수종료일  */
	private String rceptEndde;
	/** 접수마감시분 */
	private String rceptClosHm;
	/** 공고상태코드(G:PBLANC_STTUS) */
	private String pblancSttusCode;
	/** 게시여부 */
	private Boolean ntceAt;
	/** 사용여부 */
	private Boolean useAt;
	/** 첨부파일그룹ID */
	private String atchmnflGroupId;
	/** 썸네일파일ID */
	private String thumbFileId;
	/** 생성자ID */
	@JsonIgnore
	private String creatrId;
	/** 생성일시 */
	@JsonIgnore
	private Date creatDt;
	/** 수정자ID */
	@JsonIgnore
	private String updusrId;
	/** 수정일시 */
	@JsonIgnore
	private Date updtDt;
	
	public Date getCreatDt() {
		if (this.creatDt != null) {
			return new Date(this.creatDt.getTime());
		}
		return null;
	}
	
	public void setCreatDt(Date creatDt) {
		this.creatDt = null;
		if (creatDt != null) {
			this.creatDt = new Date(creatDt.getTime());
		}
	}
	
	public Date getUpdtDt() {
		if (this.updtDt != null) {
			return new Date(this.updtDt.getTime());
		}
		return null;
	}
	
	public void setUpdtDt(Date updtDt) {
		this.updtDt = null;
		if (updtDt != null) {
			this.updtDt = new Date(updtDt.getTime());
		}
	}
}
