package aicluster.framework.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtSmsRcver;

@Repository("FwCmmtSmsRcverDao")
public interface CmmtSmsRcverDao {
	void insertList(@Param("list") List<CmmtSmsRcver> list);
}
