package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsCharger;

@Repository
public interface UsptBsnsChargerDao {
	void insert(UsptBsnsCharger info);
	void insertList(List<UsptBsnsCharger> list);
	int update(UsptBsnsCharger info);
	int delete(UsptBsnsCharger info);
	List<UsptBsnsCharger> selectList(String bsnsCd);
}
