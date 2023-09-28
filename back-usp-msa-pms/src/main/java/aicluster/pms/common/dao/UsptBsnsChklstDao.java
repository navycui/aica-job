package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsns.dto.Chklst;
import aicluster.pms.common.entity.UsptBsnsChklst;

@Repository
public interface UsptBsnsChklstDao {
	void insert(UsptBsnsChklst info);
	void insertList(List<UsptBsnsChklst> list);
	int update(UsptBsnsChklst info);
	int delete(UsptBsnsChklst info);
	List<Chklst> selectList(String bsnsCd);
}
