package aicluster.member.common.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtCodeGroup;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.CoreUtils.string;

//@Slf4j
public class CmmtCodeGroupDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtCodeGroupDao dao;

	private CmmtCodeGroup cmmtCodeGroup;
	@BeforeEach
	public void init() {
		cmmtCodeGroup = CmmtCodeGroup.builder()
				.codeGroup("TEST-CODE-GROUP")
				.codeGroupNm("테스트-코드-그룹")
				.remark("비고")
				.enabled(true)
				.build();
	}

	@Test
	public void test() {
		// insert
		dao.insert(cmmtCodeGroup);

		// select
		CmmtCodeGroup c1 = dao.select(cmmtCodeGroup.getCodeGroup());
		Assert.assertNotNull(c1);
		Assert.assertEquals(c1.getCodeGroup(), cmmtCodeGroup.getCodeGroup());

		// update
		cmmtCodeGroup.setCodeGroupNm("aaaa");
		dao.update(cmmtCodeGroup);
		c1 = dao.select(cmmtCodeGroup.getCodeGroup());
		Assert.assertEquals(c1.getCodeGroupNm(), string.upperCase(cmmtCodeGroup.getCodeGroupNm()));

		// selectList
		List<CmmtCodeGroup> list = dao.selectList(cmmtCodeGroup.getCodeGroup(), null);
		Assert.assertEquals(list.size(), 1);

		// delete
		dao.delete(cmmtCodeGroup.getCodeGroup());
		c1 = dao.select(cmmtCodeGroup.getCodeGroup());
		Assert.assertNull(c1);
	}
}
