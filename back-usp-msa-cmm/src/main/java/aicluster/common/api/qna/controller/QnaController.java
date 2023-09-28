package aicluster.common.api.qna.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.qna.dto.QnaParam;
import aicluster.common.api.qna.service.QnaService;
import aicluster.common.common.dto.QnaQuestStatusCountItem;
import aicluster.common.common.entity.CmmtQna;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/qna")
public class QnaController {

	@Autowired
	private QnaService service;

	/**
	 * 질의응답 게시판 목록조회
	 *
	 * @param systemId 포털구분
	 * @param enabled 사용여부
	 * @param qnaId 게시판ID
	 * @param qnaNm 게시판명
	 * @return
	 */
	@GetMapping("")
	public JsonList<CmmtQna> getList(String systemId, Boolean enabled, String qnaId, String qnaNm) {
		return service.getList(systemId, enabled, qnaId, qnaNm);
	}

	/**
	 * 질의응답 게시판 추가
	 * @param param
	 * @return
	 */
	@PostMapping("")
	public CmmtQna add(@RequestBody QnaParam param) {
		return service.add(param);
	}

	/**
	 * 질의응답 게시판 조회
	 * @param qnaId
	 * @return
	 */
	@GetMapping("/{qnaId}")
	public CmmtQna get(@PathVariable String qnaId) {
		return service.get(qnaId);
	}

	/**
	 * 질의응답 게시판 수정
	 * @param qnaId
	 * @param param
	 * @return
	 */
	@PutMapping("/{qnaId}")
	public CmmtQna modify(@PathVariable String qnaId, @RequestBody QnaParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.getQna().setQnaId(qnaId);
		return service.modify(param);
	}

	/**
	 * 질의응답 게시판 삭제
	 * @param qnaId
	 */
	@DeleteMapping("/{qnaId}")
	public void remove(@PathVariable String qnaId) {
		service.remove(qnaId);
	}

	/**
	 * 질의게시판 질의상태별 건수 조회
	 *
	 * @param qnaId
	 * @return
	 */
	@GetMapping("/status/status-count")
	public JsonList<QnaQuestStatusCountItem> getQnaQuestCount() {
		return service.getQnaQuestCount();
	}
}
