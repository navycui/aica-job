package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptTaskRspnberHist;

@Repository
public interface UsptTaskRspnberHistDao {

	/**
	 * 과제책임자변경이력
	 */
	int insert(UsptTaskRspnberHist usptTaskRspnberHist);

	// 최근이력조회
	UsptTaskRspnberHist selectMaxHist(String cnvnChangeReqId);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptTaskRspnberHistChangeDe(String cnvnChangeReqId);
	//전후 이력 조회
	List<UsptTaskRspnberHist> selectUsptTaskRspnberHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}
