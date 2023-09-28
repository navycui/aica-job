package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class HistDto implements Serializable {
	private static final long serialVersionUID = 4746130855567950415L;
	/** 순번 */
	private String rn;
	/** 처리상태 */
	private String processSttus;
	/** 사유내용 */
	private String resnCn;
	/** 로그인ID */
	private String loginId;
	/** 처리자 */
	private String memberNm;

	/** 처리일시 */
	@JsonIgnore
	private Date createdDt;
	public String getProcessDate() {
		if(this.createdDt == null) {
			return "";
		} else {
			return date.format(this.createdDt, "yyyy-MM-dd HH:mm");
		}
	}

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
