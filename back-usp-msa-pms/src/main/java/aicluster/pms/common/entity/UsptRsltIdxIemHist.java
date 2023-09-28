package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptRsltIdxIemHist implements Serializable {
	private static final long serialVersionUID = 313556552972330467L;
	/** 성과이력ID */
	private String rsltHistId;
	/** 성과지표항목ID */
	private String rsltIdxIemId;
	/** 성과지표명 */
	private String rsltIdxNm;
	/** 성과지표유형코드(G:RSLT_IDX_TYPE) */
	private String rsltIdxTypeCd;
	/** 증빙첨부파일ID */
	private String prufAttachmentId;
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
