package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.RecomendClDto;
import aicluster.pms.common.entity.UsptStdrRecomendCl;

@Repository
public interface UsptStdrRecomendClDao {
	void insert(UsptStdrRecomendCl info);
	int deleteAll(String stdrBsnsCd);
	List<RecomendClDto> selectList(String stdrBsnsCd);
}
