package aicluster.pms.api.bsnsplan.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanDto;
import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanParam;
import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanResnDto;
import aicluster.pms.api.bsnsplan.dto.TaskTaxitmWctDto;
import aicluster.pms.api.bsnsplan.service.FrontBsnsPlanService;
import aicluster.pms.common.dto.CmmtMemberBizDto;
import aicluster.pms.common.entity.UsptBsnsPlanDoc;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.entity.UsptTaskTaxitmWct;
import aicluster.pms.common.util.Code;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import lombok.extern.slf4j.Slf4j;

/**
 * 사업계획서 접수관리 front
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/front/bsns-plan")
public class FrontBsnsPlanController {

	@Autowired
	private FrontBsnsPlanService frontBsnsPlanService;


	/**
	 * 사업계획서 목록조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptBsnsPlanDoc> getList(FrontBsnsPlanParam frontBsnsPlanParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getList : {}", frontBsnsPlanParam);
		return frontBsnsPlanService.getList(frontBsnsPlanParam, page);
	}



	/**
	 * 사업계획서 사유 확인팝업
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/resn")
	public FrontBsnsPlanResnDto getResnInfo(FrontBsnsPlanParam frontBsnsPlanParam){
		log.debug("#####	getResnInfo : {}", frontBsnsPlanParam);
		 return frontBsnsPlanService.getResnInfo(frontBsnsPlanParam);
	}

	/**
	 * 사업계획서 사유 확인팝업-파일 전체 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/resn/grp-file-dwln/{makeupReqFileGroupId}")
	public void getAllFileDown(HttpServletResponse response, @PathVariable("makeupReqFileGroupId") String makeupReqFileGroupId){
		log.debug("#####	getAllFileDown : {}", makeupReqFileGroupId);
		frontBsnsPlanService.getAllFileDown(response, makeupReqFileGroupId);
	}


	/**
	 * 사업계획서 상세 조회
	 * @param frontBsnsPlanParam(receiptNo:사업공고신청자 접수번호)
	 * @return
	 */
	@GetMapping("/detail-info")
	public FrontBsnsPlanDto getBsnsPlanDocInfo(FrontBsnsPlanParam frontBsnsPlanParam){
		log.debug("#####	getBsnsPlanDocInfo : {}", frontBsnsPlanParam);
		return frontBsnsPlanService.getBsnsPlanDocInfo(frontBsnsPlanParam);
	}


	/**
	 * 사업계획서 비목별 사업비 구성팝업_조회
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/taxitm-wct")
	public JsonList<UsptTaskTaxitmWct> getTaxitmWct(FrontBsnsPlanParam frontBsnsPlanParam){
		log.debug("#####	getTaxitmWct : {}", frontBsnsPlanParam);
		return new JsonList<>(frontBsnsPlanService.getTaxitmWct(frontBsnsPlanParam));
	}

	/**
	 * 사업계획서 비목별 사업비 구성팝업_저장
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@PostMapping("/taxitm-wct/save")
	public void addTaxitmWct( @RequestBody TaskTaxitmWctDto taskTaxitmWctDto){
		log.debug("#####	addTaxitmWct : {}", taskTaxitmWctDto);
		frontBsnsPlanService.addTaxitmWct(taskTaxitmWctDto);
	}

	/**
	 * 사업계획서 임시저장
	 * @param applyId
	 * @return
	 */
	@PutMapping("/{planPresentnSttusCd}/tmp-save")
	public FrontBsnsPlanDto modifyPlanTmp(@PathVariable("planPresentnSttusCd") String planPresentnSttusCd
													   ,@RequestPart(value = "info") FrontBsnsPlanDto frontBsnsPlanDto
													   ,@RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyPlanTmp : {}", frontBsnsPlanDto);

		String bsnsPlanDocId = 	frontBsnsPlanService.modifyBsnsPlan(Code.planPresentnSttus.미제출 , frontBsnsPlanDto, fileList);

		FrontBsnsPlanParam frontBsnsPlanParam = new FrontBsnsPlanParam();
		frontBsnsPlanParam.setBsnsPlanDocId(bsnsPlanDocId);
		frontBsnsPlanParam.setBsnsSlctnId(frontBsnsPlanDto.getUsptBsnsPlanDoc().getBsnsSlctnId());
		return frontBsnsPlanService.getBsnsPlanDocInfo(frontBsnsPlanParam);

	}

	/**
	 * 사업계획서 저장
	 * @param applyId
	 * @return
	 */
	@PutMapping("/{planPresentnSttusCd}/save")
	public void modifyPlan(@PathVariable("planPresentnSttusCd") String planPresentnSttusCd
			, @RequestPart(value = "info") FrontBsnsPlanDto frontBsnsPlanDto
			, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontBsnsPlanService.modifyBsnsPlan(Code.planPresentnSttus.제출 , frontBsnsPlanDto, fileList);
	}

	/**
	 * 사업계획서 참여기업 조회
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/task-prtcmpny/{bsnsPlanDocId}")
	public JsonList<UsptTaskPrtcmpny> selectUsptTaskPrtcmpnyList( @PathVariable("bsnsPlanDocId") String bsnsPlanDocId){
		log.debug("#####	bsnsPlanDocId : {}", bsnsPlanDocId);
		return new JsonList<>(frontBsnsPlanService.selectUsptTaskPrtcmpnyList(bsnsPlanDocId));
	}

	/**
	 * 사업계획서  기업검색 팝업
	 * @return
	 */
	@GetMapping("/bizList")
	public CorePagination<CmmtMemberBizDto> selectBizList(FrontBsnsPlanParam frontBsnsPlanParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getList : {}", frontBsnsPlanParam);
		return frontBsnsPlanService.selectBizList(frontBsnsPlanParam, page);
	}

	/**
	 * 사업계획서 -파일 단건 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		frontBsnsPlanService.getOneFileDown(response, attachmentId);
	}

}