package aicluster.member.api.code.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;

public class CodeServiceTest extends TestServiceSupport {
	@Autowired
	private CodeService service;

	@Test
	public void test() {

		BnMember member = SecurityUtils.getCurrentMember();
		member.setAuthorityId("ADMIN");
		System.out.println("member:" + member);

		System.out.println("\n\n\n");

		//////////////////// 코드 관리 서비스 ////////////////////
		// 코드목록 조회
		System.out.println("코드 목록 조회 결과");
		JsonList<CmmtCode> codeList = service.getList("gender");
		for (CmmtCode code : codeList.getList()) {
			System.out.println("\t\t" + code.toString());
		}
		System.out.println("\n\n\n");

		// 코드 등록
		System.out.println("코드 등록");
		CmmtCode insertCode = CmmtCode.builder()
				.codeGroup("gender")
				.code("s1")
				.codeNm("seok1")
				.remark(null)
				.codeType(null)
				.enabled(true)
				.build();
		JsonList<CmmtCode> result = service.add(insertCode);
		System.out.println("코드 등록 결과 : "+ result.toString());

		// 코드 수정
		System.out.println("코드 수정");
		CmmtCode requestCode = CmmtCode.builder()
				.codeGroup("gender")
				.code("s1")
				.codeNm("gwon3")
				.enabled(true)
				.sortOrder(4l)
				.build();
		JsonList<CmmtCode> upCode = service.modify(requestCode);
		System.out.println("코드 수정 결과 : " + upCode.toString());

		System.out.println("\n\n\n");

		//코드 삭제
		System.out.println("코드 삭제");
		service.remove("gender", "s1");

	}
}
