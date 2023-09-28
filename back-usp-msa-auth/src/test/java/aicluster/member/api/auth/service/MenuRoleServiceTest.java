package aicluster.member.api.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.common.dto.MenuEnabledDto;
import aicluster.member.common.entity.CmmtMenu;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;

public class MenuRoleServiceTest extends TestServiceSupport {
	@Autowired
	private MenuRoleService service;

	@Autowired
	private MenuService menuService;

	private static final String SYSTEM_ID = "PORTAL_USP";

	@Test
	public void test() {
		BnMember member = SecurityUtils.getCurrentMember();
		System.out.println("member:" + member);
		System.out.println("\n\n\n");

		System.out.println("메뉴 역할 매핑 목록 조회");
		JsonList<MenuEnabledDto> menuRoleList = service.getMenuEnabledList(SYSTEM_ID, "ROLE_USER");
		for (MenuEnabledDto dto : menuRoleList.getList()) {
			System.out.println("\t\t" + dto);
		}
		System.out.println("\n\n\n");

		System.out.println("메뉴 역할 매핑 결과");
		List<String> menuIdList = new ArrayList<>();
		JsonList<CmmtMenu> menuList = menuService.getList(SYSTEM_ID);
		for (CmmtMenu menu : menuList.getList()) {
			menuIdList.add(menu.getMenuId());
		}

		JsonList<MenuEnabledDto> list = service.updateMenuEnabled(SYSTEM_ID, "ROLE_USER", menuIdList);
		for (MenuEnabledDto dto : list.getList()) {
			System.out.println("\t\t" + dto);
		}
		System.out.println("\n\n\n");
	}
}
