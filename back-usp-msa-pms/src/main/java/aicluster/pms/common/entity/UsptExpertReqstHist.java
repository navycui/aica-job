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
public class UsptExpertReqstHist implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4374967267079323602L;

	String  expertReqstHistId;              	/** 전문가신청처리이력 */
	String  expertId;                       		/** 전문가ID */
	String  expertReqstProcessSttusCd;      /** 전문가신청처리상태코드(G:EXPERT_REQST_PROCESS_STTUS) */
	String  resnCn;                        		 /** 사유내용 */

	String  expertReqstProcessSttusNm;      /** 전문가신청처리상태명 */
	String memberNm;						/**처리자명**/
	String regDt;             					/** 처리일시**/
	String regId;             					/** 처리ID**/

	@JsonIgnore
	private String creatorId;		/** 생성자 ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date createdDt;		/** 생성일시 */

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
