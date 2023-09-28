package aicluster.common.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.common.entity.CmmtSmsSndngDtls;
import aicluster.common.support.TestDaoSupport;
import bnet.library.util.CoreUtils;

//@Slf4j
public class CmmtSmsSndngDtlsDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtSmsSndngDtlsDao smsDao;

	private CmmtSmsSndngDtls sms;

	@BeforeEach
	public void init() {
		Date now = new Date();

		sms = CmmtSmsSndngDtls.builder()
				.smsId("sms-test")
				.senderMobileNo("01011112222")
				.smsCn("안녕하세요?")
				.createdDt(now)
				.build();
	}

	@Test
	public void test() {
		// insert
		smsDao.insert(sms);

		// insertList
		List<CmmtSmsSndngDtls> list = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			CmmtSmsSndngDtls sms2 = new CmmtSmsSndngDtls();
			CoreUtils.property.copyProperties(sms2, sms);
			sms2.setSmsId(CoreUtils.string.getNewId("sms-"));
			list.add(sms2);
		}
		smsDao.insertList(list);

		// select
		List<CmmtSmsSndngDtls> s1 = smsDao.selectList("20220901", "20220930", "01011112222", null);
		Assert.assertNotNull(s1);
	}
}
