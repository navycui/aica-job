package aicluster.batch.common.entity;

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
public class CmmtMberMtUnitStats implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5864815742904121398L;
	private String statsYm;
	private String memberType;
	private Long totalMbrCnt;
	private Long joinMbrCnt;
	private Long secessionMbrCnt;
	private Long dormantMbrCnt;
	private Long blackMbrCnt;
	private Date createdDt;
	
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
