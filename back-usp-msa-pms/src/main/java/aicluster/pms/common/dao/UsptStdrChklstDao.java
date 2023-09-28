package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsns.dto.Chklst;
import aicluster.pms.common.entity.UsptStdrChklst;

@Repository
public interface UsptStdrChklstDao {
	void insert(UsptStdrChklst info);
	int update(UsptStdrChklst info);
	int delete(UsptStdrChklst info);
	List<Chklst> selectList(String stdrBsnsCd);
}
