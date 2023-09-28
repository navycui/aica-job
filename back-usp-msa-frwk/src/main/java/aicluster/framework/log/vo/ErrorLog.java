package aicluster.framework.log.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorLog extends LoginLog {
	/**
	 *
	 */
	private static final long serialVersionUID = 469793825535063059L;
	private String errorCode;
    private String errorMsg;
    private String url;
}
