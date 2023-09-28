package aicluster.common.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.common.entity.CmmtEmailSndngDtls;
import aicluster.common.support.TestDaoSupport;
import bnet.library.util.CoreUtils;

//@Slf4j
public class CmmtEmailSndngDtlsDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtEmailSndngDtlsDao emailDao;

	private CmmtEmailSndngDtls email;

	@BeforeEach
	public void init() {
		Date now = new Date();

		email = CmmtEmailSndngDtls.builder()
				.emailId("email-test")
				.title("01011112222")
				.emailCn("안녕하세요?")
				.html(false)
				.senderEmail("webmaster@atops.or.kr")
				.senderNm("관리자")
				.createdDt(now)
				.build();
	}

	@Test
	public void test() {
		// insert
		emailDao.insert(email);

		// insertList
		List<CmmtEmailSndngDtls> list = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			CmmtEmailSndngDtls sms2 = new CmmtEmailSndngDtls();
			CoreUtils.property.copyProperties(sms2, email);
			sms2.setEmailId(CoreUtils.string.getNewId("email-"));
			list.add(sms2);
		}
		emailDao.insertList(list);

		// select
		List<CmmtEmailSndngDtls> s1 = emailDao.selectList("20220901", "20220930", "webmaster@atops.or.kr", null, null);
		Assert.assertNotNull(s1);
	}
}
