package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyInfoHist;

@Repository
public interface UsptTaskPrtcmpnyInfoHistDao {
	/*
	 * 과제참여기업정보변경이력
	 *
	 */
	/********************************** 협약변경관리 FRONT*/
	//신청내역 등록
	int insert (UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHist);
	//최신이력조회
	UsptTaskPrtcmpnyInfoHist selectMaxHist(String cnvnChangeReqId);

	// 변경일자 조회
	List<UsptCnvnChangeReqHistDt> selectUsptTaskPrtcmpnyInfoHistChangeDe(String cnvnChangeReqId);

	//전후 이력 조회
	List<UsptTaskPrtcmpnyInfoHist> selectUsptTaskPrtcmpnyInfoHistByDt(@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("changeDt") String changeDt);
}
