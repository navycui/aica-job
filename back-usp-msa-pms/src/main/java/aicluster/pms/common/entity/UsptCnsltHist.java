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
public class UsptCnsltHist implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6774097412612536955L;
	/** 순번 */
	private int rn;

	/** 이력ID */
	String  histId;
	/** 추출위원ID */
	String  extrcMfcmmId;
	/** 통화날짜 */
	String  talkDe;
	/** 통화시간 */
	String  talkTime;
	/** 통화분 */
	String  talkMin;
	/** 통화일시 */
	Date    talkDt;
	/** 통화일시명 */
	String  talkDtNm;
	/** 섭외결과코드(G:LIAISON_RESULT) */
	String  lsnResultCd;
	/** 섭외결과명 */
	String  lsnResultNm;
	/** 상담내용 */
	String  cnsltCn;
	/** 등록자명 */
	String  registerNm;
	/** 등록일시 */
	String  registDt;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
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
