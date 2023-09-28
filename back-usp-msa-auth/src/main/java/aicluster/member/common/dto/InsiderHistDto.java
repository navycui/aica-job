package aicluster.member.common.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsiderHistDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 608669773552186281L;
	private String histId;
	private Date histDt;
	private String memberId;
	private String memberNm;	/** 회원명 */
	private String workerId;
	private String workCn;

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
