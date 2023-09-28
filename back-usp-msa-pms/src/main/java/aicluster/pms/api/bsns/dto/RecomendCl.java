package aicluster.pms.api.bsns.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class RecomendCl {
	/** 추천분류유형코드(G:RECOMEND_CL_TYPE) */
	private String recomendClTypeCd;
	/** 추천분류코드(G:RECOMEND_CL) */
	private String recomendClCd;
	/** 생성자ID */
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
