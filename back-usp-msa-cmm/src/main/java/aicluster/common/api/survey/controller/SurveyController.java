package aicluster.common.api.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.survey.dto.QuestionSaveParam;
import aicluster.common.api.survey.dto.SurveyAddParam;
import aicluster.common.api.survey.dto.SurveyListParam;
import aicluster.common.api.survey.dto.SurveyModifyParam;
import aicluster.common.api.survey.service.SurveyService;
import aicluster.common.common.dto.SurveyListItem;
import aicluster.common.common.entity.CmmtQustnr;
import aicluster.common.common.entity.CmmtQustnrQesitm;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

	@Autowired
	private SurveyService service;

	/**
	 * 설문지 목록조회
	 *
	 * @param param
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<SurveyListItem> getList(SurveyListParam param, CorePaginationParam pageParam) {
		return service.getSurveyList(param, pageParam);
	}

	/**
	 * 설문지 기본정보 등록
	 *
	 * @param param
	 * @return
	 */
	// 설문 기본정보 등록
	@PostMapping("")
	public CmmtQustnr addSurvey(@RequestBody SurveyAddParam param) {
		return service.addSurvey(param);
	}

	/**
	 * 설문지 기본정보 조회
	 *
	 * @param surveyId
	 * @return
	 */
	@GetMapping("{surveyId}")
	public CmmtQustnr getSurvey(@PathVariable String surveyId) {
		return service.getSurvey(surveyId);
	}

	/**
	 * 설문 기본정보 수정
	 *
	 * @param surveyId
	 * @param param
	 * @return
	 */
	@PutMapping("{surveyId}")
	public CmmtQustnr modifySurvey(@PathVariable String surveyId, @RequestBody SurveyModifyParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.setSurveyId(surveyId);
		return service.modifySurvey(param);
	}

	/**
	 * 설문지 삭제
	 *
	 * @param surveyId
	 */
	@DeleteMapping("{surveyId}")
	public void removeSurvey(@PathVariable String surveyId) {
		service.removeSurvey(surveyId);
	}

	/**
	 * 설문질문 목록조회
	 *
	 * @param surveyId
	 * @return
	 */
	@GetMapping("{surveyId}/questions")
	public JsonList<CmmtQustnrQesitm> getQuestions(@PathVariable String surveyId) {
		return service.getQuestionList(surveyId);
	}

	/**
	 * 설문질문 목록 저장
	 *
	 * @param surveyId
	 * @param param
	 * @return
	 */
	@PutMapping("{surveyId}/questions")
	public JsonList<CmmtQustnrQesitm> saveQuestions(@PathVariable String surveyId, @RequestBody QuestionSaveParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}
		param.setSurveyId(surveyId);
		return service.saveQuestions(param);
	}
}
