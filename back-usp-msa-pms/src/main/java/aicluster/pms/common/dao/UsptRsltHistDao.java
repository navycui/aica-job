package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.RsltHistPresentnDto;
import aicluster.pms.common.entity.UsptRsltHist;

@Repository
public interface UsptRsltHistDao {
	void insert(UsptRsltHist info);
	UsptRsltHist select(@Param("rsltId") String rsltId, @Param("rsltHistId") String rsltHistId);
	List<UsptRsltHist> selectList(@Param("rsltId") String rsltId, @Param("processMberType") String processMberType);
	List<RsltHistPresentnDto> selectPresentnDtList(String rsltId);
}
