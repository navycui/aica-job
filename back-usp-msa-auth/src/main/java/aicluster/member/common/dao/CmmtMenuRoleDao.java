package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtMenuRole;

@Repository
public interface CmmtMenuRoleDao {
	List<CmmtMenuRole> selectByMenuId(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	void deleteByRoleId(
			@Param("systemId") String systemId,
			@Param("roleId") String roleId);
	void insert(CmmtMenuRole cmmtMenuRole);
	void insertList(@Param("list") List<CmmtMenuRole> list );
}
