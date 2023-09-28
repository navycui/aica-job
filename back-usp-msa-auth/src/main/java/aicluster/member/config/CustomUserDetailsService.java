package aicluster.member.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwAuthorDao;
import aicluster.framework.common.dao.FwEmpInfoDao;
import aicluster.framework.common.dao.FwMberInfoDao;
import aicluster.framework.common.dao.FwUsptEvlMfcmmDao;
import aicluster.framework.common.entity.BnAuthorityRole;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CustomUserDetails;
import bnet.library.util.CoreUtils.password;
import bnet.library.util.CoreUtils.string;

/**
 * 로그인할 때, Spring security가 사용한다.
 * 사용자 정보를 조회하고, Spring security가 이를 이용해
 * 아이디/비번을 비교 판단한다.
 *
 * @author patrick
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private FwMberInfoDao memberDao;

	@Autowired
	private FwEmpInfoDao insiderDao;

	@Autowired
	private FwUsptEvlMfcmmDao evaluatorDao;

	@Autowired
    private FwAuthorDao authorityDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	BnMember member = null;
    	if (string.startsWith(username, "sso:")) {
    		String[] tokens = string.split(username, ':');
    		username = tokens[1];
    		String passwd = tokens[2];
    		member = memberDao.selectBnMember_loginId(username);
    		member.setEncPasswd(passwd);
    	}
    	else if (string.startsWith(username, "member:")) {
    		username = string.substring(username, 7);
    		member = memberDao.selectBnMember_loginId(username);
    	}
    	else if(string.startsWith(username, "insider:")){
    		username = string.substring(username, 8);
    		member = insiderDao.selectBnMember_loginId(username);
    	}
    	else if(string.startsWith(username, "evaluator:")) {
    		username = string.substring(username, 10);
    		member = evaluatorDao.selectBnMember(username);
    	}
    	else if (string.startsWith(username, "sns:")) {
    		username = string.substring(username, 4);
    		member = memberDao.selectBnMember_loginId(username);
    		if (member != null) {
	    		member.setLoginId(username);
	    		member.setEncPasswd(password.encode(username));
    		}
    	}

    	if (member == null) {
    		throw new UsernameNotFoundException(username + " -> not found");
    	}
    	return createUserDetails(member);
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(BnMember member) {
    	List<GrantedAuthority> authorityList = new ArrayList<>();
    	List<BnAuthorityRole> roleList = authorityDao.selectAuthorityRoles(member.getAuthorityId());
    	for (BnAuthorityRole role : roleList) {
    		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleId());
    		authorityList.add(grantedAuthority);
    	}
    	if (member.getEnabled() == null) {
    		member.setEnabled(true);
    	}
    	CustomUserDetails details = new CustomUserDetails(member.getLoginId(), member.getPasswd(), member.getEnabled(), authorityList, member);
    	return details;
    }
}

