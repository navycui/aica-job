package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.dto.ResultAnswerDto;
import aicluster.common.common.dto.SurveyAnswerersListItem;
import aicluster.common.common.entity.CmmtQustnrMberRspns;

@Repository
public interface CmmtQustnrMberRspnsDao {

	void insertList(@Param("list") List<CmmtQustnrMberRspns> list);

	Boolean existsSurveyAns(String surveyId);

	List<SurveyAnswerersListItem> selectCmmvUserList_answerer(
			@Param("surveyId") String surveyId,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);

	Boolean existsMember(
			@Param("surveyId") String surveyId,
			@Param("memberId") String memberId);

	boolean existsMember_day(
			@Param("surveyId") String surveyId,
			@Param("memberId") String memberId,
			@Param("day") String day);

	List<ResultAnswerDto> selectResultAnswerList(
			@Param("surveyId") String surveyId,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);
}
