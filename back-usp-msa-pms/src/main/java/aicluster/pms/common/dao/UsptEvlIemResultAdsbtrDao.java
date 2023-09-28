package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEvlIemResultAdsbtr;

@Repository
public interface UsptEvlIemResultAdsbtrDao {
	void insert(UsptEvlIemResultAdsbtr info);
	int update(UsptEvlIemResultAdsbtr info);
}
