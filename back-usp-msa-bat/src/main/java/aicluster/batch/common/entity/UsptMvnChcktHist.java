package aicluster.batch.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsptMvnChcktHist implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4547367723378772584L;

	private String  histId       ;                  /** 이력ID : hist- */
    private Date    histDt       ;                  /** 이력일시 */
    private String  checkoutReqId;                  /** 퇴실신청ID */
    private String  workTypeNm   ;                  /** 작업구분명 */
    private String  workCn       ;                  /** 작업내용 */
    private String  workerId     ;                  /** 작업자ID : CMMV_USER.MEMBER_ID */
    private String  workerNm     ;                  /** 작업자명 : CMMV_USER.MEMBER_NM */

    public String getFmtHistDt() {
      if (this.histDt == null) {
        return null;
      }
      return date.format(this.histDt, "yyyy-MM-dd HH:mm:ss");
    }
    
    public Date getHistDt() {
		if (this.histDt != null) {
			return new Date(this.histDt.getTime());
		}
		return null;
	}
	
	public void setHistDt(Date histDt) {
		this.histDt = null;
		if (histDt != null) {
			this.histDt = new Date(histDt.getTime());
		}
	}
}
