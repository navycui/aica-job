package aicluster.common.api.board.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 580591200431838254L;
	private String boardId;
	private String title;
	private Boolean notice;
	private String article;
	private String categoryCd;
	private Boolean posting;
	private Boolean webEditor;
	private String sharedUrl;
	private String thumbnailAltCn;

	private List<BoardArticleCnParam> articleCnList;
	private List<BoardArticleUrlParam> articleUrlList;
	
	public List<BoardArticleCnParam> getArticleCnList() {
		List<BoardArticleCnParam> articleCnList = new ArrayList<>();
		if(this.articleCnList != null) {
			articleCnList.addAll(this.articleCnList);
		}
		return articleCnList;
	}

	public void setArticleCnList(List<BoardArticleCnParam> articleCnList) {
		this.articleCnList = new ArrayList<>();
		if(articleCnList != null) {
			this.articleCnList.addAll(articleCnList);
		}
	}
	
	public List<BoardArticleUrlParam> getArticleUrlList() {
		List<BoardArticleUrlParam> articleUrlList = new ArrayList<>();
		if(this.articleUrlList != null) {
			articleUrlList.addAll(this.articleUrlList);
		}
		return articleUrlList;
	}

	public void setArticleUrlList(List<BoardArticleUrlParam> articleUrlList) {
		this.articleUrlList = new ArrayList<>();
		if(articleUrlList != null) {
			this.articleUrlList.addAll(articleUrlList);
		}
	}
}
