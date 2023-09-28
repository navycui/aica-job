package aicluster.framework.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import aicluster.framework.common.entity.BnMember;
import bnet.library.exception.ForbiddenException;
import bnet.library.exception.UnauthorizedException;

public class SecurityUtils {
	public static final String 미로그인_역할 = "ROLE_ANONYMOUS";
	private static final List<String> ANONYMOUS_ROLES = Arrays.asList(미로그인_역할);

	private SecurityUtils() {

	}

	/**
	 * 현재 사용자 기본 정보 조회
	 *
	 * @return 회원정보
	 */
	public static BnMember getCurrentMember() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getPrincipal() == null) {
			return null;
		}

		Object user = authentication.getPrincipal();
		if (user instanceof BnMember) {
			return (BnMember)user;
		}
		return null;
	}

	/**
	 * 로그인하지 않은 경우의 사용자 정보
	 *
	 * @return 회원정보
	 */
	public static BnMember getAnonymousMember() {
		BnMember member = BnMember.builder()
				.memberNm("Anonymous")
				.gender("X")
				.roles(ANONYMOUS_ROLES)
				.enabled(false)
				.build();
		return member;
	}

	/**
	 * 로그인 여부 검사
	 * @return 회원정보
	 */
	public static BnMember checkLogin() {
		BnMember worker = getCurrentMember();
		if (worker == null) {
			throw new UnauthorizedException();
		}

		return worker;
	}

	/**
	 * 작업자가 내부사용자인지 검사
	 *
	 * @return 회원정보
	 */
	public static BnMember checkWorkerIsInsider() {
		BnMember worker = getCurrentMember();
		if (worker == null) {
			throw new UnauthorizedException();
		}

		if(!Code.memberType.isInsider(worker.getMemberType())) {
			throw new ForbiddenException();
		}

		return worker;
	}

	/**
	 * 작업자가 회원인지 검사
	 * @return 회원정보
	 */
	public static BnMember checkWorkerIsMember() {
		BnMember worker = getCurrentMember();
		if (worker == null) {
			throw new UnauthorizedException();
		}

		if(!Code.memberType.isMember(worker.getMemberType())) {
			throw new ForbiddenException();
		}

		return worker;
	}

	/**
	 * 작업자가 평가위원인지 검사
	 *
	 * @return 회원정보
	 */
	public static BnMember checkWorkerIsEvaluator() {
		BnMember worker = getCurrentMember();
		if (worker == null) {
			throw new UnauthorizedException();
		}

		if(!Code.memberType.isEvaluator(worker.getMemberType())) {
			throw new ForbiddenException();
		}

		return worker;
	}
}
