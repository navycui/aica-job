package aicluster.common.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.common.entity.CmmtQna;
import aicluster.common.support.TestDaoSupport;

//@Slf4j
public class CmmtQnaDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtQnaDao qnaDao;

	private CmmtQna qna;

	@BeforeEach
	public void init() {
		Date now = new Date();

		qna = CmmtQna.builder()
				.qnaId("qna-test")
				.systemId("PORTAL-USP")
				.qnaNm("test-nm")
				.systemId("portal")
				.attachable(true)
				.attachmentSize(100000000L)
				.attachmentExt("PDF/EXLS")
				.enabled(true)
				.createdDt(now)
				.updatedDt(now)
				.build();
	}

	@Test
	public void test() {

		// void insert(CmmtQna qna);
		qnaDao.insert(qna);

		// CmmtQna select(String qnaId);
		CmmtQna q1 = qnaDao.select(qna.getQnaId());
		Assert.assertNotNull(q1);
		Assert.assertEquals(q1.getQnaNm(), qna.getQnaNm());

		// List<CmmtQna> selectList();
		List<CmmtQna> list = qnaDao.selectList(qna.getSystemId(), qna.getQnaId(), qna.getQnaNm(), true);
		Assert.assertTrue(list.size() == 1);

		// void update(CmmtQna qna);
		qna.setQnaNm("mine");
		qnaDao.update(qna);
		q1 = qnaDao.select(qna.getQnaId());
		Assert.assertEquals(q1.getQnaNm(), qna.getQnaNm());

		// void delete(String qnaId);
		qnaDao.delete(qna.getQnaId());
		q1 = qnaDao.select(qna.getQnaId());
		Assert.assertNull(q1);
	}
}
