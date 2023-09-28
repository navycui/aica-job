package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsCnvnPrtcmpnySign;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;

@Repository
public interface UsptTaskPrtcmpnyDao {
	/** 최근발송일 저장 */
	int updateRecentSendDate(UsptTaskPrtcmpny info);

	List<UsptTaskPrtcmpny> selectList(UsptTaskPrtcmpny usptTaskPrtcmpny);

	List<UsptTaskPrtcmpny> selectBoxList(String bsnsPlanDocId);

	/*전자협약에서 기업정보 조회(전자협약 생성시)*/
	List<UsptBsnsCnvnPrtcmpnySign> selectBsnsCnvnList(String bsnsPlanDocId);

	int insert(UsptTaskPrtcmpny usptTaskPrtcmpny);

	int update(UsptTaskPrtcmpny usptTaskPrtcmpny);

	int delete(UsptTaskPrtcmpny usptTaskPrtcmpny);

	int deleteBsnsPlanDocIdAll(String  bsnsPlanDocId);

}
