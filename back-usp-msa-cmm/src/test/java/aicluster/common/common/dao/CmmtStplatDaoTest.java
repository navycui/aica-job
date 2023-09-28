package aicluster.common.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.common.dto.TermsListItem;
import aicluster.common.common.entity.CmmtStplat;
import aicluster.common.common.util.CodeExt;
import aicluster.common.support.TestDaoSupport;
import bnet.library.util.CoreUtils.date;

public class CmmtStplatDaoTest extends TestDaoSupport{
	@Autowired
	private CmmtStplatDao termsDao;

	private CmmtStplat terms;

	@BeforeEach
	public void init() {
		Date now = new Date();

		terms = CmmtStplat.builder()
				.termsType("약관타입")
				.beginDay(date.format(now, "yyyyMMdd"))
				.required(true)
				.termsCn("약관내용")
				.possessTermCd(CodeExt.possessTerm.회원탈퇴)
				.build();
	}

	@Test
	public void test() {
		termsDao.insert(terms);

		List<CmmtStplat> testSet = termsDao.selectSet(terms.getTermsType(), terms.getBeginDay());
		Assert.assertNotNull(testSet);

		List<TermsListItem> list = termsDao.selectList_beginDay(terms.getTermsType(), "TODAY");
		Assert.assertNotNull(list);

		terms.setTermsCn("두번째 내용");
		termsDao.update(terms);
		CmmtStplat test = termsDao.select(terms.getTermsType(), terms.getBeginDay(), terms.isRequired());
		Assert.assertNotNull(test);
		Assert.assertEquals(test.getTermsCn(), terms.getTermsCn());

		testSet = termsDao.select_today(terms.getTermsType());
		Assert.assertNotNull(test);

		termsDao.delete(terms.getTermsType(),terms.getBeginDay());
		test = termsDao.select(terms.getTermsType(), terms.getBeginDay(), terms.isRequired());
		Assert.assertNull(test);


	}
}
