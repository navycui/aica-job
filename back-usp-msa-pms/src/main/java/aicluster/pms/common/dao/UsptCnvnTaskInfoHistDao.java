package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptCnvnTaskInfoHist;

@Repository
public interface UsptCnvnTaskInfoHistDao {
	/*
	 * 협약과제정보변경이력
	 *
	 */

	int insert (UsptCnvnTaskInfoHist usptCnvnTaskInfoHist);
	// 최신이력조회
	UsptCnvnTaskInfoHist selectMaxHist(String cnvnChangeReqId);
	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptCnvnTaskInfoHistChangeDe(String cnvnChangeReqId);

	//전후 이력 조회
	List<UsptCnvnTaskInfoHist> selectUsptCnvnTaskInfoHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}
