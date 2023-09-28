package aicluster.framework.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.log.LogUtils;
import aicluster.framework.log.vo.AccessLog;
import aicluster.framework.security.SecurityUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.webutils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FwCoreInterceptor implements HandlerInterceptor {
	@Autowired
	private EnvConfig config;

	@Autowired
	private LogUtils logUtils;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String queryString = request.getQueryString();
        String httpMethod = request.getMethod();
        BnMember member = SecurityUtils.getCurrentMember();

        request.setAttribute("contextPath", contextPath);
        request.setAttribute("loginUser", member);

//        if (string.isNotBlank(contextPath)) {
//            requestURI = requestURI.substring(contextPath.length());
//        }

    	String remoteIp = webutils.getRemoteIp(request);
        log.info("===============================================================");
        log.info("   Remote IP: " + remoteIp);
        log.info(" HTTP Method: " + httpMethod);
        log.info(" ContextPath: " + contextPath);
        log.info("  RequestURI: " + requestURI);
        log.info(" QueryString: " + queryString);
        log.info("---------------------------------------------------------------");

        if (member == null) {
        	log.info("        User: not logined");
        	member = SecurityUtils.getAnonymousMember();
        }
        else {
        	log.info("    memberId: " + member.getMemberId());
            log.info("     loginId: " + member.getLoginId());
            log.info("    memberNm: " + member.getMemberNm());
            log.info(" authoirtyId: " + member.getAuthorityId());
            log.info("       roles: " + member.getRoles());
        }
        log.info("===============================================================");

        writeLog(requestURI, remoteIp, member);

		return true;
	}

    @Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	private void writeLog(String url, String memberIp, BnMember member) {
    	Date now = new Date();
    	AccessLog log = new AccessLog();
    	log.setApiSystemId(config.getSystemId());
    	log.setAge(date.getAge(member.getBirthday()));
    	log.setDeptNm(member.getDeptNm());
    	log.setGender(member.getGender());
    	log.setLogDt(now);
    	log.setLoginId(member.getLoginId());
    	log.setMemberId(member.getMemberId());
    	log.setMemberIp(memberIp);
    	log.setMemberNm(member.getMemberNm());
    	log.setMemberType(member.getMemberType());
    	log.setPositionNm(member.getPositionNm());
    	//log.setPstinstNm(member.getPstinstNm());
    	log.setUrl(url);
    	logUtils.saveAccessLog(log);
    }
}
