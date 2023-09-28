package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlResultListDto implements Serializable {
	private static final long serialVersionUID = -6509112203934498445L;
	/** 평가대상ID */
	private String evlTrgetId;
	/** 접수번호 */
	private String receiptNo;
	/** 평가방식코드 (G: EVL_MTH) */
	private String evlMthdCd;
	/** 선정여부 */
	private Boolean slctn;
	/** 회원명 */
	private String memberNm;
	/** 가점 */
	private Integer addScore;
	/** 감점 */
	private Integer subScore;
	/** 최고점수 */
	private Integer maxEvlScore;
	/** 최저점수 */
	private Integer minEvlScore;
	/** 총점 */
	private Integer sumEvlScore;
	/** 평균 */
	private Float avgEvlScore;
	/** 종합점수 */
	private Float totEvlScore;
	/** 순번 */
	private Long rn;
	/** 순위 */
	@JsonIgnore
	private Integer rankRn;

	/** 평가위원 목록 */
	List<EvlSttusMfcmmListDto> evlSttusMfcmmList;

	@JsonIgnore
	public String getSlctnAtNm() {
		if(this.slctn == null) {
			return "";
		}

		if(this.slctn) {
			return "선정";
		} else {
			return "탈락";
		}
	}
}
