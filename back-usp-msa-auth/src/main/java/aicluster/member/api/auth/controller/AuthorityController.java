package aicluster.member.api.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.auth.dto.AuthorityRequestDto;
import aicluster.member.api.auth.service.AuthorityService;
import aicluster.member.common.entity.CmmtAuthor;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/auth")
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;

	/**
	 * 권한-역할 목록조회
	 *
	 * @return
	 */
	@GetMapping("/authorities")
	public JsonList<CmmtAuthor> getList(){
		return authorityService.getList();
	}

	/**
	 * 권한-역할 등록
	 *
	 * @param authorityRequest
	 * @return
	 */
	@PostMapping("/authorities")
	public CmmtAuthor add(@RequestBody(required = false) AuthorityRequestDto param){
		return authorityService.add(param);
	}

	/**
	 * 권한-역할 조회
	 *
	 * @param authorityId
	 * @return
	 */
	@GetMapping("/authorities/{authorityId}")
	public CmmtAuthor get(@PathVariable("authorityId") String authorityId) {
		return authorityService.get(authorityId);
	}

	/**
	 * 권한-역할 수정
	 *
	 * @param authorityId
	 * @param authorityRequest
	 * @return
	 */
	@PutMapping("/authorities/{authorityId}")
	public CmmtAuthor modify(@PathVariable("authorityId") String authorityId, @RequestBody(required = false) AuthorityRequestDto param) {
		param.setAuthorityId(authorityId);
		return authorityService.modify(param);
	}

	/**
	 * 권한-역할 삭제
	 *
	 * @param authorityId
	 */
	@DeleteMapping("/authorities/{authorityId}")
	public void remove(@PathVariable("authorityId") String authorityId) {
		authorityService.remove(authorityId);
	}

}
