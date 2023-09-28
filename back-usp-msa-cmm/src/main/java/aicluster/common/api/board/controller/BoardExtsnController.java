package aicluster.common.api.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.board.service.BoardService;
import aicluster.common.api.qna.dto.QnaExtsnResponseParam;

@RestController
@RequestMapping("/api/board/extsn")
public class BoardExtsnController {
	@Autowired
	private BoardService service;
	
	@GetMapping("/{boardId}")
	public QnaExtsnResponseParam getBoardExtsn(@PathVariable String boardId) {
		return service.getBoardExtsn(boardId);
	}
	
}
