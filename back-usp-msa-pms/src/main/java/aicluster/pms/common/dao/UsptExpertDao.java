package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.expert.dto.ExpertListParam;
import aicluster.pms.api.expertReqst.dto.ExpertReqstListParam;
import aicluster.pms.common.entity.UsptEvlMfcmmExtrc;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertExcel;
import aicluster.pms.common.entity.UsptExpertParam;
import aicluster.pms.common.entity.UsptExpertResult;

@Repository
public interface UsptExpertDao {
	List<UsptExpert> selectList(UsptExpertParam usptExpert);

	//사전 추출대상 리스트
	List<UsptExpert> selectExpertTargetList(UsptEvlMfcmmExtrc usptEvlMfcmmExtrcParam);

	//제외 대상 리스트
	List<UsptExpert> selectExpertTargetLmttList(UsptEvlMfcmmExtrc usptEvlMfcmmExtrcParam);

	long selectListCount(UsptExpertParam usptExpert);

	List<UsptExpert> selectListByExtrc(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	List<UsptExpertExcel> selectListByExtrcExcel(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	List<UsptExpert> selectListByExcl(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	List<UsptExpertExcel> selectListByExclExcel(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	UsptExpert selectMfcmmDetail(String extrcMfcmmId);

	UsptExpertResult selectMfcmmDetailEnc(String extrcMfcmmId);

	int insertList(@Param("list") List<UsptExpert> usptExpertList);

	int insert(UsptExpert usptExpert);

	//********************************************전문가 신청 front**//
	String selectExpertSttusCd(String memberId);
	//사용자의 전문가분류id List (memberId or expertId)
	List<String> selectExpertClId(UsptExpert usptExpert);

	//사용자의 전문가id
	String selectExpertId(String  memberId);

	//********************************************전문가 신청 admin**//
	long selectExpertListCnt(ExpertReqstListParam expertReqstListParam);
	/*목록*/
	List<UsptExpert> selectExpertList(ExpertReqstListParam expertReqstListParam);
	/*excel*/
	List<UsptExpert> selectList(ExpertReqstListParam expertReqstListParam);
	/*단건조회*/
	UsptExpert selectOneExpert(String expertId);
	/*상태값 update*/
	int updateExpertReturn(ExpertReqstListParam expertReqstListParam);
	/*전문가 정보 업데이트*/
	int update(UsptExpert usptExpert);

	//********************************************전문가 관리 admin**//
	long selectExpertListCnt(ExpertListParam expertListParam);
	/*목록*/
	List<UsptExpert> selectExpertList(ExpertListParam expertListParam);

	List<UsptExpert> selectExpertList2(ExpertListParam expertListParam);

	//주관기관 참여기관명 리스트
	List<UsptExpert> selectInsttNmList(String cmitId);

	//주관기관 및 참여기관에 해당하는 전문가 리스트
	List<UsptExpert> selectExpertListByInsttNm(String wrcNm);

	/*excel*/
	List<UsptExpert> selectList(ExpertListParam expertListParam);
	/*삭제*/
	int delete(String expertId);

	/*매칭이력*/
	long selectExpertMatchHistListCnt(ExpertListParam expertListParam);
	List<UsptExpert> selectExpertMatchHistList(ExpertListParam expertListParam);

	/*평가계획ID 구하기*/
	String selectEvlPlanIdByCmitId(String cmitId);

}
