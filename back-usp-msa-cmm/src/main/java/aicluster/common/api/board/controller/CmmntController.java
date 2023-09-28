package aicluster.common.api.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.board.service.BoardCmmntService;
import aicluster.common.common.entity.CmmtBbsAnswer;
import bnet.library.util.pagination.CorePagination;

@RestController
@RequestMapping("/api/boards/{boardId}/articles/{articleId}/cmmnts")
public class CmmntController {

	@Autowired
	private BoardCmmntService service;

	/***
	 * 댓글 목록 조회
	 *
	 * @param boardId 게시판ID
	 * @param articleId 게시글ID
	 * @param latest 최신순여부
	 * @param page 페이지번호
	 * @param itemsPerPage 페이지당 출력 건수
	 * @return
	 */
	@GetMapping("")
	public CorePagination<CmmtBbsAnswer> getList(@PathVariable String boardId, @PathVariable String articleId, Boolean latest, Long page, Long itemsPerPage) {
		return service.getList(boardId, articleId, latest, page, itemsPerPage);
	}

	/***
	 * 댓글 등록
	 *
	 * @param boardId 게시판ID
	 * @param articleId 게시글ID
	 * @param cmmnt 댓글내용
	 * @return
	 */
	@PostMapping("")
	public CmmtBbsAnswer add(@PathVariable String boardId, @PathVariable String articleId, String cmmnt) {
		return service.add(boardId, articleId, cmmnt);
	}

	/***
	 * 댓글 수정
	 *
	 * @param boardId 게시판ID
	 * @param articleId 게시글ID
	 * @param cmmntId 댓글ID
	 * @param cmmnt 댓글내용
	 * @return
	 */
	@PutMapping("{cmmntId}")
	public CmmtBbsAnswer modify(@PathVariable String boardId, @PathVariable String articleId, @PathVariable String cmmntId, String cmmnt) {
		return service.modify(boardId, articleId, cmmntId, cmmnt);
	}

	/***
	 * 댓글 삭제
	 * @param boardId 게시판ID
	 * @param articleId 게시글ID
	 * @param cmmntId 댓글ID
	 */
	@DeleteMapping("{cmmntId}")
	public void remove(@PathVariable String boardId, @PathVariable String articleId, @PathVariable String cmmntId) {
		service.remove(boardId, articleId, cmmntId);
	}
}
