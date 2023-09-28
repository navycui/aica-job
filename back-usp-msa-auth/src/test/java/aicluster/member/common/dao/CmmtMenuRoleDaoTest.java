package aicluster.member.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtMenu;
import aicluster.member.common.entity.CmmtMenuRole;
import aicluster.member.support.TestDaoSupport;

//@Slf4j
public class CmmtMenuRoleDaoTest extends TestDaoSupport {
	private static final String SYSTEM_ID = "USP";

	@Autowired
	private CmmtMenuRoleDao dao;

	@Autowired
	private CmmtMenuDao menuDao;

	private CmmtMenu menu;
	@BeforeEach
	public void init() {
		Date now = new Date();
		// menu = CmmtMenu.builder()
		// 		.menuId("ROOT")
		// 		.menuNm("ROOT")
		// 		.url("/")
		// 		.parentMenuId(null)
		// 		.sortOrder(0L)
		// 		.remark("비고")
		// 		.createdDt(now)
		// 		.creatorId("SYSTEM")
		// 		.updatedDt(now)
		// 		.updaterId("SYSTEM")
		// 		.build();
		// CmmtMenu m = menuDao.select(SYSTEM_ID, "ROOT");
		// if (m == null) {
		// 	menuDao.insert(menu);
		// }
		menu = CmmtMenu.builder()
				.systemId(SYSTEM_ID)
				.menuId("메뉴")
				.menuNm("--메뉴")
				.url("/")
				.parentMenuId("ROOT")
				.sortOrder(2L)
				.remark("비고")
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();
		menuDao.insert(menu);
	}

	@Test
	public void test() {
		// selectByMenuId
		List<CmmtMenuRole> list = dao.selectByMenuId(SYSTEM_ID, menu.getMenuId());
		Assert.assertEquals(list.size(), 0);
	}
}
