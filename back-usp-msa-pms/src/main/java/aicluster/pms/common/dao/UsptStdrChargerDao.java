package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrCharger;

@Repository
public interface UsptStdrChargerDao {
	void insert(UsptStdrCharger info);
	int update(UsptStdrCharger info);
	int delete(UsptStdrCharger info);
	List<UsptStdrCharger> selectList(String stdrBsnsCd);
}
