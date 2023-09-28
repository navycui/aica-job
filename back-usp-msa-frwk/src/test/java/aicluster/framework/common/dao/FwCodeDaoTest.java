package aicluster.framework.common.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.CodeDto;
import aicluster.framework.support.TestDaoSupport;

//@Slf4j
public class FwCodeDaoTest extends TestDaoSupport {
	@Autowired
	private FwCodeDao dao;

	@BeforeEach
	public void init() {
//		Date now = new Date();
	}

	@Test
	public void test() {
		CodeDto dto = dao.select("MEMBER_TYPE", "INDIVIDUAL");
		Assert.assertNotNull(dto);

		List<CodeDto> list = dao.selectList("MEMBER_TYPE");
		Assert.assertTrue(list.size() > 0);

		list = dao.selectList_codeType("MEMBER_TYPE", "PORTAL");
		Assert.assertTrue(list.size() > 0);

		list = dao.selectList_enabled("MEMBER_TYPE");
		Assert.assertTrue(list.size() > 0);

		list = dao.selectList_codeType_enabled("MEMBER_TYPE", "PORTAL");
		Assert.assertTrue(list.size() > 0);
	}
}
