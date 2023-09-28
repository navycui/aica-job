package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CmmtSmsEmailSendDto implements Serializable{

	 /**
	 * sms, email send dto
	 */
	private static final long serialVersionUID = -1898092416836602036L;

	 /** 회원ID */;
	String  memberId;
	/** 회원명 */
	String  memberNm;
	/* 이메일*/
	String email;
	/** 휴대폰번호 */
	String mbtlnum;
	  /** 사유내용 */
	String  resnCn;
	 /** 공고명 */
	String  pblancNm;
	/** sms-email 선택 */
	String sendSmsEmail;
}
