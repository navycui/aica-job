package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsCnvnTrmnatHist implements Serializable {
	/**
	 * 사업협약해지이력
	 */
	private static final long serialVersionUID = 5873457087705299540L;

	 /** 사업협약해지이력ID */
	String  bsnsCnvnTrmnatHistId;
	 /** 사업협약ID */
	String  bsnsCnvnId;
	 /** 협약해지처리구분코드 */
	String  cnvnTrmnatProcessDivCd;
	 /** 협약해지처리구분코드명 */
	String  cnvnTrmnatProcessDivNm;
	 /** 사유내용 */
	String  resnCn;
	 /** 처리자명 */
	String  creatorNm;
	 /** char_처리일시 */
	String charCreatedDt;


	/** 순번 */
	private int rn;
	/** 생성자ID */
	private String creatorId;
	/** 생성일시 */
	private Date createdDt;

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
