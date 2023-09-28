package aicluster.pms.api.bsns.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ApplyMberType {
	/** 지원가능회원유형코드 */
	private String applyMberTypeCd;
	/** 지원가능회원유형코드명 */
	private String applyMberTypeNm;
	/** 선택여부 */
	private Boolean isChecked;
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
