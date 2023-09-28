package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptBsnsPlanApplcnt implements Serializable {
	/**
	 * 사업계획신청자
	 */
	private static final long serialVersionUID = -8052521779355619465L;
	/** 사업계획신청자ID */
	String  bsnsPlanApplcntId;
	/** 사업계획서ID */
	String  bsnsPlanDocId;
	/** 신청자명 */
	String  applcntNm;
	/** 성별코드(G:GENDER) */
	String  genderCd;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 생년월일 */
	String  encBrthdy;
	/** 내국인여부 */
	Boolean nativeYn;
	/** 암호화된 이메일 */
	String  encEmail;
	/** 개인사업자구분코드(INDVDL_BSNM_DIV) */
	String indvdlBsnmDivCd;
	/** 담당자명 */
	 String chargerNm;
	 /** 대표자명 */
	 String ceoNm;
	 /** 사업자번호(기업회원) */
	 String bizrno;

	private int rn;							/** 순번 */

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

	/** I:등록, U:수정, D:삭제 */
	private String flag;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	String  updaterId;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date updatedDt;         /** 수정일시 */

	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.bsnsPlanApplcntId);
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
		return aes256.decrypt(this.encMbtlnum, this.bsnsPlanApplcntId);
	}

	/**
	 * 복호화된 이메일
	 *
	 * @return
	 */
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.bsnsPlanApplcntId);
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
