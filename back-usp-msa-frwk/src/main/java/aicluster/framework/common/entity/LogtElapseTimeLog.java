package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogtElapseTimeLog implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8622166734011373349L;
	private String logId;
	private Date logDt;
	private String url;
	private Long elapsedTime;
	private String apiSystemId;
}
