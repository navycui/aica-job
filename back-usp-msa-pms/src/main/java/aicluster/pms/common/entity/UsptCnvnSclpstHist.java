package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptCnvnSclpstHist implements Serializable {
	/**
	 * 협약수행기관신분변경이력
	 */
	private static final long serialVersionUID = -1574834954889555564L;

	/*협약수행기관신분변경이력ID*/
	String cnvnSclpstHistId;
	/*협약변경요청ID*/
	String cnvnChangeReqId;
	/*승인협약수행기관신분변경이력ID*/
	String confmCnvnSclpstHistId;
	 /*개인사업자구분코드*/
	String indvdlBsnmDivCd;
	 /*이름*/
	String nm;
	 /*성별코드(G:GENDER)*/
	String genderCd;
	 /*내국인여부*/
	Boolean NativeYn;
	/*암호화된 휴대폰번호*/
	String encMbtlnum;
	 /*암호화된 이메일*/
	String encEmail;
	/** 암호화된 생년월일 */
	String  encBrthdy;
	  /*사업자명*/
	String bsnmNm;
	/*사업자번호*/
	String bizrno;
	 /*대표자명*/
	String ceoNm;
	 /*담당자명*/
	String chargerNm;
	 /*변경일자*/
	String changeDe;
	 /*변경전후*/
	String changeBeAf;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;


	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.cnvnSclpstHistId);
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
		return aes256.decrypt(this.encMbtlnum, this.cnvnSclpstHistId);
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
		return aes256.decrypt(this.encEmail, this.cnvnSclpstHistId);
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
