package aicluster.pms.api.cnvnchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.cnvnchange.dto.CnvnChangeParam;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnApplcntHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnSclpstHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnTaskInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPartcptsHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPrtcmpnyInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskReqstWctHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskRspnberHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskTaxitmWctHistDto;
import aicluster.pms.api.cnvnchange.service.CnvnChangeService;
import aicluster.pms.common.dto.CnvnChangeDto;
import aicluster.pms.common.entity.UsptBsnsCnvnHist;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 협약변경관리_admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/cnvn-change")
public class CnvnChangeController {

	@Autowired
	private CnvnChangeService cnvnChangeService;

	/**
	 * 협약변경신청 목록 조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<CnvnChangeDto> getList(CnvnChangeParam cnvnChangeParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getList : {}");
		return cnvnChangeService.selectCnvnChangeReqListAdmin(cnvnChangeParam, page);
	}

	/**
	 * 협약변경신청 목록 엑셀 다운로드
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcel(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	getListExcel : {}", cnvnChangeParam);
		List<CnvnChangeDto> list = cnvnChangeService.getListExcel(cnvnChangeParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("협약변경관리 목록");
		ExcelSheet<CnvnChangeDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "처리상태"                  ,"접수번호"  , "과제명"      	, "변경유형"	                , "변경항목"	            ,  "사업자명/이름"	, "신청일");
		sheet.setProperties("rn"	, "cnvnChangeSttusNm"	,"receiptNo"	, "taskNmKo"	, "cnvnChangeTypeNm"	, "changeIemDivNm"	, "memberNm"	     , "reqDe" );
		sheet.setTitle("사업계획서접수 관리 목록");
		sheet.setSheetName("사업계획서접수 관리 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/****************************************수행기관신분변경****************************************************/
	/**
	 * 협약변경관리 수행기관신분변경 상세조회
	 *  cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/cnvn_sclpst")
	public UsptCnvnSclpstHistDto selectChangeCnvnSclpstInfo(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectChangeCnvnSclpstInfo : {}", cnvnChangeParam);
		return cnvnChangeService.selectChangeCnvnSclpstInfo(request, cnvnChangeParam);
	}
	/**
	 * 협약변경관리 수행기관신분변경 신청 승인
	 * cnvnChangeReqId
	 */
	@PutMapping("/cnvn_sclpst/approval")
	public void modifyChangeCnvnSclpstApproval(@RequestBody(required = false) UsptCnvnSclpstHistDto usptCnvnSclpstHistDto) {
		log.debug("#####	modifyChangeCnvnSclpst : {}", usptCnvnSclpstHistDto);
		 cnvnChangeService.modifyChangeCnvnSclpstApproval(usptCnvnSclpstHistDto);
	}

	/****************************************과제정보****************************************************/
	/**
	 * 협약변경관리 과제정보 상세조회
	 *  cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/cnvn_task_info")
	public UsptCnvnTaskInfoHistDto selectChangeCnvnTaskInfo(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectChangeCnvnTaskInfo : {}", cnvnChangeParam);
		return cnvnChangeService.selectChangeCnvnTaskInfo(cnvnChangeParam);
	}
	/**
	 * 협약변경관리 과제정보 신청 승인
	 * cnvnChangeReqId(협약변경요청ID)
	 */
	@PutMapping("/cnvn_task_info/approval")
	public void modifyChangeCnvnTaskInfoApproval(@RequestBody(required = false) UsptCnvnTaskInfoHistDto usptCnvnTaskInfoHistDto) {
		log.debug("#####	modifyChangeCnvnTaskInfoApproval : {}", usptCnvnTaskInfoHistDto);
		 cnvnChangeService.modifyChangeCnvnTaskInfoApproval(usptCnvnTaskInfoHistDto);
	}

	/****************************************참여기업****************************************************/
	/**
	 * 협약변경관리 참여기업 조회
	 * cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_prtcmpny_info")
	public UsptTaskPrtcmpnyInfoHistDto selectTaskPrtcmpnyInfoHist(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectTaskPrtcmpnyInfoHist : {}", cnvnChangeParam);
		return cnvnChangeService.selectTaskPrtcmpnyInfoHist(request, cnvnChangeParam);
	}
	/**
	 * 협약변경관리 참여기업 승인
	 * cnvnChangeReqId(협약변경요청ID)
	 */
	@PutMapping("/task_prtcmpny_info/approval")
	public void modifyTaskPrtcmpnyInfoHistApproval(@RequestBody(required = false) UsptTaskPrtcmpnyInfoHistDto usptTaskPrtcmpnyInfoHistDto) {
		log.debug("#####	modifyTaskPrtcmpnyInfoHistApproval : {}", usptTaskPrtcmpnyInfoHistDto);
		 cnvnChangeService.modifyTaskPrtcmpnyInfoHistApproval(usptTaskPrtcmpnyInfoHistDto);
	}


	/****************************************참여인력****************************************************/
	/**
	 * 협약변경관리 참여인력 조회
	 *  cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_partcpt")
	public UsptTaskPartcptsHistDto selectTaskPartcptsHist(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectTaskPartcptsHist : {}", cnvnChangeParam);
		return cnvnChangeService.selectTaskPartcptsHist(request, cnvnChangeParam);
	}

	/**
	 * 협약변경관리 참여인력  신청 승인
	 * cnvnChangeReqId(협약변경요청ID)
	 */
	@PutMapping("/task_partcpt/approval")
	public void modifyTaskPartcptsHistApproval(@RequestBody(required = false) UsptTaskPartcptsHistDto usptTaskPartcptsHistDto	) {
		log.debug("#####	modifyTaskPartcptsHistApproval : {}", usptTaskPartcptsHistDto);
		 cnvnChangeService.modifyTaskPartcptsHistApproval(usptTaskPartcptsHistDto );
	}

	/****************************************사업비****************************************************/
	/**
	 * 협약변경관리 사업비 조회
	 *  cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_reqst_wct")
	public UsptTaskReqstWctHistDto selectTaskReqstWctHist(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectTaskReqstWctHist : {}", cnvnChangeParam);
		return cnvnChangeService.selectTaskReqstWctHist(cnvnChangeParam);
	}

	/**
	 * 협약변경관리 사업비 신청 승인
	 *cnvnChangeReqId(협약변경요청ID)
	 */
	@PutMapping("/task_reqst_wct/approval")
	public void modifyTaskReqstWctHistApproval(@RequestBody(required = false) UsptTaskReqstWctHistDto usptTaskReqstWctHistDto	) {
		log.debug("#####	modifyTaskReqstWctHistCancel : {}", usptTaskReqstWctHistDto);
		 cnvnChangeService.modifyTaskReqstWctHistApproval(usptTaskReqstWctHistDto );
	}

	/****************************************비목별사업비****************************************************/

	/**
	 * 협약변경관리 과제세목별사업비변경 전체 사업년도 조회
	 * bsnsYear
	 */
	@GetMapping("/task_taxitm_wc/bsnsYear")
	public JsonList <String> selectBsnsPlanTaxitmWctBsnsYearList(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectBsnsPlanTaxitmWctBsnsYearList : {}", cnvnChangeParam);
		return new JsonList<>(cnvnChangeService.selectBsnsPlanTaxitmWctBsnsYearList(cnvnChangeParam));
	}

	/**
	 * 협약변경관리 비목별사업비 조회
	 *  cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_taxitm_wc")
	public UsptTaskTaxitmWctHistDto selectTaskTaxitmWctHist(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectTaskTaxitmWctHist : {}", cnvnChangeParam);
		return cnvnChangeService.selectTaskTaxitmWctHist(cnvnChangeParam);
	}
	/**
	 * 협약변경관리 비목별사업비 신청 승인
	 *cnvnChangeReqId(협약변경요청ID)
	 */
	@PutMapping("/task_taxitm_wc/approval")
	public void modifyTaskTaxitmWctHistApproval(@RequestBody(required = false) UsptTaskTaxitmWctHistDto usptTaskTaxitmWctHistDto	) {
		log.debug("#####	modifyTaskTaxitmWctHistApproval : {}", usptTaskTaxitmWctHistDto);
		 cnvnChangeService.modifyTaskTaxitmWctHistApproval(usptTaskTaxitmWctHistDto );
	}

	/****************************************신청자정보****************************************************/
	/**
	 * 협약변경관리 신청자정보 조회
	 * cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/cnvn_applcnt")
	public UsptCnvnApplcntHistDto selectCnvnApplcntHist(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectCnvnApplcntHist : {}", cnvnChangeParam);
		return cnvnChangeService.selectCnvnApplcntHist(request, cnvnChangeParam);
	}
	/**
	 * 협약변경관리 신청자정보 신청 승인
	 * cnvnChangeReqId(협약변경요청ID)
	 */
	@PutMapping("/cnvn_applcnt/approval")
	public void modifyCnvnApplcntHistApproval(@RequestBody(required = false) UsptCnvnApplcntHistDto usptCnvnApplcntHistDto){
		log.debug("#####	modifyCnvnApplcntHistApproval : {}", usptCnvnApplcntHistDto);
		 cnvnChangeService.modifyCnvnApplcntHistApproval(usptCnvnApplcntHistDto );
	}

	/****************************************과제책임자****************************************************/
	/**
	 * 협약변경관리 과제책임자 조회
	 * changeIemDivCd, bsnsPlanDocId
	 */
	@GetMapping("/task_rspnber")
	public UsptTaskRspnberHistDto selectTaskRspnberHist(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectTaskRspnberHist : {}", cnvnChangeParam);
		return cnvnChangeService.selectTaskRspnberHist(cnvnChangeParam);
	}
	/**
	 * 협약변경관리 과제책임자 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/task_rspnber/approval")
	public void modifyTaskRspnberHistApproval(@RequestBody(required = false) UsptTaskRspnberHistDto usptTaskRspnberHistDto){
		log.debug("#####	modifyTaskRspnberHistApproval : {}", usptTaskRspnberHistDto);
		 cnvnChangeService.modifyTaskRspnberHistApproval(usptTaskRspnberHistDto );
	}

	/****************************************보완요청, 반려  ****************************************************/
	/**
	 * 협약변경관리 수행기관신분변경 반려
	 * cnvnChangeReqId ,changeIemDivCd,  resnCn(사유)
	 */
	@PutMapping("/reject")
	public void modifyCnvnChangeReject(@RequestBody(required = false) CnvnChangeParam cnvnChangeParam	) {
		log.debug("#####	modifyCnvnChangeReject : {}", cnvnChangeParam);
		 cnvnChangeService.modifyCnvnChangeReject(cnvnChangeParam);
	}

	/**
	 * 협약변경관리 수행기관신분변경 보완요청
	 * cnvnChangeReqId ,changeIemDivCd,  resnCn(사유)
	 */
	@PutMapping("/reason")
	public void modifyCnvnChangeReason(@RequestBody(required = false) CnvnChangeParam cnvnChangeParam	) {
		log.debug("#####	modifyCnvnChangeReason : {}", cnvnChangeParam);
		 cnvnChangeService.modifyCnvnChangeReason(cnvnChangeParam);
	}
	/****************************************협약변경항목구분별 이력 조회 ****************************************************/
	/**
	 * 협약변경항목구분별 이력 조회
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@GetMapping("/bsns_cnvn_hist")
	public JsonList <UsptBsnsCnvnHist> selectBsnsCnvnHist(CnvnChangeParam cnvnChangeParam){
		log.debug("#####	selectProcessHist : {}", cnvnChangeParam);
		return new JsonList<>(cnvnChangeService.selectBsnsCnvnHist(cnvnChangeParam));
	}

	/**
	 * 협약변경관리 첨부파일 일괄 다운
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/file-dwln")
	public void getCnvnChangeFileDown(HttpServletResponse response, CnvnChangeParam cnvnChangeParam){
		log.debug("#####	getCnvnChangeFileDown : {}", cnvnChangeParam);
		cnvnChangeService.getCnvnChangeFileDown(response, cnvnChangeParam);
	}
	/**
	 * 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		cnvnChangeService.getOneFileDown(response, attachmentId);
	}

}