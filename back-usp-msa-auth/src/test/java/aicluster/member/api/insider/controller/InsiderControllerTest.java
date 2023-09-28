package aicluster.member.api.insider.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;

import aicluster.member.support.TestControllerSupport;

public class InsiderControllerTest extends TestControllerSupport {

	@Test
	public void test() throws Exception {
		/*
		 * 내부자 목록 조회
		 */
		this.mvc.perform(getMethodBuilder("/member/api/insiders")).andDo(print());

		/*
		 * 내부자 등록
		 */


		/*
		 * 내부자 정보 조회
		 */


		/*
		 * 내부자 정보 수정
		 */


	}

}
