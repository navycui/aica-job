package aicluster.pms.api.stdnt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StdntListParam implements Serializable {
	private static final long serialVersionUID = -7216383061343611190L;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
	/** 희망직무 */
	private String hopeDtyCd;
	/** 접수번호 */
	private String receiptNo;
	/** 이름 */
	private String memberNm;
	/** 접수시작일 */
	private String rceptStartDate;
	/** 접수종료일 */
	private String rceptEndDate;

	private Long beginRowNum;
	private Long itemsPerPage;
}
