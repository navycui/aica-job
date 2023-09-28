package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class UsptExpertParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1778208862867675245L;

	/** 전문가ID */
	String  expertId;
	/** 전문가명 */
	String  expertNm;
	/** 직장명 */
	String  wrcNm;

	/** 암호화된 생년월일 */
	String  encBrthdy;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 이메일 */
	String  encEmail;
	/** 전문가분류ID */
	String[] expertClId;
	/** 신청조회여부 */
	String  expertReqstYn;

	/** 전문가신청처리상태코드(G:EXPERT_REQST_PROCESS_STTUS) */
	String  expertReqstProcessSttusCd;

	/**신청일시작**/
	String  reqstDayStart;
	/**신청일종료**/
	String  reqstDayEnd;

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;


	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
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
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.expertId);
	}

}
