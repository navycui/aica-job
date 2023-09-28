package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CmmtBbs implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5651152886391978552L;
	private String boardId;
	private String systemId;
	private String boardNm;
	private Long articleCnt;
	private Boolean enabled;
	@JsonIgnore
	private Boolean webEditor;
	private Boolean noticeAvailable;
	private Boolean commentable;
	private Boolean category;
	private String categoryCodeGroup;
	private Boolean attachable;
	private Long attachmentSize;
	private String attachmentExt;
	private Boolean useSharedUrl;
	private Boolean useThumbnail;
	private Boolean useForm;
	private Boolean allReadable;
	@JsonIgnore
	private String creatorId;
	@JsonIgnore
	private Date createdDt;
	@JsonIgnore
	private String updaterId;
	@JsonIgnore
	private Date updatedDt;

	private List<CmmtBbsAuthor> authorityList;
	
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
	
	public List<CmmtBbsAuthor> getAuthorityList() {
		List<CmmtBbsAuthor> authorityList = new ArrayList<>();
		if(this.authorityList != null) {
			authorityList.addAll(this.authorityList);
		}
		return authorityList;
	}

	public void setAuthorityList(List<CmmtBbsAuthor> authorityList) {
		this.authorityList = new ArrayList<>();
		if(authorityList != null) {
			this.authorityList.addAll(authorityList);
		}
	}
}
