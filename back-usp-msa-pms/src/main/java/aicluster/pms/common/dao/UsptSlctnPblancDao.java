package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.pblanc.dto.SlctnPblancListParam;
import aicluster.pms.common.dto.FrontSlctnPblancDto;
import aicluster.pms.common.dto.FrontSlctnPblancListDto;
import aicluster.pms.common.dto.FrontSlctnPblancSlctnDto;
import aicluster.pms.common.dto.SlctnPblancDto;
import aicluster.pms.common.dto.SlctnPblancListDto;
import aicluster.pms.common.entity.UsptSlctnPblanc;

@Repository
public interface UsptSlctnPblancDao {
	void insert(UsptSlctnPblanc info);
	int update(UsptSlctnPblanc info);
	SlctnPblancDto select(@Param("slctnPblancId") String slctnPblancId, @Param("insiderId") String insiderId);
	Long selectListCount(SlctnPblancListParam bsnsPblancListParam);
	List<SlctnPblancListDto> selectList(SlctnPblancListParam param);
	/** 포털 게시여부 수정 */
	int updateNtce(UsptSlctnPblanc info);
	/** 삭제처리 */
	int updateUnable(UsptSlctnPblanc info);

	/** 선정결과공고 목록 조회 */
	Long selectFrontListCount(
			@Param("slctnPblancNm") String slctnPblancNm
			, @Param("memberId") String memberId);
	/** 선정결과공고 목록 조회 */
	List<FrontSlctnPblancListDto> selectFrontList(
			@Param("slctnPblancNm") String slctnPblancNm
			, @Param("memberId") String memberId
			, @Param("beginRowNum") Long beginRowNum
			, @Param("itemsPerPage") Long itemsPerPage
			, @Param("slctnPblancId") String slctnPblancId);
	/** 선정결과공고 상세조회 */
	FrontSlctnPblancDto selectFront(String slctnPblancId);
	/** 선정결과공고 선정업체 목록 */
	List<FrontSlctnPblancSlctnDto> selectFrontSlctnList(String slctnPblancId);

	/** 선정결과공고 미리보기 */
	FrontSlctnPblancDto selectPreview(String slctnPblancId);
}
