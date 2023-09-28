package aicluster.common.api.board.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.CmmtBbscttDao;
import aicluster.common.common.dao.CmmtBbsAnswerDao;
import aicluster.common.common.dao.CmmtBbsDao;
import aicluster.common.common.entity.CmmtBbs;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.common.common.entity.CmmtBbsAnswer;
import aicluster.common.common.util.BoardUtils;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class BoardCmmntService {

	@Autowired
	private CmmtBbsDao cmmtBoardDao;

	@Autowired
	private CmmtBbscttDao cmmtBoardArticleDao;

	@Autowired
	private CmmtBbsAnswerDao cmmtBoardCmmntDao;

	@Autowired
	private BoardUtils boardUtils;

	public CorePagination<CmmtBbsAnswer> getList(String boardId, String articleId, Boolean latest, Long page, Long itemsPerPage) {
		/*
		 * 게시판에 대한 조회권한이 있어야 조회할 수 있다.
		 */

		BnMember worker = SecurityUtils.getCurrentMember();

		if (latest == null) {
			latest = false;
		}
		if (page == null) {
			page = 1L;
		}
		if (itemsPerPage == null) {
			itemsPerPage = 10L;
		}

		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		if (!cmmtBoard.getCommentable()) {
			throw new InvalidationException("댓글을 달 수 없는 게시판입니다.");
		}

		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		// 권한검사(조회권한)
		if ( !boardUtils.canReadArticle(cmmtBoard, worker) ) {
			throw new InvalidationException("권한이 없습니다.");
		}

		long totalItems = cmmtBoardCmmntDao.selectList_count(articleId);
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		List<CmmtBbsAnswer> list = cmmtBoardCmmntDao.selectList(articleId, latest, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());
		CorePagination<CmmtBbsAnswer> dto = new CorePagination<>(info, list);

		return dto;
	}

	public CmmtBbsAnswer add(String boardId, String articleId, String cmmnt) {
		/*
		 * 게시판에 대한 조회 권한만 있으면 등록할 수 있다.
		 */

		BnMember worker = SecurityUtils.getCurrentMember();

		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		if (!cmmtBoard.getCommentable()) {
			throw new InvalidationException("댓글을 달 수 없는 게시판입니다.");
		}

		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null || !cmmtBoardArticle.getPosting()) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		// 권한검사(조회권한)
		if ( !boardUtils.canReadArticle(cmmtBoard, worker) ) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 입력검사
		if (string.isBlank(cmmnt)) {
			throw new InvalidationException("내용을 입력하세요.");
		}

		// 입력
		Date now = new Date();
		CmmtBbsAnswer cmmtBoardCmmnt = CmmtBbsAnswer.builder()
				.articleId(articleId)
				.cmmntId(string.getNewId("cmmnt-"))
				.cmmnt(cmmnt)
				.createdDt(now)
				.creatorId(worker.getMemberId())
				.updatedDt(now)
				.updaterId(worker.getMemberId())
				.build();

		cmmtBoardCmmntDao.insert(cmmtBoardCmmnt);

		// 출력
		cmmtBoardCmmnt = cmmtBoardCmmntDao.select(cmmtBoardCmmnt.getArticleId(), cmmtBoardCmmnt.getCmmntId());
		return cmmtBoardCmmnt;
	}

	public CmmtBbsAnswer modify(String boardId, String articleId, String cmmntId, String cmmnt) {
		BnMember worker = SecurityUtils.getCurrentMember();

		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null || !cmmtBoardArticle.getPosting()) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		CmmtBbsAnswer cmmtBoardCmmnt = cmmtBoardCmmntDao.select(articleId, cmmntId);
		if (cmmtBoardCmmnt == null) {
			throw new InvalidationException("댓글이 존재하지 않습니다.");
		}

		// 권한검사
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if (!string.equals(cmmtBoardCmmnt.getCreatorId(), worker.getMemberId())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 입력검사
		if (string.isBlank(cmmnt)) {
			throw new InvalidationException("댓글 내용을 입력하세요.");
		}

		Date now = new Date();
		cmmtBoardCmmnt.setCmmnt(cmmnt);
		cmmtBoardCmmnt.setUpdatedDt(now);
		cmmtBoardCmmnt.setUpdaterId(worker.getMemberId());

		cmmtBoardCmmntDao.update(cmmtBoardCmmnt);

		cmmtBoardCmmnt = cmmtBoardCmmntDao.select(articleId, cmmntId);
		return cmmtBoardCmmnt;
	}

	public void remove(String boardId, String articleId, String cmmntId) {
		BnMember worker = SecurityUtils.getCurrentMember();
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null || !cmmtBoardArticle.getPosting()) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}
		CmmtBbsAnswer cmmtBoardCmmnt = cmmtBoardCmmntDao.select(articleId, cmmntId);
		if (cmmtBoardCmmnt == null) {
			throw new InvalidationException("댓글이 존재하지 않습니다.");
		}

		// 권한검사
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if (!string.equals(cmmtBoardCmmnt.getCreatorId(), worker.getMemberId())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 삭제
		cmmtBoardCmmntDao.delete(articleId, cmmntId);
	}

}
