package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CmmtQna implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1305010762425671956L;
	private String qnaId;
	private String qnaNm;
	private String systemId;
	private Long articleCnt;
	private Boolean category;
	private String categoryCodeGroup;
	private Boolean attachable;
	private Long attachmentSize;
	private String attachmentExt;
	private Boolean enabled;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;

	/*
	 * Helper
	 */
	private List<CmmtQnaRespond> answererList;
	
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
	
	public List<CmmtQnaRespond> getAnswererList() {
		List<CmmtQnaRespond> answererList = new ArrayList<>();
		if(this.answererList != null) {
			answererList.addAll(this.answererList);
		}
		return answererList;
	}

	public void setAnswererList(List<CmmtQnaRespond> answererList) {
		this.answererList = new ArrayList<>();
		if(answererList != null) {
			this.answererList.addAll(answererList);
		}
	}
}
