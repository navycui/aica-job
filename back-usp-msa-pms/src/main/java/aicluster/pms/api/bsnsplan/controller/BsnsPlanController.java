package aicluster.pms.api.bsnsplan.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.bsnsplan.dto.BsnsPlanDto;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanParam;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanResnDto;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanResnParam;
import aicluster.pms.api.bsnsplan.service.BsnsPlanService;
import aicluster.pms.common.entity.UsptBsnsPlanDoc;
import aicluster.pms.common.entity.UsptBsnsPlanProcessHist;
import aicluster.pms.common.entity.UsptTaskTaxitmWct;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 사업계획서 접수관리 admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/bsns-plan")
public class BsnsPlanController {

	@Autowired
	private BsnsPlanService bsnsPlanService;


	/**
	 * 사업계획서 목록조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptBsnsPlanDoc> getList(BsnsPlanParam bsnsPlanParam, @RequestParam(defaultValue = "1") Long page){
		return bsnsPlanService.getList(bsnsPlanParam, page);
	}

	/**
	 * 사업계획서목록 제출파일 전체 다운
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/all-file-dwln")
	public void getAllFileDown(HttpServletResponse response, BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getAllFileDown : {}", bsnsPlanParam);
		bsnsPlanService.getAllFileDown(response, bsnsPlanParam);
	}

	/**
	 * 사업계획서 목록 엑셀 다운로드
	 * @param expertReqstListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcel(BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getListExcel : {}", bsnsPlanParam);
		List<UsptBsnsPlanDoc> list = bsnsPlanService.getListExcel(bsnsPlanParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("사업계획서접수 관리 목록");
		ExcelSheet<UsptBsnsPlanDoc> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "처리상태",   "접수번호",    "과제명"	, "사업년도"	, "사업명"	,"공고명"	, "회원명"	, "제출일");
		sheet.setProperties("rn"	, "planPresentnSttusNm"	,"receiptNo"	, "taskNmKo"	, "bsnsYear"	, "bsnsNm"	, "pblancNm"	, "memberNm"	, "presentnDy");
		sheet.setTitle("사업계획서접수 관리 목록");
		sheet.setSheetName("사업계획서접수 관리 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 사업계획서 상세 조회
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/detail-info")
	public BsnsPlanDto getBsnsPlanDocInfo(HttpServletRequest request, BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getBsnsPlanDocInfo : {}", bsnsPlanParam);
		return bsnsPlanService.getBsnsPlanDocInfo(request, bsnsPlanParam);
	}

	/**
	 * 사업계획서 제출파일 일괄 다운
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/file-dwln")
	public void getPrsentAllFileDown(HttpServletResponse response, BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getPrsentAllFileDown : {}", bsnsPlanParam);
		bsnsPlanService.getPrsentAllFileDown(response, bsnsPlanParam);
	}

	/**
	 * 사업계획서첨부파일 단건 다운
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/file-dwln-one")
	public void getPrsentFileDownOne(HttpServletResponse response, BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getPrsentFileDownOne : {}", bsnsPlanParam);
		bsnsPlanService.getPrsentFileDownOne(response, bsnsPlanParam);
	}

	/**
	 * 사업계획서 처리이력
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/hist")
	public JsonList<UsptBsnsPlanProcessHist> getBsnsPlanHistList(BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getBsnsPlanHistList : {}", bsnsPlanParam);
		return new JsonList<>(bsnsPlanService.getBsnsPlanHistList(bsnsPlanParam));
	}

	/**
	 * 사업계획서 사유 확인팝업
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/resn")
	public BsnsPlanResnDto getResnInfo(BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getResnInfo : {}", bsnsPlanParam);
		 return bsnsPlanService.getResnInfo(bsnsPlanParam);
	}

	/**
	 * 사업계획서 사유 확인팝업-전송
	 * @param bsnsPlanParam
	 * @return
	 */
	@PutMapping("/resn/send")
	public void modifyResnInfo( @RequestPart(value = "info") BsnsPlanResnParam bsnsPlanResnParam
			, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyResnInfo : {}", bsnsPlanResnParam);
		bsnsPlanService.modifyResnInfo( bsnsPlanResnParam, fileList);
	}

	/**
	 * 사업계획서 승인, 승인취소
	 * @param BsnsPlanParam
	 * @return
	 */
	@PutMapping("/approval")
	public void modifyPlanSttus(@RequestBody(required = false) BsnsPlanParam bsnsPlanParam){
		log.debug("#####	modifyPlanSttus : {}", bsnsPlanParam);
		bsnsPlanService.modifyPlanSttus(bsnsPlanParam);
	}

	/**
	 * 사업계획서 비목별 사업비 구성팝업_조회
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/taxitm-wct")
	public JsonList<UsptTaskTaxitmWct> getTaxitmWct(BsnsPlanParam bsnsPlanParam){
		log.debug("#####	getTaxitmWct : {}", bsnsPlanParam);
		return new JsonList<>(bsnsPlanService.getTaxitmWct(bsnsPlanParam));
	}
}