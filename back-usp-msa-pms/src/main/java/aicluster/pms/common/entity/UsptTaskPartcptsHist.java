package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptTaskPartcptsHist implements Serializable {
	/**
	 * 과제참여자변경이력
	 */
	private static final long serialVersionUID = -4723171883712467921L;

	/** 과제참여자변경이력ID */
	String  taskPartcptsHistId;
	 /** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 승인과제참여자변경이력IDID */
	String  confmTaskPartcptsHistId;
	/** 참여자명 */
	String  partcptsNm;
	/** 담당분야명 */
	String  chrgRealmNm;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 생년월일 */
	String  encBrthdy;
	/** 참여율 */
	Integer partcptnRate;
	/** 회원ID */
	String memberId;
	/** 회원명(사업자명) */
	String memberNm;
	 /*변경일자*/
	String changeDe;
	 /*변경전후*/
	String changeBeAf;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */


	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.taskPartcptsHistId);
	}

	/**
	 * 복호화된 휴대폰번호
	 *
	 * @return
	 */
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.taskPartcptsHistId);
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

}
