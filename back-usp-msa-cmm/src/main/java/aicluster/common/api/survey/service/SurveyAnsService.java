package aicluster.common.api.survey.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.survey.dto.SurveyAnsParam;
import aicluster.common.api.survey.dto.SurveyAnswer;
import aicluster.common.api.survey.dto.SurveyQuestionAns;
import aicluster.common.common.dao.CmmtQustnrRspnsDao;
import aicluster.common.common.dao.CmmtQustnrDao;
import aicluster.common.common.dao.CmmtQustnrMberRspnsDao;
import aicluster.common.common.dao.CmmtQustnrQesitmDao;
import aicluster.common.common.entity.CmmtQustnr;
import aicluster.common.common.entity.CmmtQustnrRspns;
import aicluster.common.common.entity.CmmtQustnrMberRspns;
import aicluster.common.common.entity.CmmtQustnrQesitm;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.SurveyUtils;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;

@Service
public class SurveyAnsService {

	@Autowired
	private CmmtQustnrDao cmmtSurveyDao;

	@Autowired
	private CmmtQustnrMberRspnsDao cmmtSurveyMemberAnsDao;

	@Autowired
	private CmmtQustnrQesitmDao cmmtSurveyQuestionDao;

	@Autowired
	private CmmtQustnrRspnsDao cmmtSurveyAnswerDao;

	@Autowired
	private SurveyUtils surveyUtils;


	public CmmtQustnr get(String surveyId) {
		BnMember worker = SecurityUtils.getCurrentMember();

		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(surveyId);
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지가 없습니다.");
		}

		// 진행중인 설문지인가?
		if (!surveyUtils.isOnGoing(cmmtSurvey)) {
			throw new InvalidationException("설문기간이 아닙니다.");
		}

		if (cmmtSurvey.getDuplicated() == null) {
			cmmtSurvey.setDuplicated(false);
		}

		// 이미 답변한 설문지인가?
		boolean answered = surveyUtils.answered(cmmtSurvey, worker.getMemberId());
		if (answered) {
			throw new InvalidationException("이미 참여하셨습니다.");
		}

		List<CmmtQustnrQesitm> questionList = cmmtSurveyQuestionDao.selectList(surveyId);
		List<CmmtQustnrRspns> allAnswerList = cmmtSurveyAnswerDao.selectList_surveyId(surveyId);

		for (CmmtQustnrQesitm q : questionList) {
			List<CmmtQustnrRspns> answerList = surveyUtils.findQuestionAnswers(allAnswerList, q);
			q.setAnswerList(answerList);
		}

		cmmtSurvey.setQuestionList(questionList);
		return cmmtSurvey;
	}


	public void answer(SurveyAnsParam param) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("로그인후 사용하세요.");
		}

		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(param.getSurveyId());
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지가 없습니다.");
		}

		if (!surveyUtils.isOnGoing(cmmtSurvey)) {
			throw new InvalidationException("설문기간이 아닙니다.");
		}

		// 이미 답변한 설문지인가?
		boolean answered = surveyUtils.answered(cmmtSurvey, worker.getMemberId());
		if (answered) {
			throw new InvalidationException("이미 참여하셨습니다.");
		}

		/*
		 * 답변저장
		 */
		List<CmmtQustnrMberRspns> memberAnsList = new ArrayList<>();
		InvalidationsException exs = new InvalidationsException();
		for (SurveyQuestionAns ans : param.getQuestions()) {
			CmmtQustnrQesitm cmmtSurveyQuestion = cmmtSurveyQuestionDao.select(param.getSurveyId(), ans.getQuestionId());
			if (cmmtSurveyQuestion == null) {
				exs.add("questionId-" + ans.getQuestionId(), "질문이 존재하지 않습니다.");
				throw exs;
			}
			if (ans.getAnswers() == null) {
				ans.setAnswers(new ArrayList<>());
			}

			// 필수 검사
			if (cmmtSurveyQuestion.getRequired() && ans.getAnswers().size() == 0) {
				exs.add("questionId-" + ans.getQuestionId(), "이 질문은 답변을 하셔야 합니다.");
			}

			// 주관식
			if (string.equals(CodeExt.surveyQuestionType.주관식, cmmtSurveyQuestion.getQuestionType())) {
				if (ans.getAnswers().size() > 1) {
					exs.add("questionId-" + ans.getQuestionId(), "주관식은 답변이 하나여야 합니다.");
				}
			}
			// 객관식
			else if (string.equals(CodeExt.surveyQuestionType.객관식, cmmtSurveyQuestion.getQuestionType())) {
				if (ans.getAnswers().size() > 1) {
					exs.add("questionId-" + ans.getQuestionId(), "객관식은 답변이 하나여야 합니다.");
				}
			}

			for (SurveyAnswer sa : ans.getAnswers()) {
				CmmtQustnrMberRspns sma = CmmtQustnrMberRspns.builder()
						.memberAnsId(string.getNewId("sma-"))
						.surveyId(param.getSurveyId())
						.questionId(ans.getQuestionId())
						.answerId(sa.getAnswerId())
						.memberId(worker.getMemberId())
						.shortAnswer(sa.getShortAnswer())
						.createdDt(now)
						.build();
				memberAnsList.add(sma);
			}
		}

		if (exs.size() > 0) {
			throw exs;
		}

		cmmtSurveyMemberAnsDao.insertList(memberAnsList);
	}

}
