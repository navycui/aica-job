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
public class UsptExclMfcmm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5594614889574288852L;

	/** 제외위원ID */
	String exclMfcmmId;
	/** 위원추출ID */
	String mfcmmExtrcId;
	/** 전문가ID */
	String expertId;
	/** 제외사유내용 */
	String exclResnCn;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;

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

}
