package aicluster.member.api.member.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.member.dto.BlackListDto;
import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.api.member.dto.MemberParam;
import aicluster.member.api.member.service.MemberService;
import aicluster.member.common.dto.MemberDto;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

//@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {

	@Autowired
	private MemberService memberService;

	/**
	 * 회원목록 조회
	 *
	 * @param loginId
	 * @param memberNm
	 * @param memberSt
	 * @param memberType
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<MemberDto> getlist(MemberGetListParam param, CorePaginationParam pageParam) {
		return memberService.getList(param, pageParam);
	}

	/**
	 * 회원정보 조회
	 *
	 * @param memberId
	 * @return
	 */
	@GetMapping("/{memberId}")
	public CmmtMberInfo get(@PathVariable  String memberId) {
		return memberService.get(memberId);
	}

	/**
	 * 회원정보 수정
	 *
	 * @param memberId
	 * @param memberRequest
	 * @return
	 */
	@PutMapping("/{memberId}")
	public CmmtMberInfo modify(@PathVariable String memberId,@RequestBody(required = false) MemberParam param) {
		param.setMemberId(memberId);
		return memberService.modify(param);
	}

	/**
	 * 로그인 사용자 회원탈퇴
	 *
	 * @param memberId
	 */
	@PutMapping("/secession")
	public void secession() {
		memberService.secession();
	}

	/**
	 * 불량회원 등록
	 *
	 * @param memberId
	 * @param blackListDto
	 * @return
	 */
	@PutMapping("/{memberId}/black")
	public CmmtMberInfo black(@PathVariable String memberId,@RequestBody(required = false) BlackListDto blackListDto) {
		blackListDto.setMemberId(memberId);
		return memberService.black(blackListDto);
	}

	/**
	 * 불량회원 해제
	 *
	 * @param memberId
	 * @return
	 */
	@PutMapping("/{memberId}/unblack")
	public CmmtMberInfo unblack(@PathVariable String memberId) {
		return memberService.unblack(memberId);
	}

	/**
	 * 회원 처리이력목록 조회
	 *
	 * @param memberId
	 * @return
	 */
	@GetMapping("/{memberId}/hist")
	public JsonList<CmmtMberInfoHist> getHistList(@PathVariable String memberId) {
		return memberService.getHistList(memberId);

	}

	/**
	 * 회원 비밀번호 초기화
	 *
	 * @param memberId
	 */
	@PutMapping("/{memberId}/passwd-init")
	public void passwdInit(@PathVariable String memberId) {
		memberService.passwdInit(memberId);
	}
}
