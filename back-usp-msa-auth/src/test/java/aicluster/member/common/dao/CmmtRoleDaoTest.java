package aicluster.member.common.dao;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtRole;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.CoreUtils.string;

//@Slf4j
public class CmmtRoleDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtRoleDao dao;

	private CmmtRole cmmtRole;

	@BeforeEach
	public void init() {
		Date now = new Date();
		cmmtRole = CmmtRole.builder()
				.roleId("--role-id")
				.roleNm("--역할이름")
				.creatorId("SYSTEM")
				.createdDt(now)
				.updaterId("SYSTEM")
				.updatedDt(now)
				.build();
	}

	@Test
	public void test() {
		// 최초 전체 건수
		List<CmmtRole> list = dao.selectList();
		int size1 = list.size();

		// 1건 insert
		dao.insert(cmmtRole);

		list = dao.selectList();
		assertTrue(size1 + 1 == list.size());

		CmmtRole r2 = dao.select(cmmtRole.getRoleId());
		assertTrue(r2 != null);
		assertTrue(string.equals(cmmtRole.getRoleNm(), r2.getRoleNm()));

		// 1건 수정
		cmmtRole.setRoleNm("--역할이름수정");
		dao.update(cmmtRole);

		r2 = dao.select(cmmtRole.getRoleId());
		assertTrue(r2 != null);
		assertTrue(string.equals(r2.getRoleNm(), cmmtRole.getRoleNm()));

		// 1건 삭제
		dao.delete(cmmtRole.getRoleId());
		r2 = dao.select(cmmtRole.getRoleId());
		assertTrue(r2 == null);
		list = dao.selectList();
		assertTrue(size1 == list.size());
	}
}
