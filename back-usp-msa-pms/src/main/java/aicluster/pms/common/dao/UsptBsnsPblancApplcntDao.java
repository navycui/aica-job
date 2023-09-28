package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsns.dto.EntrpsSttusListParam;
import aicluster.pms.api.bsns.dto.EntrpsSttusSendParam;
import aicluster.pms.api.bsnsapply.dto.BsnsApplyListParam;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyListParam;
import aicluster.pms.api.pblanc.dto.PblancEvlTargetParam;
import aicluster.pms.api.stdnt.dto.StdntCareerParam;
import aicluster.pms.api.stdnt.dto.StdntMtchListParam;
import aicluster.pms.common.dto.ApplcntBsnsBasicDto;
import aicluster.pms.common.dto.BsnsApplyListDto;
import aicluster.pms.common.dto.EntrpsSttusListDto;
import aicluster.pms.common.dto.EvlApplcntListDto;
import aicluster.pms.common.dto.FrontBsnsApplyListDto;
import aicluster.pms.common.dto.MyBsnsNtcnListDto;
import aicluster.pms.common.dto.StdntCareerListDto;
import aicluster.pms.common.dto.StdntMtchDto;
import aicluster.pms.common.dto.StdntMtchListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;

@Repository
public interface UsptBsnsPblancApplcntDao {

	void insert(UsptBsnsPblancApplcnt info);
	int update(UsptBsnsPblancApplcnt info);
	UsptBsnsPblancApplcnt select(String applyId);

	/**
	 * 담당자권한조회
	 * @param applyId
	 * @param insiderId
	 * @return
	 */
	String selectChargerAuth(@Param("applyId") String applyId, @Param("insiderId") String insiderId);

	/**
	 * 접수완료 처리
	 * @param info
	 * @return
	 */
	int updateRceptSttusCompt(UsptBsnsPblancApplcnt info);

	/**
	 * 신청접수 목록 총건수 조회
	 * @param bsnsApplyListParam
	 * @return
	 */
	Long selectListCount(BsnsApplyListParam bsnsApplyListParam);
	/**
	 * 신청접수 목록 조회
	 * @param bsnsApplyListParam
	 * @return
	 */
	List<BsnsApplyListDto> selectList(BsnsApplyListParam bsnsApplyListParam);

	/**
	 * 지원공고 신청건수
	 * @param pblancId
	 * @param memberId
	 * @return
	 */
	Integer selectApplcntCount(@Param("pblancId") String pblancId, @Param("memberId") String memberId);

	/**
	 * 선정공고 대상자 건수 조회
	 * @param pblancEvlTargetParam
	 * @return
	 */
	Long selectEvlApplcntListCount(PblancEvlTargetParam pblancEvlTargetParam);

	/**
	 * 선정공고 대상자 목록 조회
	 * @param pblancEvlTargetParam
	 * @return
	 */
	List<EvlApplcntListDto> selectEvlApplcntList(PblancEvlTargetParam pblancEvlTargetParam);


	/**
	 * 업체현황 목록 총 건수 조회
	 * @param param
	 * @return
	 */
	Long selectEntrpsSttusListCount(EntrpsSttusListParam param);
	/**
	 * 업체현황 목록 조회
	 * @param param
	 * @return
	 */
	List<EntrpsSttusListDto> selectEntrpsSttusList(EntrpsSttusListParam param);

	/**
	 * 사업공고신청자 연락처 목록 조회
	 * @param applyIdList
	 * @return
	 */
	List<EntrpsSttusListDto> selectContactList(EntrpsSttusSendParam param);

	/**
	 * 사업신청 건수 조회
	 * @param frontBsnsapplyListParam
	 * @return
	 */
	Long selectFrontListCount(FrontBsnsApplyListParam frontBsnsapplyListParam);

	/**
	 * 사업신청 목록 조회
	 * @param frontBsnsapplyListParam
	 * @return
	 */
	List<FrontBsnsApplyListDto> selectFrontList(FrontBsnsApplyListParam frontBsnsapplyListParam);


	/**
	 * 사업신청 기본정보 조회
	 * @param applyId
	 * @param insiderId
	 * @return
	 */
	ApplcntBsnsBasicDto selectBsnsBasic(@Param("applyId") String applyId, @Param("insiderId") String insiderId);

	/**
	 * 교육생매칭 관리 목록 건수 조회
	 * @param param
	 * @return
	 */
	Long selectStdntMtchListCount(StdntMtchListParam param);

	/**
	 * 교육생매칭 관리 목록 조회
	 * @param param
	 * @return
	 */
	List<StdntMtchListDto> selectStdntMtchList(StdntMtchListParam param);

	/**
	 * 교육생매칭 상세조회
	 * @param lastSlctnTrgetId
	 * @param insiderId
	 * @return
	 */
	StdntMtchDto selectStdntMtch(@Param("lastSlctnTrgetId") String lastSlctnTrgetId, @Param("insiderId") String insiderId);


	/**
	 * 교육생경력 관리 목록 총건수 조회
	 * @param param
	 * @return
	 */
	Long selectStdntCareerListCount(StdntCareerParam param);
	/**
	 * 교육생경력 관리 목록 조회
	 * @param param
	 * @return
	 */
	List<StdntCareerListDto> selectStdntCareerList(StdntCareerParam param);

	/**
	 * 나의 사업 알림 목록 조회
	 * @param memberId
	 * @return
	 */
	List<MyBsnsNtcnListDto> seletMyBsnsNtcnList(String memberId);

}
