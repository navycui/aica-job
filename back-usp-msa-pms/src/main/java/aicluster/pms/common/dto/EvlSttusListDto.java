package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class EvlSttusListDto implements Serializable {
	private static final long serialVersionUID = 8350019862810309857L;
	/** 평가대상ID */
	private String evlTrgetId;
	/** 접수번호 */
	private String receiptNo;
	/** 평가방식코드 (G: EVL_MTH) */
	private String evlMthdCd;
	/** 회원명 */
	private String memberNm;
	/** 가점 */
	private Integer addScore;
	/** 가점 내용 */
	private String addCn;
	/** 감점 */
	private Integer subScore;
	/** 감정 내용 */
	private String subCn;
	/** 순번 */
	private Long rn;
	/** 평가위원 목록 */
	List<EvlSttusMfcmmListDto> evlSttusMfcmmList;
}
