package aicluster.framework.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.FwLogDao;
import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.LogtIndvdlinfoDwldLog;
import aicluster.framework.common.entity.LogtIndvdlinfoDwldTrget;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.common.util.dto.LogIndvdInfTrgtItem;
import aicluster.framework.common.util.dto.LogIndvdlInfDown;
import aicluster.framework.security.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.validation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogIndvdlInfDownUtils {

	@Autowired
	private FwLogDao logDao;

	@Autowired
	private FwUserDao userDao;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 개인정보 다운로드 로그 생성
	 *
	 * @param param	개인정보 다운로드 정보(LogIndvdlInfDown)
	 */
	public void insertList(LogIndvdlInfDown param)
	{
		UserDto user = userDao.select(param.getMemberId());
		if (user == null) {
			throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "다운로드 하는 사용자 정보"));
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

		if ( string.isBlank(param.getMenuId()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "메뉴ID"));
		}

		if ( string.isBlank(param.getFileNm()) ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "다운로드 파일명"));
		}

		if ( param.getTrgtIdList().isEmpty() ) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "대상자ID 목록"));
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

		// 로그데이터 생성
		Date now = new Date();
		String logId = string.getNewId("inddlog-");
		LogtIndvdlinfoDwldLog downLog = LogtIndvdlinfoDwldLog.builder()
											.logId(logId)
											.logDt(now)
											.mberId(param.getMemberId())
											.mberIp(param.getMemberIp())
											.opertTyNm(param.getWorkTypeNm())
											.opertCn(param.getWorkCn())
											.menuId(param.getMenuId())
											.menuNm(param.getMenuNm())
											.menuUrl(menuUrl)
											.apiUrl(apiUrl)
											.paramtr(parameter)
											.sysIp(serverIp)
											.apiSysId(apiSystemId)
											.fileNm(param.getFileNm())
											.build();

		// 다운로드 대상자 정보 생성
		List<LogtIndvdlinfoDwldTrget> trgtList = new ArrayList<>();
		for (LogIndvdInfTrgtItem trgt : param.getTrgtIdList()) {
			// 개인정보가 포함된 경우
//			if (string.isNotBlank(trgt.getEncEmail()) || string.isNotBlank(trgt.getTrgterNm()) || string.isNotBlank(trgt.getEncMobileNo())) {
				// 대상자 정보 중 개인정보(이메일,생년월일,휴대폰번호)을 암호화하여 저장
				LogtIndvdlinfoDwldTrget trgtDto = LogtIndvdlinfoDwldTrget.builder()
													.logId(logId)
													.trgterId(trgt.getTrgterId())
													.trgterNm(trgt.getTrgterNm())
													.encptEmail(trgt.getEncEmail())
													.encptBrthdy(trgt.getEncBirthday())
													.encptMbtlnum(trgt.getEncMobileNo())
													.build();
				trgtList.add(trgtDto);
//			}
//			else {
//				log.info(String.format("[%d] 행의 개인정보가 미포함되어 이력 데이터가 생성되지 않았습니다.\n 대상자: [%s]", (i+1), trgt.toString()));
//			}
		}

		if (trgtList.size() > 0) {
			logDao.insertLogtIndvdlinfoDwldLog(downLog);
			logDao.insertLogtIndvdlinfoDwldTrget(trgtList);
		}
	}

}
