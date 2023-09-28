package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExclMfcmm;

@Repository
public interface UsptExclMfcmmDao {
	//제외위원등록
	void insertList(@Param("list") List<UsptExclMfcmm> list);

	void deleteByCmitId(String evlCmitId);

	void deleteByExpertId(String expertId);
}
