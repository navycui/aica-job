package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptTaskTaxitmWct;
import lombok.Data;

@Data
public class TaskTaxitmWctDto implements Serializable{
	/**
	 * 과제세목별사업비
	 */
	private static final long serialVersionUID = 6018751350526299171L;

	/** 사업계획서ID */
	String  bsnsPlanDocId;
	/**사업년도*/
	String bsnsYear;

	/** 과제세목별사업비 */
	List <UsptTaskTaxitmWct> usptTaskTaxitmWctList;
}
