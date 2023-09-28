package aicluster.member.common.dao;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtProgrm;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.CoreUtils.string;

//@Slf4j
public class CmmtProgrmDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtProgrmDao dao;

	private CmmtProgrm cmmtProgram;

	@BeforeEach
	public void init() {
		Date now = new Date();
		cmmtProgram = CmmtProgrm.builder()
				.programId("--id")
				.programNm("--프로그램명")
				.systemId("--portal")
				.httpMethod("*")
				.urlPattern("/sss/**")
				.checkOrder(4L)
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();
	}

	@Test
	public void test() {
		dao.insert(cmmtProgram);
		List<CmmtProgrm> list = dao.selectList(cmmtProgram.getSystemId(), cmmtProgram.getProgramNm(), cmmtProgram.getUrlPattern());
		assertTrue(list.size() == 1);

		CmmtProgrm p = dao.selectByName(cmmtProgram.getSystemId(), cmmtProgram.getProgramNm());
		assertTrue( p != null );

		p = dao.selectByUrlPattern(cmmtProgram.getSystemId(), cmmtProgram.getHttpMethod(), cmmtProgram.getUrlPattern());
		assertTrue( p != null );

		cmmtProgram.setProgramNm("--program-name");
		dao.update(cmmtProgram);
		p = dao.select(cmmtProgram.getProgramId());
		assertTrue(string.equals(cmmtProgram.getProgramNm(), p.getProgramNm()));

		dao.incCheckOrder(cmmtProgram.getCheckOrder());
		p = dao.select(cmmtProgram.getProgramId());
		assertTrue( p != null );
		assertTrue(p.getCheckOrder() == cmmtProgram.getCheckOrder() + 1);

	}
}
