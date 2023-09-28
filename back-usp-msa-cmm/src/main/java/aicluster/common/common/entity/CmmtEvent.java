package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtEvent implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6892923799416157764L;
	private String eventId;
	private String eventNm;
	private Boolean posting;
	private String beginDay;
	private String endDay;
	private String pcThumbnailFileId;
	private String mobileThumbnailFileId;
	private String thumbnailAltCn;
	private String pcImageFileId;
	private String mobileImageFileId;
	private String imageAltCn;
	private String url;
	private Boolean newWindow;
	private String attachmentGroupId;
	@JsonIgnore
	private String creatorId;
	@JsonIgnore
	private Date createdDt;
	@JsonIgnore
	private String updaterId;
	@JsonIgnore
	private Date updatedDt;

	private Long readCnt;

	/*
	 * Helper
	 */
	private List<CmmtEventCn> eventCnList;

	public String getFmtBeginDay() {
		if (string.isBlank(this.beginDay)) {
			return null;
		}

		return date.format(string.toDate(this.beginDay), "yyyy-MM-dd");
	}

	public String getFmtEndDay() {
		if (string.isBlank(this.endDay)) {
			return null;
		}

		return date.format(string.toDate(this.endDay), "yyyy-MM-dd");
	}

	public String getFmtCreatedDay() {
		if (this.createdDt == null) {
			return null;
		}

		return date.format(this.createdDt, "yyyy-MM-dd");
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
	
	public List<CmmtEventCn> getEventCnList() {
		List<CmmtEventCn> eventCnList = new ArrayList<>();
		if(this.eventCnList != null) {
			eventCnList.addAll(this.eventCnList);
		}
		return eventCnList;
	}

	public void setEventCnList(List<CmmtEventCn> eventCnList) {
		this.eventCnList = new ArrayList<>();
		if(eventCnList != null) {
			this.eventCnList.addAll(eventCnList);
		}
	}
}
