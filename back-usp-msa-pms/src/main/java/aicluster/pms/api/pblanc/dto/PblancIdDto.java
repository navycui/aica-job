package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PblancIdDto implements Serializable {
	private static final long serialVersionUID = 2506142061439313260L;
	/** 공고ID */
	private String pblancId;
}
