package aicluster.framework.common.dao;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.support.TestDaoSupport;

//@Slf4j
public class FwMberInfoDaoTest extends TestDaoSupport {
	@Autowired
	private FwMberInfoDao dao;

	@BeforeEach
	public void init() {
//		Date now = new Date();
	}

	@Test
	public void test() {
		BnMember member = dao.selectBnMember_loginId("ymyoo");
		Assert.assertNotNull(member);
	}
}
