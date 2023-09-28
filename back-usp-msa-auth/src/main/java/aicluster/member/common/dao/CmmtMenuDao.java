package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.api.auth.dto.MyMenuDto;
import aicluster.member.common.dto.MenuEnabledDto;
import aicluster.member.common.entity.CmmtMenu;

@Repository
public interface CmmtMenuDao {
	void insert(CmmtMenu menu);
	void update(CmmtMenu menu);
	void delete(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	List<CmmtMenu> selectAll(String systemId);
	CmmtMenu select(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	CmmtMenu selectLastChild(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	CmmtMenu selectAboveMenu(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	CmmtMenu selectBelowMenu(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	List<MyMenuDto> selectList_authorityId(
			@Param("systemId") String systemId,
			@Param("authorityId") String authorityId);
	List<MyMenuDto> selectList_roleId(
			@Param("systemId") String systemId,
			@Param("roleId") String roleId);
	List<CmmtMenu> selectList_parentMenuId(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	List<MenuEnabledDto> selectAll_roleId(
			@Param("systemId") String systemId,
			@Param("roleId") String roleId);
	Integer selectCount_menuIds(
			@Param("systemId") String systemId,
			@Param("menuIdList") List<String> menuIdList);
	void incSortOrder(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId,
			@Param("sortOrder") Long sortOrder);
	void decSortOrder(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId,
			@Param("sortOrder") Long sortOrder);
	List<CmmtMenu> selectChildAll(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	List<CmmtMenu> selectParentByChildId(
			@Param("systemId") String systemId,
			@Param("menuId") String menuId);
	void updateSortOrder(CmmtMenu menu);
}
