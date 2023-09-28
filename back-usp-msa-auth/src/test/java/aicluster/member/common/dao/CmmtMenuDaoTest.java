package aicluster.member.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.api.auth.dto.MyMenuDto;
import aicluster.member.common.dto.MenuEnabledDto;
import aicluster.member.common.entity.CmmtMenu;
import aicluster.member.common.entity.CmmtMenuRole;
import aicluster.member.common.util.CodeExt;
import aicluster.member.support.TestDaoSupport;

//@Slf4j
public class CmmtMenuDaoTest extends TestDaoSupport {
	private static final String SYSTEM_ID = "TEST";

	@Autowired
	private CmmtMenuDao dao;

	@Autowired
	private CmmtMenuRoleDao roleDao;

	private CmmtMenu rootMenu;
	private CmmtMenu childMenu1;
	private CmmtMenu childMenu2;
	private CmmtMenuRole cmmtMenuRole1;
	private CmmtMenuRole cmmtMenuRole2;

	@BeforeEach
	public void init() {
		Date now = new Date();
		rootMenu = CmmtMenu.builder()
				.systemId(SYSTEM_ID)
				.menuId("ROOT")
				.menuNm("ROOT")
				.url("/")
				.parentMenuId(null)
				.sortOrder(0L)
				.remark(null)
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

		childMenu1 = CmmtMenu.builder()
				.systemId(SYSTEM_ID)
				.menuId("menu-1")
				.menuNm("메뉴1")
				.url("/first")
				.parentMenuId("ROOT")
				.sortOrder(9999999991L)
				.remark("비고")
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

		childMenu2 = CmmtMenu.builder()
				.systemId(SYSTEM_ID)
				.menuId("menu-2")
				.menuNm("메뉴2")
				.url("/second")
				.parentMenuId("ROOT")
				.sortOrder(9999999992L)
				.remark("비고")
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

		cmmtMenuRole1 = CmmtMenuRole.builder()
				.systemId(SYSTEM_ID)
				.menuId(childMenu1.getMenuId())
				.roleId(CodeExt.role.일반사용자)
				.build();
		cmmtMenuRole2 = CmmtMenuRole.builder()
				.systemId(SYSTEM_ID)
				.menuId(childMenu2.getMenuId())
				.roleId(CodeExt.role.일반사용자)
				.build();
	}

	@Test
	public void test() {
		// insert
		dao.insert(rootMenu);
		dao.insert(childMenu1);
		dao.insert(childMenu2);

		roleDao.insert(cmmtMenuRole1);
		roleDao.insert(cmmtMenuRole2);

		// selectAll
		List<CmmtMenu> list = dao.selectAll(SYSTEM_ID);
		Assert.assertEquals(list.size(), 3);

		// select
		CmmtMenu m1 = dao.select(SYSTEM_ID, childMenu1.getMenuId());
		Assert.assertNotNull(m1);

		// selectLastChild
		m1 = dao.selectLastChild(SYSTEM_ID, "ROOT");
		Assert.assertEquals(m1.getMenuId(), childMenu2.getMenuId());

		// selectAboveMenu
		m1 = dao.selectAboveMenu(SYSTEM_ID, childMenu2.getMenuId());
		Assert.assertEquals(m1.getMenuId(), childMenu1.getMenuId());

		// selectBelowMenu
		m1 = dao.selectBelowMenu(SYSTEM_ID, childMenu1.getMenuId());
		Assert.assertEquals(m1.getMenuId(), childMenu2.getMenuId());

		List<MyMenuDto> myList = new ArrayList<>();

		// selectList_authorityId
		myList = dao.selectList_authorityId(SYSTEM_ID, CodeExt.authorityId.일반사용자);
		Assert.assertEquals(myList.size(), 2);

		// selectList_roleId
		myList = dao.selectList_roleId(SYSTEM_ID, CodeExt.role.일반사용자);
		Assert.assertEquals(myList.size(), 2);

		// selectAll_roleId
		List<MenuEnabledDto> enabledList = dao.selectAll_roleId(SYSTEM_ID, CodeExt.role.일반사용자);
		Assert.assertEquals(enabledList.size(), 3);

		// selectCount_menuIds
		List<String> menuIdList = new ArrayList<>();
		menuIdList.add(childMenu1.getMenuId());
		menuIdList.add(childMenu2.getMenuId());
		int cnt = dao.selectCount_menuIds(SYSTEM_ID, menuIdList);
		Assert.assertEquals(cnt, menuIdList.size());

		// incSortOrder
		dao.incSortOrder(SYSTEM_ID, childMenu2.getMenuId(), childMenu2.getSortOrder());
		m1 = dao.select(SYSTEM_ID, childMenu2.getMenuId());
		Assert.assertTrue(childMenu2.getSortOrder() + 1 == m1.getSortOrder());

		// decSortOrder
		dao.decSortOrder(SYSTEM_ID, m1.getMenuId(), m1.getSortOrder());
		m1 = dao.select(SYSTEM_ID, childMenu2.getMenuId());
		Assert.assertEquals(m1.getSortOrder(), childMenu2.getSortOrder());
	}
}
