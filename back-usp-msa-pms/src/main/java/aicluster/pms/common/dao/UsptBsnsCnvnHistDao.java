package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsCnvnHist;

@Repository
public interface UsptBsnsCnvnHistDao {
	/*
	 * 협약변경관리 사업협약변경이력
	 *
	 */
	int insert (UsptBsnsCnvnHist usptBsnsCnvnHist);
	/*사업협약변경이력  가장 최신 버전 조회*/
	UsptBsnsCnvnHist selectUsptBsnsCnvnHistMax (@Param("cnvnChangeReqId") String  cnvnChangeReqId, @Param("changeIemDivCd") String changeIemDivCd);
	/*사업협약변경이력  조회*/
	List <UsptBsnsCnvnHist> selectBsnsCnvnHist (@Param("cnvnChangeReqId") String  cnvnChangeReqId, @Param("changeIemDivCd") String changeIemDivCd);
	/*사업협약변경이력  가장 최신 버전 조회-사유*/
	UsptBsnsCnvnHist selectUsptBsnsCnvnHistResnCnMax (@Param("cnvnChangeReqId") String  cnvnChangeReqId,
																		@Param("changeIemDivCd") String changeIemDivCd,
																		@Param("cnvnChangeSttusCd") String cnvnChangeSttusCd);


}
