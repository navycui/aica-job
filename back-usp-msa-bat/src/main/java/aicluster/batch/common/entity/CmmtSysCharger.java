package aicluster.batch.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmmtSysCharger implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1063842060321746149L;
	private String apiSystemId;
	private String picEmail;
	private String picNm;
}
