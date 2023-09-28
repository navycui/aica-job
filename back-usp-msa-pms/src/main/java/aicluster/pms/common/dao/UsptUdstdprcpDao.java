package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptUdstdprcp;

@Repository
public interface UsptUdstdprcpDao {
	List<UsptUdstdprcp> selectList(@Param("evlPlanId") String evlPlanId, @Param("udstdprcpId") String udstdprcpId);

	void insertList(@Param("list") List<UsptUdstdprcp> usptUdstdprcpList);

	void deleteByPlanId(String evlPlanId);

	void delete(String usptUdstdprcpId);
}
