package aicluster.member.config.bruteforce.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.CmmtSesionInfoDao;
import aicluster.framework.common.entity.CmmtSesionInfo;
import aicluster.member.config.bruteforce.vo.LoginAttemptVo;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.json;

@Component
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 5; // 최대 시도 회수
    public static final int BLOCK_MINS = 30; // 로그인 연속 실패시, 잠금 시간(분)
    public static final int RESET_MINS = 10; // 잠금상태가 아닌 경우, 10분이 경과하면, 시도횟수를 0으로 초기화한다.
    public static final String SESSION_KEY = "-LOGIN-ATTEMPTS";

    @Autowired
    CmmtSesionInfoDao cmmtSesionInfoDao;

    @Autowired
    HttpServletRequest request;

    /**
     * 로그인시도 세션 조회
     * @param ip
     * @return
     */
    private LoginAttemptVo getSession(String ip) {
    	String sessionId = SESSION_KEY + ":" + ip;
    	CmmtSesionInfo session = cmmtSesionInfoDao.selectById(sessionId);
		if (session == null) {
			return null;
		}

		return session.getSessionJsonValue(LoginAttemptVo.class);
    }

    /**
     * 로그인시도 세션 저장
     * @param vo
     */
    private void setSession(LoginAttemptVo vo) {
    	Date now = new Date();
    	Date expiredDt = date.addMinutes(now, BLOCK_MINS * 2);

    	String sessionId = SESSION_KEY + ":" + vo.getIp();
    	String value = json.toString(vo);

    	CmmtSesionInfo oldSession = cmmtSesionInfoDao.selectById(sessionId);
    	CmmtSesionInfo newSession = CmmtSesionInfo.builder()
    				.sessionId(sessionId)
    				.sessionValue(value)
    				.expiredDt(expiredDt)
    				.createdDt(now)
    				.build();

    	if (oldSession == null) {
    		cmmtSesionInfoDao.insert(newSession);
    	}
    	else {
    		cmmtSesionInfoDao.update(newSession);
    	}
    }

    /**
     * 로그인 시도 세션 삭제
     * @param ip
     */
    private void removeSession(String ip) {
    	String sessionId = SESSION_KEY + ":" + ip;
    	cmmtSesionInfoDao.delete(sessionId);
    }

    /**
     * 로그인시도 이력 조회
     * @return
     */
    public LoginAttemptVo getLoginHist() {
    	String ip = CoreUtils.webutils.getRemoteIp(request);
    	LoginAttemptVo vo = getSession(ip);

    	// 잠금상태가 아니며, 마지막 로그인 시간으로부터 RESET_MINS가 지났다면, 초기화 한다.
    	if (vo != null && !vo.isLocked() && vo.getLoginTryCnt() > 0) {
    		Date now = new Date();
    		Date dt = CoreUtils.date.addMinutes(vo.getLoginTryDt(), RESET_MINS);
    		if (now.compareTo(dt) >= 0) {
    			this.resetLoginHist();
    			vo = null;
    		}
		}

    	if (vo == null) {
    		vo = LoginAttemptVo.builder()
    				.ip(ip)
    				.locked(false)
    				.loginTryCnt(0)
    				.loginTryDt(null)
    				.lockRmvDt(null)
    				.build();
    		setSession(vo);
    	}
    	return vo;
    }

    /**
     * 로그인시도 이력 삭제
     */
    private void resetLoginHist() {
    	String ip = CoreUtils.webutils.getRemoteIp(request);
    	removeSession(ip);
    }

    /**
     * 로그인시도 회수 증가
     */
    private void addTryCnt() {
    	Date now = new Date();
    	LoginAttemptVo vo = getLoginHist();

    	if (vo.isLocked()) {
    		return;
    	}

    	int cnt = vo.getLoginTryCnt() + 1;
    	vo.setLoginTryCnt(cnt);
    	vo.setLoginTryDt(now);

    	if (cnt >= MAX_ATTEMPT) {
    		vo.setLocked(true);
    		Date lockRmvDt = CoreUtils.date.addMinutes(now, BLOCK_MINS);
    		vo.setLockRmvDt(lockRmvDt);
    	}
    	setSession(vo);
    }

    /**
     * 로그인 성공 처리
     */
    public void loginSucceeded() {
    	this.resetLoginHist();
    }

    /**
     * 로그인 실패 처리
     */
    public void loginFailed() {
        this.addTryCnt();
    }

    /**
     * 로그인 시도 회수 조회
     * @return
     */
    public int getTryCnt() {
    	LoginAttemptVo vo = this.getLoginHist();
    	return vo.getLoginTryCnt();
    }

    /**
     * 로그인 잠김여부 조회
     * @return
     */
    public boolean isLocked() {
    	LoginAttemptVo vo = this.getLoginHist();
    	if (!vo.isLocked()) {
    		return false;
    	}

    	Date now = new Date();
    	if (now.compareTo(vo.getLockRmvDt()) >= 0) {
    		this.resetLoginHist();
    		return false;
    	}
        return true;
    }

    /**
     * 잠김해제 시각 조회
     * @return
     */
    public Date getUnlockDt() {
    	LoginAttemptVo vo = this.getLoginHist();
    	return vo.getLockRmvDt();
    }
}