package aicluster.common.api.board.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetArticlesParam implements Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = -1273977268044828196L;
	private String boardId;
	private Boolean posting;
	private Date beginDt;
	private Date endDt;
	private String title;
	private String categoryCd;
	private String articleSrchCd;
	private String articleSrchWord;

	public String getBeginDay() {
		return date.format(beginDt, "yyyyMMdd");
	}

	public String getEndDay() {
		return date.format(endDt, "yyyyMMdd");
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
}
