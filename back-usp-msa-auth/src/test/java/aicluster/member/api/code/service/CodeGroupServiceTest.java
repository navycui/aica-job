package aicluster.member.api.code.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.code.dto.CodeGroupDto;
import aicluster.member.common.entity.CmmtCodeGroup;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;

public class CodeGroupServiceTest extends TestServiceSupport {
	@Autowired
	private CodeGroupService service;

	private List<CodeGroupDto> initTempCodeGroupList;

//	@Data
//	@Builder
//	private static class tempCodeGroupDto {
//		private String codeGroup;
//		private String codeGroupNm;
//		private String remark;
//		private Boolean enabled;
//	}

	public void init() {
		initTempCodeGroupList = new ArrayList<>();
		CodeGroupDto insCodeGroup;

		insCodeGroup = null;
		insCodeGroup = CodeGroupDto.builder()
									.codeGroup("TEST-GROUP01")
									.codeGroupNm("TEST-GROUP01")
									.enabled(true)
									.build();
		initTempCodeGroupList.add(insCodeGroup);

		insCodeGroup = null;
		insCodeGroup = CodeGroupDto.builder()
									.codeGroup("TEST-GROUP02")
									.codeGroupNm("TEST-GROUP02")
									.enabled(true)
									.build();
		initTempCodeGroupList.add(insCodeGroup);

		insCodeGroup = null;
		insCodeGroup = CodeGroupDto.builder()
									.codeGroup("TEST-GROUP03")
									.codeGroupNm("TEST-GROUP03")
									.enabled(true)
									.build();
		initTempCodeGroupList.add(insCodeGroup);

		System.out.println("**************************************************************\n");
		for(CodeGroupDto codeGroup : initTempCodeGroupList) {
			System.out.println("    => " + codeGroup.toString());
		}
		System.out.println("\n**************************************************************");

	}

	@Test
	public void test() {
		init();

		BnMember member = SecurityUtils.getCurrentMember();
		System.out.println("member : " + member);

		System.out.println("\n\n\n");

		//////////////////// 코드 그룹 관리 ////////////////////
		// 코드 그룹 목록 조회
		System.out.println("***** 목록 조회 결과 *****");
		JsonList<CmmtCodeGroup> codeGroupList = service.getList(null, null);
		for (CmmtCodeGroup codeGroup : codeGroupList.getList()) {
			System.out.println("\t\t" + codeGroup.toString());
		}
		System.out.println("\n\n\n");

		// 코드 그룹 등록
		System.out.println("***** 코드 그룹 등록 결과 (" + initTempCodeGroupList.size() + "건) *****");
		List<CmmtCodeGroup> insCodeGroupRst = new ArrayList<>();
		for (CodeGroupDto codeGroup : initTempCodeGroupList) {
			CmmtCodeGroup insCodeGroup = service.addGroup(codeGroup);
			insCodeGroupRst.add(insCodeGroup);
		}

		for (CmmtCodeGroup codeGroup : insCodeGroupRst) {
			System.out.println("코드 그룹\t\t" + codeGroup.toString());
		}

		// 코드 그룹 수정
		System.out.println("***** 코드 그룹 수정 결과 *****");
		CodeGroupDto updateCodeGroup = initTempCodeGroupList.get(0);
		updateCodeGroup.setEnabled(false);
		CmmtCodeGroup upCodeGroup = service.modifyGroup(updateCodeGroup);
		System.out.println("\t\t" + upCodeGroup.toString());

		System.out.println("\n\n\n");

		// 코드 그룹 삭제
		System.out.println("***** 코드 그룹 삭제 결과 *****");
		service.removeGroup(insCodeGroupRst.get(0).getCodeGroup());
		System.out.println("[" + insCodeGroupRst.get(0).getCodeGroup() + "] 삭제");

	}

}
