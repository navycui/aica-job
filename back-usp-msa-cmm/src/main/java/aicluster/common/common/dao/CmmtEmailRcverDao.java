package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtEmailRcver;

@Repository
public interface CmmtEmailRcverDao {
	void insert(CmmtEmailRcver email);

	void insertList(@Param("list") List<CmmtEmailRcver> list);

	List<CmmtEmailRcver> selectList(String emailId);
}
