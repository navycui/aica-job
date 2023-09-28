package aicluster.framework.common.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aicluster.framework.common.dto.SessionConsentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionTermsConsent implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8962507567132007672L;

	private String userRemoteIp;		/* 사용자 IP주소 */
	private List<SessionConsentDto> consents;
	
	public List<SessionConsentDto> getConsents() {
		List<SessionConsentDto> consents = new ArrayList<>();
		if(this.consents != null) {
			consents.addAll(this.consents);
		}
		return consents;
	}

	public void setConsents(List<SessionConsentDto> consents) {
		this.consents = new ArrayList<>();
		if(consents != null) {
			this.consents.addAll(consents);
		}
	}
}
