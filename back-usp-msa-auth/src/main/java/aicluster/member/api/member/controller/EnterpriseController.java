package aicluster.member.api.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.member.dto.EnterpriseParam;
import aicluster.member.api.member.service.EnterpriseService;
import aicluster.member.common.entity.CmmtEntrprsInfo;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseController {

	@Autowired
	private EnterpriseService service;

	/**
	 * 기업정보 등록
	 *
	 * @param enterParam
	 * @return
	 */
	@PostMapping("")
	public CmmtEntrprsInfo add(@RequestBody(required = false) EnterpriseParam enterParam) {
		return service.add(enterParam);
	}

	/**
	 * 기업정보 조회
	 *
	 * @return
	 */
	@GetMapping("")
	public CmmtEntrprsInfo get() {
		return service.get();
	}

	/**
	 * 기업정보 수정
	 *
	 * @param enterReq
	 * @return
	 */
	@PutMapping("")
	public CmmtEntrprsInfo modify(@RequestBody(required = false) EnterpriseParam enterParam) {
		return service.modify(enterParam);
	}

	/**
	 * 기업정보 삭제
	 *
	 * @param memberId
	 */
	@DeleteMapping("")
	public void remove() {
		service.remove();
	}

	/**
	 * 기업정보 사용자기준 조회
	 *
	 * @param memberId
	 * @return
	 */
	@GetMapping("/{memberId}")
	public CmmtEntrprsInfo getMemberEnt(@PathVariable String memberId) {
		return service.getMemberEnt(memberId);
	}

}
