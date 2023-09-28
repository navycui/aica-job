package aicluster.member.api.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.common.entity.CmmtMenu;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;
import lombok.Builder;
import lombok.Data;

public class MenuServiceTest extends TestServiceSupport {
	@Autowired
	private MenuService service;

	private static final String SYSTEM_ID = "PORTAL_USP";
	private List<MenuServiceTest.tempMenuDto> initTempMenuList;

	@Data
	@Builder
	private static class tempMenuDto {
		private String systemId;
		private String baseMenuId;
		private String relation;
		private String menuId;
		private String menuNm;
		private String url;
		private Boolean newWindow;
		private String remark;
	}

	public void init()
	{
		initTempMenuList = new ArrayList<>();
		MenuServiceTest.tempMenuDto insMenu;

		insMenu = null;
		insMenu = MenuServiceTest.tempMenuDto.builder()
							.systemId(SYSTEM_ID)
							.baseMenuId("ROOT")
							.relation("CHILD")
							.menuId("TEST01")
							.menuNm("TEST-1")
							.url("/member/testmenu1")
							.newWindow(false)
							.remark("메뉴 1 비고")
							.build();
		initTempMenuList.add(insMenu);

		insMenu = null;
		insMenu = MenuServiceTest.tempMenuDto.builder()
							.systemId(SYSTEM_ID)
							.baseMenuId("ROOT")
							.relation("CHILD")
							.menuId("TEST02")
							.menuNm("TEST-2")
							.url("/member/testmenu2")
							.newWindow(false)
							.remark("메뉴 2 비고")
							.build();
		initTempMenuList.add(insMenu);

		insMenu = null;
		insMenu = MenuServiceTest.tempMenuDto.builder()
							.systemId(SYSTEM_ID)
							.baseMenuId("ROOT")
							.relation("CHILD")
							.menuId("TEST03")
							.menuNm("TEST-3")
							.url("/member/testmenu3")
							.newWindow(false)
							.remark("메뉴 3 비고")
							.build();
		initTempMenuList.add(insMenu);

		System.out.println("□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■");
		for(MenuServiceTest.tempMenuDto menu : initTempMenuList) {
			System.out.println("	=> " + menu.toString());
		}
		System.out.println("□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■");
	}

	@Test
	public void test() {
		init();

		BnMember member = SecurityUtils.getCurrentMember();
		System.out.println("member:" + member);

		System.out.println("\n\n\n");

		////////////////// 메뉴 관리 서비스 ///////////////////////////////////
		// 메뉴 목록 조회
		System.out.println("목록 조회 결과");
		JsonList<CmmtMenu> menuList = service.getList(SYSTEM_ID);
		for (CmmtMenu menu : menuList.getList()) {
			System.out.println("\t\t" + menu.toString());
		}
		System.out.println("\n\n\n");

		// 메뉴 정보 입력
		List<CmmtMenu> tmpMenuList = new ArrayList<>();
		System.out.println("메뉴 입력 결과("+initTempMenuList.size()+"건)");
		List<CmmtMenu> insMenuRst = new ArrayList<>();
		for (MenuServiceTest.tempMenuDto menu : initTempMenuList) {
			CmmtMenu insMenu = service.insertMenu(menu.getSystemId()
												, menu.getBaseMenuId()
												, menu.getRelation()
												, menu.getMenuId()
												, menu.getMenuNm()
												, menu.getUrl()
												, menu.getNewWindow()
												, menu.getRemark());
			insMenuRst.add(insMenu);
		}

		List<CmmtMenu> insChMenuRst = new ArrayList<>();
		// 자식 메뉴 추가
		CmmtMenu insChMenu1 = service.insertMenu(SYSTEM_ID
											, insMenuRst.get(0).getMenuId()
											, "CHILD"
											, "TEST01-1"
											, "TEST-1-C-1"
											, "/member/testmenu1/submenu1-1"
											, false
											, "");
		CmmtMenu insChMenu2 = service.insertMenu(SYSTEM_ID
											, insMenuRst.get(0).getMenuId()
											, "CHILD"
											, "TEST01-2"
											, "TEST-1-C-2"
											, "/member/testmenu1/submenu1-2"
											, false
											, "");
		CmmtMenu insChMenu3 = service.insertMenu(SYSTEM_ID
											, insMenuRst.get(2).getMenuId()
											, "CHILD"
											, "TEST03-1"
											, "TEST-3-C-1"
											, "/member/testmenu3/submenu3-1"
											, false
											, "");
		insChMenuRst.add(insChMenu1);
		insChMenuRst.add(insChMenu2);
		insChMenuRst.add(insChMenu3);

		// 입력 결과 검증
		for (CmmtMenu menu : insMenuRst) {
			System.out.println("부모\t\t" + menu.toString());
		}
		for (CmmtMenu menu : insChMenuRst) {
			System.out.println("자식\t\t" + menu.toString());
		}
		System.out.println("\n\n\n");

		// 메뉴 정보 조회
		CmmtMenu selMenu = service.selectMenu(SYSTEM_ID, "ROOT");
		System.out.println("메뉴 조회 결과 : " + selMenu.toString());
		System.out.println("\n\n\n");


		// 메뉴 정보 수정
		System.out.println("메뉴 수정 결과("+insMenuRst.size()+"건)");
		List<CmmtMenu> uptMenuRst = new ArrayList<>();
		for (CmmtMenu menu : insMenuRst) {
			CmmtMenu uptMenu = service.updateMenu(SYSTEM_ID, menu.getMenuId(), menu.getMenuNm() + "U", menu.getUrl(), menu.getNewWindow(), menu.getRemark());
			uptMenuRst.add(uptMenu);
		}
		for (CmmtMenu menu : uptMenuRst) {
			System.out.println("\t\t" + menu.toString());
		}
		System.out.println("\n\n\n");

		// 메뉴 정보 이동
		System.out.println("메뉴 이동 결과");
		List<CmmtMenu> movMenuRst = new ArrayList<>();
		int i = 0;
		for (CmmtMenu menu : insMenuRst) {
			String direction = null;
			switch (i) {
			case 0 :
			case 1 :
				direction = "DOWN";
				break;
			default :
				direction = "UP";
				break;
			}

			CmmtMenu movMenu = service.moveMenu(SYSTEM_ID, menu.getMenuId(), direction);
			movMenuRst.add(movMenu);

			i++;
		}
		for (CmmtMenu menu : movMenuRst) {
			System.out.println("\t\t" + menu.toString());
		}
		System.out.println("\n\n\n");

		// 메뉴 삭제
		System.out.println("메뉴 삭제 결과("+tmpMenuList.size()+"건)");
		for (CmmtMenu menu : insChMenuRst) {
			service.deleteMenu(SYSTEM_ID, menu.getMenuId());
			System.out.println("["+menu.getMenuId()+"] 삭제");
		}
		for (CmmtMenu menu : insMenuRst) {
			service.deleteMenu(SYSTEM_ID, menu.getMenuId());
			System.out.println("["+menu.getMenuId()+"] 삭제");
		}
		System.out.println("\n\n\n");
	}
}
