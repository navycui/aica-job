package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.cnvnchange.dto.FrontCnvnChangeParam;
import aicluster.pms.api.cnvncncls.dto.CnvnCnclsParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsInputParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsParam;
import aicluster.pms.api.cnvntrmnat.dto.CnvnTrmnatInputParam;
import aicluster.pms.api.cnvntrmnat.dto.CnvnTrmnatParam;
import aicluster.pms.api.excclc.dto.ExcclcListParam;
import aicluster.pms.common.dto.ExcclcDto;
import aicluster.pms.common.dto.ExcclcListDto;
import aicluster.pms.common.entity.UsptBsnsCnvn;

@Repository
public interface UsptBsnsCnvnDao {
	/*
	 * 사업협약
	 *
	 */
	/** 정산관리 목록 총건수 조회 */
	Long selectListCount(ExcclcListParam param);
	/** 정산관리 목록 조회 */
	List<ExcclcListDto> selectList(ExcclcListParam param);
	/** 정산내역 상세조회 */
	ExcclcDto selectDetail(@Param("bsnsCnvnId") String bsnsCnvnId
						, @Param("taskReqstWctId") String taskReqstWctId
						, @Param("insiderId") String insiderId);

	int insert(UsptBsnsCnvn usptBsnsCnvn);

	UsptBsnsCnvn selectCnvnSttusByBsnsPlanDocId(String  bsnsPlanDocId);
	int deleteByBsnsPlanDocId(String  bsnsPlanDocId);
	/********************************** 전자협약 관리 FRONT*/
	//목록 총건수조회
	long selectFrontBsnsCnvnListCnt(FrontCnvnCnclsParam frontCnvnCnclsParam);
	//목록조회
	List<UsptBsnsCnvn> selectFrontBsnsCnvnList(FrontCnvnCnclsParam frontCnvnCnclsParam);
	//상세조회
	UsptBsnsCnvn selectBsnsCnvnDetail(FrontCnvnCnclsParam frontCnvnCnclsParam);
	UsptBsnsCnvn selectBsnsCnvnDetail(FrontCnvnCnclsInputParam frontCnvnCnclsInputParam);
	//협약파일그룹ID 수정
	int updateCnvnFileGroupId(UsptBsnsCnvn usptBsnsCnvn);
	//주관업체 서명일자 수정
	int updateBsnmSignDt(UsptBsnsCnvn usptBsnsCnvn);
	// 협약 상태 변경
	int updateCnvnSttus(UsptBsnsCnvn usptBsnsCnvn);
	//주관업체 서명여부
	int selectBsnsCnvnSignCnt(@Param("bsnsCnvnId") String bsnsCnvnId, @Param("applyId") String applyId);
	//협약서 report 조회
	UsptBsnsCnvn selectBsnsCnvnDocInfo(FrontCnvnCnclsParam frontCnvnCnclsParam);

	/** ******************************** 전자협약 관리ADMIN*/
	//목록 총건수조회
	long selectBsnsCnvnListCnt(CnvnCnclsParam cnvnCnclsParam);
	//목록조회
	List<UsptBsnsCnvn> selectBsnsCnvnList(CnvnCnclsParam cnvnCnclsParam);
	//상세조회
	UsptBsnsCnvn selectBsnsCnvnDetail(CnvnCnclsParam cnvnCnclsParam);
	//협약상태코드 조회
	String selectCnvnSttusCd(@Param("bsnsCnvnId") String bsnsCnvnId, @Param("applyId") String applyId);
	//협약서생성
	int updateCnvnCnclsCreat(UsptBsnsCnvn usptBsnsCnvn);
	//협약완료
	int updateCnvnCnclsComplete(UsptBsnsCnvn usptBsnsCnvn);
	//협약서명초기화
	int updateCnvnCnclsInit(UsptBsnsCnvn usptBsnsCnvn);
	//협약서 report 조회
	UsptBsnsCnvn selectBsnsCnvnDocInfo(CnvnCnclsParam cnvnCnclsParam);

	/** ******************************** 전자협약 해지 관리 ADMIN*/
	//목록 총건수조회
	long selectCnvnTrmnatListCnt(CnvnTrmnatParam cnvnTrmnatParam);
	//목록조회
	List<UsptBsnsCnvn> selectCnvnTrmnatList(CnvnTrmnatParam cnvnTrmnatParam);
	//협약해지등록
	int updateCnvnTrmnat(UsptBsnsCnvn usptBsnsCnvn);
	//협약해지취소
	int updateCnvnSttusCancel(UsptBsnsCnvn usptBsnsCnvn);
	//상세조회
	UsptBsnsCnvn selectCnvnTrmnatDetail(CnvnTrmnatParam cnvnTrmnatParam);
	UsptBsnsCnvn selectCnvnTrmnatDetail(CnvnTrmnatInputParam cnvnTrmnatInputParam);

	/** ******************************** 협약변경관리   FRONT*/
	//협약변경관리 변경요청 조회(최신 협약일 기준)
	UsptBsnsCnvn selectCnvnChangeReqMaxOne(FrontCnvnChangeParam frontCnvnChangeParam);
	//사업년도 list
	List<String> selectCnvnChangeReqBsnsYear(String memberId);
	//과제명 list
	List<String> selectCnvnChangeReqTaskNm(@Param("bsnsYear") String bsnsYear,@Param("memberId") String memberId);

}
