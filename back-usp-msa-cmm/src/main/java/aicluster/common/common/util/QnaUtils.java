package aicluster.common.common.util;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.common.common.dao.CmmtCodeGroupDao;
import aicluster.common.common.dao.CmmtQnaRespondDao;
import aicluster.common.common.entity.CmmtCodeGroup;
import aicluster.common.common.entity.CmmtQna;
import aicluster.common.common.entity.CmmtQnaRespond;
import aicluster.common.common.entity.CmmtQnaQuest;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;

@Component
public class QnaUtils {

	@Autowired
	private CmmtCodeGroupDao cmmtCodeGroupDao;

	@Autowired
	private CmmtQnaRespondDao cmmtQnaAnswererDao;

	/**
	 * 게시판ID는 '영문/숫자/-'로만 입력해야 합니다.
	 * @param qnaId
	 * @return
	 */
	public boolean isValidQnaId(String qnaId) {
		String pattern = "^[a-zA-Z0-9\\-]*$";
        return Pattern.matches(pattern, qnaId);
	}

	/**
	 * 게시판 정보 기본 검사
	 *
	 * @param qna
	 * @param exs
	 */
	public void checkCmmtQna(CmmtQna qna, InvalidationsException exs) {
		// boardId 검사
		qna.setQnaId(string.lowerCase(qna.getQnaId()));
		if (string.isBlank(qna.getQnaId())) {
			exs.add("qnaId", "게시판ID를 입력하세요.");
		}

		// systemId검사
		if (string.isBlank(qna.getSystemId())) {
			exs.add("systemId", "포털구분을 입력하세요.");
		}

		// 게시판명 검사
		qna.setQnaNm(string.trim(qna.getQnaNm()));
		if (string.isBlank(qna.getQnaNm())) {
			exs.add("boardNm", "게시판명을 입력하세요.");
		}

		qna.setArticleCnt(0L);

		// 사용여부 검사
		if (qna.getEnabled() == null) {
			qna.setEnabled(true);
		}

		// 카테고리 사용여부 검사
		if (qna.getCategory() == null) {
			qna.setCategory(false);
		}

		if (qna.getCategory()) {
			if (string.isBlank(qna.getCategoryCodeGroup())) {
				exs.add("categoryCodeGroup", "카테고리 코드그룹을 입력하세요.");
			}
			else {
				CmmtCodeGroup cmmtCodeGroup = cmmtCodeGroupDao.select(qna.getCategoryCodeGroup());
				if (cmmtCodeGroup == null) {
					exs.add("categoryCodeGroup", "카테고리 코드그룹이 코드그룹에 존재하지 않습니다.");
				}
			}
		}
		else {
			qna.setCategoryCodeGroup(null);
		}

		// 첨부가능여부 검사
		if (qna.getAttachable() == null) {
			qna.setAttachable(false);
		}
		if (qna.getAttachable()) {
			if (qna.getAttachmentSize() == null) {
				exs.add("attachmentSize", "첨부파일크기제한을 입력하세요.");
			}
			String exts = string.validateFileExtStrings(qna.getAttachmentExt(), '/');
			if (string.isBlank(exts)) {
				exs.add("attachmentExt", "첨부파일 확장자를 입력하세요.");
			}
			qna.setAttachmentExt(exts);
		}
		else {
			qna.setAttachmentExt(null);
			qna.setAttachmentSize(0L);
		}
	}

	/**
	 * 답변을 달 수 있는 권한을 갖고 있는가?
	 *
	 * @param qnaId
	 * @param memberId
	 * @return
	 */
	public boolean canAnswer(String qnaId, String memberId) {
		CmmtQnaRespond cmmtQnaAnswerer = cmmtQnaAnswererDao.select(qnaId, memberId);
		return (cmmtQnaAnswerer != null);
	}

	/**
	 * 글을 읽을 수 있는 권한을 갖고 있는가?
	 * - 글 작성자 또는 답변권한자
	 *
	 * @param cmmtQna
	 * @param cmmtQnaQuest
	 * @param memberId
	 * @return
	 */
	public boolean canRead(String qnaId, CmmtQnaQuest cmmtQnaQuest, String memberId) {
		if (cmmtQnaQuest == null) {
			return false;
		}
		boolean writer = string.equals(cmmtQnaQuest.getQuestionerId(), memberId);
		boolean canAnswer = canAnswer(qnaId, memberId);
		return writer || canAnswer;
	}

	public boolean canModify(CmmtQnaQuest cmmtQnaQuest, String memberId) {
		if (cmmtQnaQuest == null) {
			return false;
		}
		return string.equals(cmmtQnaQuest.getQuestionerId(), memberId);
	}
}
