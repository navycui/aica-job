package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrApplyRealm;

@Repository
public interface UsptStdrApplyRealmDao {
	void insert(UsptStdrApplyRealm info);
	int update(UsptStdrApplyRealm info);
	int delete(UsptStdrApplyRealm info);
	List<UsptStdrApplyRealm> selectList(String stdrBsnsCd);
}
