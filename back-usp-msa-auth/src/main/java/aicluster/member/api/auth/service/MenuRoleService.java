package aicluster.member.api.auth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtMenuDao;
import aicluster.member.common.dao.CmmtMenuRoleDao;
import aicluster.member.common.dao.CmmtRoleDao;
import aicluster.member.common.dto.MenuEnabledDto;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtMenu;
import aicluster.member.common.entity.CmmtMenuRole;
import aicluster.member.common.entity.CmmtRole;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class MenuRoleService {

	@Autowired
	private CmmtMenuDao cmmtMenuDao;

	@Autowired
	private CmmtMenuRoleDao cmmtMenuRoleDao;

	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	@Autowired
	private CmmtRoleDao cmmtRoleDao;

	/**
	 * 사용자 로그인 여부 검증
	 *
	 * @param user	사용자정보
	 */
	private void validateLogin (BnMember user)
	{
		if (user == null) {
			throw new InvalidationException(validateMessageExt.미로그인);  // 로그인을 수행하세요.
		}
	}

	/**
	 * Parameter 검증
	 *
	 * @param systemId	시스템ID
	 * @param roleId	역할ID
	 */
	private void validateMenuRole (String systemId, String roleId)
	{
		// 시스템ID 유무 검증
		CmmtCode cmmtSystemCode = cmmtCodeDao.select("SYSTEM_ID", systemId);
		if (cmmtSystemCode == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력오류, "시스템ID"));  // 잘못된 %s 입니다. %s를 다시 확인하세요.
		}

		// 역할 정보 존재 유무 검증
		CmmtRole cmmtRole = cmmtRoleDao.select(roleId);
		if (cmmtRole == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력오류, "역할ID"));  // 잘못된 %s 입니다. %s를 다시 확인하세요.
		}
	}

	/**
	 * allMenuList에서 childMenuId의 모든 조상메뉴들의 ID를 menuIdList에 추가한다.
	 *
	 * @param menuIdList	선택된 메뉴ID 목록
	 * @param allMenuList	모든 메뉴 목록
	 * @param childMenuId	자식메뉴ID
	 */
	private void addParentMenuIds(List<String> menuIdList, List<CmmtMenu> allMenuList, String childMenuId)
	{
		for (CmmtMenu cmmtMenu : allMenuList) {
			if (string.equals(cmmtMenu.getMenuId(), childMenuId)) {
				if (string.isNotBlank(cmmtMenu.getParentMenuId())) {
					if (!menuIdList.contains(cmmtMenu.getParentMenuId())) {
						menuIdList.add(cmmtMenu.getParentMenuId());
					}
					addParentMenuIds(menuIdList, allMenuList, cmmtMenu.getParentMenuId());
				}
			}
		}
	}

	/**
	 * 역할별 메뉴 목록 조회
	 *  - 메뉴 목록에 '역할 매핑 여부(enabled)'를 포함하여 조회한다.
	 *
	 * @param systemId	시스템ID
	 * @param roleId	역할ID
	 */
	public JsonList<MenuEnabledDto> getMenuEnabledList(String systemId, String roleId)
	{
		// 메뉴 사용여부 Parameter 검증
		validateMenuRole(systemId, roleId);

		// 메뉴-역할 매핑 목록 조회
		List<MenuEnabledDto> list = cmmtMenuDao.selectAll_roleId(systemId, roleId);

		return new JsonList<>(list);
	}

	/**
	 * 메뉴-역할 매핑 수정
	 *  - 메뉴ID에 대해서 역할ID를 매핑한다.(메뉴ID의 부모 메뉴까지 모두 포함하여 메뉴-역할을 매핑한다.)
	 *
	 * @param systemId	시스템ID
	 * @param roleId	역할ID
	 * @param menuIds	메뉴ID 목록
	 */
	public JsonList<MenuEnabledDto> updateMenuEnabled(String systemId, String roleId, List<String> menuIds)
	{
		Date now = new Date();
		BnMember user = SecurityUtils.getCurrentMember();

		// 로그인 여부 검증
		validateLogin(user);

		// 메뉴 사용여부 Parameter 검증
		validateMenuRole(systemId, roleId);

		// 매핑 대상 메뉴ID 목록 생성
		List<String> newMenuIdList = new ArrayList<>();
		if (!menuIds.isEmpty()) {
			// menuIdList가 cmmt_menu에 모두 존재하는 지 검사
			int menuIdCnt = cmmtMenuDao.selectCount_menuIds(systemId, menuIds);
			if (menuIdCnt != menuIds.size()) {
				throw new InvalidationException(String.format(validateMessageExt.포함불가, "메뉴ID"));  // 유효하지 않은 %s이(가) 포함되어 있습니다.
			}

			// 자식메뉴ID가 enabled이면, 그 부모메뉴도 enabled이어야 한다.
			List<CmmtMenu> allMenuList = cmmtMenuDao.selectAll(systemId);
			for (String menuId : menuIds) {
				if (!newMenuIdList.contains(menuId)) {
					newMenuIdList.add(menuId);
				}
				addParentMenuIds(newMenuIdList, allMenuList, menuId);
			}
		}

		// cmmt_menu_role 삭제
		cmmtMenuRoleDao.deleteByRoleId(systemId, roleId);

		// 매핑 대상 메뉴ID가 있는 경우 insert
		if (!newMenuIdList.isEmpty()) {
			// cmmt_menu_role에 newMenuIdList insert
			List<CmmtMenuRole> cmmtMenuRoleList = new ArrayList<>();
			CmmtMenuRole cmmtMenuRole = null;
			for (String menuId : newMenuIdList) {
				cmmtMenuRole = CmmtMenuRole.builder()
						.systemId(systemId)
						.menuId(menuId)
						.roleId(roleId)
						.createdDt(now)
						.creatorId(user.getMemberId())
						.build();
				cmmtMenuRoleList.add(cmmtMenuRole);
			}
			cmmtMenuRoleDao.insertList(cmmtMenuRoleList);
		}

		// roleId의 메뉴 조회
		List<MenuEnabledDto> list = cmmtMenuDao.selectAll_roleId(systemId, roleId);

		return new JsonList<>(list);
	}
}