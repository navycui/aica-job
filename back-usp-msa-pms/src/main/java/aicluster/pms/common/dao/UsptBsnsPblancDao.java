package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.pblanc.dto.BsnsPblancListParam;
import aicluster.pms.api.pblanc.dto.FrontBsnsPblancListParam;
import aicluster.pms.api.pblanc.dto.FrontMainRecomendParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancTargetListParam;
import aicluster.pms.common.dto.ApplyMberTypeDto;
import aicluster.pms.common.dto.BsnsPblancListDto;
import aicluster.pms.common.dto.FrontBsnsPblancDto;
import aicluster.pms.common.dto.FrontBsnsPblancListDto;
import aicluster.pms.common.dto.FrontMainListDto;
import aicluster.pms.common.entity.UsptBsnsPblanc;

@Repository
public interface UsptBsnsPblancDao {
	/** 등록 */
	void insert(UsptBsnsPblanc info);
	/** 수정 */
	int update(UsptBsnsPblanc info);
	/** 포털 게시여부 수정 */
	int updateNtce(UsptBsnsPblanc info);
	/** 상태정보 수정 */
	int updatePblancSttus(UsptBsnsPblanc info);
	/** 삭제처리 */
	int updateUnable(UsptBsnsPblanc info);
	/** 상세조회 */
	UsptBsnsPblanc select(@Param("pblancId") String pblancId, @Param("insiderId") String insiderId);
	/** 첨부파일 그룹ID 조회 */
	String selectAttachmentGroupId(String pblancId);
	/** 공고 접수자 건수 조회 */
	Integer selectPblancApplcntCount(String pblancId);
	/** 공고 목록 건수 조회 */
	Long selectListCount(BsnsPblancListParam bsnsPblancListParam);
	/** 공고 목록 조회 */
	List<BsnsPblancListDto> selectList(BsnsPblancListParam bsnsPblancListParam);
	/** 선정공고 - 공고목록 조회 */
	Long selectPblancListCount(SlctnPblancTargetListParam slctnPblancTargetListParam);
	/** 선정공고 - 공고목록 조회 */
	List<BsnsPblancListDto> selectPblancList(SlctnPblancTargetListParam slctnPblancTargetListParam);

	/** 사용자 모집공고 목록 조회 */
	Long selectFrontListCount(FrontBsnsPblancListParam frontBsnsPblancListParam);
	/** 사용자 모집공고 목록 조회 */
	List<FrontBsnsPblancListDto> selectFrontList(FrontBsnsPblancListParam frontBsnsPblancListParam);
	/** 사용자 모집공고 상세조회 */
	FrontBsnsPblancDto selectFront(String pblancId);
	/** 지원가능 회원유형 조회 */
	ApplyMberTypeDto selectApplyMberType(String pblancId);
	/** 조회수 수정 */
	int updateRdcnt(String pblancId);
	/** 메인화면 인기공고 목록 조회 */
	List<FrontMainListDto> selectFrontMainPopularList();
	/** 메인화면 나에게맞는 사업 추천 목록 조회 */
	List<FrontMainListDto> selectFrontMainRecomendList(FrontMainRecomendParam param);
	/** 공고번호 건수 조회 */
	int selectPblancNoCount(String pblancNo);

}
