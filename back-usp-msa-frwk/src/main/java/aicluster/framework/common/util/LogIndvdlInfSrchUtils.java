package aicluster.framework.common.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.FwLogDao;
import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.LogtIndvdlinfoConectLog;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.security.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.validation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogIndvdlInfSrchUtils {

	@Autowired
	private FwLogDao logDao;

	@Autowired
	private FwUserDao userDao;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 개인정보접속 로그 생성
	 *
	 * @param param	개인정보 접속 정보(LogIndvdlInfSrch)
	 */
	public void insert(LogIndvdlInfSrch param)
	{
		UserDto user = userDao.select(param.getMemberId());
		if (user == null) {
			throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "조회하는 사용자 정보"));
		}

		if ( string.isBlank(param.getMemberIp()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사용자 IP주소"));
		}
		if ( !validation.isIpAddress(param.getMemberIp()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.유효오류, "사용자 IP주소"));
		}

		if ( string.isBlank(param.getWorkTypeNm()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "작업유형명"));
		}

		if ( string.isBlank(param.getWorkCn()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "작업내용"));
		}

		// 조회대상 중 평가위원 미선정 대상자들도 존재하여 대상자에 대한 회원ID 검증 주석처리하고 null 체크만 하는 것으로 대체
		if ( string.isBlank(param.getTrgterId()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "조회 대상자 정보"));
		}
		String trgterNm = param.getTrgterNm();
		UserDto trgter = userDao.select(param.getTrgterId());
		if (trgter != null) {
			trgterNm = trgter.getMemberNm();
		}
		else {
			if ( string.isBlank(trgterNm) ) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "조회 대상자명"));
			}
		}

		// 이력 부가정보 생성
		String menuUrl = request.getHeader("front-referer");
		String apiUrl = request.getRequestURI();
		String parameter = request.getQueryString();
		String serverIp = request.getLocalAddr();
		String contextPath = request.getContextPath();
		String apiSystemId = null;
		
		if(contextPath.indexOf("/common") > -1) {
			apiSystemId = Code.apiSystemId.공통API;
		}else if(contextPath.indexOf("/member") > -1) {
			apiSystemId = Code.apiSystemId.인증API;
		}else if(contextPath.indexOf("/tsp") > -1) {
			apiSystemId = Code.apiSystemId.실증API;
		}else if(contextPath.indexOf("/pms") > -1) {
			apiSystemId = Code.apiSystemId.사업관리API;
		}else if(contextPath.indexOf("/edu") > -1) {
			apiSystemId = Code.apiSystemId.교육API;
		}else if(contextPath.indexOf("/mvn") > -1) {
			apiSystemId = Code.apiSystemId.입주API;
		}else if(contextPath.indexOf("/rsvt") > -1) {
			apiSystemId = Code.apiSystemId.예약API;
		}else if(contextPath.indexOf("/batch") > -1) {
			apiSystemId = Code.apiSystemId.배치API;
		}

		// 개인정보가 미포함된 경우 이력 데이터 생성 안함.
		if (string.isBlank(param.getEncEmail()) && string.isBlank(param.getEncBirthday()) && string.isBlank(param.getEncMobileNo())) {
			log.info(String.format("개인정보가 미포함되어 이력 데이터가 생성되지 않았습니다.\n Parameter: [%s]", param.toString()));
			return;
		}

		// 로그데이터 생성
		Date now = new Date();
		String logId = string.getNewId("indslog-");
		LogtIndvdlinfoConectLog srchLog = LogtIndvdlinfoConectLog.builder()
											.logId(logId)
											.logDt(now)
											.memberId(param.getMemberId())
											.memberIp(param.getMemberIp())
											.workTypeNm(param.getWorkTypeNm())
											.workCn(param.getWorkCn())
											.menuUrl(menuUrl)
											.apiUrl(apiUrl)
											.paramtr(parameter)
											.sysIp(serverIp)
											.apiSysId(apiSystemId)
											.trgterId(param.getTrgterId())
											.trgterNm(trgterNm)
											.encptEmail(param.getEncEmail())
											.encptBrthdy(param.getEncBirthday())
											.encptMbtlnum(param.getEncMobileNo())
											.build();

		logDao.insertLogtIndvdlinfoConectLog(srchLog);
	}

}
