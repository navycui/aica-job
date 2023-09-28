package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtPopupNotice implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8793589546046429659L;

	private String  popupId    ;                    /** 팝업ID */
	private String  systemId   ;                    /** 시스템ID */
	private String  popupType  ;                    /** 팝업구분(G:POPUP_TYPE) */
	private String  title      ;                    /** 제목 */
	private String  article    ;                    /** 내용 */
	private Boolean posting    ;                    /** 게시여부 */
	private Date    beginDt    ;                    /** 시작일시 */
	private Date    endDt      ;                    /** 종료일시 */
	private Long    popupWidth ;                    /** 팝업폭 */
	private Long    popupHeight;                    /** 팝업높이 */
	private String  imageFileId;                    /** 이미지파일ID */
	private String  imageAltCn ;
	private String  linkUrl    ;                    /** 링크URL */
	private Boolean newWindow  ;                    /** 새창여부 */
	@JsonIgnore
	private String  creatorId  ;                    /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt  ;                    /** 생성일시 */
	@JsonIgnore
	private String  updaterId  ;                    /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt  ;                    /** 수정일시 */

	private String popupTypeNm;
	
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
