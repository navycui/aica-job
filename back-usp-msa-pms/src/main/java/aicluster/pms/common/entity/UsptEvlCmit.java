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
public class UsptEvlCmit implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6215902905978799501L;
	/** 평가위원회ID */
	private String evlCmitId;
	/** 분과ID */
	private String sectId;
	/** 평가단계ID */
	private String evlStepId;
	/** 평가표ID */
	private String evlTableId;
	/** 평가표명 */
	private String evlTableNm;
	/** 평가위원회명 */
	private String evlCmitNm;
	/** 위원수 */
	private Integer mfcmmCo;
	/** 평가예정일 */
	private String evlPrarnde;
	/** 시작시간 */
	private String beginHour;
	/** 종료시간 */
	private String endHour;
	/** 평가장소 */
	private String evlPlace;
	/** 간사ID(CMMT_INSIDER.MEMBER_ID) */
	private String orgnzrId;

	/** 간사명 */
	private String orgnzrNm;

	/** 평가위원장종합의견내용 */
	private String evlCharmnOpinionCn;
	/** 평가상태코드(G:EVL_ST) */
	private String evlSttusCd;
	/** 평가상태일시 */
	private Date evlSttusDt;
	/** 선정통보여부 */
	private Boolean slctnDspth;
	/** 선정 통보일시 */
	private Date slctnDspthDt;
	/** 온라인여부 */
	private Boolean online;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 선정발송방법 */
	private String slctnSndngMth;
	/** 선정발송내용 */
	private String slctnSndngCn;
	/** 선정SMSID */
	private String slctnSmsId;
	/** 선정이메일ID */
	private String slctnEmailId;
	/** 탈락통보여부 */
	private Boolean ptrmsnDspth;
	/** 탈락통보일시 */
	private Date ptrmsnDspthDt;
	/** 탈락발송방법 */
	private String ptrmsnSndngMth;
	/** 탈락발송내용 */
	private String ptrmsnSndngCn;
	/** 탈락SMSID */
	private String ptrmsnSmsId;
	/** 탈락이메일ID */
	private String ptrmsnEmailId;
	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자 ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 분과명 */
	String  sectNm;
	/** 평가단계명 */
	String  evlStepNm;
	/** 평가상태명*/
	String  evlSttusNm;
	/** 선정통보여부명 */
	String  slctnDspthNm;
	/** 탈락통보여부명 */
	String  ptrmsnDspthNm;

	public Date getEvlSttusDt() {
		if (this.evlSttusDt != null) {
			return new Date(this.evlSttusDt.getTime());
         }
		return null;
	}
	public void setEvlSttusDt(Date evlSttusDt) {
		this.evlSttusDt = null;
		if (evlSttusDt != null) {
			this.evlSttusDt = new Date(evlSttusDt.getTime());
		}
	}

	public Date getSlctnDspthDt() {
		if (this.slctnDspthDt != null) {
			return new Date(this.slctnDspthDt.getTime());
         }
		return null;
	}
	public void setSlctnDspthDt(Date slctnDspthDt) {
		this.slctnDspthDt = null;
		if (slctnDspthDt != null) {
			this.slctnDspthDt = new Date(slctnDspthDt.getTime());
		}
	}

	public Date getPtrmsnDspthDt() {
		if (this.ptrmsnDspthDt != null) {
			return new Date(this.ptrmsnDspthDt.getTime());
         }
		return null;
	}
	public void setPtrmsnDspthDt(Date ptrmsnDspthDt) {
		this.ptrmsnDspthDt = null;
		if (ptrmsnDspthDt != null) {
			this.ptrmsnDspthDt = new Date(ptrmsnDspthDt.getTime());
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
