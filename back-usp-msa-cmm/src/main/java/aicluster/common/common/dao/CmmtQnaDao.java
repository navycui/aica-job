package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.qna.dto.QnaExtsnResponseParam;
import aicluster.common.common.dto.QnaQuestStatusCountItem;
import aicluster.common.common.entity.CmmtQna;

@Repository
public interface CmmtQnaDao {

	CmmtQna select(String qnaId);

	List<CmmtQna> selectList(
			@Param("systemId") String systemId,
			@Param("qnaId") String qnaId,
			@Param("qnaNm") String qnaNm,
			@Param("enabled") Boolean enabled);

	void insert(CmmtQna qna);

	void update(CmmtQna qna);

	void delete(String qnaId);

	List<QnaQuestStatusCountItem> selectQuestSt_count();

	QnaExtsnResponseParam getQnaExtsn(String qnaId);
}
