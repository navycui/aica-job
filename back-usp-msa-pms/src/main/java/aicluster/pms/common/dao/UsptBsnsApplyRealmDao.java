package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsApplyRealm;

@Repository
public interface UsptBsnsApplyRealmDao {
	void insert(UsptBsnsApplyRealm info);
	void insertList(List<UsptBsnsApplyRealm> list);
	int update(UsptBsnsApplyRealm info);
	int delete(UsptBsnsApplyRealm info);
	List<UsptBsnsApplyRealm> selectList(String bsnsCd);
}
