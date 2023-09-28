package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.qna.dto.QnaQuestListParam;
import aicluster.common.common.dto.QnaQuestListItem;
import aicluster.common.common.entity.CmmtQnaQuest;

@Repository
public interface CmmtQnaQuestDao {

	Boolean existsQnaQuests(String qnaId);

	CmmtQnaQuest select(String questId);

	long selectList_count(
			@Param("qnaId") String qnaId,
			@Param("param") QnaQuestListParam param,
			@Param("questionerId") String questionerId);

	List<QnaQuestListItem> selectList(
			@Param("qnaId") String qnaId,
			@Param("param") QnaQuestListParam param,
			@Param("questionerId") String questionerId,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	void update(CmmtQnaQuest quest);

	void insert(CmmtQnaQuest quest);

	void delete(String questId);

	long selectMinuteCount(@Param("qnaId") String qnaId, @Param("memberId") String memberId);


}
