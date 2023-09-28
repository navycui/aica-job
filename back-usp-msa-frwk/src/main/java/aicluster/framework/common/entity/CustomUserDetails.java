package aicluster.framework.common.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	/**
	 *
	 */
	private static final long serialVersionUID = 5851826667744820831L;
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
	private boolean enabled;
	private BnMember member;

	public CustomUserDetails() {

	}

	public CustomUserDetails(String username, String password, boolean enabled, Collection<GrantedAuthority> authorities, BnMember member) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.member = member;
		this.authorities = new ArrayList<>();
		for (GrantedAuthority authority : authorities) {
			this.authorities.add(authority);
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public Object getMember() {
		return member;
	}

	public void setMember(BnMember member) {
		this.member = member;
	}

}
