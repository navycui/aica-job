package aicluster.batch.common.entity;

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
	private static final long serialVersionUID = 9204780628774457423L;
	private String ymd;
	private String ymdNm;
	private Boolean userDsgn;
}
