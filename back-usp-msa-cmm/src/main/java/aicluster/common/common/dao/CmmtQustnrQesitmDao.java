package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtQustnrQesitm;

@Repository
public interface CmmtQustnrQesitmDao {

	List<CmmtQustnrQesitm> selectList(String surveyId);

	CmmtQustnrQesitm select(
			@Param("surveyId") String surveyId,
			@Param("questionId") String questionId);

	List<CmmtQustnrQesitm> selectResultList(
			@Param("surveyId") String surveyId,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);

	void deleteList_surveyId(String surveyId);

	void insertList(@Param("list") List<CmmtQustnrQesitm> questions);



}
