package aicluster.common.api.survey.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.survey.dto.Question;
import aicluster.common.api.survey.dto.QuestionSaveParam;
import aicluster.common.api.survey.dto.SurveyAddParam;
import aicluster.common.api.survey.dto.SurveyListParam;
import aicluster.common.api.survey.dto.SurveyModifyParam;
import aicluster.common.common.dao.CmmtQustnrDao;
import aicluster.common.common.dao.CmmtQustnrMberRspnsDao;
import aicluster.common.common.dao.CmmtQustnrQesitmDao;
import aicluster.common.common.dao.CmmtQustnrRspnsDao;
import aicluster.common.common.dto.SurveyListItem;
import aicluster.common.common.entity.CmmtQustnr;
import aicluster.common.common.entity.CmmtQustnrQesitm;
import aicluster.common.common.entity.CmmtQustnrRspns;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.SurveyUtils;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class SurveyService {

	@Autowired
	private CmmtQustnrDao cmmtSurveyDao;

	@Autowired
	private CmmtQustnrMberRspnsDao cmmtSurveyMemberAnsDao;

	@Autowired
	private CmmtQustnrRspnsDao cmmtSurveyAnswerDao;

	@Autowired
	private CmmtQustnrQesitmDao cmmtSurveyQuestionDao;

	@Autowired
	private SurveyUtils surveyUtils;

	public CorePagination<SurveyListItem> getSurveyList(SurveyListParam param, CorePaginationParam pageParam)
	{
		BnMember worker = SecurityUtils.getCurrentMember();

		if (param.getBeginDay() == null || param.getEndDay() == null) {
			throw new InvalidationException("설문기간을 입력하세요.");
		}

		// 로그인 사용자가 내부사용자, 평가위원이 아닌 경우 설문 참여여부 확인 항목을 추가한다.
		String srchMemberId = null;
		if ( worker != null && !CodeExt.memberTypeExt.isInsider(worker.getMemberType()) && !CodeExt.memberTypeExt.isEvaluator(worker.getMemberType()) ) {
			srchMemberId = worker.getMemberId();
		}

		long totalItems = cmmtSurveyDao.selectList_count(param, srchMemberId);
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<SurveyListItem> list = cmmtSurveyDao.selectList(param, srchMemberId, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());

		return new CorePagination<>(info, list);
	}

	public CmmtQustnr addSurvey(SurveyAddParam param) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if (!string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		InvalidationsException exs = new InvalidationsException();
		if (string.isBlank(param.getSystemId())) {
			exs.add("systemId", "포털구분을 입력하세요.");
		}
		if (string.isBlank(param.getSurveyNm())) {
			exs.add("surveyNm", "설문지명을 입력하세요.");
		}
		if (param.getBeginDay() == null) {
			exs.add("beginDay", "시작일을 입력하세요.");
		}
		if (param.getEndDay() == null) {
			exs.add("endDay", "종료일을 입력하세요.");
		}
		if (param.getEndDay() == null) {
			exs.add("endDay", "종료일을 입력하세요.");
		}
		if (date.compareDay(string.toDate(param.getBeginDay()), string.toDate(param.getEndDay())) > 0) {
			exs.add("beginDay", "진행기간(시작일)은 진행기간(종료일) 보다 클수 없습니다");
		}
		if (param.getEnabled() == null) {
			param.setEnabled(true);
		}
		if (param.getDuplicated() == null) {
			param.setDuplicated(false);
		}

		if (exs.size() > 0) {
			throw exs;
		}

		CmmtQustnr cmmtSurvey = CmmtQustnr.builder()
				.surveyId(string.getNewId("survey-"))
				.surveyNm(param.getSurveyNm())
				.systemId(param.getSystemId())
				.beginDay(param.getBeginDay())
				.endDay(param.getEndDay())
				.remark(param.getRemark())
				.enabled(param.getEnabled())
				.duplicated(param.getDuplicated())
				.createdDt(now)
				.creatorId(worker.getMemberId())
				.updatedDt(now)
				.updaterId(worker.getMemberId())
				.build();
		cmmtSurveyDao.insert(cmmtSurvey);

		cmmtSurvey = cmmtSurveyDao.select(cmmtSurvey.getSurveyId());
		return cmmtSurvey;
	}

	public CmmtQustnr getSurvey(String surveyId) {
		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(surveyId);
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지가 존재하지 않습니다.");
		}
		return cmmtSurvey;
	}

	public CmmtQustnr modifySurvey(SurveyModifyParam param) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if (!string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(param.getSurveyId());
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지 정보가 없습니다.");
		}

		InvalidationsException exs = new InvalidationsException();

		if (string.isBlank(param.getSystemId())) {
			exs.add("systemId", "포털구분을 입력하세요.");
		}
		if (string.isBlank(param.getSurveyNm())) {
			exs.add("surveyNm", "설문지명을 입력하세요.");
		}
		if (param.getBeginDay() == null) {
			exs.add("beginDay", "시작일을 입력하세요.");
		}
		if (param.getEndDay() == null) {
			exs.add("endDay", "종료일을 입력하세요.");
		}
		if (param.getEnabled() == null) {
			param.setEnabled(true);
		}
		if (param.getDuplicated() == null) {
			param.setDuplicated(false);
		}

		if (exs.size() > 0) {
			throw exs;
		}

		cmmtSurvey.setSurveyNm(param.getSurveyNm());
		cmmtSurvey.setSystemId(param.getSystemId());
		cmmtSurvey.setBeginDay(param.getBeginDay());
		cmmtSurvey.setEndDay(param.getEndDay());
		cmmtSurvey.setRemark(param.getRemark());
		cmmtSurvey.setEnabled(param.getEnabled());
		cmmtSurvey.setDuplicated(param.getDuplicated());
		cmmtSurvey.setUpdatedDt(now);
		cmmtSurvey.setUpdaterId(worker.getMemberId());
		cmmtSurveyDao.update(cmmtSurvey);

		cmmtSurvey = cmmtSurveyDao.select(cmmtSurvey.getSurveyId());
		return cmmtSurvey;
	}

	public void removeSurvey(String surveyId) {
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if (!string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(surveyId);
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지 정보가 없습니다.");
		}

		// 답변이 존재하는 설문지는 삭제불가
		Boolean ansExists = cmmtSurveyMemberAnsDao.existsSurveyAns(surveyId);
		if (ansExists) {
			throw new InvalidationException("사용자의 답변이 존재하는 설문지는 변경할 수 없습니다.");
		}

		// 진행중인 설문지는 삭제불가
		if (surveyUtils.isOnGoing(cmmtSurvey)) {
			throw new InvalidationException("진행중인 설문은 삭제할 수 없습니다.");
		}

		// 답변 목록 삭제
		cmmtSurveyAnswerDao.deleteList_surveyId(surveyId);

		// 질문 목록 삭제
		cmmtSurveyQuestionDao.deleteList_surveyId(surveyId);

		// 설문지 삭제
		cmmtSurveyDao.delete(surveyId);
	}

	public JsonList<CmmtQustnrQesitm> getQuestionList(String surveyId) {
		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(surveyId);
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지 기본정보가 없습니다.");
		}

		List<CmmtQustnrQesitm> questionList = cmmtSurveyQuestionDao.selectList(surveyId);
		List<CmmtQustnrRspns> allAnswerList = cmmtSurveyAnswerDao.selectList_surveyId(surveyId);

		for (CmmtQustnrQesitm question : questionList) {
			List<CmmtQustnrRspns> answerList = surveyUtils.findQuestionAnswers(allAnswerList, question);
			question.setAnswerList(answerList);
		}
		return new JsonList<>(questionList);
	}

	public JsonList<CmmtQustnrQesitm> saveQuestions(QuestionSaveParam param) {
		CmmtQustnr cmmtSurvey = cmmtSurveyDao.select(param.getSurveyId());
		if (cmmtSurvey == null) {
			throw new InvalidationException("설문지 기본정보가 없습니다.");
		}

		// 사용자 답변이 존재하는 경우, 수정할 수 없다.
		boolean ansExists = cmmtSurveyMemberAnsDao.existsSurveyAns(param.getSurveyId());
		if (ansExists) {
			throw new InvalidationException("사용자의 답변이 존재하는 설문지는 변경할 수 없습니다.");
		}

		/*
		 * 질문 삭제
		 */
		cmmtSurveyAnswerDao.deleteList_surveyId(param.getSurveyId());
		cmmtSurveyQuestionDao.deleteList_surveyId(param.getSurveyId());

		/*
		 * 질문 추가
		 */
		List<CmmtQustnrQesitm> questionList = new ArrayList<>();
		List<CmmtQustnrRspns> answerList = new ArrayList<>();
		int questionNo = 0;
		for (Question question : param.getQuestionList()) {
			questionNo++;
			if (question.getRequired() == null) {
				question.setRequired(false);
			}
			if (string.isBlank(question.getQuestionCn())) {
				throw new InvalidationException("[질문 " + questionNo + "] 질문을 입력하세요.");
			}
			if (string.isBlank(question.getQuestionType())) {
				throw new InvalidationException("[질문 " + questionNo + "] 질문유형을 입력하세요.");
			}

			CmmtQustnrQesitm cmmtSurveyQuestion = CmmtQustnrQesitm.builder()
					.surveyId(param.getSurveyId())
					.questionId(string.getNewId("quest-"))
					.questionNo(questionNo)
					.questionType(question.getQuestionType())
					.required(question.getRequired())
					.questionCn(question.getQuestionCn())
					.build();
			questionList.add(cmmtSurveyQuestion);

			// 질문유형이 '주관식'인데 항목이 없는 경우 임의 항목 생성
			if (string.equals(question.getQuestionType(), CodeExt.surveyQuestionType.주관식) && (question.getAnswerCnList() == null || question.getAnswerCnList().isEmpty())) {
				List<String> shortAnswerCn = new ArrayList<>();
				shortAnswerCn.add("입력 고정 항목");
				question.setAnswerCnList(shortAnswerCn);
			}

			int answerNo = 0;
			for (String answerCn : question.getAnswerCnList()) {
				answerNo++;
				if (string.isBlank(answerCn)) {
					throw new InvalidationException("[질문 " + questionNo + " > 항목 " + answerNo + "] 항목 내용을 입력하세요.");
				}
				CmmtQustnrRspns cmmtSurveyAnswer = CmmtQustnrRspns.builder()
						.surveyId(cmmtSurveyQuestion.getSurveyId())
						.questionId(cmmtSurveyQuestion.getQuestionId())
						.answerId(string.getNewId("ans-"))
						.answerNo(answerNo)
						.answerCn(answerCn)
						.build();
				answerList.add(cmmtSurveyAnswer);
			}

			if (string.equals(question.getQuestionType(), CodeExt.surveyQuestionType.주관식) && answerNo != 1) {
				throw new InvalidationException("[질문 " + questionNo + "] 주관식 질문은 답변 항목이 1개여야 합니다.");
			}
			else if (!string.equals(question.getQuestionType(), CodeExt.surveyQuestionType.주관식) && answerNo < 2) {
				throw new InvalidationException("[질문 " + questionNo + "] 객관식과 다중선택 질문은 답변 항목이 2개 이상이어야 합니다.");
			}
		}

		if(questionList != null && questionList.size() > 0) {
			cmmtSurveyQuestionDao.insertList(questionList);
		}
		
		if(answerList != null && answerList.size() > 0) {
			cmmtSurveyAnswerDao.insertList(answerList);
		}
		
		return getQuestionList(param.getSurveyId());
	}

}
