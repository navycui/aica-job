package aicluster.member.api.logout.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwEmpInfoDao;
import aicluster.framework.common.dao.FwMberInfoDao;
import aicluster.framework.common.dao.FwUsptEvlMfcmmDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.JwtUtils;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.logout.dto.LogoutResponseDto;
import aicluster.member.common.dao.CmmtEmpInfoDao;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtEmpInfo;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.util.CodeExt;
import bnet.library.exception.UnauthorizedException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.json;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogoutService {

	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;
	@Autowired
	private CmmtEmpInfoDao cmmtInsiderDao;
	@Autowired
	private FwUsptEvlMfcmmDao fwUsptEvlMfcmmDao;
	@Autowired
	private FwMberInfoDao fwMemberDao;
	@Autowired
	private FwEmpInfoDao fwInsiderDao;

	public void logout_member(HttpServletRequest request) {
		/*
		 * 1. AccessToken 또는 RefreshToken으로 사용자 정보 조회
		 * 2. RefreshToken 삭제
		 */

		// AccessToken으로 사용자 정보 조회
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			// Refresh token으로 사용자 정보 조회
			Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
	    	if (cookie == null) {
	    		return;
	    	}

	    	String refreshToken = cookie.getValue();
	    	worker = fwMemberDao.selectBnMember_refreshToken(refreshToken);
	        if (worker == null) {
	        	// 평가위원인지 확인
	        	worker = fwUsptEvlMfcmmDao.selectBnMember_refreshToken(refreshToken);
	        	if (worker == null) {
	        		return;
	        	}
	        }
		}

		// 평가위원이면, 평가위원 로그아웃
		if ( CodeExt.memberTypeExt.isEvaluator(worker.getMemberType()) ) {
			logout_evaluator(worker.getMemberId());
			return;
		}

		// 일반회원이면, 일반위원 로그아웃
		CmmtMberInfo member = cmmtMemberDao.select(worker.getMemberId());
		if (member == null) {
			return;
		}
		member.setRefreshToken(null);
		cmmtMemberDao.update(member);
	}

	public void logout_evaluator(String memberId) {
		fwUsptEvlMfcmmDao.updateRefreshToken(memberId, null);
	}

	public void logout_insider(HttpServletRequest request) {
		/*
		 * 1. AccessToken 또는 RefreshToken으로 사용자 정보 조회
		 * 2. RefreshToken 삭제
		 */

		// AccessToken으로 사용자정보 조회
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.내부사용자용);
	    	if (cookie == null) {
	    		throw new UnauthorizedException();
	    	}

	    	String refreshToken = cookie.getValue();
	    	worker = fwInsiderDao.selectBnMember_refreshToken(refreshToken);
	        if (worker == null) {
	        	// 평가위원인지 확인
	        	worker = fwUsptEvlMfcmmDao.selectBnMember_refreshToken(refreshToken);
	        	if (worker == null) {
	        		return;
	        	}
	        }
		}

		//TODO : (유영민) 내부사용자 페이지에 평가위원이 있을 수 있나?
		// 평가위원이면, 평가위원 로그아웃
		if ( CodeExt.memberTypeExt.isEvaluator(worker.getMemberType()) ) {
			logout_evaluator(worker.getMemberId());
			return;
		}

		CmmtEmpInfo insider = cmmtInsiderDao.select(worker.getMemberId());
		if (insider == null) {
			return;
		}
		insider.setRefreshToken(null);
		cmmtInsiderDao.update(insider);
	}
	
	public LogoutResponseDto session_logout_member(HttpServletRequest request) {
		/*
		 * 1. AccessToken 또는 RefreshToken으로 사용자 정보 조회
		 * 2. RefreshToken 삭제
		 */

		String result = "fail";
		int resultCode = -1;
		LogoutResponseDto dto = new LogoutResponseDto();
		dto.setResult(result);
		dto.setResultCode(resultCode);
		
		// AccessToken으로 사용자 정보 조회
		Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
		if (cookie == null) {
    		return dto;
    	}
		String refreshToken = cookie.getValue();
		BnMember worker = fwMemberDao.selectBnMember_refreshToken(refreshToken);
		if (worker == null) {
			return dto;
		}

		// 일반회원이면, 일반위원 로그아웃
		CmmtMberInfo member = cmmtMemberDao.select(worker.getMemberId());
		if (member == null) {
			return dto;
		}
		member.setRefreshToken(null);
		cmmtMemberDao.update(member);
		
		result = "success";
		resultCode = 0;
		
		dto.setResult(result);
		dto.setResultCode(resultCode);
		return dto;
	}
	
	public void session_logout_post(String url) {
		// AccessToken으로 사용자 정보 조회
		BnMember worker = SecurityUtils.getCurrentMember();
		if(worker == null) {
			log.info("login info is null");
			return;
		}
				
		try {
			HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
			HttpPost postRequest = new HttpPost(url); //POST 메소드 URL 새성 
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Content-Type", "application/json");

			if(url.indexOf("/dxp") > -1) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("loginId", worker.getLoginId());
				log.info("param json toString : {}", json.toString(param));
				StringEntity entity = new StringEntity(json.toString(param), "UTF-8");
				postRequest.setEntity(entity);
			}

			HttpResponse response = client.execute(postRequest);

			log.info("logout post response is status code : {}", response.getStatusLine().getStatusCode());
		} catch (Exception e){
			log.error(e.getMessage());
		}
	}
}
