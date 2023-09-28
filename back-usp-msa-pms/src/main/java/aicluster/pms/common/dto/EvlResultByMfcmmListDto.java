package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlResultByMfcmmListDto implements Serializable {

	private static final long serialVersionUID = -3091604015318185640L;

	String  evlMfcmmId;                     /** 평가위원ID */
	String  evlMfcmmNm;                     /** 평가위원명 */
	Integer evlScore;                       /** 평가점수 */
	String  evlTrgetId;                     /** 평가대상ID */
	String  mfcmmEvlSttusCd;                /** 위원평가상태코드(G:MFCMM_EVL_STTUS) */
	String  mfcmmEvlSttusNm;                /** 위원평가상태코드명(G:MFCMM_EVL_STTUS) */
	String  evlResultId;                    /** 평가결과ID */

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자 ID */
	@JsonIgnore
	private Date updatedDt;			/** 수정일시 */

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
