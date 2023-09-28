package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcDstbHistDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3533403892584608288L;

	private Date    histDt     ;                    /** 이력일시 */
	@JsonIgnore
	private boolean firstDstbYn;					/** 최초배분여부 */
	private String  alrsrcId   ;                    /** 자원할당ID */
    private String  gpuAlrsrcCn		;       /** 가속기 자원할당내용           */
    private String  strgAlrsrcCn	;	       /** 소토리지 자원할당내용         */
    private String  saasAlrsrcCn	;	       /** Saas 자원할당내용             */
    private String  dtlkAlrsrcCn	;	       /** 데이터레이크 자원할당내용     */
	private String  workTypeNm ;                    /** 작업구분명 */
	@JsonIgnore
	private String  workerId   ;                    /** 작업자ID : CMMV_USER.MEMBER_ID */
	private String  workerNm   ;                    /** 작업자명 : CMMV_USER.MEMBER_NM */

	public String getFmtHistDt() {
		return date.format(this.histDt, "yyyy-MM-dd HH:mm:ss");
	}
}
