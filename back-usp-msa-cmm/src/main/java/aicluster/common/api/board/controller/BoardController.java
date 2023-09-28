package aicluster.common.api.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.board.dto.BoardParam;
import aicluster.common.api.board.service.BoardService;
import aicluster.common.common.entity.CmmtBbs;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

	@Autowired
	private BoardService service;

	/**
	 * 게시판 목록 조회
	 * @param systemId
	 * @param enabled
	 * @param boardId
	 * @param boardNm
	 * @return
	 */
	@GetMapping("")
	public JsonList<CmmtBbs> getList(String systemId, Boolean enabled, String boardId, String boardNm) {
		return service.getList(systemId, enabled, boardId, boardNm);
	}

	/**
	 * 게시판 등록
	 * @param param
	 * @return
	 */
	@PostMapping("")
	public CmmtBbs add(@RequestBody BoardParam param) {
		return service.add( param );
	}

	/**
	 * 게시판 조회
	 * @param boardId
	 * @return
	 */
	@GetMapping("/{boardId}")
	public CmmtBbs get(@PathVariable String boardId) {
		return service.get(boardId);
	}

	/**
	 * 게시판 수정
	 * @param boardId
	 * @param param
	 * @return
	 */
	@PutMapping("/{boardId}")
	public CmmtBbs modify(@PathVariable String boardId, @RequestBody BoardParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		if (param.getBoard() == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.getBoard().setBoardId(boardId);
		if (param.getAuthority() == null) {
			throw new InvalidationException("게시판 권한 정보가 없습니다.");
		}
		return service.modify(param);
	}

	/**
	 * 게시판 삭제
	 * @param boardId
	 */
	@DeleteMapping("/{boardId}")
	public void remove(@PathVariable String boardId) {
		service.remove(boardId);
	}
}
