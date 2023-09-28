package aicluster.member.common.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtCodeGroup;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.CoreUtils.string;

//@Slf4j
public class CmmtCodeDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtCodeDao dao;

	@Autowired
	private CmmtCodeGroupDao gdao;

	private CmmtCodeGroup cmmtCodeGroup;
	private CmmtCode cmmtCode;

	@BeforeEach
	public void init() {
		cmmtCodeGroup = CmmtCodeGroup.builder()
				.codeGroup("test-code-group")
				.codeGroupNm("테스트-코드그룹명")
				.remark("비고")
				.enabled(true)
				.build();

		cmmtCode = CmmtCode.builder()
				.codeGroup(cmmtCodeGroup.getCodeGroup())
				.code("code1")
				.codeNm("code1-nm")
				.remark("비고")
				.codeType(null)
				.enabled(false)
				.sortOrder(1L)
				.build();
	}

	@Test
	public void test() {
		// insert
		gdao.insert(cmmtCodeGroup);
		dao.insert(cmmtCode);

		// select
		CmmtCode c1 = dao.select(cmmtCode.getCodeGroup(), cmmtCode.getCode());
		Assert.assertNotNull(c1);
		Assert.assertEquals(c1.getCode(), string.upperCase(cmmtCode.getCode()));
		Assert.assertEquals(c1.getCodeGroup(), string.upperCase(cmmtCode.getCodeGroup()));

		// selectList
		List<CmmtCode> list = dao.selectList(cmmtCode.getCodeGroup());
		Assert.assertEquals(list.size(), 1);

		// selectList_enabled
		list = dao.selectList_enabled(cmmtCode.getCodeGroup(), null);
		Assert.assertEquals(list.size(), 0);

		// delete
		dao.delete(cmmtCode.getCodeGroup(), cmmtCode.getCode());
		c1 = dao.select(cmmtCode.getCodeGroup(), cmmtCode.getCode());
		Assert.assertNull(c1);
	}
}
