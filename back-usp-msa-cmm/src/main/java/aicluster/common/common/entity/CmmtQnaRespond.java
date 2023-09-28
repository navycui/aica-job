package aicluster.common.common.entity;

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
public class CmmtQnaRespond implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8938367906514298837L;
	private String qnaId;
	private String memberId;
	private String creatorId;
	private Date createdDt;

	/*
	 * Helper
	 */
	private String loginId;
	private String memberNm;
	private String deptNm;
	private String positionNm;
	
	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
		}
		return null;
	}
	
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}
}
