package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtRole;

@Repository
public interface CmmtRoleDao {
	CmmtRole select(String roleId);
	List<CmmtRole> selectList();
	CmmtRole selectByName(String roleNm);
	CmmtRole selectByName(
			@Param("roleNm") String roleNm
			, @Param("roleId") String roleId);
	void insert(CmmtRole role);
	void update(CmmtRole role);
	void delete(String roleId);
}
