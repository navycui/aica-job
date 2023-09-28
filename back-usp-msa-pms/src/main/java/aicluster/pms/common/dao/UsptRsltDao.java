package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.rslt.dto.BsnsMvnRsltParam;
import aicluster.pms.api.rslt.dto.FrontRsltListParam;
import aicluster.pms.api.rslt.dto.RsltListParam;
import aicluster.pms.api.rslt.dto.RsltStatsParam;
import aicluster.pms.common.dto.FrontRsltListDto;
import aicluster.pms.common.dto.RsltListDto;
import aicluster.pms.common.dto.RsltStatsListDto;
import aicluster.pms.common.entity.UsptRslt;

@Repository
public interface UsptRsltDao {
	void insert(UsptRslt info);
	int update(UsptRslt info);
	UsptRslt select(String rsltId);
	UsptRslt selectByApplyId(String applyId);
	/**
	 * 입주사업 관련 정보 조회
	 * @param param
	 * @return
	 */
	UsptRslt selectByMvnApplyId(BsnsMvnRsltParam param);
	Long selectListCount(RsltListParam param);
	List<RsltListDto> selectList(RsltListParam param);

	Long selectFrontListCount(FrontRsltListParam param);
	List<FrontRsltListDto> selectFrontList(FrontRsltListParam param);

	/**
	 * 성과 현황 목록 조회
	 * @param param
	 * @return
	 */
	List<RsltStatsListDto> selectStatsList(RsltStatsParam param);
}
