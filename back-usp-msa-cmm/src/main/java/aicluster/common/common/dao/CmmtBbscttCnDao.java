package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtBbscttCn;

@Repository
public interface CmmtBbscttCnDao {

	void insertList(@Param("list") List<CmmtBbscttCn> list);

	List<CmmtBbscttCn> selectList(String articleId);

	void deleteList(String articleId);
}
