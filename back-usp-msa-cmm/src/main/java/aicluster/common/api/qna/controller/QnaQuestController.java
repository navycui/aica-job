package aicluster.common.api.qna.controller;

import java.util.List;

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

import aicluster.common.api.qna.dto.QnaQuestAddParam;
import aicluster.common.api.qna.dto.QnaQuestListParam;
import aicluster.common.api.qna.dto.QnaQuestModifyParam;
import aicluster.common.api.qna.service.QnaQuestService;
import aicluster.common.common.dto.CreatrInfDto;
import aicluster.common.common.dto.QnaQuestListItem;
import aicluster.common.common.entity.CmmtQnaQuest;
import bnet.library.exception.InvalidationException;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/qna/{qnaId}/quests")
public class QnaQuestController {

	@Autowired
	private QnaQuestService service;

	/**
	 * 질의응답 게시글 목록 조회
	 *
	 * @param qnaId : 질의게시판ID
	 * @param param : 검색조건 (questStatus:질의상태코드, categoryCd:카테고리코드, title:질의제목, memberNm:질의자명)
	 * @param pageParam : 페이징
	 * @return CorePagination<CmmtQnaQuest> : 질의게시글 목록
	 */
	@GetMapping("")
	public CorePagination<QnaQuestListItem> getList(@PathVariable String qnaId, QnaQuestListParam param, CorePaginationParam pageParam) {
		return service.getList(qnaId, param, pageParam);
	}

	/**
	 * 질문 등록
	 *
	 * @param qnaId
	 * @param param
	 * @param files
	 * @return
	 */
	@PostMapping("")
	public CmmtQnaQuest add(
			@PathVariable String qnaId,
			@RequestPart(value="qnaQuest", required=true) QnaQuestAddParam param,
			@RequestPart(value="file", required=false) List<MultipartFile> files) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.setQnaId(qnaId);

		return service.add(param, files);
	}

	/**
	 * 질의응답 조회
	 *
	 * @param qnaId
	 * @param questId
	 * @return
	 */
	@GetMapping("{questId}")
	public CmmtQnaQuest get(@PathVariable String qnaId, @PathVariable String questId) {
		return service.get(qnaId, questId);
	}

	/**
	 * 질문 수정
	 *
	 * @param qnaId
	 * @param questId
	 * @param param
	 * @param files
	 * @return
	 */
	@PutMapping("{questId}")
	public CmmtQnaQuest modify(
			@PathVariable String qnaId,
			@PathVariable String questId,
			@RequestPart(value="qnaQuest", required=true) QnaQuestModifyParam param,
			@RequestPart(value="file", required=false) List<MultipartFile> files) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.setQnaId(qnaId);
		param.setQuestId(questId);

		return service.modify(param, files);
	}

	/**
	 * 질문 삭제
	 *
	 * @param qnaId
	 * @param questId
	 */
	@DeleteMapping("{questId}")
	public void remove(@PathVariable String qnaId, @PathVariable String questId) {
		service.remove(qnaId, questId);
	}

	/**
	 * 질의응답 답변등록
	 * @param qnaId
	 * @param questId
	 * @param answer
	 * @return
	 */
	@PutMapping("{questId}/ans")
	public CmmtQnaQuest answer(@PathVariable String qnaId, @PathVariable String questId, String answer) {
		return service.answer(qnaId, questId, answer);
	}

	/**
	 * 질의응답 질의자 정보 조회
	 * @param qnaId
	 * @param questId
	 * @return
	 */
	@GetMapping("{questId}/quester-info")
	public CreatrInfDto getWriterInfo(@PathVariable String qnaId, @PathVariable String questId)
	{
		return service.getQnaQuestrInfo(qnaId, questId);
	}
}
