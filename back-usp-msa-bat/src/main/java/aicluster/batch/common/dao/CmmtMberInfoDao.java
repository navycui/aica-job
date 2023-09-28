package aicluster.batch.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.CmmtMberInfo;

@Repository
public interface CmmtMberInfoDao {

	CmmtMberInfo select(String memberId);

	List<CmmtMberInfo> selectList_blackMemberToNormal();

	void update(CmmtMberInfo cmmtMember);

	List<CmmtMberInfo> selectList_lastLoginDt(String day);

	int updateToDormant(String day);

	List<CmmtMberInfo> selectList_memberSt(
			@Param("memberSt") String memberSt,
			@Param("day") int day);

	void delete(String memberId);
}
