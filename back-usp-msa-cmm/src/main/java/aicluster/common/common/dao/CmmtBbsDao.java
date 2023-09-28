package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.qna.dto.QnaExtsnResponseParam;
import aicluster.common.common.entity.CmmtBbs;

@Repository
public interface CmmtBbsDao {

	List<CmmtBbs> selectList(
			@Param("systemId") String systemId,
			@Param("enabled") Boolean enabled,
			@Param("boardId") String boardId,
			@Param("boardNm") String boardNm);

	CmmtBbs select(String boardId);

	void insert(CmmtBbs board);

	void update(CmmtBbs board);

	void delete(String boardId);

	void updateArticleCnt(String boardId);

	QnaExtsnResponseParam selectBoardExtsn(String boardId);

}
