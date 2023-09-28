package aicluster.framework.common.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnAuthorityRole;
import aicluster.framework.common.entity.ProgramRole;
import aicluster.framework.support.TestDaoSupport;

//@Slf4j
public class FwAuthorDaoTest extends TestDaoSupport {
	@Autowired
	private FwAuthorDao dao;

	@BeforeEach
	public void init() {
//		Date now = new Date();
	}

	@Test
	public void test() {
		//
		List<BnAuthorityRole> list1 = dao.selectAuthorityRoles("USER");
		Assert.assertTrue(list1.size() > 0);

		List<ProgramRole> list2 = dao.selectProgramRoles();
		Assert.assertTrue(list2.size() > 0);
	}
}
