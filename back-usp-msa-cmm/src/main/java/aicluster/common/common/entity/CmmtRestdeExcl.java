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
public class CmmtRestdeExcl implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1246270840045849862L;
	private String ymd;
	private String ymdNm;
	private String exclReason;
}
