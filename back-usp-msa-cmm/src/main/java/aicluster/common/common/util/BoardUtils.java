package aicluster.common.common.util;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.common.common.dao.CmmtBbsAuthorDao;
import aicluster.common.common.dao.CmmtCodeGroupDao;
import aicluster.common.common.entity.CmmtBbs;
import aicluster.common.common.entity.CmmtBbsAuthor;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.common.common.entity.CmmtCodeGroup;
import aicluster.framework.common.entity.BnMember;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.string;

@Component
public class BoardUtils {

	@Autowired
	private CmmtCodeGroupDao cmmtCodeGroupDao;

	@Autowired
	private CmmtBbsAuthorDao cmmtBoardAuthorityDao;

	public static class boardAuthority {
		public static final String 조회 = "READ";
		public static final String 쓰기 = "WRITE";
		public static final String 관리자 = "MANAGER";
	}

	/**
	 * 게시판ID는 '영문/숫자/-'로만 입력해야 합니다.
	 * @param boardId
	 * @return
	 */
	public boolean isValidBoardId(String boardId) {
		String pattern = "^[a-zA-Z0-9\\-]*$";
        return Pattern.matches(pattern, boardId);
	}

	/**
	 * 게시판 정보 기본 검사
	 *
	 * @param board
	 * @param exs
	 */
	public void checkCmmtBoard(CmmtBbs board, InvalidationsException exs) {
		// boardId 검사
		board.setBoardId(string.lowerCase(board.getBoardId()));
		if (string.isBlank(board.getBoardId())) {
			exs.add("boardId", "게시판ID를 입력하세요.");
		}

		// systemId검사
		if (string.isBlank(board.getSystemId())) {
			exs.add("systemId", "포털구분을 입력하세요.");
		}

		// boardNm 검사
		board.setBoardNm(string.trim(board.getBoardNm()));
		if (string.isBlank(board.getBoardNm())) {
			exs.add("boardNm", "게시판명을 입력하세요.");
		}

		board.setArticleCnt(0L);

		// 사용여부 검사
		if (board.getEnabled() == null) {
			board.setEnabled(true);
		}

		// 웹에디터 사용여부 검사
		if (board.getWebEditor() == null) {
			board.setWebEditor(false);
		}

		// 고정공지 사용여부 검사
		if (board.getNoticeAvailable() == null) {
			board.setNoticeAvailable(false);
		}

		// 댓글 사용여부 검사
		if (board.getCommentable() == null) {
			board.setCommentable(false);
		}

		// 카테고리 사용여부 검사
		if (board.getCategory() == null) {
			board.setCategory(false);
		}

		if (board.getCategory()) {
			if (string.isBlank(board.getCategoryCodeGroup())) {
				exs.add("categoryCodeGroup", "카테고리 코드그룹을 입력하세요.");
			}
			else {
				CmmtCodeGroup cmmtCodeGroup = cmmtCodeGroupDao.select(board.getCategoryCodeGroup());
				if (cmmtCodeGroup == null) {
					exs.add("categoryCodeGroup", "카테고리 코드그룹이 코드그룹에 존재하지 않습니다.");
				}
			}
		}
		else {
			board.setCategoryCodeGroup(null);
		}

		// 첨부가능여부 검사
		if (board.getAttachable() == null) {
			board.setAttachable(false);
		}
		if (board.getAttachable()) {
			if (board.getAttachmentSize() == null) {
				exs.add("attachmentSize", "첨부파일크기제한을 입력하세요.");
			}
			String exts = string.validateFileExtStrings(board.getAttachmentExt(), '/');
			if (string.isBlank(exts)) {
				exs.add("attachmentExt", "첨부파일 확장자를 입력하세요.");
			}
			board.setAttachmentExt(exts);
		}
		else {
			board.setAttachmentExt(null);
			board.setAttachmentSize(0L);
		}

		// 공유URL 사용여부 검사
		if (board.getUseSharedUrl() == null) {
			board.setUseSharedUrl(false);
		}

		// 썸네일 사용여부 검사
		if (board.getUseThumbnail() == null) {
			board.setUseThumbnail(false);
		}

		// 양식 사용여부
		if (board.getUseForm() == null) {
			board.setUseForm(false);
		}

		// 모든 사용자 조회가능여부 검사
		if (board.getAllReadable() == null) {
			exs.add("allReadable", "모든 사용자 조회가능여부를 입력하세요.");
		}
	}

	/***
	 * 게시글 조회권한 검사
	 * @param cmmtBoard
	 * @param worker
	 * @return
	 */
	public boolean canReadArticle(CmmtBbs cmmtBoard, BnMember worker) {
		/*
		 * 게시판에 모두조회가능으로 설정되어 있거나
		 * 게시판에 대해, 조회/쓰기/관리자 권한이 있으면 조회가능하다.
		 */

		if (cmmtBoard.getAllReadable()) {
			return true;
		}

		if (worker == null) {
			return false;
		}

		CmmtBbsAuthor cmmtBoardAuthority = cmmtBoardAuthorityDao.select(cmmtBoard.getBoardId(), worker.getAuthorityId());
		if (cmmtBoardAuthority == null) {
			return false;
		}

		String[] accept = {boardAuthority.조회, boardAuthority.쓰기, boardAuthority.관리자};
		return array.contains(accept, cmmtBoardAuthority.getBoardAuthority());
	}

	/***
	 * 게시글 쓰기권한 검사
	 * @param boardId
	 * @param worker
	 * @return
	 */
	public boolean canWriteArticle(String boardId, BnMember worker) {
		/*
		 * 게시판에 대해 쓰기 또는 관리자 권한이 있어야 한다.
		 */

		if (worker == null) {
			return false;
		}

		CmmtBbsAuthor cmmtBoardAuthority = cmmtBoardAuthorityDao.select(boardId, worker.getAuthorityId());
		if (cmmtBoardAuthority == null) {
			return false;
		}

		String[] accept = {boardAuthority.쓰기, boardAuthority.관리자};
		if (array.contains(accept, cmmtBoardAuthority.getBoardAuthority())) {
			return true;
		}
		return false;
	}

	public boolean canWriteNotice(String boardId, BnMember worker) {
		/*
		 * 게시판의 관리자 권한이어야 공지사항을 등록할 수 있다.
		 */

		if (worker == null) {
			return false;
		}

		CmmtBbsAuthor cmmtBoardAuthority = cmmtBoardAuthorityDao.select(boardId, worker.getAuthorityId());
		if (cmmtBoardAuthority == null) {
			return false;
		}

		String[] accept = {boardAuthority.관리자};
		if (array.contains(accept, cmmtBoardAuthority.getBoardAuthority())) {
			return true;
		}
		return false;
	}

	/***
	 * 게시글 수정권한 검사
	 * @param boardId
	 * @param cmmtBoardArticle
	 * @param worker
	 * @return
	 */
	public boolean canModifyArticle(String boardId, CmmtBbsctt cmmtBoardArticle, BnMember worker) {
		/*
		 * 1) 게시판에 대해, 쓰기권한 또는 관리자권한이 있어야 한다.
		 * 2) 글 쓴 당사자 이어야 수정할 수 있다.
		 */

		if (worker == null) {
			return false;
		}

		CmmtBbsAuthor cmmtBoardAuthority = cmmtBoardAuthorityDao.select(boardId, worker.getAuthorityId());
		if (cmmtBoardAuthority == null) {
			return false;
		}

		String[] accept = {boardAuthority.쓰기, boardAuthority.관리자};
		if (!array.contains(accept, cmmtBoardAuthority.getBoardAuthority())) {
			return false;
		}
		return string.equals(cmmtBoardArticle.getCreatorId(), worker.getMemberId());
	}

	/***
	 * 게시글 게시권한 검사
	 * @param boardId
	 * @param cmmtBoardArticle
	 * @param worker
	 * @return
	 */
	public boolean canPostingArticle(String boardId, CmmtBbsctt cmmtBoardArticle, BnMember worker) {
		/*
		 * 1) 게시판에 대해 관리자권한이 있어야 한다.
		 * 2) 관리자 권한자 이어야 수정할 수 있다.
		 */
		if (worker == null) {
			return false;
		}

		CmmtBbsAuthor cmmtBoardAuthority = cmmtBoardAuthorityDao.select(boardId, worker.getAuthorityId());
		if (cmmtBoardAuthority == null) {
			return false;
		}

		String[] accept = {boardAuthority.관리자};
		if (!array.contains(accept, cmmtBoardAuthority.getBoardAuthority())) {
			return false;
		}

		return true;
	}

	/***
	 * 게시글 삭제권한 검사
	 * @param boardId
	 * @param cmmtBoardArticle
	 * @param worker
	 * @return
	 */
	public boolean canRemoveArticle(String boardId, CmmtBbsctt cmmtBoardArticle, BnMember worker) {
		/*
		 * 1) 게시판에 대해, 쓰기 또는 관리자 권한을 갖고 있어야 한다.
		 * 2) 게시판에 대해 관리자 권한을 갖고 있으면, 어느 글이나 삭제할 수 있다.
		 * 3) 이 게시글을 작성한 사람은 삭제할 수 있다.
		 */

		if (worker == null) {
			return false;
		}

		CmmtBbsAuthor cmmtBoardAuthority = cmmtBoardAuthorityDao.select(boardId, worker.getAuthorityId());
		if (cmmtBoardAuthority == null) {
			return false;
		}

		String[] accept = {boardAuthority.쓰기, boardAuthority.관리자};
		if (!array.contains(accept, cmmtBoardAuthority.getBoardAuthority())) {
			return false;
		}
		if (string.equals(cmmtBoardAuthority.getBoardAuthority(), boardAuthority.관리자)) {
			return true;
		}

		return string.equals(cmmtBoardArticle.getCreatorId(), worker.getMemberId());
	}
}
