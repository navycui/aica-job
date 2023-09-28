package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsns.dto.ApplyMberType;
import aicluster.pms.common.entity.UsptBsnsApplyMberType;

@Repository
public interface UsptBsnsApplyMberTypeDao {
	int deleteAll(String bsnsCd);
	void insert(UsptBsnsApplyMberType info);
	void insertList(List<UsptBsnsApplyMberType> list);
	List<ApplyMberType> selectList(String bsnsCd);
}
