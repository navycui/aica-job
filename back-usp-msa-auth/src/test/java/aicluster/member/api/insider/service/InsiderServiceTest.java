package aicluster.member.api.insider.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.Code;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.insider.dto.InsiderListParam;
import aicluster.member.api.insider.dto.InsiderMemberDto;
import aicluster.member.common.dto.InsiderDto;
import aicluster.member.common.dto.VerifyLoginIdResultDto;
import aicluster.member.common.entity.CmmtEmpInfo;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

public class InsiderServiceTest extends TestServiceSupport {
	@Autowired
	private InsiderService service;

//	private static final String SYSTEM_ID = "PORTAL_INSIDER";
	private List<InsiderMemberDto> initTempInsiderList;

//	@Data
//	@Builder
//	private static class tempInsiderDto {
//		private String memberId;
//		private String loginId;
//		private String memberNm;
//		private String deptNm;
//		private String positionNm;
//		private String authorityId;
//		private String memberSt;
//		private String mobileNo;
//		private String email;
//		private String memberIps;
//	}

	@BeforeAll
	public void set() {
		setTokenMemberType(Code.memberType.내부사용자);
	}

	public void init() {
		initTempInsiderList = new ArrayList<>();
		InsiderMemberDto insInsider;

		insInsider = null;
		insInsider = InsiderMemberDto.builder()
				.memberId("insider-ID1")
				.loginId("tester11")
				.memberNm("tester11")
				.authorityId("USER")
				.memberSt("NORMAL")
				.telNo("061-1010-1010")
				.mobileNo("010-1010-1010")
				.email("tester11@test.com")
				.memberIps("127.0.0.1")
				.deptNm("DEPT_0101")
				.build();
		initTempInsiderList.add(insInsider);

		insInsider = null;
		insInsider = InsiderMemberDto.builder()
				.memberId("insider-ID2")
				.loginId("tester22")
				.memberNm("tester22")
				.authorityId("USER")
				.memberSt("NORMAL")
				.telNo("061-2020-2020")
				.mobileNo("010-2020-2020")
				.email("tester22@test.com")
				.memberIps("127.0.0.2")
				.deptNm("DEPT_0104")
				.build();
		initTempInsiderList.add(insInsider);

		System.out.println("**************************************************************\n");
		for(InsiderMemberDto insider : initTempInsiderList) {
			System.out.println("    => " + insider.toString());
		}
		System.out.println("\n**************************************************************");

	}

	@Test
	public void test() {
		init();

		BnMember member = SecurityUtils.getCurrentMember();
		System.out.println("\n member : " + member);

		System.out.println("\n\n\n");

		//////////////////////////// 내부자 관리 서비스////////////////////////////
		// 내부자 목록 조회
		System.out.println("***** 목록 조회 결과 *****");
		InsiderListParam param = new InsiderListParam();
		CorePagination<InsiderDto> insiderList = service.getList(param, new CorePaginationParam());

		for (InsiderDto insider : insiderList.getList()) {
			System.out.println("\t\t" + insider);
		}

		System.out.println("\n\n\n");

		// 내부자 로그인ID 중복 확인
		System.out.println("***** 내부자 로그인ID 중복 확인 - 중복 *****");
		VerifyLoginIdResultDto duplicateDto1 = service.verifyLoginId("sckim");
		System.out.println("\t\t" + duplicateDto1.toString());

		System.out.println("***** 내부자 로그인ID 중복 확인 - 미중복 *****");
		VerifyLoginIdResultDto duplicateDto2 = service.verifyLoginId("sckim11");
		System.out.println("\t\t" + duplicateDto2.toString());

		System.out.println("\n\n\n");

		// 내부자 정보 등록
		System.out.println("***** 내부자 등록 결과 (" + initTempInsiderList.size() + "건) *****");
		List<CmmtEmpInfo> insInsiderRst = new ArrayList<>();
		for (InsiderMemberDto insider : initTempInsiderList) {
			CmmtEmpInfo insInsider = service.addInsider(insider);
			insInsiderRst.add(insInsider);
		}

		for (CmmtEmpInfo insider : insInsiderRst) {
			System.out.println("내부자\t\t" + insider.toString());
		}

		// 내부자 정보 조회
		System.out.println("***** 정보 조회 결과 *****");
		CmmtEmpInfo selInsider = service.getInsider(insInsiderRst.get(0).getMemberId());
		System.out.println("내부자 조회 결과 : " + selInsider.toString());

		System.out.println("\n\n\n");

		// 내부자 정보 수정
		System.out.println("***** 내부자 정보 수정 결과 *****");
		InsiderMemberDto updateInsider = initTempInsiderList.get(0);
		updateInsider.setAuthorityId("admin");
		updateInsider.setMemberIps(null);
		CmmtEmpInfo uptInsider = service.modifyInsider(updateInsider);
		System.out.println("\t\t" + uptInsider.toString());

		System.out.println("\n\n\n");

	}

}
