package aicluster.framework.common.entity;

import lombok.Data;

@Data
public class ProgramRole {
	private String httpMethod;
	private String urlPattern;
	private String roleId;
}
