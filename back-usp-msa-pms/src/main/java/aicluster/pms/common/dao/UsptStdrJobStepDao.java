package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.JobStepDto;
import aicluster.pms.common.entity.UsptStdrJobStep;

@Repository
public interface UsptStdrJobStepDao {
	void insert(UsptStdrJobStep info);
	int deleteAll(String stdrBsnsCd);
	List<JobStepDto> selectViewList(String stdrBsnsCd);
	List<UsptStdrJobStep> selectList(String stdrBsnsCd);
}
