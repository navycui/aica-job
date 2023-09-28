package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CnvnChangeChangeIemDivDto implements Serializable{
	/**
	 *  협약변경내역 상세_협약변경항목
	 */
	private static final long serialVersionUID = 589493430531011091L;

	 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
	String  changeIemDivCd;
	 /** 협약변경항목구분명 */
	String  changeIemDivNm;
	/** 협약변경요청ID */
	String  cnvnChangeReqId;
}
