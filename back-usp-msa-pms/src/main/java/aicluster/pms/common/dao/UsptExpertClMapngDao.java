package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.expertClfc.dto.ExpertClChargerParam;
import aicluster.pms.common.entity.UsptExpertClMapng;

@Repository
public interface UsptExpertClMapngDao {

	/*전문가단 담당자 목록 조회*/
	List<UsptExpertClMapng> selectMapngList(String expertClId);
	/*전문가단 담당자 등록*/
	int insertExpertClMapng(UsptExpertClMapng inputParam);
	/*전문가단 담당자  삭제*/
	int deleteExpertClMapng(UsptExpertClMapng inputParam);

	/*전문가단 담당자 총건수*/
	int selectExpertCount(ExpertClChargerParam inputParam);
	/*전문가단 담당자 추가 목록 조회(팝업)*/
	List<UsptExpertClMapng> selectExpertList(ExpertClChargerParam inputParam);
	/*전문가단 담당자 분류조회*/
	List<UsptExpertClMapng> selectMyExpertClList(String memberId);


	/*전문가 신청 상세정보 전문분야*/
	List<UsptExpertClMapng> selectExpertReqsList(String expertId);
	/*수정*/
	int update(UsptExpertClMapng usptExpertClMapng);

	int deleteExpert(String  expertId);

	int selectCheckCnt(UsptExpertClMapng usptExpertClMapng);

	int selectCheckMapngCnt(String expertClId);
}
