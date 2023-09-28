package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptTaskPartcptsHist;

@Repository
public interface UsptTaskPartcptsHistDao {
	/*
	 * 과제참여자변경이력
	 *
	 */
	//신청내역 등록
	int insert (UsptTaskPartcptsHist usptTaskPartcptsHist);
	//최근이력조회
	List<UsptTaskPartcptsHist>selectMaxHistList(String cnvnChangeReqId);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptTaskPartcptsHistChangeDe(String cnvnChangeReqId);
	//전후 이력 조회
	List<UsptTaskPartcptsHist> selectUsptTaskPartcptsHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}
