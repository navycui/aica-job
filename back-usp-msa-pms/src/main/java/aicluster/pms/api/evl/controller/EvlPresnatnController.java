package aicluster.pms.api.evl.controller;

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

import aicluster.pms.api.evl.service.EvlPresnatnService;
import aicluster.pms.common.dto.EvlPresnatnListDto;
import aicluster.pms.common.dto.EvlPresnatnRequstDto;
import aicluster.pms.common.entity.UsptEvlTrgetPresentnHist;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/evl-presnatn")
public class EvlPresnatnController {

	@Autowired
	private EvlPresnatnService evlPresnatnService;

	//발표자료 대상 목록조회
	@GetMapping("/target-list")
	public CorePagination<EvlPresnatnListDto> getPresnatnTargetList(EvlPresnatnListDto evlPresnatnListDto, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	evlPresnatnListDto : {}", evlPresnatnListDto.toString());
		return evlPresnatnService.getPresnatnTargetList(evlPresnatnListDto, page);
	}


	//발표자료 대상 목록 엑셀 다운로드
	@GetMapping("/target-list/excel-dwld")
	public ModelAndView getPresnatnTargetListExcel(EvlPresnatnListDto evlPresnatnListDto){
		List<EvlPresnatnListDto> list = evlPresnatnService.getPresnatnTargetListExcel(evlPresnatnListDto);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("발표자료 대상 목록");
		ExcelSheet<EvlPresnatnListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "처리상태"						,"접수번호"	, "사업년도"	, "공고명"			, "모집유형"			,"평가단계"		, "접수차수"	, "제출일");
		sheet.setProperties("rn"	, "presnatnProcessDivNm"		,"receiptNo"	, "bsnsYear"	, "pblancNm"	, "ordtmRcritNm"	, "evlStepNm"	, "rceptOdr"	, "fmtPresentnDt");
		sheet.setTitle("발표자료 대상 목록");
		sheet.setSheetName("발표자료 대상 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	//발표자료 대상 상세조회
	@GetMapping("/{evlTrgetId}")
	public EvlPresnatnListDto get(HttpServletRequest request, @PathVariable String evlTrgetId) {
		return evlPresnatnService.get(request, evlTrgetId);
	}


	//제출요청
	@PutMapping("/presentn-req")
	public CorePagination<EvlPresnatnListDto> modifyPresnatnRequst(@RequestBody EvlPresnatnRequstDto evlPresnatnRequstDto) {
		log.debug("#####	evlPresnatnRequstDto : {}", evlPresnatnRequstDto.toString());

		evlPresnatnService.modifyPresnatnRequst(evlPresnatnRequstDto);

		//재조회
		EvlPresnatnListDto evlPresnatnListDto = new EvlPresnatnListDto();
		return evlPresnatnService.getPresnatnTargetList(evlPresnatnListDto, 1l);
	}


	//보완요청
	@PutMapping("/{evlTrgetId}/presentn-re-req")
	public void modifyPresnatnReRequst(@PathVariable String evlTrgetId, @RequestBody UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist) {
		log.debug("#####	usptEvlTrgetPresentnHist : {}", usptEvlTrgetPresentnHist.toString());
		log.debug("#####	evlTrgetId : {}", evlTrgetId);

		usptEvlTrgetPresentnHist.setEvlTrgetId(evlTrgetId);
		evlPresnatnService.modifyPresnatnReRequst(usptEvlTrgetPresentnHist);

		//재조회
//		return evlPresnatnService.get(evlTrgetId);
	}


	//접수완료
	@PutMapping("/{evlTrgetId}/presentn-rcept")
	public void modifyPresnatnRcept(@PathVariable String evlTrgetId, @RequestBody UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist) {
		log.debug("#####	usptEvlTrgetPresentnHist : {}", usptEvlTrgetPresentnHist.toString());
		log.debug("#####	evlTrgetId : {}", evlTrgetId);

		usptEvlTrgetPresentnHist.setEvlTrgetId(evlTrgetId);
		evlPresnatnService.modifyPresnatnRcept(usptEvlTrgetPresentnHist);

		//재조회
//		return evlPresnatnService.get(evlTrgetId);
	}


	//접수완료 취소
	@PutMapping("/{evlTrgetId}/presentn-cancl")
	public void  modifyPresnatnCancl(@PathVariable String evlTrgetId, @RequestBody UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist) {
		log.debug("#####	usptEvlTrgetPresentnHist : {}", usptEvlTrgetPresentnHist.toString());
		log.debug("#####	evlTrgetId : {}", evlTrgetId);

		usptEvlTrgetPresentnHist.setEvlTrgetId(evlTrgetId);
		evlPresnatnService.modifyPresnatnCancl(usptEvlTrgetPresentnHist);

		//재조회
//		return evlPresnatnService.get(evlTrgetId);
	}


	//발표자료 처리이력 상세조회
	@GetMapping("/{evlTrgetId}/hist")
	public JsonList<UsptEvlTrgetPresentnHist> getHistList(@PathVariable String evlTrgetId) {
		return new JsonList<>(evlPresnatnService.getHistList(evlTrgetId));
	}

	/**
	 * 발표자료 목록 제출파일 전체 다운
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/target-list/all-file-dwld")
	public void getTargetListAllFileDown(HttpServletResponse response, EvlPresnatnListDto evlPresnatnListDto){
		log.debug("#####	getListAllFileDown : {}", evlPresnatnListDto);
		evlPresnatnService.getTargetListAllFileDown(response, evlPresnatnListDto);
	}


	/**
	 * 발표자료 -파일 일괄다운로드
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/all-file-dwld/{attachmentGroupId}")
	public void getAllFileDown(HttpServletResponse response, @PathVariable("attachmentGroupId") String attachmentGroupId){
		log.debug("#####	getAllFileDown : {}", attachmentGroupId);
		evlPresnatnService.getAllFileDown(response, attachmentGroupId);
	}

	/**
	 * 사업계획서 -파일 단건 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		evlPresnatnService.getOneFileDown(response, attachmentId);
	}
}
