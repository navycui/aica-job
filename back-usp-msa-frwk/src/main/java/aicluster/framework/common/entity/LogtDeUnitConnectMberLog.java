package aicluster.framework.common.entity;

import java.io.Serializable;

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
public class LogtDeUnitConnectMberLog implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6979584370702239489L;
	private String ymd;
	private String memberId;
}
