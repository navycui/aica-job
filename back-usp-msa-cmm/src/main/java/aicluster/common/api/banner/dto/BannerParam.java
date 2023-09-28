package aicluster.common.api.banner.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 347496684019659856L;

	private String  bannerId;                       /** BANNER_ID */
	private String  bannerNm;                       /** 배너명 */
	private String  systemId;                       /** 시스템ID(G:SYSTEM_ID) */
	private String  bannerType;                     /** 배너구분(G:BANNER_TYPE) */
	private Date    beginDt;                        /** 시작일시 */
	private Date    endDt;                          /** 종료일시 */
	private String  targetUrl;                      /** 대상 URL */
	private Boolean newWindow;                      /** 새창여부(1:여,0:부) */
	private Boolean animation;                      /** 애니메이션여부(1:여,0:부) */
	private String  textHtml;                       /** 텍스트 HTML */
	private String  imageAltCn;                     /** 이미지 ALT태그 내용 */
	private Long    sortOrder;                      /** 정렬순서 */
	private Boolean enabled;                        /** 사용여부(0:NO,1:YES) */

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
}
