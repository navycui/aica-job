package aicluster.pms.api.bsns.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.common.entity.UsptBsnsApplyRealm;
import aicluster.pms.common.entity.UsptBsnsAppnTask;
import lombok.Data;

@Data
public class BsnsTaskDto implements Serializable {
	private static final long serialVersionUID = -2652142427983006421L;
	/** 사업코드 */
	@JsonIgnore
	private String bsnsCd;
	/** 과제유형코드 */
	private String taskTypeCd;
	/** 지원분야 */
	List<UsptBsnsApplyRealm> bsnsApplyRealmList;
	/** 지정과제 */
	List<UsptBsnsAppnTask> bsnsAppnTaskList;
	/** 수정자ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
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
