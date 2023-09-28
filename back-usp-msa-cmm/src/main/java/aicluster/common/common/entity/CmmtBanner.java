package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtBanner implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1462228579447945575L;
	private String  bannerId;                       /** BANNER_ID */
	private String  bannerNm;                       /** 배너명 */
	private String  systemId;                       /** 시스템ID */
	private String  bannerType;                     /** 배너구분(G:BANNER_TYPE) */
	private Date    beginDt;                        /** 시작일시 */
	private Date    endDt;                          /** 종료일시 */
	private String  targetUrl;                      /** 대상 URL */
	private Boolean newWindow;                      /** 새창여부 */
	private Boolean animation;                      /** 애니메이션여부(1:여,0:부) */
	private String  textHtml;                       /** 텍스트 HTML */
	private String  pcImageFileId;                  /** PC 이미지 파일 ID */
	private String  mobileImageFileId;              /** 모바일 이미지 파일ID */
	private String  imageAltCn;                     /** 이미지 ALT태그 내용 */
	private Long    sortOrder;                      /** 정렬순서 */
	private Boolean enabled;                        /** 사용여부 */
	@JsonIgnore
	private String  creatorId;                      /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt;                      /** 생성일시 */
	@JsonIgnore
	private String  updaterId;                      /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt;                      /** 수정일시 */
	private Long    rn;

	public String getFmtBeginDt() {
		if (this.beginDt == null) {
			return null;
		}

		return date.format(this.beginDt, "yyyy-MM-dd HH:mm");
	}

	public String getFmtEndDt() {
		if (this.endDt == null) {
			return null;
		}
		return date.format(this.endDt, "yyyy-MM-dd HH:mm");
	}

	public String getFmtBeginDay() {
		if (this.beginDt == null) {
			return null;
		}

		return date.format(this.beginDt, "yyyy-MM-dd");
	}

	public String getFmtEndDay() {
		if (this.endDt == null) {
			return null;
		}
		return date.format(this.endDt, "yyyy-MM-dd");
	}

	public String getFmtBeginTm() {
		if (this.beginDt == null) {
			return null;
		}

		return date.format(this.beginDt, "HH:mm");
	}

	public String getFmtEndTm() {
		if (this.endDt == null) {
			return null;
		}
		return date.format(this.endDt, "HH:mm");
	}

	public String getFmtCreatedDay() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd");
	}

	public String getFmtCreatedDt() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd HH:mm:ss");
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
