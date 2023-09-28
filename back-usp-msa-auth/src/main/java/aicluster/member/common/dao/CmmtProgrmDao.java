package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtProgrm;
import aicluster.member.common.entity.CmmtProgrmRole;

@Repository
public interface CmmtProgrmDao {
	CmmtProgrm select(String programId);

	List<CmmtProgrm> selectList(
			@Param("systemId") String systemId,
			@Param("programNm") String programNm,
			@Param("urlPattern") String urlPattern);

	List<CmmtProgrmRole> selectByRoleId(String roleId);

	CmmtProgrm selectByName(
			@Param("systemId") String systemId,
			@Param("programNm") String programNm);

	CmmtProgrm selectByUrlPattern(
			@Param("systemId") String systemId,
			@Param("httpMethod") String httpMethod,
			@Param("urlPattern") String urlPattern);

	void incCheckOrder(long checkOrder);

	void insert(CmmtProgrm program);

	void update(CmmtProgrm program);

	// delete 기능 추가
	void deleteProgramId(String programId);

	long selectTotalCount();
}
