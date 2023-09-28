package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.ApplyAttachDto;
import aicluster.pms.common.entity.UsptBsnsApplyRealm;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import lombok.Data;

@Data
public class FrontBsnsApplyInfoDto implements Serializable {
	private static final long serialVersionUID = -3517425136233810578L;
	/** 사업기간 */
	private String bsnsPd = "";
	/** 사업기간(전체) */
	private String bsnsPdAll = "";
	/** 사업기간(당해) */
	private String bsnsPdYw = "";
	/** 사업유형코드 */
	private String bsnsTypeCd;
	/** 사업유형명 */
	private String bsnsTypeNm;
	/** 공고ID */
	private String pblancId;
	/** 과제유형코드 */
	private String taskTypeCd;

	/** 과제정보 */
	UsptBsnsPblancApplyTask taskInfo;
	/** 지원분야(과제분야) 목록 */
	List<UsptBsnsApplyRealm> applyRealmList;
	/** 첨부파일 목록 */
	List<ApplyAttachDto> atchmnflList;
	/** 참여인력 목록 */
	List<UsptBsnsPblancApplyTaskPartcpts> partcptslist;
}
