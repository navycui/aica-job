package aicluster.framework.log.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessLog extends LoginLog {
	private static final long serialVersionUID = -2298687921762441978L;
	private String url;
}
