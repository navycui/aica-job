package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtBbsAuthor;

@Repository
public interface CmmtBbsAuthorDao {

	void delete_board(String boardId);

	List<CmmtBbsAuthor> selectList(String boardId);

	void save(CmmtBbsAuthor cmmtBoardAuthority);

	void insertList(@Param("list") List<CmmtBbsAuthor> list);

	CmmtBbsAuthor select(
			@Param("boardId") String boardId,
			@Param("authorityId") String authorityId);

}
