package aicluster.pms.api.reprt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.reprt.dto.PresentnReqParam;
import aicluster.pms.api.reprt.dto.ReprtListParam;
import aicluster.pms.api.reprt.dto.ReprtMakeupDto;
import aicluster.pms.api.reprt.dto.ReprtMakeupParam;
import aicluster.pms.api.reprt.service.ReprtService;
import aicluster.pms.common.dto.ReprtDto;
import aicluster.pms.common.dto.ReprtListDto;
import aicluster.pms.common.entity.UsptReprtHist;
import aicluster.pms.common.util.Code;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 보고서관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/reprt")
public class ReprtController {

	@Autowired
	private ReprtService reprtService;

	/**
	 * 중간보고서 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("/interim")
	public CorePagination<ReprtListDto> getInterimList(ReprtListParam param, @RequestParam(defaultValue = "1") Long page) {
		param.setReprtTypeCd(Code.reprtType.중간보고서);
		return reprtService.getList(param, page);
	}

	/**
	 * 중간보고서 목록 엑셀 다운로드
	 * @param param
	 * @return
	 */
	@GetMapping("/interim/excel-dwld")
	public ModelAndView getInterimListExcelDwld(ReprtListParam param){
		param.setReprtTypeCd(Code.reprtType.중간보고서);
		List<ReprtListDto> list = reprtService.getListExcelDwld(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("중간보고서");
		ExcelSheet<ReprtListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "접수번호", "과제명", "사업연도" , "사업명", "회원명", "제출일");
		sheet.setProperties("reprtSttus", "receiptNo", "taskNm", "bsnsYear", "bsnsNm", "memberNm", "presentnDate");
		sheet.setTitle("중간보고서 목록");
		sheet.setSheetName("중간보고서");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param param
	 */
	@GetMapping("/interim/atchmnfl-dwld")
	public void getInterimAtchmnflDwld(HttpServletResponse response, ReprtListParam param){
		param.setReprtTypeCd(Code.reprtType.중간보고서);
		reprtService.getAtchmnflDwld(response, param);
	}

	/**
	 * 중간보고서 일괄 제출요청
	 * @param list
	 */
	@PostMapping("/interim/presentn-req")
	public CorePagination<ReprtListDto> modifyInterimPresentnReq(@RequestBody PresentnReqParam param) {
		reprtService.addPresentnReq(param, Code.reprtType.중간보고서);
		ReprtListParam listParam = new ReprtListParam();
		listParam.setReprtTypeCd(Code.reprtType.중간보고서);
		return reprtService.getList(listParam, null);
	}

	/**
	 * 결과보고서 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("/result")
	public CorePagination<ReprtListDto> getResultList(ReprtListParam param, @RequestParam(defaultValue = "1") Long page) {
		param.setReprtTypeCd(Code.reprtType.결과보고서);
		return reprtService.getList(param, page);
	}

	/**
	 * 결과보고서 목록 엑셀 다운로드
	 * @param param
	 * @return
	 */
	@GetMapping("/result/excel-dwld")
	public ModelAndView getResultListExcelDwld(ReprtListParam param){
		param.setReprtTypeCd(Code.reprtType.결과보고서);
		List<ReprtListDto> list = reprtService.getListExcelDwld(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("결과보고서");
		ExcelSheet<ReprtListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "접수번호", "과제명", "사업연도" , "사업명", "회원명", "제출일");
		sheet.setProperties("reprtSttus", "receiptNo", "taskNm", "bsnsYear", "bsnsNm", "memberNm", "presentnDate");
		sheet.setTitle("중간보고서 목록");
		sheet.setSheetName("중간보고서");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param param
	 */
	@GetMapping("/result/atchmnfl-dwld")
	public void getResultAtchmnflDwld(HttpServletResponse response, ReprtListParam param){
		param.setReprtTypeCd(Code.reprtType.결과보고서);
		reprtService.getAtchmnflDwld(response, param);
	}

	/**
	 * 결과보고서 일괄 제출요청
	 * @param list
	 * @return
	 */
	@PostMapping("/result/presentn-req")
	public CorePagination<ReprtListDto> modifyResultPresentnReq(@RequestBody PresentnReqParam param) {
		reprtService.addPresentnReq(param, Code.reprtType.결과보고서);
		ReprtListParam listParam = new ReprtListParam();
		listParam.setReprtTypeCd(Code.reprtType.결과보고서);
		return reprtService.getList(listParam, null);
	}


	/**
	 * 보고서 상세조회
	 * @param reprtId
	 * @return
	 */
	@GetMapping("/{reprtId}")
	public ReprtDto get(HttpServletRequest request, @PathVariable("reprtId") String reprtId){
		return reprtService.get(request, reprtId);
	}

	/**
	 * 보완요청 조회
	 * @param reprtId
	 * @return
	 */
	@GetMapping("/{reprtId}/makeup")
	public ReprtMakeupDto getMakeup(@PathVariable("reprtId") String reprtId) {
		return reprtService.getMakeup(reprtId);
	}

	/**
	 * 보완요청
	 * @param reprtId
	 * @param makeupInfo
	 * @param fileList
	 */
	@PutMapping("/{reprtId}/makeup")
	public void modifyMakeup(@PathVariable("reprtId") String reprtId
							, @RequestPart("makeupInfo") ReprtMakeupParam makeupInfo
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList){
		reprtService.makeupInfo(reprtId, makeupInfo, fileList);
	}

	/**
	 * 접수완료
	 * @param reprtId
	 */
	@PutMapping("/{reprtId}/receipt")
	public ReprtDto modifyReceipt(HttpServletRequest request, @PathVariable("reprtId") String reprtId){
		reprtService.modifyReprtSttus(reprtId, Code.reprtSttus.접수완료);
		return reprtService.get(request, reprtId);
	}


	/**
	 * 접수완료취소
	 * @param reprtId
	 * @return
	 */
	@PutMapping("/{reprtId}/receipt-cancel")
	public ReprtDto modifyReceiptCancel(HttpServletRequest request, @PathVariable("reprtId") String reprtId){
		reprtService.modifyReprtSttus(reprtId, Code.reprtSttus.제출);
		return reprtService.get(request, reprtId);
	}


	/**
	 * 첨부파일 다운로드
	 * @param reprtId
	 * @param response
	 * @param attachmentId
	 */
	@GetMapping("/{reprtId}/atchmnfl/{attachmentId}")
	public void getAtchmnflDwld(HttpServletResponse response, @PathVariable("reprtId") String reprtId, @PathVariable("attachmentId") String attachmentId) {
		reprtService.getAtchmnflDwld(response, reprtId, attachmentId);
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param reprtId
	 * @param response
	 */
	@GetMapping("/{reprtId}/atchmnfl")
	public void getAtchmnfl(@PathVariable("reprtId") String reprtId, HttpServletResponse response) {
		reprtService.getAtchmnflAllDwld(response, reprtId);
	}

	/**
	 * 처리이력 조회
	 * @param reprtId
	 * @return
	 */
	@GetMapping("/{reprtId}/hist")
	public JsonList<UsptReprtHist>  getHistList(@PathVariable("reprtId") String reprtId){
		return reprtService.getHistList(reprtId);
	}


}
