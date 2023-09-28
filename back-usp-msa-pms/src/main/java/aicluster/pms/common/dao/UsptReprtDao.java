package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.reprt.dto.FrontReprtListParam;
import aicluster.pms.api.reprt.dto.ReprtListParam;
import aicluster.pms.common.dto.FrontReprtDto;
import aicluster.pms.common.dto.FrontReprtListDto;
import aicluster.pms.common.dto.ReprtDto;
import aicluster.pms.common.dto.ReprtListDto;
import aicluster.pms.common.entity.UsptReprt;

@Repository
public interface UsptReprtDao {
	void insert(UsptReprt info);
	int update(UsptReprt info);
	UsptReprt select(String reprtId);
	ReprtDto selectBasic(@Param("reprtId") String reprtId, @Param("insiderId") String insiderId);
	Integer selectCount(@Param("lastSlctnTrgetId") String lastSlctnTrgetId, @Param("reprtTypeCd") String reprtTypeCd);
	Long selectListCount(ReprtListParam param);
	List<ReprtListDto> selectList(ReprtListParam param);

	FrontReprtDto selectFront(String reprtId);
	Long selectFrontListCount(FrontReprtListParam param);
	List<FrontReprtListDto> selectFrontList(FrontReprtListParam param);
}
