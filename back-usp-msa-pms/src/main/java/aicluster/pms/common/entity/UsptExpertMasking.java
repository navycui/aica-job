package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptExpertMasking implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2971930093801117123L;

	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 전문가ID */
	String  expertId;
	/** 전문가명 */
	String  expertNm;
	/** 성별코드(G:GENDER) */
	String  genderCd;
	/** 성별명(G:GENDER) */
	String  genderNm;
	/** 내국인여부 */
	Boolean nativeYn;
	/** 내국인여부명 */
	String  nativeNm;
	/** 암호화된 생년월일 */
	String  encBrthdy;
	/** 암호화된 휴대폰번호 */
	@JsonIgnore
	String  encMbtlnum;
	/** 암호화된 이메일 */
	@JsonIgnore
	String  encEmail;
	/** 직장명 */
	String  wrcNm;
	/** 부서명 */
	String  deptNm;
	/** 직위명 */
	String  ofcpsNm;
	/** 직무명 */
	String  dtyNm;
	/** 직장주소 */
	String  wrcAdres;
	/** 직장주소상세 */
	String  wrcAdresDetail;
	/** 직장주소우편번호 */
	String  wrcAdresZip;
	/** 직장전화번호 */
	String  wrcTelno;
	/** 최종대학명 */
	String  lastUnivNm;
	/** 대학학부명 */
	String  univDeptNm;
	 /** 활동분야코드(G:ACT_REALM) */
	String  actRealmCd;
	/** 첨부파일그룹ID */
	String  attachmentGroupId;
	/** 전문가신청처리상태코드(G:EXPERT_REQST_PROCESS_STTUS) */
	String  expertReqstProcessSttusCd;
	/** 전문분야명 */
	String  expertClNm;
	/** 제외사유내용 */
	String  exclResnCn;
	/** 추출위원ID */
	String  extrcMfcmmId;
	/** 회원ID */
	String  memberId;
	/**신청일시작**/
	String  reqstDayStart;
	/**신청일종료**/
	String  reqstDayEnd;
	/**신청일자**/
	String  reqstDay;
	/** 섭외결과상태코드 */
	String  lsnResultCd;
	/** 섭외결과상태명 */
	String  lsnResultNm;
	/** 추출차수명 */
	String  odrNm;

	/** 전문가신청처리상태명 */
	String  expertReqstProcessSttusNm;
	/** 처리일시**/
	String  regDt;

	/**사업연도*/
	String bsnsYear;
	/**사업명*/
	String bsnsNm;
	/**공고ID*/
	String pblancId ;
	/**공고명*/
	String pblancNm;
	/**공고번호*/
	String pblancNo;
	/**활동분양*/
	String actRealm;
	/**분과ID*/
	String sectId;
	/**접수차수*/
	String rceptOdr;
	/** 순번 */
	private int rn;

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;


	@JsonIgnore
	private String creatorId;
	@JsonIgnore
	private Date createdDt;
	@JsonIgnore
	String  updaterId;
	@JsonIgnore
	private Date updatedDt;

	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	@JsonIgnore
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.expertId);
	}

	/**
	 * 복호화된 휴대폰번호
	 *
	 * @return
	 */
	@JsonIgnore
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.expertId);
	}

	/**
	 * 복호화된 이메일
	 *
	 * @return
	 */
	@JsonIgnore
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.expertId);
	}

	/** 마스킹 이메일 */
	public String getMaskingEmail() {
		String decryptEmail = this.getEmail();
		if(CoreUtils.string.isBlank(decryptEmail) || decryptEmail.length() < 5) {
			return "";
		}
		return CoreUtils.masking.maskingEmail(decryptEmail);
	}

	/** 마스킹 휴대폰번호 */
	public String getMaskingMbtlnum() {
		String decryptMbtlnum = this.getMbtlnum();
		if(CoreUtils.string.isBlank(decryptMbtlnum) || decryptMbtlnum.length() < 5) {
			return "";
		}
		return CoreUtils.masking.maskingMobileNo(decryptMbtlnum);
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
