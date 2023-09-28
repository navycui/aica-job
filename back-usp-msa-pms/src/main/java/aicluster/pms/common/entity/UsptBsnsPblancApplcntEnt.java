package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptBsnsPblancApplcntEnt implements Serializable {
	private static final long serialVersionUID = 2941864139177489960L;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 기업유형코드(G:CMPNY_TYPE) */
	private String cmpnyTypeCd;
	/** 기업유형 */
	private String cmpnyType;
	/** 산업분야코드(G:INDUST_REALM) */
	private String industRealmCd;
	/** 산업분야 */
	private String industRealm;
	/** 설립일 */
	private String fondDay;
	/** 종업원수 */
	private Long emplyCnt;
	/** 상주인원수 */
	private Long resdngNmpr;
	/** 채용예정인력수 */
	private Long empmnPrearngeNmpr;
	/** 업종 */
	private String induty;
	/** 주업종 */
	private String mainInduty;
	/** 주요기술 및 생산품 */
	private String mainTchnlgyProduct;
	/** 우편번호 */
	private String zip;
	/** 주소 */
	private String adres;
	/** 암호화된 팩스번호 */
	@JsonIgnore
	private String encFxnum;
	/** 팩스번호 */
	private String fxnum;
	/** 암호화된 대표 전화번호 */
	@JsonIgnore
	private String encReprsntTelno;
	/** 대표 전화번호 */
	private String reprsntTelno;
	/** 암호화된 대표자 연락처 */
	@JsonIgnore
	private String encCeoTelno;
	/** 대표자 연락처 */
	private String ceoTelno;
	/** 암호화된 대표자 이메일 */
	@JsonIgnore
	private String encCeoEmail;
	/** 대표자 이메일 */
	private String ceoEmail;
	/** 신규창업계획코드(G:NEW_FNTN_PLAN) */
	private String newFntnPlanCd;
	/** 신규창업계획 */
	private String newFntnPlan;
	/** 이전 및 설립계획코드(G:FOND_PLAN) */
	private String fondPlanCd;
	/** 이전 및 설립계획 */
	private String fondPlan;
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
	public String getDecFxnum() {
		if (string.isBlank(this.encFxnum)) {
			return null;
		}
		return aes256.decrypt(this.encFxnum, this.applyId);
	}
	@JsonIgnore
	public String getDecReprsntTelno() {
		if (string.isBlank(this.encReprsntTelno)) {
			return null;
		}
		return aes256.decrypt(this.encReprsntTelno, this.applyId);
	}
	@JsonIgnore
	public String getDecCeoTelno() {
		if (string.isBlank(this.encCeoTelno)) {
			return null;
		}
		return aes256.decrypt(this.encCeoTelno, this.applyId);
	}
	@JsonIgnore
	public String getDecCeoEmail() {
		if (string.isBlank(this.encCeoEmail)) {
			return null;
		}
		return aes256.decrypt(this.encCeoEmail, this.applyId);
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
