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
public class UsptEvlMfcmmResult implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6305838127339958405L;

	String  evlResultId;                    /** 평가결과ID */
	String  evlMfcmmId;                     /** 평가위원ID */
	String  evlTrgetId;                     /** 평가대상ID */
	String  mfcmmEvlSttusCd;                /** 위원평가상태코드(G:MFCMM_EVL_STTUS) */
	Date    mfcmmEvlSttusDt;                /** 위원평가상태변경일시 */
	String  evlOpinion;                     /** 평가의견 */

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
