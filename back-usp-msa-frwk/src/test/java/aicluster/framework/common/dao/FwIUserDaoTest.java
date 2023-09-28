package aicluster.framework.common.dao;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.UserDto;
import aicluster.framework.support.TestDaoSupport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FwIUserDaoTest extends TestDaoSupport {
	@Autowired
	private FwUserDao dao;

	@BeforeEach
	public void init() {
//		Date now = new Date();
	}

	@Test
	public void test() {
		UserDto user = dao.select("member-9f1fae272be44afaa9a2bee4e9711059");
		Assert.assertNotNull(user);
		log.info("============================");
		log.info(user.toString());
	}
}
