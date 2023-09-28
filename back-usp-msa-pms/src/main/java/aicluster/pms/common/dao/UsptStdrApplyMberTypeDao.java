package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsns.dto.ApplyMberType;
import aicluster.pms.common.entity.UsptStdrApplyMberType;

@Repository
public interface UsptStdrApplyMberTypeDao {
	int deleteAll(String stdrBsnsCd);
	void insert(UsptStdrApplyMberType info);
	List<ApplyMberType> selectViewList(String stdrBsnsCd);
	List<UsptStdrApplyMberType> selectList(String stdrBsnsCd);
}
