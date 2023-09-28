package aicluster.pms.api.cnvnchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.cnvnchange.dto.FrontCnvnChangeParam;
import aicluster.pms.api.cnvnchange.dto.FrontCnvnChangeRequestDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnApplcntHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnSclpstHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnTaskInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPartcptsHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPrtcmpnyInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskReqstWctHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskRspnberHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskTaxitmWctHistDto;
import aicluster.pms.api.cnvnchange.service.FrontCnvnChangeService;
import aicluster.pms.common.entity.UsptBsnsCnvnHist;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import lombok.extern.slf4j.Slf4j;

/**
 * 협약변경관리_front
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/front/cnvn-change")
public class FrontCnvnChangeController {

	@Autowired
	private FrontCnvnChangeService frontCnvnChangeService;

	/**
	 * 협약변경관리  변경요청 조회
	 * @return
	 */
	@GetMapping("")
	public FrontCnvnChangeRequestDto selectCnvnChangeReqOne(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	frontCnvnCnclsParam : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectCnvnChangeReqOne(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 신청내역조회
	 * @return
	 */
	@GetMapping("/request")
	public CorePagination<UsptCnvnChangeReq> getList(FrontCnvnChangeParam frontCnvnChangeParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getList : {}");
		return frontCnvnChangeService.selectCnvnChangeReqList(frontCnvnChangeParam, page);
	}

	/****************************************수행기관신분변경****************************************************/
	/**
	 * 협약변경관리 수행기관신분변경 상세조회
	 * bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/cnvn_sclpst")
	public UsptCnvnSclpstHistDto selectChangeCnvnSclpstInfoFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectChangeCnvnSclpstInfoFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectChangeCnvnSclpstInfoFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 수행기관신분변경 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/cnvn_sclpst/request")
	public void modifyChangeCnvnSclpstFront(@RequestPart(value = "info") UsptCnvnSclpstHistDto usptCnvnSclpstHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyChangeCnvnSclpstFront : {}", usptCnvnSclpstHistDto);
		 frontCnvnChangeService.modifyChangeCnvnSclpstFront(usptCnvnSclpstHistDto, fileList);
	}
	/**
	 * 협약변경관리 수행기관신분변경 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/cnvn_sclpst/cancel")
	public void modifyChangeCnvnSclpstCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyChangeCnvnSclpstCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************과제정보****************************************************/
	/**
	 * 협약변경관리 과제정보 조회
	 *  bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/cnvn_task_info")
	public UsptCnvnTaskInfoHistDto selectChangeCnvnTaskInfoFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectChangeCnvnTaskInfoFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectChangeCnvnTaskInfoFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 과제정보 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/cnvn_task_info/request")
	public void modifyChangeCnvnTaskInfoFront(@RequestPart(value = "info") UsptCnvnTaskInfoHistDto usptCnvnTaskInfoHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyChangeCnvnTaskInfoFront : {}", usptCnvnTaskInfoHistDto);
		 frontCnvnChangeService.modifyChangeCnvnTaskInfoFront(usptCnvnTaskInfoHistDto, fileList);
	}
	/**
	 * 협약변경관리 과제정보 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/cnvn_task_info/cancel")
	public void modifyChangeCnvnTaskInfoCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyChangeCnvnTaskInfoCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************참여기업****************************************************/
	/**
	 * 협약변경관리 참여기업 조회
	 *  bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_prtcmpny_info")
	public UsptTaskPrtcmpnyInfoHistDto selectTaskPrtcmpnyInfoHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectTaskPrtcmpnyInfoHistFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectTaskPrtcmpnyInfoHistFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 참여기업 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/task_prtcmpny_info/request")
	public void modifyTaskPrtcmpnyInfoHistFront(@RequestPart(value = "info") UsptTaskPrtcmpnyInfoHistDto usptTaskPrtcmpnyInfoHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyTaskPrtcmpnyInfoHistFront : {}", usptTaskPrtcmpnyInfoHistDto);
		 frontCnvnChangeService.modifyTaskPrtcmpnyInfoHistFront(usptTaskPrtcmpnyInfoHistDto, fileList);
	}
	/**
	 * 협약변경관리 참여기업 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/task_prtcmpny_info/cancel")
	public void modifyTaskPrtcmpnyInfoHistCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyTaskPrtcmpnyInfoHist : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************참여인력****************************************************/
	/**
	 * 협약변경관리 참여인력 조회
	 *  bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_partcpt")
	public UsptTaskPartcptsHistDto selectTaskPartcptsHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectTaskPartcptsHistFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectTaskPartcptsHistFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 참여인력 신청
	 *cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/task_partcpt/request")
	public void modifyTaskPartcptsHistFront(@RequestPart(value = "info") UsptTaskPartcptsHistDto usptTaskPartcptsHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	selectTaskPartcptsHistFront : {}", usptTaskPartcptsHistDto);
		 frontCnvnChangeService.modifyTaskPartcptsHistFront(usptTaskPartcptsHistDto, fileList);
	}
	/**
	 * 협약변경관리 참여인력 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/task_partcpt/cancel")
	public void modifyTaskPartcptsHistCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyTaskPartcptsHistCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************사업비****************************************************/
	/**
	 * 협약변경관리 사업비 조회
	 *  bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_reqst_wct")
	public UsptTaskReqstWctHistDto selectTaskReqstWctHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectTaskReqstWctHistFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectTaskReqstWctHistFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 사업비 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/task_reqst_wct/request")
	public void modifyTaskReqstWctHistFront(@RequestPart(value = "info") UsptTaskReqstWctHistDto usptTaskReqstWctHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyTaskReqstWctHistFront : {}", usptTaskReqstWctHistDto);
		 frontCnvnChangeService.modifyTaskReqstWctHistFront(usptTaskReqstWctHistDto, fileList);
	}
	/**
	 * 협약변경관리 사업비 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/task_reqst_wct/cancel")
	public void modifyTaskReqstWctHistCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyTaskReqstWctHistCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************비목별사업비****************************************************/

	/**
	 * 협약변경관리 과제세목별사업비변경 전체 사업년도 조회
	 * bsnsYear
	 */
	@GetMapping("/task_taxitm_wc/bsnsYear")
	public JsonList <String> selectBsnsPlanTaxitmWctBsnsYearList(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectBsnsPlanTaxitmWctBsnsYearList : {}", frontCnvnChangeParam);
		return new JsonList<>(frontCnvnChangeService.selectBsnsPlanTaxitmWctBsnsYearList(frontCnvnChangeParam));
	}

	/**
	 * 협약변경관리 비목별사업비 조회
	 *  bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/task_taxitm_wc")
	public UsptTaskTaxitmWctHistDto selectTaskTaxitmWctHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectTaskTaxitmWctHistFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectTaskTaxitmWctHistFront(frontCnvnChangeParam);
	}


	/**
	 * 협약변경관리 비목별사업비 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/task_taxitm_wc/request")
	public void modifyTaskTaxitmWctHistFront(@RequestPart(value = "info") UsptTaskTaxitmWctHistDto usptTaskTaxitmWctHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyTaskTaxitmWctHistFront : {}", usptTaskTaxitmWctHistDto);
		 frontCnvnChangeService.modifyTaskTaxitmWctHistFront(usptTaskTaxitmWctHistDto, fileList);
	}
	/**
	 * 협약변경관리 비목별사업비 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/task_taxitm_wc/cancel")
	public void modifyTaskTaxitmWctHistCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyTaskTaxitmWctHistCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************신청자정보****************************************************/
	/**
	 * 협약변경관리 신청자정보 조회
	 *  bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 */
	@GetMapping("/cnvn_applcnt")
	public UsptCnvnApplcntHistDto selectCnvnApplcntHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectCnvnApplcntHistFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectCnvnApplcntHistFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 신청자정보 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/cnvn_applcnt/request")
	public void modifyCnvnApplcntHistFront(@RequestPart(value = "info") UsptCnvnApplcntHistDto usptCnvnApplcntHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyCnvnApplcntHistFront : {}", usptCnvnApplcntHistDto);
		 frontCnvnChangeService.modifyCnvnApplcntHistFront(usptCnvnApplcntHistDto, fileList);
	}
	/**
	 * 협약변경관리 신청자정보 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/cnvn_applcnt/cancel")
	public void modifyCnvnApplcntHistCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyCnvnApplcntHistCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************과제책임자****************************************************/
	/**
	 * 협약변경관리 과제책임자 조회
	 * changeIemDivCd, bsnsPlanDocId
	 */
	@GetMapping("/task_rspnber")
	public UsptTaskRspnberHistDto selectTaskRspnberHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectTaskRspnberHistFront : {}", frontCnvnChangeParam);
		return frontCnvnChangeService.selectTaskRspnberHistFront(frontCnvnChangeParam);
	}
	/**
	 * 협약변경관리 과제책임자 신청
	 * cnvnChangeReqId , bsnsCnvnId(사업협약ID), resnCn(사유)
	 */
	@PutMapping("/task_rspnber/request")
	public void modifyTaskRspnberHistFront(@RequestPart(value = "info") UsptTaskRspnberHistDto usptTaskRspnberHistDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyTaskRspnberHistFront : {}", usptTaskRspnberHistDto);
		 frontCnvnChangeService.modifyTaskRspnberHistFront(usptTaskRspnberHistDto, fileList);
	}
	/**
	 * 협약변경관리 과제책임자 신청 취소
	 * cnvnChangeReqId, changeIemDivCd
	 */
	@PutMapping("/task_rspnber/cancel")
	public void modifyTaskRspnberHistCancel(@RequestBody(required = false) FrontCnvnChangeParam frontCnvnChangeParam	) {
		log.debug("#####	modifyTaskRspnberHistCancel : {}", frontCnvnChangeParam);
		 frontCnvnChangeService.modifyCnvnChangeReqCancel(frontCnvnChangeParam );
	}

	/****************************************협약변경관리   사유 확인(반려, 보완요청)****************************************************/
	/**
	 * 협약변경관리 사유 확인팝업
	 * @param cnvnChangeReqId, changeIemDivCd, cnvnChangeSttusCd
	 * @return
	 */
	@GetMapping("/resn")
	public UsptBsnsCnvnHist getResnInfo(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	getResnInfo : {}", frontCnvnChangeParam);
		 return frontCnvnChangeService.getResnInfo(frontCnvnChangeParam);
	}

	/****************************************협약변경관리  변경요청 조회(사업년도, 과제명)****************************************************/
	/**
	 * 협약변경관리  변경요청 사업년도 조회
	 *
	 */
	@GetMapping("/bsnsYear")
	public JsonList<String>selectCnvnChangeReqBsnsYear(){
		return new JsonList<>(frontCnvnChangeService.selectCnvnChangeReqBsnsYear());
	}

	/**
	 * 협약변경관리  변경요청 과제명 조회
	 * bsnsYear
	 */
	@GetMapping("/taskNm")
	public JsonList <String> selectCnvnChangeReqTaskNm(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectCnvnChangeReqTaskNm : {}", frontCnvnChangeParam);
		return new JsonList<>(frontCnvnChangeService.selectCnvnChangeReqTaskNm(frontCnvnChangeParam));
	}

	/**
	 * 협약변경관리  변경요청 업체조회
	 * bsnsYear
	 */
	@GetMapping("/entrpsNm")
	public JsonList <UsptTaskPrtcmpny> selectCnvnChangeEntrpsNm(FrontCnvnChangeParam frontCnvnChangeParam){
		log.debug("#####	selectCnvnChangeEntrpsNm : {}", frontCnvnChangeParam);
		return new JsonList<>(frontCnvnChangeService.selectCnvnChangeEntrpsNm(frontCnvnChangeParam));
	}

	/**
	 * 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	@GetMapping("/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		frontCnvnChangeService.getOneFileDown(response, attachmentId);
	}
}