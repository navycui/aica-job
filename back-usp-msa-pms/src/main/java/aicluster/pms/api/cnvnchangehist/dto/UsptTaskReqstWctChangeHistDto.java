package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptTaskReqstWctHist;
import lombok.Data;

@Data
public class UsptTaskReqstWctChangeHistDto implements Serializable{

	/**
	 *협약변경내역_과제신청사업비변경이력
	 */
	private static final long serialVersionUID = 7766366937456788556L;
	/** 총사업비 */
	Long totalWct;

	/** 과제신청사업비변경이력 변경전*/
	List<UsptTaskReqstWctHist> usptTaskReqstWctHistBeforeList;
	/** 과제신청사업비변경이력 변경 후*/
	List<UsptTaskReqstWctHist> usptTaskReqstWctHistAfterList;

}
