package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.survey.dto.SurveyListParam;
import aicluster.common.common.dto.SurveyListItem;
import aicluster.common.common.entity.CmmtQustnr;

@Repository
public interface CmmtQustnrDao {

	void insert(CmmtQustnr survey);

	CmmtQustnr select(String surveyId);

	long selectList_count(
			@Param("param") SurveyListParam param,
			@Param("memberId") String memberId);

	List<SurveyListItem> selectList(
			@Param("param") SurveyListParam param,
			@Param("memberId") String memberId,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	void delete(String surveyId);

	void update(CmmtQustnr dbSurvey);
}
