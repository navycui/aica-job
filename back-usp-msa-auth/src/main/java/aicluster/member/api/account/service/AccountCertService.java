package aicluster.member.api.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.util.NiceIdUtils;
import aicluster.framework.common.util.SessionUtils;
import aicluster.framework.common.util.dto.NiceIdConfig;
import aicluster.framework.common.util.dto.NiceIdResult;
import aicluster.framework.common.util.dto.PkiResult;
import aicluster.member.api.member.dto.PkiCertParam;
import aicluster.member.api.module.dto.NiceIdResultParam;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.SessionAuthUtils;
import aicluster.member.common.util.dto.SessionAccountCertDto;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;

@Service
public class AccountCertService {
	@Autowired
	private CmmtMberInfoDao memberDao;

    @Autowired
    private NiceIdConfig niceIdConfig;

    @Autowired
    private NiceIdUtils niceIdUtils;

    @Autowired
    private SessionAuthUtils sessionAuthUtils;

    @Autowired
    private SessionUtils fwSessionUtils;

	/**
	 * 개인회원 계정 인증
	 *
	 * @param param: 휴대폰 본인인증 결과
	 * @return 세션ID
	 */
	public SessionKeyDto certForIndividual(NiceIdResultParam param)
	{
		NiceIdResult result = niceIdUtils.getResult(niceIdConfig, param.getEncodeData());

        // 휴대폰 본인인증 CI 값으로 회원정보를 조회한다.
        CmmtMberInfo member = memberDao.selectByCi(result.getConnInfo());
        if (member == null) {
        	throw new InvalidationException("휴대폰 본인인증 정보로 가입된 회원정보가 없습니다.");
        }
        if (!CodeExt.memberTypeExt.isIndividual(member.getMemberType())) {
        	throw new InvalidationException("개인회원이 아닙니다. 공동인증서로 인증하세요.");
        }
        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.잠김) && !string.equals(member.getMemberSt(), CodeExt.memberSt.휴면)) {
        	throw new InvalidationException("계정 인증에 해당하는 회원상태가 아닙니다. 로그인 ID를 다시 확인하세요.");
        }

        SessionAccountCertDto certDto = SessionAccountCertDto.builder()
        										.isIndividual(true)
        										.memberId(member.getMemberId())
        										.loginId(member.getLoginId())
        										.ci(result.getConnInfo())
        										.mobileNo(result.getMobileNo())
        										.build();

        String sessionId = sessionAuthUtils.accountSessId.set(certDto);

        return new SessionKeyDto(sessionId);
    }

	/**
	 * 사업자회원 계정 인증
	 *
	 * @param param: 공동인증서 인증결과 세션ID
	 * @return 세션ID
	 */
	public SessionKeyDto certForBzmn(PkiCertParam param)
	{
		// 공동인증서 인증결과 정보를 세션 테이블로부터 조회
		PkiResult result = fwSessionUtils.pkiCertSession.check(param.getPkiCertSessionId());

        // 공동인증서 인증 CI 값으로 회원정보를 조회한다.
        CmmtMberInfo member = memberDao.selectByCi(result.getIdentifyData());
        if (member == null) {
        	throw new InvalidationException("공동인증서 인증 정보로 가입된 회원정보가 없습니다.");
        }
        if (!CodeExt.memberTypeExt.isBzmn(member.getMemberType())) {
        	throw new InvalidationException("사업자회원이 아닙니다. 휴대폰 본인인증으로 인증하세요.");
        }
        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.잠김) && !string.equals(member.getMemberSt(), CodeExt.memberSt.휴면)) {
        	throw new InvalidationException("계정 인증에 해당하는 회원상태가 아닙니다. 로그인 ID를 다시 확인하세요.");
        }

        SessionAccountCertDto certDto = SessionAccountCertDto.builder()
				.isIndividual(false)
				.memberId(member.getMemberId())
				.loginId(member.getLoginId())
				.ci(result.getIdentifyData())
				.bizrno(result.getBizrno())
				.build();

        String sessionId = sessionAuthUtils.accountSessId.set(certDto);

        return new SessionKeyDto(sessionId);
	}
}
