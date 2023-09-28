package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtBbsAnswer;

@Repository
public interface CmmtBbsAnswerDao {

	void deleteList(String articleId);

	long selectList_count(String articleId);

	List<CmmtBbsAnswer> selectList(
			@Param("articleId") String articleId,
			@Param("latest") Boolean latest,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	void insert(CmmtBbsAnswer boardCmmnt);

	CmmtBbsAnswer select(
			@Param("articleId") String articleId,
			@Param("cmmntId") String cmmntId);

	void update(CmmtBbsAnswer boardCmmnt);

	void delete(
			@Param("articleId") String articleId,
			@Param("cmmntId") String cmmntId);

}