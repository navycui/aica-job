package aicluster.batch.api.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	private MemberService service;

	@GetMapping("/stats")
	public void stats() {
		int cnt = service.sendEmailToDormantMember();
		log.info("휴면회원전환 이메일발송:" + cnt);
	}

	@PutMapping("/black")
	public void black() {
		int cnt = service.updateBlackMemberToNormal();
		log.info("불량회원복권:" + cnt);
	}


	@DeleteMapping("/secession")
	public void secession() {
		int cnt = service.deleteSecessionMember();
		log.info("7일 경과 탈퇴회원삭제:" + cnt);
	}
}
