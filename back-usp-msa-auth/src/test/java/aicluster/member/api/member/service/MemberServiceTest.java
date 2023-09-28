package aicluster.member.api.member.service;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.Code;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.member.dto.BlackListDto;
import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.api.member.dto.MemberParam;
import aicluster.member.common.dto.MemberDto;
import aicluster.member.common.dto.MemberStatsDto;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

public class MemberServiceTest extends TestServiceSupport {
	@Autowired
	private MemberService service;
	private static final String memberId = "member-cf191939537f4c3f9e056c1a13cfc521";

    @BeforeAll
    void set() {
        setTokenMemberType(Code.memberType.내부사용자);
    }

	@Test
	public void test() {
		// list
		BnMember member = SecurityUtils.getCurrentMember();
		member.setAuthorityId("ADMIN");
		System.out.println("\n member : " + member);

		System.out.println("\n\n\n");

		System.out.println("회원목록 조회");
		MemberGetListParam param = new MemberGetListParam();
		CorePaginationParam pageParam = new CorePaginationParam();
		param.setLoginId(null);
		param.setMemberNm(null);
		param.setMemberSt(null);
		param.setMemberType(null);
		CorePagination<MemberDto> list = service.getList(param, pageParam);
		Assert.assertTrue(list.getList().size() >= 0);
		for (MemberDto dto : list.getList()) {
			System.out.println("\t\t" + dto.toString());
		}

		System.out.println("\n\n\n");

		//회원 정보 조회
		System.out.println("회원정보 조회");
		CmmtMberInfo getMember = service.get(memberId);
		System.out.println("회원정보 조회 결과 : " + getMember.toString());
		System.out.println("\n\n\n");

		System.out.println("회원정보 수정");
		String authorityId = "USER";
		String memberSt = "NORMAL";
		MemberParam mp = new MemberParam();
		mp.setAuthorityId(authorityId);
		mp.setMemberId(memberId);
		mp.setMemberSt(memberSt);

		CmmtMberInfo modifyMember = service.modify(mp);
		System.out.println("회원정보 수정 결과 :" + modifyMember.toString());
		System.out.println("\n\n\n");

		System.out.println("불량회원 등록");
		String[] registerReasons = {"RI01","RI02","RI03","RI04","RI05","RI06","RI07"};

		String limitBeginDt = "20210101";
		String limitEndDt = "20220113";
		String detailReason = "상세사유";
		BlackListDto blackListDto = BlackListDto.builder()
								.memberId(memberId)
								.registerReasons(registerReasons)
								.limitBeginDt(limitBeginDt)
								.limitEndDt(limitEndDt)
								.detailReason(detailReason)
								.build();

		CmmtMberInfo black = service.black(blackListDto);
		System.out.println("불량회원 등록 결과 :" + black.toString());
		System.out.println("\n\n\n");

		System.out.println("불량회원 해제");
		CmmtMberInfo unblack = service.unblack(memberId);
		System.out.println("불량회원 해제 결과 :" + unblack.toString());

//		System.out.println("회원탈퇴");
//		service.secession(memberId);
//		CmmtMember secession = service.get(memberId);
//		System.out.println("회원탈퇴 결과 : " + secession.toString());

		System.out.println("회원 처리이력목록 조회");
		JsonList<CmmtMberInfoHist> memberHistList = service.getHistList(memberId);
		System.out.println("회원 처리이력목록 조회 결과 : "+memberHistList.toString());

		System.out.println("일별 사용자현황 조회");
		MemberStatsDto memberStatsDays = service.getStatsList("DAY", null, "20220201", "20220331");
		System.out.println("일별 사용자현황 조회 결과 : " + memberStatsDays.toString());
		System.out.println("월별 사용자현황 조회");
		MemberStatsDto memberStatsMonths = service.getStatsList("MONTH", null, "20220201", "20220331");
		System.out.println("월별 사용자현황 조회 결과 : " + memberStatsMonths.toString());
	}
}
