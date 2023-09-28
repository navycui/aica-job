package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsnsplan.dto.BsnsPlanParam;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanResnParam;
import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanParam;
import aicluster.pms.common.entity.UsptBsnsPlanDoc;

@Repository
public interface UsptBsnsPlanDocDao {

	int delete(UsptBsnsPlanDoc upstBsnsPlanDoc);

	int insert(UsptBsnsPlanDoc upstBsnsPlanDoc);

	int update(UsptBsnsPlanDoc upstBsnsPlanDoc);

	List<UsptBsnsPlanDoc> selectList(UsptBsnsPlanDoc upstBsnsPlanDoc);

	/**사업계획서 접수 front*/
	/*사업계획서 관리 목록 총건수 조회*/
	long selectFrontBsnsPlanDocCnt(FrontBsnsPlanParam frontBsnsPlanParam);
	/*사업계획서 관리 목록 조회*/
	List<UsptBsnsPlanDoc> selectFrontBsnsPlanDocList(FrontBsnsPlanParam frontBsnsPlanParam);
	/*사업계획서 단건 조회*/
	UsptBsnsPlanDoc selectOne(FrontBsnsPlanParam frontBsnsPlanParam);
	/*사업계획서 기본정보 조회*/
	UsptBsnsPlanDoc selectBsnsPlanBaseDocInfo(FrontBsnsPlanParam frontBsnsPlanParam);

	/**사업계획서 접수 admin*/
	/*사업계획서 관리 목록 총건수 조회*/
	long selectBsnsPlanDocCnt(BsnsPlanParam bsnsPlanParam);
	/*사업계획서 관리 목록 조회*/
	List<UsptBsnsPlanDoc> selectBsnsPlanDocList(BsnsPlanParam bsnsPlanParam);
	/*사업계획서 관리 목록 전체 첨부파일 조회*/
	List<UsptBsnsPlanDoc> selectBsnsPlanDocFileList(BsnsPlanParam bsnsPlanParam);
	/*사업계획서 단건 조회*/
	UsptBsnsPlanDoc selectOne(BsnsPlanParam bsnsPlanParam);
	/*사업계획서 기본정보 조회*/
	UsptBsnsPlanDoc selectBsnsPlanBaseDocInfo(BsnsPlanParam bsnsPlanParam);
	/*보안요청 사유 변경*/
	int updateResnInfo(UsptBsnsPlanDoc upstBsnsPlanDoc);
	/*사업계획제출상태코드 변경*/
	int updatePlanSttus(BsnsPlanParam bsnsPlanParam);

	/*사업계획서 사유팝업 내용 조회*/
	UsptBsnsPlanDoc selectOne(BsnsPlanResnParam bsnsPlanResnParam);

	/*사업계획서 과제정보 변경*/
	int updateTaskInfo(UsptBsnsPlanDoc upstBsnsPlanDoc);
	/*사업계획서 참여기업수 변경*/
	int updatePrtcmpnyCnt(UsptBsnsPlanDoc upstBsnsPlanDoc);
}
