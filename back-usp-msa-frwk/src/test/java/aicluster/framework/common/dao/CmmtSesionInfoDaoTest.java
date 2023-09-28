package aicluster.framework.common.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.CmmtSesionInfo;
import aicluster.framework.support.TestDaoSupport;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Slf4j
public class CmmtSesionInfoDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtSesionInfoDao dao;

	@BeforeEach
	public void init() {
//		Date now = new Date();
	}

	@Data
	@NoArgsConstructor
	public static class TestClass {
		private String username;
		private Integer age;
	}
	@Test
	public void test() {
		String sessionId = string.getNewId("ss_");
		TestClass obj1 = new TestClass();
		obj1.setUsername("유영민");
		obj1.setAge(50);
		String objStr = json.toString(obj1);

		// 생성
		Date now = new Date();
		Date expiredDt = date.addSeconds(now, 2);
		CmmtSesionInfo session1 = CmmtSesionInfo.builder()
				.sessionId(sessionId)
				.sessionValue(objStr)
				.expiredDt(expiredDt)
				.createdDt(now)
				.build();

		dao.insert(session1);

		// 조회
		CmmtSesionInfo session2 = dao.select(sessionId);
		Assert.assertNotNull(session2);
		Assert.assertEquals(session1.getSessionValue(), session2.getSessionValue());
		TestClass obj2 = session2.getSessionJsonValue(TestClass.class);
		Assert.assertEquals(obj1.getUsername(), obj2.getUsername());
		Assert.assertEquals(obj1.getAge(), obj2.getAge());

		// 11초 후
		try {
			Thread.sleep(1000L * 2);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		CmmtSesionInfo session3 = dao.select(sessionId);
		Assert.assertNull(session3);

		// 삭제
		dao.deleteExpired();

		// 세션 추가 및 삭제
		sessionId = "--test-session-id";
		CmmtSesionInfo session4 = CmmtSesionInfo.builder()
				.sessionId(sessionId)
				.sessionValue(sessionId)
				.expiredDt(date.addMinutes(now, 30))
				.createdDt(now)
				.build();
		dao.insert(session4);
		CmmtSesionInfo session5 = dao.select(sessionId);
		Assert.assertNotNull(session5);
		dao.delete(sessionId);
		session5 = dao.select(sessionId);
		Assert.assertNull(session5);
	}
}
