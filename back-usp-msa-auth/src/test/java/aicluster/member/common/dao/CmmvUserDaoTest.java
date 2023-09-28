package aicluster.member.common.dao;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmvUser;
import aicluster.member.support.TestDaoSupport;

//@Slf4j
public class CmmvUserDaoTest extends TestDaoSupport {
	@Autowired
	private CmmvUserDao dao;

	@BeforeEach
	public void init() {

	}

	@Test
	public void test() {
		CmmvUser user = dao.select("member-cf191939537f4c3f9e056c1a13cfc521");
		Assert.assertNotNull(user);
	}
}
