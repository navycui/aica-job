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
public class CmmtRestde implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3468691216102798762L;

	private String  ymd;                            /** 날짜 */
	private String  ymdNm;                          /** 날짜명 */
	private Boolean userDsgn;                       /** 사용자지정여부 */
}
