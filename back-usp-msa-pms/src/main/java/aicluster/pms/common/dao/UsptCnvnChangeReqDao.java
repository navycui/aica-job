package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.cnvnchange.dto.CnvnChangeParam;
import aicluster.pms.api.cnvnchange.dto.FrontCnvnChangeParam;
import aicluster.pms.api.cnvnchangehist.dto.CnvnChangeHistParam;
import aicluster.pms.common.dto.CnvnChangeChangeIemDivDto;
import aicluster.pms.common.dto.CnvnChangeDto;
import aicluster.pms.common.entity.UsptCnvnChangeReq;

@Repository
public interface UsptCnvnChangeReqDao {
	/*
	 * 협약변경관리
	 *
	 */

	/********************************** 협약변경관리 FRONT*/
	//신청내역 등록
	int insert (UsptCnvnChangeReq usptCnvnChangeReq);
	//신청내역 변경
	int update (UsptCnvnChangeReq usptCnvnChangeReq);
	//협약변경상태코드 변경
	int updateCnvnChangeSttus (@Param("cnvnChangeReqId") String cnvnChangeReqId, @Param("cnvnChangeSttusCd") String cnvnChangeSttusCd);

	//신청내역 목록 총건수조회
	long selectCnvnChangeReqListCnt(FrontCnvnChangeParam frontCnvnChangeParam);
	//신청내역 목록조회
	List<UsptCnvnChangeReq> selectCnvnChangeReqList(FrontCnvnChangeParam frontCnvnChangeParam);
	//협약변경관리 변경신청  기존정보 조회
	UsptCnvnChangeReq selectCnvnChangeBaseInfoFront(@Param("changeIemDivCd") String changeIemDivCd,
			 														   @Param("bsnsCnvnId") String bsnsCnvnId,
			 														   @Param("cnvnChangeReqId") String cnvnChangeReqId);
	//등록상태 확인
	long selectCnvnChangeRegCnt(@Param("cnvnChangeReqId") String cnvnChangeReqId,
										   @Param("changeIemDivCd") String changeIemDivCd,
										   @Param("cnvnChangeSttusCd") String cnvnChangeSttusCd);
	//협약변경요청 신청건조회
	String selectCnvnChangeReqCCHS01(@Param("memberId") String memberId,
												  @Param("bsnsCnvnId") String bsnsCnvnId,
												  @Param("changeIemDivCd") String changeIemDivCd);

	/********************************** 협약변경관리 ADMIN*/
	//신청내역 목록 총건수조회
	long selectCnvnChangeReqListAdminCnt(CnvnChangeParam cnvnChangeParam);
	//신청내역 목록조회
	List<CnvnChangeDto> selectCnvnChangeReqListAdmin(CnvnChangeParam cnvnChangeParam);
	//협약변경관리 변경신청  기존정보 조회
	UsptCnvnChangeReq selectCnvnChangeBaseInfo(@Param("cnvnChangeReqId") String cnvnChangeReqId,
																@Param("changeIemDivCd") String changeIemDivCd);

	/****************************************협약변경내역 ****************************************************/
	//신청내역 목록 총건수조회
	long selectProcessHistCnt(CnvnChangeHistParam cnvnChangeHistParam);
	//신청내역 목록조회
	List<CnvnChangeDto> selectProcessHistList(CnvnChangeHistParam cnvnChangeHistParam);
	//신청상세 기본정보 조회
	CnvnChangeDto selectProcessHistBaseInfo(String bsnsCnvnId);
	//신청상세 변경항목 조회
	List<CnvnChangeChangeIemDivDto> selectChangeChangeIemDiv(String bsnsCnvnId);
}
