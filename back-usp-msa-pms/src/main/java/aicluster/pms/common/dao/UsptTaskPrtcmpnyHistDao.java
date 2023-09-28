package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyHist;

@Repository
public interface UsptTaskPrtcmpnyHistDao {
	/*
	 * 과제참여기업변경이력
	 *
	 */
	/********************************** 협약변경관리 FRONT*/
	//신청내역 등록
	int insert (UsptTaskPrtcmpnyHist usptTaskPrtcmpnyHist);
	//조회
	List<UsptTaskPrtcmpnyHist> selectList(String cnvnChangeReqId);

}
