package aicluster.pms.common.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptCnvnSclpstHist;

@Repository
public interface UsptCnvnSclpstHistDao {
	/*
	 * 협약수행기관신분변경이력
	 *
	 */

	int insert (UsptCnvnSclpstHist usptCnvnSclpstHist);
	// 최근이력조회
	UsptCnvnSclpstHist selectMaxHist(String cnvnChangeReqId);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptCnvnSclpstHistChangeDe(String cnvnChangeReqId);

	//전후 이력 조회
	List<UsptCnvnSclpstHist> selectUsptCnvnSclpstHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);

}
