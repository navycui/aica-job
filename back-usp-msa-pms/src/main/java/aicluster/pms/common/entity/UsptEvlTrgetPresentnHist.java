package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlTrgetPresentnHist implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 4326865297892750050L;

	private int rn;							/** 순번 */
	 /** 제출이력ID */
	String  presentnHistId;
	/** 평가대상ID */
	String  evlTrgetId;
	/** 발표자료처리구분코드(G:PRESNATN_PROCESS_DIV) */
	String  presnatnProcessDivCd;
	/** 발표자료처리구분명 */
	String  presnatnProcessDivNm;
	/** 처리일시 */
	Date    processDt;
	/** String_처리일시 */
	String  stProcessDt;

	/** 회원ID */
	String  memberId;
	/** 사유내용 */
	String  resnCn;
	/** 처리자id*/
	String  opetrId;
	/** 처리자명*/
	String  opetrNm;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */

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
