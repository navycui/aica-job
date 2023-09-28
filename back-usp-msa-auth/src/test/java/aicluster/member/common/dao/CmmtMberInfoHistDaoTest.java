package aicluster.member.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.common.dto.MemberDto;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import aicluster.member.support.TestDaoSupport;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;

//@Slf4j
public class CmmtMberInfoHistDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtMberInfoHistDao dao;

	@Autowired
	private CmmtMberInfoDao mdao;

	@BeforeEach
	public void init() {

	}

	@Test
	public void test() {
		Date now = new Date();
		MemberGetListParam param = new MemberGetListParam();
		List<MemberDto> memberList = mdao.selectList(param, 1L, 1L);
		Assert.assertTrue(memberList.size() > 0);
		CmmtMberInfo member = mdao.select( memberList.get(0).getMemberId());
		Assert.assertNotNull(member);

		CmmtMberInfoHist hist = new CmmtMberInfoHist();
		CoreUtils.property.copyProperties(hist, member);
		hist.setHistDt(now);
		hist.setHistId(string.getNewId("hist-"));

		dao.insert(hist);

//		List<CmmtMemberHist> list = dao.selectListByMemberId(hist.getMemberId());
//		Assert.assertTrue(list.size() > 0);

	}
}
