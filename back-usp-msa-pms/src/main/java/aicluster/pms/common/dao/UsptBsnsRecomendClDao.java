package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.RecomendClDto;
import aicluster.pms.common.entity.UsptBsnsRecomendCl;

@Repository
public interface UsptBsnsRecomendClDao {
	void insert(UsptBsnsRecomendCl info);
	void insertList(List<UsptBsnsRecomendCl> list);
	int deleteAll(String bsnsCd);
	List<RecomendClDto> selectList(String bsnsCd);
}
