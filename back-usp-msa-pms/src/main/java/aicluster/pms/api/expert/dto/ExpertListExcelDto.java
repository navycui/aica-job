package aicluster.pms.api.expert.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExpertListExcelDto implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -589953750351265472L;
	/**성공여부*/
	String successYn;
	/** 전문가명 */
	String  expertNm;
	 /** 암호화된 생년월일 */
	String  encBrthdy;
	/** 성별코드(G:GENDER) */
	String  genderCd;
	/** 성별코드(G:GENDER) */
	String  genderNm;
	 /** 내국인여부 */
	Boolean nativeYn;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 이메일 */
	String  encEmail;
	/** 직장명 */
	String  wrcNm;
	/** 직위명 */
	String  ofcpsNm;

	/** 전문가분류ID */
	String expertClId;
	/** 전문가분류명 */
	String  expertClNm;
	/** 부모전문가분류ID */
	String  parntsExpertClId;
}
