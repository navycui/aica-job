package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptAcdmcr implements Serializable {
	private static final long serialVersionUID = 8451232539091784532L;
	/** 학력ID */
	private String acdmcrId;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 시작일 */
	private String bgnde;
	/** 종료일 */
	private String endde;
	/** 학교명 */
	private String schulNm;
	/** 전공 */
	private String major;
	/** 졸업구분코드(G:GRDTN_DIV) */
	private String grdtnDivCd;
	/** 졸업구분 */
	private String grdtnDiv;
	/** 순번 */
	private Long rn;
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
