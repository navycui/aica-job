package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtEventCn;

@Repository
public interface CmmtEventCnDao {

	void insertList(@Param("list") List<CmmtEventCn> list);

	List<CmmtEventCn> selectList(String eventId);

	void deleteList(String articleId);
}
