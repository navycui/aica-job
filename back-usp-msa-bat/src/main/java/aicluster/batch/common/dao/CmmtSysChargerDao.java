package aicluster.batch.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.CmmtSysCharger;

@Repository
public interface CmmtSysChargerDao {
	List<CmmtSysCharger> selectList(String apiSystemId);

	List<CmmtSysCharger> selectAll();
}
