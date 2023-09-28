package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class EvlSystemDto implements Serializable {

	/**
	 * 평가시스템
	 */
	private static final long serialVersionUID = -4570979767923841116L;
	/** 평가위원ID */
	private String evlMfcmmId;
	/** 평가위원회ID */
	private String evlCmitId;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 공고명 */
	private String pblancNm;
	/** 평가단계명 */
	private String evlStepNm;
	/** 평가위원회명 */
	private String evlCmitNm;
    /** 위원장여부 */
	private Boolean charmn;
    /** 체크리스트여부 */
	private Boolean chklstChkAt;
	/** 출석상태코드(G:ATEND_STTUS) */
	private String atendSttusCd;
	/** 출석일시 */
	private Date atendDt;
	/** 회피동의여부 */
	private Boolean evasAgreAt;
	/** 회피사유내용 */
	private String evasResnCn;

	public Date getAtendDt() {
		if (this.atendDt != null) {
			return new Date(this.atendDt.getTime());
         }
		return null;
	}
	public void setAtendDt(Date atendDt) {
		this.atendDt = null;
		if (atendDt != null) {
			this.atendDt = new Date(atendDt.getTime());
		}
	}
}
