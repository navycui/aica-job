package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptTaskTaxitmWctHist;

@Repository
public interface UsptTaskTaxitmWctHistDao {
	/*
	 * 과제세목별사업비변경이력
	 *
	 */

	int insert (UsptTaskTaxitmWctHist usptTaskTaxitmWctHist);
	//조회List
	List<UsptTaskTaxitmWctHist> selectMaxHistList(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("bsnsYear") String bsnsYear);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptTaskTaxitmWctHistChangeDe(String cnvnChangeReqId);
	//전후 이력 조회
	List<UsptTaskTaxitmWctHist> selectUsptTaskTaxitmWctHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}
