package aicluster.pms.api.slctnObjc.controller;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.slctnObjc.dto.DlbrtParam;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqst;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqstListParam;
import aicluster.pms.api.slctnObjc.service.SlctnObjcService;
import aicluster.pms.common.dto.HistDto;
import aicluster.pms.common.dto.SlctnObjcDto;
import aicluster.pms.common.dto.SlctnObjcReqstListDto;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 이의신청접수 관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/slctn-objc")
public class SlctnObjcController {

	@Autowired
	private SlctnObjcService slctnObjcService;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<SlctnObjcReqstListDto> getList(SlctnObjcReqstListParam param, @RequestParam(defaultValue = "1") Long page) {
		return slctnObjcService.getList(param, page);
	}

	/**
	 * 엑셀저장
	 * @param param
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getExcelDwld(SlctnObjcReqstListParam param){
		List<SlctnObjcReqstListDto> list = slctnObjcService.getListExcelDwld(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("이의신청접수 관리");
		ExcelSheet<SlctnObjcReqstListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "회원명", "사업연도" , "공고명", "모집유형", "접수차수", "담당부서", "담당자", "접수일");
		sheet.setProperties("lastSlctnObjcProcessSttus", "memberNm", "bsnsYear", "pblancNm", "ordtmRcrit", "rceptOdr", "deptNm", "chargerNm", "rceptDate");
		sheet.setTitle("이의신청접수 관리 목록");
		sheet.setSheetName("이의신청접수 관리");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 상세조회
	 * @param objcReqstId
	 * @return
	 */
	@GetMapping("/{objcReqstId}")
	public SlctnObjcDto get(HttpServletRequest request, @PathVariable("objcReqstId") String objcReqstId) {
		return slctnObjcService.get(request, objcReqstId);
	}

	/**
	 * 접수완료
	 * @param objcReqstId
	 */
	@PutMapping("/{objcReqstId}/receipt")
	public SlctnObjcDto modifyReceipt(HttpServletRequest request, @PathVariable("objcReqstId") String objcReqstId) {
		slctnObjcService.modifyReceipt(objcReqstId);
		return slctnObjcService.get(request, objcReqstId);
	}


	/**
	 * 반려
	 * @param objcReqstId
	 * @param info
	 */
	@PutMapping("/{objcReqstId}/reject")
	public SlctnObjcDto modifyReject(HttpServletRequest request, @PathVariable("objcReqstId") String objcReqstId, @RequestBody SlctnObjcReqst info) {
		slctnObjcService.modifyReject(objcReqstId, info.getRejectReasonCn());
		return slctnObjcService.get(request, objcReqstId);
	}


	/**
	 * 심의완료
	 * @param objcReqstId
	 * @param param
	 * @param fileList
	 */
	@PutMapping("/{objcReqstId}/dlbrt")
	public SlctnObjcDto modifyDlbrt(HttpServletRequest request
			, @PathVariable("objcReqstId") String objcReqstId
			, @RequestPart("info") DlbrtParam param
			, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		slctnObjcService.modifyDlbrt(objcReqstId, param, fileList);
		return slctnObjcService.get(request, objcReqstId);
	}


	/**
	 * 이의신청 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	@GetMapping("/{objcReqstId}/objc/atchmnfl/{attachmentId}")
	public void getObjcAtchmnfl(HttpServletResponse response
							, @PathVariable("objcReqstId") String objcReqstId
							, @PathVariable("attachmentId") String attachmentId) {
		slctnObjcService.getObjcAtchmnfl(response, objcReqstId, attachmentId);
	}

	/**
	 * 이의신청 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	@GetMapping("/{objcReqstId}/objc/atchmnfl")
	public void getObjcAtchmnflAll(HttpServletResponse response, @PathVariable("objcReqstId") String objcReqstId) {
		slctnObjcService.getObjcAtchmnflAll(response, objcReqstId);
	}




	/**
	 * 심의결과 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	@GetMapping("/{objcReqstId}/dlbrt/atchmnfl/{attachmentId}")
	public void getDlbrtAtchmnfl(HttpServletResponse response
							, @PathVariable("objcReqstId") String objcReqstId
							, @PathVariable("attachmentId") String attachmentId) {
		slctnObjcService.getDlbrtAtchmnfl(response, objcReqstId, attachmentId);
	}


	/**
	 * 심의결과 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	@GetMapping("/{objcReqstId}/dlbrt/atchmnfl")
	public void getDlbrtAtchmnflAll(HttpServletResponse response, @PathVariable("objcReqstId") String objcReqstId) {
		slctnObjcService.getDlbrtAtchmnflAll(response, objcReqstId);
	}



	/**
	 * 이력조회
	 * @param objcReqstId
	 * @return
	 */
	@GetMapping("/{objcReqstId}/hist")
	public JsonList<HistDto> getHist(@PathVariable("objcReqstId") String objcReqstId) {
		return slctnObjcService.getHist(objcReqstId);
	}

}
