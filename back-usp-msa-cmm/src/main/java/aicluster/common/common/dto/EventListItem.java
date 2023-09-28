package aicluster.common.common.dto;

import java.io.Serializable;
import java.util.Date;

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
public class EventListItem implements Serializable {

	private static final long serialVersionUID = 4792544160794467362L;

	private String eventId;
	private String eventNm;
	private Boolean posting;
	private String beginDay;
	private String endDay;
	private String pcThumbnailFileId;
	private String mobileThumbnailFileId;
	private String thumbnailAltCn;
	private Long readCnt;
	@JsonIgnore
	private String creatorId;
	private String creatorNm;
	private Date createdDt;
	private Long rn;

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
