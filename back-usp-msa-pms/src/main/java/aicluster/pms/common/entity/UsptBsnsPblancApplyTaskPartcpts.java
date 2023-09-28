package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptBsnsPblancApplyTaskPartcpts implements Serializable {
	private static final long serialVersionUID = -1203714622625351344L;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 참여자ID */
	private String partcptsId;
	/** 참여자이름 */
	private String partcptsNm;
	/** 담당분야명 */
	private String chrgRealmNm;
	/** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String encMbtlnum;
	/** 휴대폰번호 */
	private String mbtlnum;
	/** 암호화된 생년월일 */
	@JsonIgnore
	private String encBrthdy;
	/** 생년월일 */
	private String brthdy;
	/** 참여율 */
	private Integer partcptnRate;
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

	@JsonIgnore
	public String getDecBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.applyId);
	}

	@JsonIgnore
	public String getDecMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.applyId);
	}

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
