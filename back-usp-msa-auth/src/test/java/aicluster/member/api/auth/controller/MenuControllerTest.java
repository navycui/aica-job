package aicluster.member.api.auth.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import aicluster.member.support.TestControllerSupport;

public class MenuControllerTest extends TestControllerSupport {

	@Test
	public void test() throws Exception {
		/*
		 * 메뉴 목록 조회
		 */
		this.mvc.perform(getMethodBuilder("/member/api/auth/menus/PORTAL_USP")).andDo(print());

		/*
		 * 메뉴 등록
		 */
		List<MultiValueMap<String, String>> insertList = new ArrayList<MultiValueMap<String,String>>();
		MultiValueMap<String, String> insertParam = new LinkedMultiValueMap<>();
		insertParam.add("baseMenuId", "ROOT");
		insertParam.add("relation", "CHILD");
		insertParam.add("menuId", "MENU1");
		insertParam.add("menuNm", "Menu-1");
		insertParam.add("url", "/member/menu1");
		insertParam.add("newWindow", "false");
		insertParam.add("remark", "Menu 1Depth No 1");
		insertList.add(insertParam);

		insertParam = new LinkedMultiValueMap<>();
		insertParam.add("baseMenuId", "ROOT");
		insertParam.add("relation", "CHILD");
		insertParam.add("menuId", "MENU2");
		insertParam.add("menuNm", "Menu-2");
		insertParam.add("url", "/member/menu2");
		insertParam.add("newWindow", "false");
		insertParam.add("remark", "Menu 1Depth No 2");
		insertList.add(insertParam);

		insertParam = new LinkedMultiValueMap<>();
		insertParam.add("baseMenuId", "menu-MENU1");
		insertParam.add("relation", "CHILD");
		insertParam.add("menuId", "MENU1_2");
		insertParam.add("menuNm", "Menu-1-2");
		insertParam.add("url", "/member/menu1/submenu2");
		insertParam.add("newWindow", "false");
		insertParam.add("remark", "Menu 2Depth No 1-2");
		insertList.add(insertParam);

		insertParam = new LinkedMultiValueMap<>();
		insertParam.add("baseMenuId", "menu-MENU1_2");
		insertParam.add("relation", "ABOVE");
		insertParam.add("menuId", "MENU1_1");
		insertParam.add("menuNm", "Menu-1-1");
		insertParam.add("url", "/member/menu1/submenu1");
		insertParam.add("newWindow", "false");
		insertParam.add("remark", "Menu 2Depth No 1-1");
		insertList.add(insertParam);

		insertParam = new LinkedMultiValueMap<>();
		insertParam.add("baseMenuId", "menu-MENU1_2");
		insertParam.add("relation", "BELOW");
		insertParam.add("menuId", "MENU1_3");
		insertParam.add("menuNm", "Menu-1-3");
		insertParam.add("url", "/member/menu1/submenu3");
		insertParam.add("newWindow", "false");
		insertParam.add("remark", "Menu 2Depth No 1-3");
		insertList.add(insertParam);

		for (MultiValueMap<String, String> params : insertList) {
			this.mvc.perform(postMethodBuilder("/member/api/auth/menus/PORTAL_USP").params(params)).andDo(print());
		}

		/*
		 * 메뉴 조회
		 */
		this.mvc.perform(getMethodBuilder("/member/api/auth/menus/PORTAL_USP/menu-MENU1_2")).andDo(print());

		/*
		 * 메뉴 수정
		 */
		MultiValueMap<String, String> updateParam = new LinkedMultiValueMap<>();
		updateParam.add("menuNm", "Menu-1-1U");
		updateParam.add("url", "/member/menu1/submenu1");
		updateParam.add("newWindow", "true");
		updateParam.add("remark", "Menu 2Depth No 1-1 Update");

		this.mvc.perform(putMethodBuilder("/member/api/auth/menus/PORTAL_USP/menu-MENU1_1").params(updateParam)).andDo(print());

		/*
		 * 메뉴 삭제
		 */
		this.mvc.perform(deleteMethodBuilder("/member/api/auth/menus/PORTAL_USP/menu-MENU1_3")).andDo(print());

		/*
		 * 메뉴 이동
		 */
		this.mvc.perform(putMethodBuilder("/member/api/auth/menus/PORTAL_USP/menu-MENU1_1/move").param("direction", "DOWN")).andDo(print());

		/*
		 * 사용자메뉴조회
		 */
		this.mvc.perform(getMethodBuilder("/member/api/auth/menus/PORTAL_USP/me")).andDo(print());
	}
}
