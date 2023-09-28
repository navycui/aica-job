package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApplyMberTypeDto implements Serializable {
	private static final long serialVersionUID = 8650692518878667166L;
	private String mberTypeCd;
	private String mberType;
}
