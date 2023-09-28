package aicluster.pms.common.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsExcclc;

@Repository
public interface UsptBsnsExcclcDao {
	void insert(UsptBsnsExcclc info);
	int update(UsptBsnsExcclc info);
	UsptBsnsExcclc select(@Param("bsnsCnvnId") String bsnsCnvnId
						, @Param("taskReqstWctId") String taskReqstWctId);
}
