package aicluster.mvn.common.entity;

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
public class UsptResrceAsgnDstbHist implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7531005122098127633L;

	private String  histId     ;                    /** 이력ID : hist- */
	private Date    histDt     ;                    /** 이력일시 */
	@JsonIgnore
	private boolean firstDstbYn;					/** 최초배분여부 */
	private String  alrsrcId   ;                    /** 자원할당ID */
	private String  rsrcId     ;                    /** 자원ID */
	private String  rsrcGroupCd;
	private String  rsrcGroupNm;
	private String  rsrcTypeNm ;
	private boolean rsrcUseYn  ;                    /** 자원사용여부 */
	private int     rsrcDstbQy ;                    /** 자원배분량 */
	private String  rsrcDstbCn ;                    /** 자원배분내용 */
	private String  workTypeNm ;                    /** 작업구분명 */
	private String  workerId   ;                    /** 작업자ID : CMMV_USER.MEMBER_ID */
	private String  workerNm   ;                    /** 작업자명 : CMMV_USER.MEMBER_NM */

	public String getFmtHistDt() {
		return date.format(this.histDt, "yyyy-MM-dd HH:mm:ss");
	}
}
