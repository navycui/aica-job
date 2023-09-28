package aicluster.tsp.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CmmtBoardArticle implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4341590894196249759L;
	private String articleId;                      /**게시글ID*/
	private String boardId;                        /**게시판ID*/
	private String title;                          /**제목*/
	private String article;                        /**내용*/
	private Boolean notice;                        /**공지여부*/
	private String attachmentGroupId;              /**첨부파일그룹ID*/
	private String imageGroupId;                   /**이미지파일 그룹ID*/
	private String categoryCd;                     /**카테고리코드*/
	private Boolean posting;                       /**게시여부*/
	private Boolean webEditor;                     /**웹데이터 사용여부*/
	private String sharedUrl;                      /**공유URL*/
	private String pcThumbnailFileId;              /**PC용 썸네일 파일ID*/
	private String mobileThumbnailFileId;          /**모바일용 썸네일 파일ID*/
	private Long readCnt;                       /**조회수*/
	private String creatorId;                      /**생성자ID(CMMT_MEMBER.MEMBER_ID)*/
	private Date createdDt;                        /**생성일시*/
	private String updaterId;                      /**수정자ID(CMMT_MEMBER.MEMBER_ID)*/
	private Date updatedDt;                        /**수정일시*/
	private Long    rn;                            /**글번호*/

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
