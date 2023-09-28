package aicluster.common.api.survey.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.survey.service.SurveyResultService;
import aicluster.common.common.entity.CmmtQustnr;
import aicluster.common.common.dto.SurveyAnswerersListItem;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/survey/{surveyId}/result")
public class SurveyResultController {

	@Autowired
	private SurveyResultService service;

	/**
	 * 설문결과 답변 목록 조회
	 * @param surveyId
	 * @param beginDay
	 * @param endDay
	 * @return
	 */
	@GetMapping("")
	public CmmtQustnr getResult(@PathVariable String surveyId, Date beginDay, Date endDay) {
		String fmtBeginDay = date.format(beginDay, "yyyyMMdd");
		String fmtEndDay = date.format(endDay, "yyyyMMdd");
		return service.getResult(surveyId, fmtBeginDay, fmtEndDay);
	}

	/**
	 * 설문결과 응답자 목록 조회
	 * @param surveyId
	 * @param beginDay
	 * @param endDay
	 * @return
	 */
	@GetMapping("/answerers")
	public JsonList<SurveyAnswerersListItem> getAnswerers(@PathVariable String surveyId, Date beginDay, Date endDay) {
		String fmtBeginDay = date.format(beginDay, "yyyyMMdd");
		String fmtEndDay = date.format(endDay, "yyyyMMdd");

		return service.getAnswerers(surveyId, fmtBeginDay, fmtEndDay);
	}
}
