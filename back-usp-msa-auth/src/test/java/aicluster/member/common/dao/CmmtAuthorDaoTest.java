package aicluster.member.common.dao;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.CoreUtils;

//@Slf4j
public class CmmtAuthorDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtAuthorDao dao;

	private CmmtAuthor cmmtAuthority;

	@BeforeEach
	public void init() {
		Date now = new Date();
		cmmtAuthority = CmmtAuthor.builder()
				.authorityId("test")
				.authorityNm("--테스트")
				.creatorId("SYSTEM")
				.createdDt(now)
				.updaterId("SYSTEM")
				.updatedDt(now)
				.build();
	}

	@Test
	public void test() {
		// 현재 건수
		List<CmmtAuthor> list = dao.selectAll();
		int size = list.size();

		// 1개 insert
		dao.insert(cmmtAuthority);

		list = dao.selectAll();
		assertTrue(size + 1 == list.size());

		CmmtAuthor a2 = dao.select(cmmtAuthority.getAuthorityId());
		assertTrue(CoreUtils.string.equals(cmmtAuthority.getAuthorityNm(), a2.getAuthorityNm()));

		// update
		cmmtAuthority.setAuthorityNm("--수정");
		dao.update(cmmtAuthority);
		a2 = dao.select(cmmtAuthority.getAuthorityId());
		assertTrue(CoreUtils.string.equals(cmmtAuthority.getAuthorityNm(), a2.getAuthorityNm()));

		// selectByName
		a2 = dao.selectByName(cmmtAuthority.getAuthorityNm());
		assertTrue(CoreUtils.string.equals(cmmtAuthority.getAuthorityId(), a2.getAuthorityId()));

		// delete
		dao.delete(cmmtAuthority.getAuthorityId());
		list = dao.selectAll();
		assertTrue(size == list.size());
		a2 = dao.select(cmmtAuthority.getAuthorityId());
		assertTrue(a2 == null);
	}
}
