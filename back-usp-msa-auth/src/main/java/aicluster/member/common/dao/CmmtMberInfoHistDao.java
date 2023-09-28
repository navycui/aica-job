package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtMberInfoHist;

@Repository
public interface CmmtMberInfoHistDao {
	void insert(CmmtMberInfoHist cmmtMemberHist);
	List<CmmtMberInfoHist> selectListByMemberId(String memberId);
	void insertList(@Param("list") List<CmmtMberInfoHist> uplist);
	long selectCount(String memberId);
	List<CmmtMberInfoHist> selectList(
			@Param("memberId") String memberId,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage);

}

