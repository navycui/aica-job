package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontInfoNtcnEduListDto implements Serializable {
	private static final long serialVersionUID = 977742376094758963L;
	/** 강의ID */
	private String olecId;
	/** 강의명 */
	private String lecNm;
	/** 겅의내용 */
	private String lecIntro;
	/** 신청시작날짜 */
	private String apyFrom;
	/** 신청종료날짜 */
	private String apyTo;
	/** 교육인정시간 */
	private String lecTime;
	/** 강의상태 (OK:강의, COS:과정) */
	private String lecType;
	/** 강의챕터수 */
	private String chpCnt;
	/** 이미지파일 경로 */
	private String imgFile;
}
