package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnApplcntHist;
import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;

@Repository
public interface UsptCnvnApplcntHistDao {
	/*
	 * 협약신청자정보변경이력
	 *
	 */

	int insert (UsptCnvnApplcntHist usptCnvnApplcntHist);

	// 최근이력조회
	UsptCnvnApplcntHist selectMaxHist(String cnvnChangeReqId);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptCnvnApplcntHistChangeDe(String cnvnChangeReqId);
	//전후 이력 조회
	List<UsptCnvnApplcntHist> selectUsptCnvnApplcntHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}
