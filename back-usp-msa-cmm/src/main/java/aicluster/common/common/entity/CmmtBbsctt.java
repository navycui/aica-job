package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtBbsctt implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5266077391452519813L;
	private String articleId;
	private String boardId;
	private String title;
	private String article;
	private Boolean notice;
	private String attachmentGroupId;
	private String imageGroupId;
	private String categoryCd;
	private Boolean posting;
	private Boolean webEditor;
	private String sharedUrl;
	private String pcThumbnailFileId;
	private String mobileThumbnailFileId;
	private String thumbnailAltCn;
	private Long readCnt;
	private String creatorId;
	private String creatorNm;
	private Date createdDt;
	private String updaterId;
	private String updaterNm;
	private Date updatedDt;

	/*
	 * Helper property
	 */
	private CmmtBbs cmmtBoard;
	private List<CmmtBbscttCn> articleCnList;
	private List<CmmtBbscttLink> articleUrlList;
	private List<CmmtAtchmnfl> attachmentList;
	private List<CmmtAtchmnfl> imageList;
	
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
	
	public List<CmmtBbscttCn> getArticleCnList() {
		List<CmmtBbscttCn> articleCnList = new ArrayList<>();
		if(this.articleCnList != null) {
			articleCnList.addAll(this.articleCnList);
		}
		return articleCnList;
	}

	public void setArticleCnList(List<CmmtBbscttCn> articleCnList) {
		this.articleCnList = new ArrayList<>();
		if(articleCnList != null) {
			this.articleCnList.addAll(articleCnList);
		}
	}
	
	public List<CmmtBbscttLink> getArticleUrlList() {
		List<CmmtBbscttLink> articleUrlList = new ArrayList<>();
		if(this.articleUrlList != null) {
			articleUrlList.addAll(this.articleUrlList);
		}
		return articleUrlList;
	}

	public void setArticleUrlList(List<CmmtBbscttLink> articleUrlList) {
		this.articleUrlList = new ArrayList<>();
		if(articleUrlList != null) {
			this.articleUrlList.addAll(articleUrlList);
		}
	}
	
	public List<CmmtAtchmnfl> getAttachmentList() {
		List<CmmtAtchmnfl> attachmentList = new ArrayList<>();
		if(this.attachmentList != null) {
			attachmentList.addAll(this.attachmentList);
		}
		return attachmentList;
	}

	public void setAttachmentList(List<CmmtAtchmnfl> attachmentList) {
		this.attachmentList = new ArrayList<>();
		if(attachmentList != null) {
			this.attachmentList.addAll(attachmentList);
		}
	}
	
	public List<CmmtAtchmnfl> getImageList() {
		List<CmmtAtchmnfl> imageList = new ArrayList<>();
		if(this.imageList != null) {
			imageList.addAll(this.imageList);
		}
		return imageList;
	}

	public void setImageList(List<CmmtAtchmnfl> imageList) {
		this.imageList = new ArrayList<>();
		if(imageList != null) {
			this.imageList.addAll(imageList);
		}
	}
}
