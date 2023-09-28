package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExpertReqstHist;

@Repository
public interface UsptExpertReqstHistDao {

	List<UsptExpertReqstHist> selectList(UsptExpertReqstHist inputParam);

	int insert(UsptExpertReqstHist inputParam);

	int upate(UsptExpertReqstHist inputParam);

	int delete(UsptExpertReqstHist inputParam);

	int deleteExpert(String  expertId);
	/**전문가신청 상세 처리이력**/
	List<UsptExpertReqstHist> selectExpertReqstHistList(String expertId);
}
