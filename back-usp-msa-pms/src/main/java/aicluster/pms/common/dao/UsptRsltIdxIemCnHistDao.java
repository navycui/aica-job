package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.RsltIdxIemCnHistDto;
import aicluster.pms.common.entity.UsptRsltIdxIemCnHist;

@Repository
public interface UsptRsltIdxIemCnHistDao {
	void insert(UsptRsltIdxIemCnHist info);
	List<RsltIdxIemCnHistDto> selectList(@Param("rsltHistId") String rsltHistId, @Param("rsltIdxIemId") String rsltIdxIemId);
}
