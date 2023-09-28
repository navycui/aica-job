package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptTaskReqstWctHist;

@Repository
public interface UsptTaskReqstWctHistDao {

	/**
	 * 과제신청사업비변경이력
	 */

	int insert(UsptTaskReqstWctHist usptTaskReqstWctHist);
	//해당사업계획의 총사업비 조회
	Long selectTotalWctList(String cnvnChangeReqId);
	//최신변경이력조회List
	List<UsptTaskReqstWctHist> selectMaxHistList(String cnvnChangeReqId);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptTaskReqstWctHistChangeDe(String cnvnChangeReqId);
	//전후 이력 조회
		List<UsptTaskReqstWctHist> selectUsptTaskReqstWctHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}