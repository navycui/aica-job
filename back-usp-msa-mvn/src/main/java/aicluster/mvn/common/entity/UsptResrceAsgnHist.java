package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsptResrceAsgnHist implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 5304504653544503295L;

	private String  histId    ;                     /** 이력ID : hist- */
    private Date    histDt    ;                     /** 이력일시 */
    private String  alrsrcId  ;                     /** 자원할당ID */
    private String  workTypeNm;                     /** 작업구분명 */
    private String  workCn    ;                     /** 작업내용 */
    private String  workerId  ;                     /** 작업자ID : CMMV_USER.MEMBER_ID */
    private String  workerNm  ;                     /** 작업자명 : CMMV_USER.MEMBER_NM */

	public String getFmtHistDt() {
		return date.format(this.histDt, "yyyy-MM-dd HH:mm:ss");
	}
}
