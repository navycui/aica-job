package aicluster.member.api.account.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.member.api.account.dto.AccountParam;
import aicluster.member.api.account.dto.AccountUnlockParam;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.dao.CmmtMberInfoHistDao;
import aicluster.member.common.dao.CmmtPasswordHistDao;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import aicluster.member.common.entity.CmmtPasswordHist;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.SessionAuthUtils;
import aicluster.member.common.util.dto.SessionAccountCertDto;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.password;
import bnet.library.util.CoreUtils.string;

@Service
public class AccountService {

	@Autowired
	private CmmtMberInfoDao memberDao;

	@Autowired
	private CmmtMberInfoHistDao memberHistDao;

	@Autowired
	private CmmtPasswordHistDao cmmtPasswdHistDao;

    @Autowired
    private SessionAuthUtils sessionAuthUtils;

    private CmmtMberInfo getMember(SessionAccountCertDto certDto)
    {
        // 인증 CI 값으로 회원정보를 조회한다.
        CmmtMberInfo memberForCi = memberDao.selectByCi(certDto.getCi());
        if (memberForCi == null) {
        	throw new InvalidationException("인증 정보로 가입된 회원정보가 없습니다.");
        }

        // 회원ID로 회원정보를 조회한다.
        CmmtMberInfo member = memberDao.select(certDto.getMemberId());
        if (member == null) {
        	throw new InvalidationException("가입된 회원정보가 없습니다.");
        }

        if (!string.equals(memberForCi.getMemberId(), member.getMemberId())) {
        	throw new InvalidationException("인증된 회원정보와 조회된 회원정보가 다릅니다.");
        }

        return member;
    }

	/**
	 * 로그인 계정 휴면해제
	 *
	 * @param param
	 */
	public void undomant(AccountParam param)
	{
		Date now = new Date();

		if (param == null || string.isBlank(param.getSessionId())) {
			throw new InvalidationException("인증을 수행하세요.");
		}

		// 세션 테이블로부터 계정 인증 정보를 조회한다.
        SessionAccountCertDto certDto = sessionAuthUtils.accountSessId.get(param.getSessionId());

		// 회원정보를 가져온다.
        CmmtMberInfo member = getMember(certDto);

        // 현재 상태가 '휴면'인지 검증한다.
        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.휴면)) {
        	throw new InvalidationException("휴면 계정인 경우에만 해제할 수 있습니다.");
        }

        // 개인회원인 경우 휴대폰 번호 변경 처리
        if (certDto.isIndividual()) {
            // 회원정보의 휴대폰번호와 본인인증 휴대폰번호가 다른 경우 휴대폰 번호를 변경한다.
            if (!string.equals(member.getMobileNo(), certDto.getMobileNo())) {
            	member.setEncMobileNo(aes256.encrypt(certDto.getMobileNo(), member.getMemberId()));
            }
        }

        // 회원상태 변경
        member.setMemberSt(CodeExt.memberSt.정상);
        member.setMemberStDt(now);
        member.setUpdaterId(member.getMemberId());
        member.setUpdatedDt(now);

        // 회원정보 Update
        memberDao.update(member);

        // 회원이력정보 생성
        CmmtMberInfoHist hist = CmmtMberInfoHist.builder()
        							.histId(string.getNewId(CodeExt.prefix.이력ID))
        							.histDt(now)
        							.workTypeNm("휴면계정해제")
        							.memberId(member.getMemberId())
        							.workerId(member.getMemberId())
        							.workCn("휴면 상태인 계정을 해제하였습니다.")
        							.build();
        // 회원이력정보 insert
        memberHistDao.insert(hist);
	}

	/**
	 * 로그인 계정 잠금해제
	 *
	 * @param param
	 */
	public void unlock(AccountUnlockParam param)
	{
		Date now = new Date();
		if (param == null || string.isBlank(param.getSessionId())) {
			throw new InvalidationException("인증을 수행하세요.");
		}
		if (string.isBlank(param.getPasswd1()) || string.isBlank(param.getPasswd2())) {
			throw new InvalidationException("비밀번호를 모두 입력하세요.");
		}

		// 세션 테이블로부터 계정 인증 정보를 조회한다.
        SessionAccountCertDto certDto = sessionAuthUtils.accountSessId.get(param.getSessionId());

		// 회원정보를 가져온다.
        CmmtMberInfo member = getMember(certDto);

        // 현재 상태가 '잠김'인지 검증한다.
        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.잠김)) {
        	throw new InvalidationException("잠긴 계정인 경우에만 해제할 수 있습니다.");
        }

        if (!string.equals(param.getPasswd1(), param.getPasswd2())) {
        	throw new InvalidationException("비밀번호 2개가 일치하지 않습니다. 다시 입력하세요.");
        }

		// 비밀번호 규칙 확인
		List<CmmtPasswordHist> passwdHistList = cmmtPasswdHistDao.selectList_recent(member.getMemberId(), 3);
		CodeExt.passwd.checkValidation(param.getPasswd1(), member.getBirthday(), passwdHistList);

        // 회원상태 변경
		String encPasswd = password.encode(param.getPasswd1());
        member.setMemberSt(CodeExt.memberSt.정상);
        member.setMemberStDt(now);
		member.setPasswdInit(false);
		member.setEncPasswd(encPasswd);
		member.setPasswdDt(now);
        member.setUpdaterId(member.getMemberId());
        member.setUpdatedDt(now);

        // 회원정보 Update
        memberDao.update(member);

        // 회원이력정보 생성
        CmmtMberInfoHist hist = CmmtMberInfoHist.builder()
        							.histId(string.getNewId(CodeExt.prefix.이력ID))
        							.histDt(now)
        							.workTypeNm("잠김계정해제")
        							.memberId(member.getMemberId())
        							.workerId(member.getMemberId())
        							.workCn("잠김 상태인 계정을 해제하였습니다.")
        							.build();
        // 회원이력정보 insert
        memberHistDao.insert(hist);

		/*
		 * 비밀번호 이력 추가
		 */
		CmmtPasswordHist pwdHist = CmmtPasswordHist.builder()
									.memberId(member.getMemberId())
									.histDt(now)
									.encPasswd(encPasswd)
									.build();
		cmmtPasswdHistDao.insert(pwdHist);
	}

}
