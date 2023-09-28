package aicluster.pms.api.bsnsapply.controller;

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

import aicluster.pms.api.bsnsapply.dto.BsnsApplyBhExmntDto;
import aicluster.pms.api.bsnsapply.dto.BsnsApplyBhExmntParam;
import aicluster.pms.api.bsnsapply.dto.BsnsApplyListParam;
import aicluster.pms.api.bsnsapply.dto.SamenssRateParam;
import aicluster.pms.api.bsnsapply.service.BsnsApplyService;
import aicluster.pms.common.dto.BsnsApplyDto;
import aicluster.pms.common.dto.BsnsApplyListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntHist;
import aicluster.pms.common.entity.UsptBsnsPblancApplyDplctAtchmnfl;
import aicluster.pms.common.util.Code;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 사업신청 관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/bsns-apply")
public class BsnsApplyController {

	@Autowired
	BsnsApplyService bsnsApplyService;


	/**
	 * 사업신청 목록 조회
	 * @param bsnsApplyListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<BsnsApplyListDto> getList(BsnsApplyListParam bsnsApplyListParam, @RequestParam(defaultValue = "1") Long page){
		return bsnsApplyService.getList(bsnsApplyListParam, page, false);
	}

	/**
	 * 사업신청 목록 엑셀 저장
	 * @param bsnsApplyListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(BsnsApplyListParam bsnsApplyListParam){
		List<BsnsApplyListDto> list = bsnsApplyService.getListExcelDwld(bsnsApplyListParam, false);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("신청접수목록");
		ExcelSheet<BsnsApplyListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("접수번호", "신청상태", "사업연도", "사업명" , "공고명", "접수상태", "회원명", "과제명", "접수일");
		sheet.setProperties("receiptNo", "rceptSttus", "bsnsYear", "bsnsNm", "pblancNm", "pblancSttus", "memberNm", "taskNm", "rceptDt");
		sheet.setTitle("신청접수목록");
		sheet.setSheetName("신청접수");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 상시접수 목록 조회
	 * @param bsnsApplyListParam
	 * @param page
	 * @return
	 */
	@GetMapping("/ordinary")
	public CorePagination<BsnsApplyListDto> getOrdinaryList(BsnsApplyListParam bsnsApplyListParam, @RequestParam(defaultValue = "1") Long page){
		return bsnsApplyService.getList(bsnsApplyListParam, page, true);
	}

	/**
	 * 상시접수 목록 엑셀 저장
	 * @param bsnsApplyListParam
	 * @return
	 */
	@GetMapping("/ordinary/excel-dwld")
	public ModelAndView getOrdinaryListExcelDwld(BsnsApplyListParam bsnsApplyListParam){
		List<BsnsApplyListDto> list = bsnsApplyService.getListExcelDwld(bsnsApplyListParam, true);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("신청접수목록");
		ExcelSheet<BsnsApplyListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("접수번호", "신청상태", "사업연도", "사업명" , "공고명", "접수상태", "접수차수", "회원명", "과제명", "접수일");
		sheet.setProperties("receiptNo", "rceptSttus", "bsnsYear", "bsnsNm", "pblancNm", "pblancSttus", "rceptOdr", "memberNm", "taskNm", "rceptDt");
		sheet.setTitle("신청접수목록");
		sheet.setSheetName("신청접수");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 사업신청 일괄 접수완료 처리
	 * @param applyIdList
	 */
	@PutMapping("/receipt")
	public void modifyReceipt(@RequestBody List<UsptBsnsPblancApplcnt> applyIdList) {
		bsnsApplyService.modifyReceipt(applyIdList);
	}


	/**
	 * 사업신청 접수관리 상세조회
	 * @param request
	 * @param applyId
	 */
	@GetMapping("/{applyId}")
	public BsnsApplyDto get(HttpServletRequest request, @PathVariable("applyId") String applyId) {
		return bsnsApplyService.get(request, applyId);
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param applyId
	 * @param attachmentId
	 */
	@GetMapping("/{applyId}/atchmnfl/{attachmentId}")
	public void getFileDownload(HttpServletResponse response, @PathVariable("applyId") String applyId, @PathVariable("attachmentId") String attachmentId) {
		bsnsApplyService.getFileDownload(response, applyId, attachmentId, "");
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param response
	 * @param applyId
	 * @param attachmentId
	 */
	@GetMapping("/{applyId}/atchmnfl")
	public void getFileDownloadAll(HttpServletResponse response, @PathVariable("applyId") String applyId) {
		bsnsApplyService.getFileDownload(response, applyId, null, "ALL");
	}


	/**
	 * 사업신청 반려
	 * @param applyId
	 */
	@PutMapping("/{applyId}/reject")
	public void modifyReject(@PathVariable("applyId") String applyId, @RequestBody UsptBsnsPblancApplcntHist hist) {
		bsnsApplyService.modifyRceptSttus(applyId, Code.rceptSttus.반려, hist.getProcessCn());
	}


	/**
	 * 사업신청 보완요청
	 * @param applyId
	 * @param processCn
	 */
	@PutMapping("/{applyId}/makeup")
	public void modifyMakeup(@PathVariable("applyId") String applyId, @RequestBody UsptBsnsPblancApplcntHist hist) {
		bsnsApplyService.modifyRceptSttus(applyId, Code.rceptSttus.보완요청, hist.getProcessCn());
	}

	/**
	 * 사업신청 접수완료
	 * @param applyId
	 */
	@PutMapping("/{applyId}/receipt")
	public void modifyReceipt(@PathVariable("applyId") String applyId) {
		bsnsApplyService.modifyRceptSttus(applyId, Code.rceptSttus.접수완료, Code.histMessage.접수완료);
	}

	/**
	 * 사업신청 접수완료 취소
	 * @param applyId
	 */
	@PutMapping("/{applyId}/receipt-cancel")
	public void modifyReceiptCancel(@PathVariable("applyId") String applyId) {
		bsnsApplyService.modifyRceptSttus(applyId, Code.rceptSttus.신청, Code.histMessage.접수완료취소);
	}


	/**
	 * 사업신청 사전검토 조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/{applyId}/bh-exmnt")
	public BsnsApplyBhExmntDto getBhExmnt(@PathVariable("applyId") String applyId) {
		return bsnsApplyService.getBhExmnt(applyId);
	}


	/**
	 * 문서 동일비율 조회 및 저장
	 * @param applyId
	 * @return
	 */
	@PutMapping("/{applyId}/dplct-cnfirm/{attachmentId}")
	public BsnsApplyBhExmntDto modifySamenssRate(@PathVariable("applyId") String applyId, @PathVariable("attachmentId") String attachmentId) {
		bsnsApplyService.modifySamenssRate(applyId, attachmentId);
		return bsnsApplyService.getBhExmnt(applyId);
	}

	/**
	 * 문서 동일비율 조회 목록
	 * @param applyId
	 * @return
	 */
	@GetMapping("/dplct-cnfirm-list")
	public CorePagination<UsptBsnsPblancApplyDplctAtchmnfl> getSamenssRateList(SamenssRateParam samenssRateParam, @RequestParam(defaultValue = "1") Long page){
		return bsnsApplyService.getSamenssRateList(samenssRateParam,page);
	}


	/**
	 * 사업신청 사전검토 저장/수정
	 * @param applyId
	 * @param bsnsApplyBhExmntParam
	 */
	@PutMapping("/{applyId}/bh-exmnt")
	public void modifyBhExmnt(@PathVariable("applyId") String applyId, @RequestBody BsnsApplyBhExmntParam bsnsApplyBhExmntParam) {
		bsnsApplyService.modifyBhExmnt(applyId, bsnsApplyBhExmntParam);
	}

	/**
	 * 사업신청 처리이력 조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/{applyId}/hist")
	public JsonList<UsptBsnsPblancApplcntHist> getHist(@PathVariable("applyId") String applyId) {
		return bsnsApplyService.getHist(applyId);
	}

}
