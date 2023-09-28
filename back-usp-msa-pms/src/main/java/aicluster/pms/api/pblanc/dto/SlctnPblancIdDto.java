package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlctnPblancIdDto implements Serializable {
	private static final long serialVersionUID = 1049364397805526167L;
	/** 선정결과공고ID */
	private String slctnPblancId;
}
