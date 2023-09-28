package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptRsltIdxIemCn implements Serializable {
	private static final long serialVersionUID = -7419130907407708640L;
	/** 성과지표항목내용ID */
	private String rsltIdxIemCnId;
	/** 성과지표항목ID */
	@JsonIgnore
	private String rsltIdxIemId;
	/** 성과지표세부항목ID */
	private String rsltIdxDetailIemId;
	/** 성과지표세부항목명 */
	private String detailIemNm;
	/** 성과지표기준항목ID */
	private String rsltIdxStdIemId;
	/** 성과지표기준항목명 */
	private String stdIemNm;
	/** 항목단위코드(G:IEM_UNIT) */
	private String iemUnitCd;
	/** 항목단위명 */
	private String iemUnitNm;
	/** 성과지표항목내용 */
	private String rsltIdxIemCn;
	/** 정렬순서 */
	private Integer sortOrder;
	/** 삭제여부 */
	private Boolean deleteAt;
	/** I:등록, U:수정, D:삭제 */
	private String flag;
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
