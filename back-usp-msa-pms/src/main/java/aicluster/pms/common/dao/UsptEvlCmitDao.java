package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.evl.dto.EvlCmitExtrcTargetParam;
import aicluster.pms.api.evl.dto.EvlTableParam;
import aicluster.pms.api.evl.dto.EvlTargetListParam;
import aicluster.pms.api.evl.dto.PointListParam;
import aicluster.pms.common.dto.EvlLoginCmitListDto;
import aicluster.pms.common.dto.EvlPointListDto;
import aicluster.pms.common.dto.EvlResultListDto;
import aicluster.pms.common.dto.EvlSttusListDto;
import aicluster.pms.common.dto.EvlSystemDto;
import aicluster.pms.common.dto.EvlSystemLogInDto;
import aicluster.pms.common.dto.EvlSystemTargetDto;
import aicluster.pms.common.dto.EvlTableListDto;
import aicluster.pms.common.dto.EvlTargetListDto;
import aicluster.pms.common.dto.GnrlzEvlIemListDto;
import aicluster.pms.common.dto.GnrlzEvlOpinionListDto;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlCmitDta;
import aicluster.pms.common.entity.UsptEvlCmitExtrcTarget;
import aicluster.pms.common.entity.UsptEvlMfcmmExtrc;

@Repository
public interface UsptEvlCmitDao {
	//등록된 대상 리스트
	List<UsptEvlCmit> selectList(String evlPlanId);

	//위원테이블 기본 추출 리스트
	List<UsptEvlCmit> selectCmitList(UsptEvlCmit usptEvlCmit);

	//자동등록 대상 리스트 조회
	//List<UsptEvlCmit> selectRegInfoList(String evlPlanId);

	int update(UsptEvlCmit usptEvlCmit);

	int insert(UsptEvlCmit usptEvlCmit);

	void delete(String evlPlanId);

	int deleteByCmitId(String evlCmitId);

	//평가자료 조회
	List<UsptEvlCmitDta> selectCmitDtaList(String evlPlanId);

	void updateCmitAttachInfo(UsptEvlCmitDta usptEvlCmitDta);

	//평가상태코드 변경
	int updateEvlSttus(UsptEvlCmit usptEvlCmit);

	//첨부파일그룹ID 조회
	String selectCmitDtaInfo(String evlCmitId);

	//평가위원회 추출 대상 전체 수
	Long selectEvlCmitExtrcTargetListCount(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam);

	//평가위원회 추출 대상 목록조회
	List<UsptEvlCmitExtrcTarget> selectEvlCmitExtrcTargetList(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam);

	//평가위원회 추출 대상 상세조회단건
	UsptEvlCmitExtrcTarget selectEvlCmitExtrcTarget(String evlCmitId);

	//섭외결과 건수 조회
	UsptEvlCmitExtrcTarget selectEvlCmitExtrcTargetCount(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	//위원회 기본정보 조회
	UsptEvlCmit select(String evlCmitId);

	//종합의견 수정
	int updateCharmOpinion(UsptEvlCmit usptEvlCmit);

	//로그인가능한 평가위원회 목록
	List<EvlLoginCmitListDto> selectLoginCmitList();

	List<UsptEvlCmit> selectExistList(UsptEvlCmit usptEvlCmit);


	/**
	 * 평가진행 목록 총 건수
	 * @param param
	 * @return
	 */
	Long selectEvlTargetListCount(EvlTargetListParam param);
	/**
	 * 평가진행 목록 조회
	 * @param param
	 * @return
	 */
	List<EvlTargetListDto> selectEvlTargetList(EvlTargetListParam param);

	/**
	 * 평가현황 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	List<EvlSttusListDto> selectEvlSttusList(String evlCmitId);


	/**
	 * 평가 가감 목록 조회
	 * @param param
	 * @return
	 */
	List<EvlPointListDto> selectEvlPointList(PointListParam param);

	/**
	 * 평가표 목록 조회
	 * @param param
	 * @return
	 */
	List<EvlTableListDto> selectEvlTableList(EvlTableParam param);


	/**
	 * 평가결과 목록 조회
	 * @param evlCmitId
	 * @param selectOnlySlctn : 선정대상만 조회할 경우 ("Y")
	 * @return
	 */
	List<EvlResultListDto> selectEvlResultList(
			@Param(value = "evlCmitId") String evlCmitId
			, @Param(value = "selectOnlySlctn") String selectOnlySlctn);

	/**
	 * 발송 정보 수정
	 * @param info
	 * @return
	 */
	int updateDspth(UsptEvlCmit info);


	/**
	 * 종합평가 목록 조회
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @return
	 */
	List<GnrlzEvlIemListDto> selectGnrlzEvlIemList(
			@Param(value = "evlCmitId") String evlCmitId
			, @Param(value = "evlTrgetId") String evlTrgetId);


	/**
	 * 종합평가 개별의견 목록 조회
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @param evlIemId
	 * @return
	 */
	List<GnrlzEvlOpinionListDto> selectGnrlzEvlOpinionList(
			@Param(value = "evlCmitId") String evlCmitId
			, @Param(value = "evlTrgetId") String evlTrgetId);

	/**************************************************************** 평가 시스템 ************************************************************************************/
	/**
	 * 평가시스템 평가위원 기본정보 조회
	 * @param evlMfcmmId
	 * @return
	 */
	EvlSystemDto selectEvlBaseInfo(String evlMfcmmId);

	/**
	 * 온라인 평가대상 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	 int selectEvlSystemTargetMFEV03(	@Param(value = "evlCmitId") String evlCmitId
											  , @Param(value = "evlMfcmmId") String evlMfcmmId);

	/**
	 * 온라인 평가대상 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	 List<EvlSystemTargetDto> selectEvlSystemTargetList(	@Param(value = "evlCmitId") String evlCmitId
																	  , @Param(value = "evlMfcmmId") String evlMfcmmId);
	 /**
	 * 위원장 평가여부
	 * @param evlCmitId
	 * @return
	 */
	 List<EvlSystemTargetDto> selectEvlMfcmmCompleteYn(	@Param(value = "evlCmitId") String evlCmitId);



	/**
	 * 온라인 평가표 목록 조회
	 * @param param
	 * @return
	 */
	List<EvlTableListDto> selectEvlSystemTableList(EvlTableParam param);

	/**
	 * 위원장 종합의견 등록
	 * @param param
	 * @return
	 */
	 int updateCharmnOpinion(UsptEvlCmit usptEvlCmit);

	 /**
	 * 평가시스템 login 평가위원회ID 조회
	 * @param usptEvlMfcmm
	 * @return
	 */
	List<EvlSystemLogInDto> selectEvlCmitIdByExpertId(String expertId);


	/**
	 * 입력된 평가위원회를 제외한
	 * 평가단계별 평가상태가 평가완료가 아닌 평가위원회의 건수
	 * @param evlCmitId
	 * @return
	 */
	Integer selectEvlCmitCountByNotEvlCompStep(String evlCmitId);

}
