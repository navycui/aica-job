package aicluster.member.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.api.insider.dto.InsiderListParam;
import aicluster.member.common.dto.InsiderDto;
import aicluster.member.common.entity.CmmtEmpInfo;
import aicluster.member.common.util.CodeExt;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.pagination.CorePaginationInfo;

//@Slf4j
public class CmmtEmpInfoDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtEmpInfoDao dao;

	private CmmtEmpInfo cmmtInsider;

	@BeforeEach
	public void init() {
		Date now = new Date();

		cmmtInsider = CmmtEmpInfo.builder()
				.memberId("test-member")
				.loginId("test-login-id")
				.memberNm("테스터이름")
				.authorityId(CodeExt.authorityId.일반사용자)
				.deptNm("DEPT_0000")
				.memberSt(CodeExt.memberSt.정상)
				.memberStDt(now)
				.build();
	}

	@Test
	public void test() {
		// insert
		dao.insert(cmmtInsider);

		// select
		CmmtEmpInfo m1 = dao.select(cmmtInsider.getMemberId());
		Assert.assertNotNull(m1);
		Assert.assertEquals(m1.getMemberId(), cmmtInsider.getMemberId());

		InsiderListParam param = InsiderListParam.builder()
									.loginId(cmmtInsider.getLoginId())
									.build();

		long totalItems = dao.selectCount(param);
		Assert.assertTrue(totalItems == 1L);

		CorePaginationInfo info = new CorePaginationInfo(1L, 10L, totalItems);
		List<InsiderDto> list = dao.selectList(param, info.getBeginRowNum(), 10L);
		Assert.assertTrue(list.size() == 1);
	}
}
