package aicluster.common.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopupListItem implements Serializable {

	private static final long serialVersionUID = 4512526969153859818L;

	private String  popupId    ;                    /** 팝업ID */
	private String  systemId   ;                    /** 시스템ID */
	private String  popupType  ;                    /** 팝업구분(G:POPUP_TYPE) */
	private String  popupTypeNm;
	private String  title      ;                    /** 제목 */
	private Boolean posting    ;                    /** 게시여부 */
	private Date    beginDt    ;                    /** 시작일시 */
	private Date    endDt      ;                    /** 종료일시 */
	@JsonIgnore
	private String  creatorId  ;                    /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	private String 	creatorNm  ;
	private Date    createdDt  ;                    /** 생성일시 */
	private Long	rn;

	public String getFmtBeginDt() {
		if (this.beginDt == null) {
			return null;
		}
		return date.format(this.beginDt, "yyyy-MM-dd");
	}

	public String getFmtEndDt() {
		if (this.endDt == null) {
			return null;
		}
		return date.format(this.endDt, "yyyy-MM-dd");
	}

	public String getFmtCreatedDt() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd HH:mm:ss");
	}
	public String getFmtCreatedDay() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd");
	}
	
	public Date getBeginDt() {
		if (this.beginDt != null) {
			return new Date(this.beginDt.getTime());
		}
		return null;
	}
	
	public void setBeginDt(Date beginDt) {
		this.beginDt = null;
		if (beginDt != null) {
			this.beginDt = new Date(beginDt.getTime());
		}
	}
	
	public Date getEndDt() {
		if (this.endDt != null) {
			return new Date(this.endDt.getTime());
		}
		return null;
	}
	
	public void setEndDt(Date endDt) {
		this.endDt = null;
		if (endDt != null) {
			this.endDt = new Date(endDt.getTime());
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
