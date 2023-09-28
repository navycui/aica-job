package aicluster.member.api.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.auth.dto.MenuOrderRequestDto;
import aicluster.member.api.auth.dto.MyMenuDto;
import aicluster.member.api.auth.service.MenuService;
import aicluster.member.common.entity.CmmtMenu;
import bnet.library.util.dto.JsonList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class MenuController {

	@Autowired
	MenuService menuService;

	/**
	 * 메뉴 목록 조회
	 * @param systemId
	 * @return
	 */
	@GetMapping("/menus/{systemId}")
	public JsonList<CmmtMenu> getList(@PathVariable String systemId)
	{
		log.debug("메뉴 목록(getList) Parameter : ["+systemId+"]");
		return menuService.getList(systemId);
	}

	/**
	 * 메뉴 등록
	 * @param systemId
	 * @param baseMenuId
	 * @param relation
	 * @param menuId
	 * @param menuNm
	 * @param url
	 * @param newWindow
	 * @param remark
	 * @return
	 */
	@PostMapping("/menus/{systemId}")
	public CmmtMenu add(@PathVariable String systemId, String baseMenuId, String relation, String menuId, String menuNm, String url, Boolean newWindow, String remark)
	{
		log.debug("메뉴 등록(add) Parameter ======================================");
		log.debug("\t- systemId		: ["+systemId+"]");
		log.debug("\t- baseMenuId	: ["+baseMenuId+"]");
		log.debug("\t- relation		: ["+relation+"]");
		log.debug("\t- menuId		: ["+menuId+"]");
		log.debug("\t- menuNm		: ["+menuNm+"]");
		log.debug("\t- url			: ["+url+"]");
		log.debug("\t- newWindow	: ["+newWindow+"]");
		log.debug("\t- remark		: ["+remark+"]");
		return menuService.insertMenu(systemId, baseMenuId, relation, menuId, menuNm, url, newWindow, remark);
	}

	/**
	 * 메뉴 조회
	 * @param systemId
	 * @param menuId
	 * @return
	 */
	@GetMapping("/menus/{systemId}/{menuId}")
	public CmmtMenu get(@PathVariable String systemId, @PathVariable String menuId)
	{
		log.debug("메뉴 조회(get) Parameter ======================================");
		log.debug("\t- systemId	: ["+systemId+"]");
		log.debug("\t- menuId	: ["+menuId+"]");

		return menuService.selectMenu(systemId, menuId);
	}

	/**
	 * 메뉴 수정
	 * @param systemId
	 * @param menuId
	 * @param menuNm
	 * @param url
	 * @param newWindow
	 * @param remark
	 * @return
	 */
	@PutMapping("/menus/{systemId}/{menuId}")
	public CmmtMenu modify(@PathVariable String systemId, @PathVariable String menuId, String menuNm, String url, Boolean newWindow, String remark)
	{
		log.debug("메뉴 수정(modify) Parameter ======================================");
		log.debug("\t- systemId		: ["+systemId+"]");
		log.debug("\t- menuId		: ["+menuId+"]");
		log.debug("\t- menuNm		: ["+menuNm+"]");
		log.debug("\t- url			: ["+url+"]");
		log.debug("\t- newWindow	: ["+newWindow+"]");
		log.debug("\t- remark		: ["+remark+"]");

		return menuService.updateMenu(systemId, menuId, menuNm, url, newWindow, remark);
	}

	/**
	 * 메뉴 삭제
	 * @param systemId
	 * @param menuId
	 */
	@DeleteMapping("/menus/{systemId}/{menuId}")
	public void remove(@PathVariable String systemId, @PathVariable String menuId)
	{
		log.debug("메뉴 삭제(remove) Parameter ======================================");
		log.debug("\t- systemId	: ["+systemId+"]");
		log.debug("\t- menuId	: ["+menuId+"]");

		menuService.deleteMenu(systemId, menuId);
	}

	/**
	 * 메뉴 위치 이동
	 * @param systemId
	 * @param menuId
	 * @param direction
	 * @return
	 */
	@PutMapping("/menus/{systemId}/{menuId}/move")
	public CmmtMenu move(@PathVariable String systemId, @PathVariable String menuId, String direction)
	{
		log.debug("메뉴 이동(move) Parameter ======================================");
		log.debug("\t- systemId		: ["+systemId+"]");
		log.debug("\t- menuId		: ["+menuId+"]");
		log.debug("\t- direction	: ["+direction+"]");
		return menuService.moveMenu(systemId, menuId, direction);
	}

	/**
	 * 사용자 메뉴목록 조회
	 * @param systemId
	 * @return
	 */
	@GetMapping("/menus/{systemId}/me")
	public JsonList<MyMenuDto> getUserMenuList(@PathVariable String systemId)
	{
		log.debug("사용자 메뉴 목록(getUserMenuList) Parameter : ["+systemId+"]");
		return menuService.getUserMenuList(systemId);
	}
	
	/**
	 * 메뉴 정렬
	 * @param systemId
	 * @param list
	 */
	@PutMapping("/menus/{systemId}/order")
	public void order(@PathVariable String systemId, @RequestBody List<MenuOrderRequestDto> list)
	{
		log.debug("메뉴 정렬 Parameter ======================================");
		log.debug("\t- systemId		: ["+systemId+"]");
		log.debug("\t- menuIdList		: ["+list.toString()+"]");
		menuService.orderMenu(systemId, list);
	}
}
