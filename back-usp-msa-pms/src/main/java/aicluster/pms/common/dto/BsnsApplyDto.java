package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplyChklst;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import lombok.Data;

@Data
public class BsnsApplyDto implements Serializable {
	private static final long serialVersionUID = -4566365186085944155L;

	private String applyId;
	/** 접수번호(BA + 8자리 순번) */
	private String receiptNo;
	/** 접수상태코드(G:RCEPT_STTUS) */
	private String rceptSttusCd;
	/** 접수상태 */
	private String rceptSttus;
	/** 사업명 */
	private String bsnsNm;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 공고번호 */
	private String pblancNo;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업기간 */
	private String bsnsPd = "";
	/** 사업기간(전체) */
	private String bsnsPdAll = "";
	/** 사업기간(당해) */
	private String bsnsPdYw = "";
	/** 사업유형코드 */
	private String bsnsTypeCd;
	/** 사업유형명 */
	private String bsnsTypeNm;
	/** 회원유형 */
	private String memberType;

	/** 필수확인사항 목록 조회 */
	List<UsptBsnsPblancApplyChklst> chkList;
	/** 과제정보 */
	UsptBsnsPblancApplyTask taskInfo;
	/** 첨부파일 목록 */
	List<ApplyAttachDto> atchmnflList;
	/** 참여인력 목록 */
	List<UsptBsnsPblancApplyTaskPartcpts> partcptslist;
	/** 신청자정보 */
	private CmmtMember cmmtMember;
	/** 신청자 기업정보 */
	private UsptBsnsPblancApplcntEnt applcntEnt;
}
