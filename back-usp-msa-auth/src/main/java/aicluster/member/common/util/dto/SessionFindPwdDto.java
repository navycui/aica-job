package aicluster.member.common.util.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionFindPwdDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5323324177170388782L;
	private String memberId;
	private String mobileNo;
	private String email;
	private String certNo;
	private Date certExpiredDt;
	private boolean checked;

	public Date getCertExpiredDt() {
		if (this.certExpiredDt != null) {
            return new Date(this.certExpiredDt.getTime());
        }
        return null;
	}

	public void setCertExpiredDt(Date certExpiredDt) {
		this.certExpiredDt = null;
        if (certExpiredDt != null) {
            this.certExpiredDt = new Date(certExpiredDt.getTime());
        }
	}
}
