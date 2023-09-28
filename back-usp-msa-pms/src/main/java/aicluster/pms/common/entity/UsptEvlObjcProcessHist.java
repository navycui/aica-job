package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
@Data
public class UsptEvlObjcProcessHist implements Serializable {
	private static final long serialVersionUID = 3182160955351742157L;
	/** 평가이의처리이력ID */
	private String evlObjcProcessHistId;
	/** 평가선정이의신청ID */
	private String evlSlctnObjcReqstId;
	/** 선정이의처리상태코드(G:SLCTN_OBJC_PROCESS_STTUS) */
	private String slctnObjcProcessSttusCd;
	/** 사유내용 */
	private String resnCn;
	/** 생성자ID */
	private String creatorId;
	/** 생성일시 */
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
