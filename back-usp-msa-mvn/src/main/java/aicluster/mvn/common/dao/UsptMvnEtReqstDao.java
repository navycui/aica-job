package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.request.dto.MvnExtListParam;
import aicluster.mvn.common.dto.MvnEtReqListItemDto;
import aicluster.mvn.common.dto.MvnEtReqStCountDto;
import aicluster.mvn.common.entity.UsptMvnEtReqst;

@Repository
public interface UsptMvnEtReqstDao {
	void insert(UsptMvnEtReqst usptMvnEtReq);
	UsptMvnEtReqst select(String mvnEtReqId);
	void update(UsptMvnEtReqst usptMvnEtReq);
	Long selectList_count( @Param("param") MvnExtListParam param );
	List<MvnEtReqListItemDto> selectList(
			@Param("param") MvnExtListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems
			);
	UsptMvnEtReqst select_lastMvnEtReq(
			@Param("mvnId") String mvnId,
			@Param("cmpnyId") String cmpnyId );
	UsptMvnEtReqst selectGoing_MvnId(String mvnId);
	MvnEtReqStCountDto selectMvnEtReqSt_count();
}
