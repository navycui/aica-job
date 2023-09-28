package aicluster.common.api.board.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.CmmtBbscttDao;
import aicluster.common.common.dao.CmmtBbsDao;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.common.common.entity.CmmtBbs;
import aicluster.common.common.util.BoardUtils;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class BoardAttachmentService {

	@Autowired
	private CmmtBbsDao cmmtBoardDao;

	@Autowired
	private CmmtBbscttDao cmmtBoardArticleDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	@Autowired
	private BoardUtils boardUtils;

	public JsonList<CmmtAtchmnfl> getList(String boardId, String articleId) {
		CmmtBbs board = cmmtBoardDao.select(boardId);
		if (board == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtBbsctt article = cmmtBoardArticleDao.select(articleId);
		if (article == null || !string.equals(boardId, article.getBoardId())) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		BnMember worker = SecurityUtils.getCurrentMember();

		// 권한검사
		if (!boardUtils.canReadArticle(board, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		if (!board.getAttachable()) {
			return new JsonList<>(new ArrayList<CmmtAtchmnfl>());
		}

		if (string.isBlank(article.getAttachmentGroupId())) {
			return new JsonList<>(new ArrayList<CmmtAtchmnfl>());
		}

		List<CmmtAtchmnfl> list =  attachmentService.getFileInfos_group(article.getAttachmentGroupId());

		return new JsonList<>(list);
	}

	public void remove(String boardId, String articleId, String attachmentId) {
		CmmtBbs board = cmmtBoardDao.select(boardId);
		if (board == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtBbsctt article = cmmtBoardArticleDao.select(articleId);
		if (article == null || !string.equals(boardId, article.getBoardId())) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		BnMember worker = SecurityUtils.getCurrentMember();

		// 권한검사
		if (!boardUtils.canRemoveArticle(boardId, article, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 첨부파일을 사용할 수 없는 게시판이라면...
		if (!board.getAttachable()) {
			throw new InvalidationException("파일이 없습니다.");
		}

		// 첨부파일 조회
		CmmtAtchmnfl att = attachmentService.getFileInfo(attachmentId);
		if (att == null || !string.equals(article.getAttachmentGroupId(), att.getAttachmentGroupId())) {
			throw new InvalidationException("파일이 없습니다.");
		}

		// 파일이 전부 삭제된 경우 파일그룹ID는 삭제되고 리턴값은 false가 반환된다.
		boolean exists = attachmentService.removeFile(config.getDir().getUpload(), attachmentId);

		// 파일그룹ID NULL 처리
		if (!exists) {
			Date now = new Date();
			article.setAttachmentGroupId(null);
			article.setUpdaterId(worker.getMemberId());
			article.setUpdatedDt(now);

			cmmtBoardArticleDao.update(article);
		}

	}

	public CmmtAtchmnfl get(String boardId, String articleId, String attachmentId) {
		CmmtBbs board = cmmtBoardDao.select(boardId);
		if (board == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtBbsctt article = cmmtBoardArticleDao.select(articleId);
		if (article == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		BnMember worker = SecurityUtils.getCurrentMember();

		// 권한검사
		if (!boardUtils.canReadArticle(board, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 첨부파일을 사용할 수 없는 게시판이라면...
		if (!board.getAttachable()) {
			throw new InvalidationException("파일이 없습니다.");
		}

		// 파일 조회
		CmmtAtchmnfl att = attachmentService.getFileInfo(attachmentId);
		if (att == null || !string.equals(article.getAttachmentGroupId(), att.getAttachmentGroupId())) {
			throw new InvalidationException("파일이 없습니다.");
		}

		return att;
	}

}
