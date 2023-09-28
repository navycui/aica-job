package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.company.dto.MvnCmpnyPrfrmListParam;
import aicluster.mvn.common.dto.CmpnyPrfrmListItemDto;
import aicluster.mvn.common.dto.MvnCmpnyRsltSttusCdCountDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsPfmc;
import aicluster.mvn.common.entity.UsptRslt;

@Repository
public interface UsptMvnEntrpsPfmcDao {
	void insert(UsptMvnEntrpsPfmc cmpnyPrfrm);
	UsptMvnEntrpsPfmc select(
			@Param("mvnId") String mvnId,
			@Param("sbmsnYm") String sbmsnYm);
	void delete(
			@Param("mvnId") String mvnId,
			@Param("sbmsnYm") String sbmsnYm);
	long selectList_count(
			@Param("param") MvnCmpnyPrfrmListParam param);
	List<CmpnyPrfrmListItemDto> selectList(
			@Param("param") MvnCmpnyPrfrmListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);
	UsptRslt selectRslt(String rsltId);
	String selectRecentPresentnYm(String mvnId);
	MvnCmpnyRsltSttusCdCountDto selectRsltSttusCd_count();
}
