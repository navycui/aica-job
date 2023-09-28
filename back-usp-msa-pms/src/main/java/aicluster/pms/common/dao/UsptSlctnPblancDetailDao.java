package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptSlctnPblancDetail;

@Repository
public interface UsptSlctnPblancDetailDao {
	void insert(UsptSlctnPblancDetail info);
	int update(UsptSlctnPblancDetail info);
	int delete(UsptSlctnPblancDetail info);
	List<UsptSlctnPblancDetail> selectList(String slctnPblancId);
}
