package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.JobStepDto;
import aicluster.pms.common.entity.UsptBsnsJobStep;

@Repository
public interface UsptBsnsJobStepDao {
	void insert(UsptBsnsJobStep info);
	void insertList(List<UsptBsnsJobStep> list);
	int deleteAll(String bsnsCd);
	List<JobStepDto> selectViewList(String bsnsCd);
}
