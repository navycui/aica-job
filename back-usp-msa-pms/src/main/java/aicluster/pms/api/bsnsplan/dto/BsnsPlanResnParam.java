package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class BsnsPlanResnParam implements Serializable{
	/**
	 * 사업계획서 보안요청 전송_admin
	 */
	private static final long serialVersionUID = -4330336150291645360L;
	/** 사업계획서ID */
	String  bsnsPlanDocId;
	 /** 사업선정대상ID */
	String  bsnsSlctnId;
	/** 보완요청내용 */
	String  makeupReqCn;
	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
	String  planPresentnSttusCd;

	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;

}
