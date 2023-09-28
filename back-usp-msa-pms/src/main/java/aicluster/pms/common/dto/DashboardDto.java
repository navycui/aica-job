package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DashboardDto implements Serializable {
	private static final long serialVersionUID = -5979332364494839606L;
	/** 사업신청 신청수 */
	private Integer applyReqstCo;
	/** 사업신청 보완요청수 */
	private Integer applySrCo;
	/** 선정대기수 */
	private Integer slctnWaitCo;
	/** 이의신청 신청수 */
	private Integer objcReqstApplyCo;
	/** 이의신청 접수완료(심의대기)수 */
	private Integer objcReqstComptCo;
	/** 중간보고서 제출수 */
	private Integer interimReprtPsCo;
	/** 중간보고서 보완요청수 */
	private Integer interimReprtSrCo;
	/** 결과보고서 제출수 */
	private Integer resultReprtPsCo;
	/** 결과보고서 보완요청수 */
	private Integer resultReprtSrCo;
	/** 성과관리 제출수 */
	private Integer pfmcPsCo;
	/** 성과관리 보완요청수 */
	private Integer pfmcSrCo;
	/** 교육생매칭 대기수 */
	private Integer eduWaitCo;
	/** 평가 발표자료 신청수 */
	private Integer evlReqstCo;
	/** 평가 발표자료 보완요청수 */
	private Integer evlSrCo;
	/** 사업계획서 제출수 */
	private Integer bsnsWtplnPsCo;
	/** 사업계획서 보완요청수 */
	private Integer bsnsWtplnSrCo;
	/** 사업협약 서명 완료수 */
	private Integer bsnsCnvnSignCo;
	/** 사업협약 변경 신청수 */
	private Integer bsnsCnvnChgCo;
	/** 전문가 신청수 */
	private Integer expertReqstCo;
}
