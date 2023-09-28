package aicluster.framework.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.FwAuthorDao;
import aicluster.framework.common.entity.ProgramRole;
import bnet.library.util.CoreUtils.antpath;
import bnet.library.util.CoreUtils.string;

/**
 * SecurityConfig에서 사용한다.
 * Request URL이 해당 사용자에게 허용되어 있는 지를 판단한다.
 *
 * 만일, URL과 Role과의 관계가 수정되었다면,
 * setUrlRoleList()를 호출해 주어야 한다.
 *
 * @author patrick
 *
 */
@Component
public class AuthorizationChecker {
    private List<ProgramRole> programRoleList = new ArrayList<>();

    @Autowired
    private FwAuthorDao authorityDao;

    @PostConstruct
    public void init() {
    	List<ProgramRole> list = authorityDao.selectProgramRoles();
    	setUrlRoleList(list);
    }

    synchronized public void setUrlRoleList(List<ProgramRole> list) {
    	programRoleList.clear();
    	programRoleList.addAll(list);
    }

    public boolean check(HttpServletRequest request, Authentication authentication) {
        Object principalObj = authentication.getPrincipal();

        if (principalObj == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        boolean existsUrl = false;
        boolean roleMatched = false;

        String httpMethod = string.upperCase(request.getMethod());
        String uri = request.getRequestURI();

        for (ProgramRole urlRole : programRoleList) {
        	// *: 모든 Method. http method가 동일하지 않으면 skip
        	if (!string.equals(urlRole.getHttpMethod(), "*") && !string.equalsIgnoreCase(urlRole.getHttpMethod(), httpMethod)) {
        		continue;
        	}
        	if (antpath.match(urlRole.getUrlPattern(), uri)) {
        		existsUrl = true;
        		for (GrantedAuthority grantedAuthority : roles) {
        			if (string.equals(grantedAuthority.toString(), urlRole.getRoleId())) {
        				roleMatched = true;
        				break;
        			}
        		}
        	}

        	if (existsUrl && roleMatched) {
        		return true;
        	}
        }

        if (existsUrl) {
        	return roleMatched;
        }
        return true;
    }

}
