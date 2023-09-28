package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptProgrm implements Serializable {
	private static final long serialVersionUID = 5700164521839907970L;
	/** 프로그램ID */
	private String progrmId;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 프로그램유형코드(G:PROGRM_TYPE) */
	private String progrmTypeCd;
	/** 프로그램유형 */
	private String progrmType;
	/** 프로그램 유형 입력 */
	private String progrmTypeInput;
	/** 등급유형코드(G:GRAD_TYPE) */
	private String gradTypeCd;
	/** 등급유형 */
	private String gradType;
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
