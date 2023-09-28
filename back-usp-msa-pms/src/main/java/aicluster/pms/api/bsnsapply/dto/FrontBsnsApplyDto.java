package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptBsnsApplyRealm;
import aicluster.pms.common.entity.UsptBsnsAtchmnflSetup;
import lombok.Data;

@Data
public class FrontBsnsApplyDto implements Serializable {
	private static final long serialVersionUID = 3996237248944023589L;
	/** 사업기간 */
	private String bsnsPd = "";
	/** 사업기간(전체) */
	private String bsnsPdAll = "";
	/** 사업기간(당해) */
	private String bsnsPdYw = "";
	/** 과제유형코드 */
	private String taskTypeCd;
	/** 사업유형코드 */
	private String bsnsTypeCd;
	/** 사업유형명 */
	private String bsnsTypeNm;
	/** 지원분야(과제분야) 목록 */
	List<UsptBsnsApplyRealm> applyRealmList;
	/** 첨부파일 목록 */
	List<UsptBsnsAtchmnflSetup> atchmnflList;
}
