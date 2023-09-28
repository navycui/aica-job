package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class LastSlctnProcessHistDto implements Serializable {
	private static final long serialVersionUID = 6658759752211718897L;
	/** 순번 */
	private Long rn;
	/** 최종선정처리구분*/
	private String lastSlctnProcessDiv;
	/** 사유내용 */
	private String resnCn;
	/** 처리자명 */
	private String memberNm;
	/** 처리자ID */
	private String loginId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	public String getProcessDate() {
		if(this.createdDt == null) {
			return "";
		}
		return CoreUtils.date.format(this.createdDt, "yyyy-MM-dd");
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
