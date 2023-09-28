package aicluster.pms.api.stdnt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StdntMtchListParam implements Serializable {
	private static final long serialVersionUID = -7110049009835553141L;
	/** 처리상태 (WAIT:대기중, COMPT:완료)*/
	private String processSttus;
	/** 산업분야 (G:INDUST_REALM) */
	private String industRealmCd;
	/** 키워드 검색 */
	private String keyword;
	/** 키워드 검색 구분 (rspnberNm:책임자명, memberNm:사업자명, receiptNo:접수번호) */
	private String keywordDiv;
	/** 로그인 관리자 ID */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
