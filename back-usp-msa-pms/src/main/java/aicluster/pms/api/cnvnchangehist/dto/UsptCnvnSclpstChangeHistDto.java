package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;

import aicluster.pms.common.entity.UsptCnvnSclpstHist;
import lombok.Data;

@Data
public class UsptCnvnSclpstChangeHistDto implements Serializable{

	/**
	 * 협약변경내역_수행기관신분 변경이력
	 */
	private static final long serialVersionUID = -6417495201949988124L;

	/** 협약수행기관신분변경이력  변경전*/
	private UsptCnvnSclpstHist usptCnvnSclpstHistBefore;
	/** 협약수행기관신분변경이력 변경 후*/
	private UsptCnvnSclpstHist usptCnvnSclpstHistAfter;

}
