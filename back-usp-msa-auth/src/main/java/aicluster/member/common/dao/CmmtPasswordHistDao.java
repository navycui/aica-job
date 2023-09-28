package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtPasswordHist;

@Repository
public interface CmmtPasswordHistDao {
	int insert(CmmtPasswordHist hist);

	List<CmmtPasswordHist> selectList_recent(
			@Param("memberId") String memberId,
			@Param("cnt") int cnt);
}
