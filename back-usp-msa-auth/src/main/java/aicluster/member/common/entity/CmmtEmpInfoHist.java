package aicluster.member.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtEmpInfoHist implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7991152251981031073L;
	private String histId;		/** 이력ID */
	private Date   histDt;		/** 이력일시 */
	private String memberId;	/** 회원ID */
	private String memberNm;	/** 회원명 */
	private String workerId;	/** 작업자ID */
    private String workerNm;	/** 작업자명 */
	private String workCn;		/** 작업내용 */

	public String getFmtHistDt() {
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
