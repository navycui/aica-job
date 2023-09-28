package aicluster.member.common.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.common.entity.CmmtAuthorRole;
import aicluster.member.common.entity.CmmtRole;
import aicluster.member.support.TestDaoSupport;

//@Slf4j
public class CmmtAuthorRoleDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtAuthorRoleDao dao;

	@Autowired
	private CmmtAuthorDao adao;

	@Autowired
	private CmmtRoleDao rdao;

	private String authorityId = "--id";
	private CmmtAuthorRole ar;
	private List<CmmtAuthorRole> list;

	@BeforeEach
	public void init() {
		Date now = new Date();
		CmmtAuthor authority = CmmtAuthor.builder()
				.authorityId(authorityId)
				.authorityNm("--nm")
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();
		adao.insert(authority);

		for (int i = 1; i < 4; i++) {
			CmmtRole role = CmmtRole.builder()
					.roleId("ROLE_TEST"+i)
					.roleNm("Unit Test"+i)
					.createdDt(now)
					.creatorId("SYSTEM")
					.updatedDt(now)
					.updaterId("SYSTEM")
					.build();
			rdao.insert(role);
		}

		ar = CmmtAuthorRole.builder()
				.authorityId(authorityId)
				.roleId("ROLE_TEST1")
				.createdDt(now)
				.creatorId("SYSTEM")
				.build();

		list = new ArrayList<>();
		for (int i = 2; i < 4; i++) {
			CmmtAuthorRole r = CmmtAuthorRole.builder()
					.authorityId(authorityId)
					.roleId("ROLE_TEST" + i)
					.createdDt(now)
					.creatorId("SYSTEM")
					.build();
			list.add(r);
		}
	}

	@Test
	public void test() {
		// 여러개 insert
		dao.insertList(list);
		List<CmmtAuthorRole> l1 = dao.selectList(authorityId);
		assertTrue(list.size() == l1.size());

		// 1개 insert
		dao.insert(ar);
		List<CmmtAuthorRole> l2 = dao.selectByRoleId(ar.getRoleId());
		assertTrue(l2.size() == 1);

		// 모두 삭제
		dao.deleteAuthorityId(authorityId);
		l1 = dao.selectList(authorityId);
		assertTrue(l1.size() == 0);
	}
}
