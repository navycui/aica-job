package aicluster.member.api.auth.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.auth.dto.ProgramDto;
import aicluster.member.common.entity.CmmtProgrm;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;

public class ProgramServiceTest extends TestServiceSupport {
	@Autowired
	private ProgramService service;

	private List<ProgramDto> initProgramList;

//	@Data
//	@Builder
//	private static class tempProgramDto {
//		private String programId;
//		private String programNm;
//		private String httpMethod;
//		private String urlPattern;
//		private Long checkOrder;
//		private List<String> roles;
//	}

	public void init() {
		initProgramList = new ArrayList<>();
		ProgramDto insProgram;

		String[] roles = {"ROLE_USER", "ROLE_ADMIN"};

		insProgram = null;
		insProgram = ProgramDto.builder()
				.programNm("program-01")
				.httpMethod("GET")
				.urlPattern("/member/**")
				.checkOrder(1L)
				.roles(roles)
				.build();
		initProgramList.add(insProgram);

		insProgram = null;
		insProgram = ProgramDto.builder()
				.programNm("program-02")
				.httpMethod("POST")
				.urlPattern("/member/**/*")
				.checkOrder(1L)
				.roles(roles)
				.build();
		initProgramList.add(insProgram);

		System.out.println("**************************************************************\n");
		for(ProgramDto program : initProgramList) {
			System.out.println("    => " + program.toString());
		}
		System.out.println("\n**************************************************************");
	}

	@Test
	public void test() {
		init();

		BnMember member = SecurityUtils.getCurrentMember();
		System.out.println("member : " + member);

		System.out.println("\n\n\n");

		//////////////////// 프로그램-역할 관리 ////////////////////
		// 프로그램-역할 목록 조회
		System.out.println("***** 목록 조회 결과 *****");
		JsonList<CmmtProgrm> programList = service.getList("portal", null, null);
		for (CmmtProgrm program : programList.getList()) {
			System.out.println("\t\t" + program.toString());
		}
		System.out.println("\n\n\n");

		// 프로그램-역할 등록
		System.out.println("***** 프로그램-역할 등록 결과 (" + initProgramList.size() + "건) *****");
		List<CmmtProgrm> insProgramRst = new ArrayList<>();
		for (ProgramDto program : initProgramList) {
			CmmtProgrm insProgram = service.addProgram(program);
			insProgramRst.add(insProgram);
		}

		for (CmmtProgrm program : insProgramRst) {
			System.out.println("프로그램\t\t" + program.toString());
		}

		System.out.println("\n\n\n");

		// 프로그램-역할 정보 조회
		CmmtProgrm selProgram = service.getProgram(insProgramRst.get(0).getProgramId());
		System.out.println("프로그램-역할 조회 결과 : " + selProgram.toString());

		System.out.println("\n\n\n");

		// 프로그램-역할 수정
		System.out.println("***** 프로그램-역할 수정 결과 *****");
		String[] roles = {"ROLE_USER"};
		ProgramDto updateProgram = initProgramList.get(0);
		updateProgram.setRoles(roles);
		CmmtProgrm upProgram = service.modifyProgram(updateProgram);
		System.out.println("\t\t" + upProgram.toString());

		System.out.println("\n\n\n");

		// 프로그램-역할 삭제
		System.out.println("***** 프로그램-역할 삭제 결과 *****");

		service.removeProgram(insProgramRst.get(0).getProgramId());
		System.out.println("\n\n");
		System.out.println("[" + insProgramRst.get(0).getProgramId() + "] 삭제");

	}

	@Test
	void programUrlTest() {
		try {
			service.initData();
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

}
