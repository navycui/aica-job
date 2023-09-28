package aicluster.framework.common.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BnMember {
	private String memberId;
	private String loginId;
	private String memberNm;
	private String memberType;
	private String memberSt;
	private String gender;
	@JsonIgnore
	private String encBirthday;
	@JsonIgnore
	private String encPasswd;
	private String deptNm;
	private String positionNm;
	private String refreshToken;

	private String authorityId;
	private Boolean enabled;
	private List<String> roles;

	@JsonIgnore
	public String getPasswd() {
		return encPasswd;
	}

	@JsonIgnore
	public String getBirthday() {
		if (CoreUtils.string.isBlank(encBirthday)) {
			return null;
		}
		return CoreUtils.aes256.decrypt(encBirthday, memberId);
	}

	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		if(this.roles != null) {
			roles.addAll(this.roles);
		}
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = new ArrayList<>();
		if(roles != null) {
			this.roles.addAll(roles);
		}
	}
}
