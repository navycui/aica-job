package aicluster.common.api.qna.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.qna.dto.QnaExtsnResponseParam;
import aicluster.common.api.qna.service.QnaService;

@RestController
@RequestMapping("/api/qna/extsn")
public class QnaExtsnController {

	@Autowired
	private QnaService service;
	
	@GetMapping("/{qnaId}")
	public QnaExtsnResponseParam getQnaExtsn(@PathVariable String qnaId) {
		return service.getQnaExtsn(qnaId);
	}
}
