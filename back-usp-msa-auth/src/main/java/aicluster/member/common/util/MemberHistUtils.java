package aicluster.member.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.member.common.dao.CmmtAuthorDao;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtMberInfoHistDao;
import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;

@Component
public class MemberHistUtils {
	@Autowired
	private CmmtMberInfoHistDao cmmtMemberHistDao;

	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	@Autowired
	private CmmtAuthorDao cmmtAuthorityDao;

	/**
	 * 회원가입 이력 추가
	 *
	 * @param member
	 */
	public void addJoinHist(String memberId) {
		Date now = new Date();

		if (string.isBlank(memberId)) {
			throw new InvalidationException("회원ID를 입력하세요.");
		}

		CmmtMberInfoHist hist = CmmtMberInfoHist.builder()
				.histId(string.getNewId("hist-"))
				.histDt(now)
				.workTypeNm("회원가입")
				.memberId(memberId)
				.workerId(memberId)
				.workCn("회원가입")
				.build();

		cmmtMemberHistDao.insert(hist);
	}

	/**
	 * 회웥탈퇴 이력 추가
	 *
	 * @param workerId
	 * @param member
	 */
	public void addSecessionHist(String memberId) {
		Date now = new Date();

		if (string.isBlank(memberId)) {
			throw new InvalidationException("회원ID를 입력하세요.");
		}

		CmmtMberInfoHist hist = CmmtMberInfoHist.builder()
				.histId(string.getNewId("hist-"))
				.histDt(now)
				.workTypeNm("회원탈퇴")
				.memberId(memberId)
				.workerId(memberId)
				.workCn("탈퇴 상태로 전환되었습니다.")
				.build();

		cmmtMemberHistDao.insert(hist);
	}

	/**
	 * 회원정보 변경 이력 추가
	 *
	 * @param workerId
	 * @param oldMember
	 * @param newMember
	 */
	public void addChangeHist(String workerId, CmmtMberInfo oldMember, CmmtMberInfo newMember) {
		List<CmmtMberInfoHist> list = new ArrayList<>();
		String workCn = null;
		CmmtMberInfoHist cmmtMemberHist = null;
		Date now = new Date();

		/*
		 * 입력검사
		 */
		if (oldMember == null || newMember == null) {
			throw new InvalidationException("회원정보를 입력하세요.");
		}

		if (string.isBlank(workerId)) {
			throw new InvalidationException("작업자ID를 입력하세요.");
		}

		// 회원구분변경
		if (!string.equals(oldMember.getMemberType(), newMember.getMemberType())) {
			String codeNm1 = "없음";
			String codeNm2 = "없음";
			CmmtCode code1 = cmmtCodeDao.select(CodeExt.memberType.CODE_GROUP, oldMember.getMemberType());
			CmmtCode code2 = cmmtCodeDao.select(CodeExt.memberType.CODE_GROUP, newMember.getMemberType());
			if (code1 != null) {
				codeNm1 = code1.getCodeNm();
			}
			if (code2 != null) {
				codeNm2 = code2.getCodeNm();
			}
			workCn = String.format("회원구분이 변경되었습니다(%s > %s)", codeNm1, codeNm2);
			cmmtMemberHist = CmmtMberInfoHist.builder()
					.histId(string.getNewId("hist-"))
					.histDt(now)
					.workTypeNm("회원구분변경")
					.memberId(newMember.getMemberId())
					.workerId(workerId)
					.workCn(workCn)
					.build();
			list.add(cmmtMemberHist);
		}

		// 회원상태변경
		if (!string.equals(oldMember.getMemberSt(), newMember.getMemberSt())) {
			String codeNm1 = "없음";
			String codeNm2 = "없음";
			CmmtCode code1 = cmmtCodeDao.select(CodeExt.memberSt.CODE_GROUP, oldMember.getMemberSt());
			CmmtCode code2 = cmmtCodeDao.select(CodeExt.memberSt.CODE_GROUP, newMember.getMemberSt());
			if (code1 != null) {
				codeNm1 = code1.getCodeNm();
			}
			if (code2 != null) {
				codeNm2 = code2.getCodeNm();
			}
			workCn = String.format("회원상태가 변경되었습니다(%s > %s)", codeNm1, codeNm2);
			cmmtMemberHist = CmmtMberInfoHist.builder()
					.histId(string.getNewId("hist-"))
					.histDt(now)
					.workTypeNm("회원상태변경")
					.memberId(newMember.getMemberId())
					.workerId(workerId)
					.workCn(workCn)
					.build();
			list.add(cmmtMemberHist);
		}

		// 권한변경
		if (!string.equals(oldMember.getAuthorityId(), newMember.getAuthorityId())) {
			String authorityNm1 = "권한없음";
			String authorityNm2 = "권한없음";
			CmmtAuthor authority1 = cmmtAuthorityDao.select(oldMember.getAuthorityId());
			CmmtAuthor authority2 = cmmtAuthorityDao.select(newMember.getAuthorityId());
			if (authority1 != null) {
				authorityNm1 = authority1.getAuthorityNm();
			}
			if (authority2 != null) {
				authorityNm2 = authority2.getAuthorityNm();
			}

			workCn = String.format("권한이 변경되었습니다(%s > %s)", authorityNm1, authorityNm2);
			cmmtMemberHist = CmmtMberInfoHist.builder()
					.histId(string.getNewId("hist-"))
					.histDt(now)
					.workTypeNm("권한변경")
					.memberId(newMember.getMemberId())
					.workerId(workerId)
					.workCn(workCn)
					.build();
			list.add(cmmtMemberHist);
		}

		// 강사여부변경
		if (oldMember.getInstr() != newMember.getInstr()) {
			String instrNm1 = "설정없음";
			String instrNm2 = "설정없음";
			if (oldMember.getInstr() != null) {
				instrNm1 = (oldMember.getInstr()) ? "강사":"강사아님";
			}
			if (newMember.getInstr() != null) {
				instrNm2 = (newMember.getInstr()) ? "강사":"강사아님";
			}
			workCn = String.format("강사여부가 변경되었습니다(%s > %s)", instrNm1, instrNm2);
			cmmtMemberHist = CmmtMberInfoHist.builder()
					.histId(string.getNewId("hist-"))
					.histDt(now)
					.workTypeNm("강사여부변경")
					.memberId(newMember.getMemberId())
					.workerId(workerId)
					.workCn(workCn)
					.build();
			list.add(cmmtMemberHist);
		}

		if (list.size() > 0) {
			cmmtMemberHistDao.insertList(list);
		}
	}

	public void addPasswdInitHist(String workerId, String memberId) {
		if (string.isBlank(workerId)) {
			throw new InvalidationException("작업자ID를 입력하세요.");
		}
		if (string.isBlank(memberId)) {
			throw new InvalidationException("회원ID를 입력하세요.");
		}

		Date now = new Date();
		String workCn = "비밀번호가 초기화되었습니다.";
		CmmtMberInfoHist cmmtMemberHist = CmmtMberInfoHist.builder()
				.histId(string.getNewId("hist-"))
				.histDt(now)
				.workTypeNm("비밀번호 초기화")
				.memberId(memberId)
				.workerId(workerId)
				.workCn(workCn)
				.build();
		cmmtMemberHistDao.insert(cmmtMemberHist);
	}
}
