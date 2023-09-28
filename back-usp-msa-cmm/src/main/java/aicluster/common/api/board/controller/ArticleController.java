package aicluster.common.api.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.board.dto.AddArticleParam;
import aicluster.common.api.board.dto.GetArticlesParam;
import aicluster.common.api.board.dto.ModifyArticleParam;
import aicluster.common.api.board.service.BoardArticleService;
import aicluster.common.common.dto.BoardArticleListItem;
import aicluster.common.common.dto.BoardArticlePrevNextItem;
import aicluster.common.common.dto.CreatrInfDto;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/boards/{boardId}")
public class ArticleController {

	@Autowired
	private BoardArticleService articleService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	/**
	 * 게시글 목록조회
	 * @param boardId
	 * @param param
	 * @param pageParam
	 * @return
	 */
	@GetMapping("/articles")
	public CorePagination<BoardArticleListItem> getArticles(@PathVariable String boardId, GetArticlesParam param, CorePaginationParam pageParam) {
		param.setBoardId(boardId);
		return articleService.getList(param, pageParam);
	}

	/**
	 * 최근 게시글 조회
	 * @param boardId
	 * @param categoryCd
	 * @param hasNotice
	 * @param cnt
	 * @return
	 */
	@GetMapping("/recent")
	public JsonList<BoardArticleListItem> getRecentArticles(@PathVariable String boardId, String categoryCd, Boolean hasNotice, Long cnt) {
		return articleService.getRecentList(boardId, categoryCd, hasNotice, cnt);
	}

	/**
	 * 게시글 등록
	 * @param boardId
	 * @param param
	 * @param pcThumbnailFile
	 * @param mobileThumbnailFile
	 * @param images
	 * @param attachments
	 * @return
	 */
	@PostMapping("/articles")
	// https://medium.com/jaehoon-techblog/simpleblog-%EA%B0%9C%EB%B0%9C-%EC%9D%BC%EC%A7%80-4-55a8d2a8604
	public CmmtBbsctt addArticle(
			@PathVariable String boardId,
			@RequestPart(value = "article", required = true) AddArticleParam param,
			@RequestPart(value = "pcThumbnailFile", required = false) MultipartFile pcThumbnailFile,
			@RequestPart(value = "mobileThumbnailFile", required = false) MultipartFile mobileThumbnailFile,
			@RequestPart(value = "image", required = false) List<MultipartFile> images,
			@RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) {
		param.setBoardId(boardId);
		return articleService.add(param, pcThumbnailFile, mobileThumbnailFile, images, attachments);
	}

	/**
	 * 게시글 조회
	 * @param boardId
	 * @param articleId
	 * @return
	 */
	@GetMapping("/articles/{articleId}")
	public CmmtBbsctt getArticle(@PathVariable String boardId, @PathVariable String articleId) {
		return articleService.get(boardId, articleId);
	}

	/**
	 * 게시글 이전글/다음글 조회
	 * @param boardId : 게시판ID
	 * @param articleId : 게시글ID
	 * @param param : 게시글목록조회 검색조건
	 * @return
	 */
	@GetMapping("/articles/{articleId}/previous-next")
	public BoardArticlePrevNextItem getArticlePreviousNext(@PathVariable String boardId, @PathVariable String articleId, GetArticlesParam param) {
		param.setBoardId(boardId);
		return articleService.getPreviousNext(boardId, articleId, param);
	}

	/**
	 * 게시글 삭제
	 * @param boardId
	 * @param articleId
	 */
	@DeleteMapping("/articles/{articleId}")
	public void removeArticle(@PathVariable String boardId, @PathVariable String articleId) {
		articleService.remove(boardId, articleId);
	}

	/**
	 * 게시글 수정
	 * @param boardId
	 * @param articleId
	 * @param param
	 * @param pcThumbnailFile
	 * @param mobileThumbnailFile
	 * @param images
	 * @param attachments
	 * @return
	 */
	@PutMapping("/articles/{articleId}")
	public CmmtBbsctt modifyArticle(
			@PathVariable String boardId,
			@PathVariable String articleId,
			@RequestPart(value = "article", required = true) ModifyArticleParam param,
			@RequestPart(value = "pcThumbnailFile", required = false) MultipartFile pcThumbnailFile,
			@RequestPart(value = "mobileThumbnailFile", required = false) MultipartFile mobileThumbnailFile,
			@RequestPart(value = "image", required = false) List<MultipartFile> images,
			@RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) {

		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.setBoardId(boardId);
		param.setArticleId(articleId);
		return articleService.modify(param, pcThumbnailFile, mobileThumbnailFile, images, attachments);
	}

	/**
	 * PC용 이미지 다운로드
	 * @param response
	 * @param boardId
	 * @param articleId
	 */
	@GetMapping("/articles/{articleId}/pcthumbnail")
	public void downloadPcThumbnail(HttpServletResponse response, @PathVariable String boardId, @PathVariable String articleId) {
		CmmtBbsctt article = articleService.select(boardId, articleId);
		attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), article.getPcThumbnailFileId());
	}

	/**
	 * Mobile용 이미지 다운로드
	 * @param response
	 * @param boardId
	 * @param articleId
	 */
	@GetMapping("/articles/{articleId}/mobilethumbnail")
	public void downloadMobileThumbnail(HttpServletResponse response, @PathVariable String boardId, @PathVariable String articleId) {
		CmmtBbsctt article = articleService.select(boardId, articleId);
		attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), article.getMobileThumbnailFileId());
	}

	/**
	 * 게시글 작성자 정보 조회
	 * @param boardId
	 * @param articleId
	 * @return
	 */
	@GetMapping("/articles/{articleId}/writer-info")
	public CreatrInfDto getWriterInfo(@PathVariable String boardId, @PathVariable String articleId)
	{
		return articleService.getBbscttWriterInfo(boardId, articleId);
	}

	/**
	 * 게시글 게시상태 변경
	 * @param boardId
	 * @param articleId
	 * @param posting
	 * @return
	 */
	@PutMapping("/articles/{articleId}/posting")
	public CmmtBbsctt modifyPosting(@PathVariable String boardId, @PathVariable String articleId, Boolean posting) {
		return articleService.modifyPosting(boardId, articleId, posting);
	}
}
