package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptRsltIdxIemCnHist implements Serializable {
	private static final long serialVersionUID = 393893255836071386L;
	/** 성과이력ID */
	private String rsltHistId;
	/** 성과지표항목ID */
	private String rsltIdxIemId;
	/** 성과지표항목내용ID */
	private String rsltIdxIemCnId;
	/** 성과지표항목내용 */
	private String rsltIdxIemCn;
	/** 정렬순서 */
	private Integer sortOrder;
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
