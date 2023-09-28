package aicluster.framework.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtEmailRcver;

@Repository("FwCmmtEmailRcverDao")
public interface CmmtEmailRcverDao {
	void insertList(@Param("list") List<CmmtEmailRcver> list);
}
