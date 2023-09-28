package aicluster.member.api.insider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.insider.dto.InsiderListParam;
import aicluster.member.api.insider.dto.InsiderMemberDto;
import aicluster.member.api.insider.dto.SrchPicItemDto;
import aicluster.member.api.insider.dto.SrchPicParam;
import aicluster.member.api.insider.service.InsiderService;
import aicluster.member.common.dto.InsiderDto;
import aicluster.member.common.dto.InsiderHistDto;
import aicluster.member.common.dto.VerifyLoginIdResultDto;
import aicluster.member.common.entity.CmmtEmpInfo;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/insiders")
public class InsiderController {

	@Autowired
	private InsiderService insiderService;

	/**
	 * 내부자목록 조회
	 *
	 * @param loginId
	 * @param memberNm
	 * @param memberSt
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<InsiderDto> getList(InsiderListParam param, CorePaginationParam pageParam){
		return insiderService.getList(param, pageParam);
	}

	/**
	 * 내부자 등록
	 *
	 * @param dto
	 * @return
	 */
	@PostMapping("")
	public CmmtEmpInfo add(@RequestBody(required = false) InsiderMemberDto dto) {
		CmmtEmpInfo cmmtInsider = insiderService.addInsider(dto);
		return cmmtInsider;
	}

	/**
	 * 내부자 정보 조회
	 *
	 * @param memberId
	 * @return
	 */
	@GetMapping("/{memberId}")
	public CmmtEmpInfo get(@PathVariable String memberId) {
		CmmtEmpInfo cmmtInsider = insiderService.getInsider(memberId);
		return cmmtInsider;
	}

	/**
	 * 내부자 수정
	 *
	 * @param memberId
	 * @param dto
	 * @return
	 */
	@PutMapping("/{memberId}")
	public CmmtEmpInfo modify(@PathVariable String memberId, @RequestBody(required = false) InsiderMemberDto dto) {
		dto.setMemberId(memberId);
		CmmtEmpInfo cmmtInsider = insiderService.modifyInsider(dto);
		return cmmtInsider;
	}

	/**
	 * 내부사용자 처리이력 목록 조회
	 *
	 * @param memberId
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("/{memberId}/hist")
	public JsonList<InsiderHistDto> getHistList(@PathVariable String memberId) {
		return insiderService.getHistList(memberId);
	}

	/**
	 * 내부사용자 부서/담당자 목록 조회
	 *
	 * @param deptNm
	 * @param memberNm
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("/pic")
	public CorePagination<SrchPicItemDto> getSrchPic(SrchPicParam param, CorePaginationParam pageParam) {
		return insiderService.getSrchPic(param, pageParam);
	}

	/**
	 * 비밀번호 초기화
	 *
	 * @param memberId	회원ID
	 */
	@PutMapping("/{memberId}/passwd-init")
	public void passwdInit(@PathVariable String memberId) {
		insiderService.passwdInit(memberId);
	}


	/**
	 * 로그인 ID 중복 검증
	 *
	 * @param loginId	로그인ID
	 * @return 로그인ID중복검증 결과
	 */
	@PostMapping("/verify/login-id")
	public VerifyLoginIdResultDto verifyLoginId(String loginId) {
		return insiderService.verifyLoginId(loginId);
	}
}
