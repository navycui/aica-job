package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtBbscttLink;

@Repository
public interface CmmtBbscttLinkDao {

	void insertList(@Param("list") List<CmmtBbscttLink> list);

	List<CmmtBbscttLink> selectList(String articleId);

	void deleteList(String articleId);
}
