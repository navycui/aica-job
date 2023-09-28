package aicluster.member.api.member.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.member.common.dto.VerifyLoginIdResultDto;
import aicluster.member.support.TestServiceSupport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinServiceTest extends TestServiceSupport {
	@Autowired
	private JoinService service;

	@Test
	void test() {
		// 로그인ID 검증
		String loginId = "sckim";
		VerifyLoginIdResultDto dupDto = service.verifyLoginId(loginId);

		assertNotNull(dupDto);
		assertTrue(dupDto.isDuplicateYn());

		log.info(String.format("로그인ID 중복 : [%s]", dupDto.toString()));

		loginId += "123";
		VerifyLoginIdResultDto sucDto = service.verifyLoginId(loginId);

		assertNotNull(sucDto);
		assertFalse(sucDto.isDuplicateYn());

		log.info(String.format("로그인ID 미중복 : [%s]", sucDto.toString()));

	}
}
