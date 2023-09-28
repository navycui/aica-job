package aicluster.framework.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogtErrorLog extends LogtLoginLog {
    /**
	 *
	 */
	private static final long serialVersionUID = 784282623064718076L;
	private String errorCode;
    private String errorMsg;
    private String url;
}
