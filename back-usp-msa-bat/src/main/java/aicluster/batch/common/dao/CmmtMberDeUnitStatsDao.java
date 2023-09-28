package aicluster.batch.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.batch.common.dto.MemberStatsItem;
import aicluster.batch.common.entity.CmmtMberDeUnitStats;

@Repository
public interface CmmtMberDeUnitStatsDao {
	List<MemberStatsItem> selectTotalMbrCnt(String ymd);
	List<MemberStatsItem> selectJoinMbrCnt(String ymd);
	List<MemberStatsItem> selectSecessionMbrCnt(String ymd);
	List<MemberStatsItem> selectDormantMbrCnt(String ymd);
	List<MemberStatsItem> selectBlackMbrCnt(String ymd);
	int upsert(@Param("list") List<CmmtMberDeUnitStats> list);
}
