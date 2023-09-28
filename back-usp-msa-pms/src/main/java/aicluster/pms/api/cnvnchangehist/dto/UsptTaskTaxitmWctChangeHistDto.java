package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptTaskTaxitmWctHist;
import lombok.Data;

@Data
public class UsptTaskTaxitmWctChangeHistDto implements Serializable{

	/**
	 */
	private static final long serialVersionUID = -4055214541657539540L;

	/** 과제세목별사업비변경이력 변경전*/
	List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistBeforeList;
	/** 과제세목별사업비변경이력 변경 후*/
	List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistAfterList;
}
