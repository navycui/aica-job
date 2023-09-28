package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.mvn.common.dto.AlrsrcDstbHistDto;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.masking;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsptResrceAsgnEntrps implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2575508379879308651L;

	private String  alrsrcId     ;              /** 자원할당ID */
	private String  evlLastSlctnId;				/** 평가최정선정ID : USPT_BSNS_SLCTN.EVL_LAST_SLCTN_ID */
	private String  lastSlctnTrgetId;           /** 최종선정대상ID : USPT_BSNS_SLCTN.LAST_SLCTN_TRGET_ID */
	private String  bsnsSlctnId  ;              /** 사업선정대상ID : USPT_BSNS_SLCTN.BSNS_SLCTN_ID */
	private String  receiptNo	 ;              /** 접수번호 : USPT_BSNS_PBLANC_APPLCNT.RECEIPT_NO */
	private String  cmpnyId      ;              /** 업체ID : CMMT_MEMBER.MEMBER_ID */
	private String  cmpnyNm      ;              /** 업체명 : CMMT_MEMBER.MEMBER_NM */
	private boolean ordtmRcrit	 ;              /** 상시모집여부 : USPT_BSNS_PBLANC.ORDTM_RCRIT	  */
	private String  pblancNm	 ;              /** 공고명 : USPT_BSNS_PBLANC.PBLANC_NM */
	private String  ceoNm        ;              /** 대표자명 : CMMT_MEMBER.CEO_NM */
	private String  bizrno       ;              /** 사업자번호 : CMMT_MEMBER.BIZRNO */
	private String  jurirno      ;              /** 법인등록번호 : CMMT_MEMBER.JURIRNO */
	private String  chargerNm	 ;              /** 담당자명 : CMMT_MEMBER.CHARGER_NM */
	@JsonIgnore
	private String  encMobileNo	 ;              /** 휴대폰번호 : CMMT_MEMBER.ENC_MOBIL_NO */
	@JsonIgnore
	private String  encEmail	 ;              /** 이메일 : CMMT_MEMBER.ENC_EMAIL */
	private String  alrsrcBgngDay;              /** 자원할당시작일 */
	private String  alrsrcEndDay ;              /** 자원할당종료일 */
	private String  alrsrcSt     ;              /** 자원할당상태(G:ALRSRC_ST) */
	private String  alrsrcStNm   ;              /** 자원할당상태명 : CMMT_CODE.CODE_NM */
	private Date    alrsrcStDt   ;              /** 자원할당상태변경일시 */
	private String  reasonCn     ;              /** 사유내용 */
	@JsonIgnore
	private String  creatorId    ;              /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt    ;              /** 생성일시 */
	@JsonIgnore
	private String  updaterId    ;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt    ;              /** 수정일시 */

	private List<UsptResrceInvntryInfo>		alrsrcFninfList;		/** 자원재고목록 */
	private List<UsptResrceAsgnDstb>		alrsrcDstbList;			/** 자원할당배분목록 */
	private List<AlrsrcDstbHistDto>			alrsrcDstbHist;			/** 자원할당배분이력 */

	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		return masking.maskingMobileNo(aes256.decrypt(this.encMobileNo, this.cmpnyId));
	}

	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return masking.maskingEmail(aes256.decrypt(this.encEmail, this.cmpnyId));
	}

	public String getFmtAlrsrcBgngDay() {
		if (!string.isBlank(this.alrsrcBgngDay) && !date.isValidDate(this.alrsrcBgngDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.alrsrcBgngDay), "yyyy-MM-dd");
	}

	public String getFmtAlrsrcEndDay() {
		if (!string.isBlank(this.alrsrcEndDay) && !date.isValidDate(this.alrsrcEndDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.alrsrcEndDay), "yyyy-MM-dd");
	}

	public String  getFmtAlrsrcStDt() {
		if (this.alrsrcStDt == null) {
			return null;
		}
		return date.format(this.alrsrcStDt, "yyyy-MM-dd HH:mm:ss");
	}
}
