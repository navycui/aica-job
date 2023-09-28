package aicluster.member.api.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.entity.CmmtRole;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;

class RoleServiceTest extends TestServiceSupport {
	@Autowired
	RoleService roleService;

	@Test
	void test()
	{
		String roleId = "ROLE_TEST";
		String roleNm = "테스트";

		// 역할 목록 조회
		JsonList<CmmtRole> list = roleService.getList();
		System.out.println("\n\n\n");
		System.out.println("목록 조회 결과");
		for (CmmtRole cmmtRole : list.getList()) {
			System.out.println("▷▷▷" + cmmtRole.toString());
		}
		System.out.println("\n\n\n");

		// 역할 추가
		CmmtRole insCmmtRole = roleService.insert(roleId, roleNm);
		System.out.println("\n\n\n");
		System.out.println("역할 입력 결과 : " + insCmmtRole.toString());
		System.out.println("\n\n\n");

		// 역할 조회
		CmmtRole getCmmtRole = roleService.select(roleId);
		System.out.println("\n\n\n");
		System.out.println("역할 조회 결과 : " + getCmmtRole.toString());
		System.out.println("\n\n\n");

		// 역할 수정
		CmmtRole uptCmmtRole = roleService.modify(roleId, roleNm+"123");
		System.out.println("\n\n\n");
		System.out.println("역할 수정 결과 : " + uptCmmtRole.toString());
		System.out.println("\n\n\n");

		// 역할 삭제
		roleService.delete(roleId);
	}

}
