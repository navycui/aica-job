package aicluster.pms.api.expertClfc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExpertClfcParam implements Serializable {

	/**
	 * 전문가분류
	 */
	private static final long serialVersionUID = 150795092223905993L;

	/** 전문가분류ID */
	String  expertClId;
}
