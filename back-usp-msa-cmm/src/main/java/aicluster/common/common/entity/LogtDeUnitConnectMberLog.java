package aicluster.common.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogtDeUnitConnectMberLog implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7176770151548129605L;
	private String ymd;
	private String memberId;
}
