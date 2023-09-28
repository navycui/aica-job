package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UsptAtchmnflMapng implements Serializable {
	private static final long serialVersionUID = 3710128404008988733L;
	/** 회원ID */
	private String mberId;
	/** 첨부파일ID */
	private String atchmnflId;
	/** 등록ID */
	private String registId;
	/** 상태명 */
	private String sttusNm;
	/** 파일저장경로 */
	private String fileStrePath;
	/** 완료일시 */
	private Date comptDt;
	/** 링크URL */
	private String linkUrl;
	/** 등록일시 */
	private Date creatDt;
	/** 수정일시 */
	private Date updtDt;


	public Date getComptDt() {
		if (this.comptDt != null) {
			return new Date(this.comptDt.getTime());
         }
		return null;
	}
	public void setComptDt(Date comptDt) {
		this.comptDt = null;
		if (comptDt != null) {
			this.comptDt = new Date(comptDt.getTime());
		}
	}

	public Date getCreatDt() {
		if (this.creatDt != null) {
			return new Date(this.creatDt.getTime());
         }
		return null;
	}
	public void setCreatDt(Date creatDt) {
		this.creatDt = null;
		if (creatDt != null) {
			this.creatDt = new Date(creatDt.getTime());
		}
	}
	public Date getUpdtDt() {
		if (this.updtDt != null) {
			return new Date(this.updtDt.getTime());
		}
		return null;
	}
	public void setUpdtDt(Date updtDt) {
		this.updtDt = null;
		if (updtDt != null) {
			this.updtDt = new Date(updtDt.getTime());
		}
	}

}
