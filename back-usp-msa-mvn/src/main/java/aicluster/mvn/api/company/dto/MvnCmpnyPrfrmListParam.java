package aicluster.mvn.api.company.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MvnCmpnyPrfrmListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6906537224836818692L;

	private String sbmsnYm     ;    /* 제출년월 */
	private String rsltSttusCd ;    /* 성과상태코드(G:RSLT_STTUS) */
	private String bnoRoomNo   ;    /* 입주호실 */
	private String cmpnyNm     ;    /* 입주업체명 */

}
