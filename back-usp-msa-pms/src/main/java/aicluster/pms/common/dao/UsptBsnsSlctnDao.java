package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsSlctn;

@Repository
public interface UsptBsnsSlctnDao {
	void insert(UsptBsnsSlctn info);
	List<UsptBsnsSlctn> selectList(String bsnsSlctnId);

}
