package aicluster.member.common.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtEntrprsInfo {

	@JsonIgnore
	private String    	memberId;
	private String    	cmpnyTypeCd;
	private String     cmpnyTypeNm;
	private String    	industRealmCd;
	private String     industRealmNm;
	private String	  	fondDay;
	private Long	  	emplyCnt;
	private Long	  	resdngNmpr;
	private Long	  	empmnPrearngeNmpr;
	private String    	induty;
	private String    	mainInduty;
	private String    	mainTchnlgyProduct;
	private String	  	zip;
	private String    	adres;
	@JsonIgnore
	private String    	encFxnum;
	@JsonIgnore
	private String    	encReprsntTelno;
	@JsonIgnore
	private String    	encCeoTelno;
	@JsonIgnore
	private String    	encCeoEmail;
	private String    	newFntnPlanCd;
	private String      newFntnPlanNm;
	private String    	fondPlanCd;
	private String      fondPlanNm;
	private Long        prvyySalamt;
	private String    	creatorId;
	private Date	  	createdDt;
	private String	  	updaterId;
	private Date	  	updatedDt;

	/*
	 * Helper
	 */
	public String getFxnum() {
		if (string.isBlank(this.encFxnum)) {
			return null;
		}
		return aes256.decrypt(this.encFxnum, this.memberId);
	}
	public String getReprsntTelno() {
		if (string.isBlank(this.encReprsntTelno)) {
			return null;
		}
		return aes256.decrypt(this.encReprsntTelno, this.memberId);
	}
	public String getCeoTelno() {
		if (string.isBlank(this.encCeoTelno)) {
			return null;
		}
		return aes256.decrypt(this.encCeoTelno, this.memberId);
	}
	public String getCeoEmail() {
		if (string.isBlank(this.encCeoEmail)) {
			return null;
		}
		return aes256.decrypt(this.encCeoEmail, this.memberId);
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
