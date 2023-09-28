package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class FrontBsnsPlanResnDto implements Serializable{
	/**
	 * 사업계획서 보안요청_front
	 */
	private static final long serialVersionUID = -919389982121416660L;
	 /** 사업계획서ID */
	String  bsnsPlanDocId;
	 /** 사업선정대상ID */
	String  bsnsSlctnId;
	/** 보완요청내용 */
	String  makeupReqCn;
	 /** 보완요청파일그룹ID */
	String  makeupReqFileGroupId;
	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
	String  planPresentnSttusCd;
	/** 요청일시 */
	String requestDt;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
}
