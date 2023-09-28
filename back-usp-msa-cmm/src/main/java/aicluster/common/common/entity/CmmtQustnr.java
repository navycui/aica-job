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
public class CmmtQustnr implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2668199074568235300L;
	private String surveyId;
	private String surveyNm;
	private String systemId;
	private String beginDay;
	private String endDay;
	private String remark;
	private Boolean enabled;
	private Boolean duplicated;
	@JsonIgnore
	private String creatorId;
	private Date createdDt;
	@JsonIgnore
	private String updaterId;
	@JsonIgnore
	private Date updatedDt;

	private String systemNm;
	private String creatorNm;
	private String surveyStNm;

	private List<CmmtQustnrQesitm> questionList;


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
	
	public List<CmmtQustnrQesitm> getQuestionList() {
		List<CmmtQustnrQesitm> questionList = new ArrayList<>();
		if(this.questionList != null) {
			questionList.addAll(this.questionList);
		}
		return questionList;
	}

	public void setQuestionList(List<CmmtQustnrQesitm> questionList) {
		this.questionList = new ArrayList<>();
		if(questionList != null) {
			this.questionList.addAll(questionList);
		}
	}
}
