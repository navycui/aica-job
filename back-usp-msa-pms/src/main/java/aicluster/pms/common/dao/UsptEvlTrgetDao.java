package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.evl.dto.FrontEvlPresnatnParam;
import aicluster.pms.api.evlresult.dto.FrontEvlResultListParam;
import aicluster.pms.common.dto.EvlPresnatnListDto;
import aicluster.pms.common.dto.FrontEvlOpinionListDto;
import aicluster.pms.common.dto.FrontEvlPresnatnListDto;
import aicluster.pms.common.dto.FrontEvlResultListDto;
import aicluster.pms.common.dto.SlctnListDto;
import aicluster.pms.common.entity.UsptEvlTrget;

@Repository
public interface UsptEvlTrgetDao {
	List<UsptEvlTrget> selectList(UsptEvlTrget usptEvlTarget);

	UsptEvlTrget select(String evlTrgetId);

	long selectListCount(UsptEvlTrget usptEvlTarget);

	UsptEvlTrget selectExistChk(UsptEvlTrget usptEvlTarget);

	List<UsptEvlTrget> selectExistChkByCmitId(String evlCmitId);

	List<UsptEvlTrget> selectExistChkByPlanId(UsptEvlTrget usptEvlTarget);

	List<UsptEvlTrget> selectListBySect(UsptEvlTrget usptEvlTarget);

	long selectListBySectCount(UsptEvlTrget usptEvlTarget);

	void insert(UsptEvlTrget usptEvlTrget);

	void delete(UsptEvlTrget usptEvlTrget);

	void deleteBySectId(String sectId);

	/** 사업지원한 대상이 평가대상자인지 확인 */
	Integer selectApplyIdCount(String applyId);


	/**
	 * 선정 정보 수정
	 * @param evlResultByTarget
	 * @return
	 */
	int updateSlctn(UsptEvlTrget info);

	/****************************************** 발표자료 제출 front */
	//발표자료제출 대상카운트
	long selectFrontPresnatnTargetListCount(FrontEvlPresnatnParam frontEvlTargetListParam);
	//발표자료제출 대상목록
	List<FrontEvlPresnatnListDto> selectFrontPresnatnTargetList(FrontEvlPresnatnParam frontEvlTargetListParam);
	//발표자료제출 대상 상세 조회
	FrontEvlPresnatnListDto selectFrontPresnatnTargetDetailList(@Param("evlTrgetId") String evlTrgetId);
	//제출요청시 처리구분코드 수정
	int updatePresnatn(UsptEvlTrget usptEvlTarget);

	/****************************************** 발표자료 제출 admin */
	//발표자료제출 대상카운트
	long selectPresnatnTargetListCount(EvlPresnatnListDto evlPresnatnListDto);

	//발표자료제출 대상목록
	List<EvlPresnatnListDto> selectPresnatnTargetList(EvlPresnatnListDto evlPresnatnListDto);

	//제출요청시 처리구분코드 수정
	int updatePresnatnSttus(UsptEvlTrget usptEvlTarget);

	List<EvlPresnatnListDto> selectPresnatnTargetFileList(EvlPresnatnListDto evlPresnatnListDto);


	/**
	 * 평가결과 목록 조회
	 * @param param
	 * @return
	 */
	List<FrontEvlResultListDto> selectFrontEvlResultList(FrontEvlResultListParam param);
	/**
	 * 평가결과 목록 총건수
	 * @param param
	 * @return
	 */
	Long selectFrontEvlResultListCount(FrontEvlResultListParam param);


	/**
	 * 평가위원 의견 목록 조회
	 * @param evlTrgetId
	 * @param memberId
	 * @return
	 */
	List<FrontEvlOpinionListDto> selectFrontEvlOpinionList(@Param("evlTrgetId") String evlTrgetId, @Param("memberId") String memberId);


	/**
	 * 선정된 대상목록 조회
	 * @param evlStepId
	 * @param sectId
	 * @param slctn
	 * @return
	 */
	List<SlctnListDto> selectSlctnList(@Param("evlStepId") String evlStepId, @Param("sectId") String sectId, @Param("slctn") Boolean slctn);

}
