package aicluster.member.common.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtLoginAtpt implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2745047595319320591L;
	private String memberId;
	private String memberIp;
	private Integer tryCnt;
	private Date updatedDt;

	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
            return new Date(this.updatedDt.getTime());
        }
        return null;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
        if (updatedDt != null) {
            this.updatedDt = new Date(updatedDt.getTime());
        }
	}
}
