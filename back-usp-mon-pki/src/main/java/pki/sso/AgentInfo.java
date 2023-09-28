package pki.sso;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentInfo implements Serializable {
	private static final long serialVersionUID = 147966558964456294L;
	String authLoginPage;
	String agentId;
}
