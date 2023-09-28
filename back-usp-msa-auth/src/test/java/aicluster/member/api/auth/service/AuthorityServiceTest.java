package aicluster.member.api.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.auth.dto.AuthorityRequestDto;
import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.support.TestServiceSupport;
import bnet.library.util.dto.JsonList;

public class AuthorityServiceTest extends TestServiceSupport{
	@Autowired
	private AuthorityService service;

	@Test
	public void test() {
		BnMember member = SecurityUtils.getCurrentMember();
		member.setAuthorityId("ADMIN");
		System.out.println("member:" + member);
		System.out.println("\n\n\n");

		System.out.println("권한-역할 목록조회");
		JsonList<CmmtAuthor> cmmtAuthorityList = service.getList();
		for (CmmtAuthor dto : cmmtAuthorityList.getList()) {
			System.out.println("\t\t" + dto);
		}
		System.out.println("\n\n\n");

		System.out.println("권한-역할 등록");
		String[] roleId = {"ROLE_ADMIN","ROLE_USER"};

		AuthorityRequestDto dto = new AuthorityRequestDto();
		dto.setAuthorityId("custom1");
		dto.setAuthorityNm("고객1");
		dto.setRoleId(roleId);
		CmmtAuthor addAuthority = service.add(dto);
		System.out.println("권한-역할 등록 결과" + addAuthority.toString());
		System.out.println("\n\n\n");

		System.out.println("권한-역할 조회");
		CmmtAuthor getAuthority = service.get("USER");
		System.out.println("권한-역할 조회 결과" + getAuthority.toString());
		System.out.println("\n\n\n");

		System.out.println("권한-역할 수정");
		String[] modifyRoleId = {"ROLE_ADMIN","ROLE_USER","ROLE_ANONYMOUS"};
		dto = new AuthorityRequestDto();
		dto.setAuthorityId("USER");
		dto.setAuthorityNm("일반사용자");
		dto.setRoleId(modifyRoleId);
		CmmtAuthor  modifyAuthority = service.modify(dto);
		System.out.println("권한-역할 수정 결과" + modifyAuthority.toString());
		System.out.println("\n\n\n");


	}
}
