package aicluster.pms.api.excclc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.excclc.dto.ExcclcListParam;
import aicluster.pms.api.excclc.dto.ExcclcParam;
import aicluster.pms.api.excclc.service.ExcclcService;
import aicluster.pms.common.dto.ExcclcDto;
import aicluster.pms.common.dto.ExcclcListDto;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 정산관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/excclc")
public class ExcclcController {

	@Autowired
	private ExcclcService excclcService;

	/**
	 * 정산관리 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<ExcclcListDto> getList(ExcclcListParam param, @RequestParam(defaultValue = "1") Long page) {
		return excclcService.getList(param, page);
	}

	/**
	 * 정산관리 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getInterimListExcelDwld(ExcclcListParam param){
		List<ExcclcListDto> list = excclcService.getListExcelDwld(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("정산관리");
		ExcelSheet<ExcclcListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "정산일", "신청예산 사업연도", "접수번호", "과제명", "사업연도" , "사업명", "회원명", "사업자등록번호", "협약시작일", "협약종료일", "협약총액");
		sheet.setProperties("processSttus", "excclcDate", "reqstBsnsYear", "receiptNo", "taskNm", "bsnsYear", "bsnsNm", "memberNm", "bizrno", "cnvnBgnDate", "cnvnEndDate", "totalWct");
		sheet.setTitle("정산관리 목록");
		sheet.setSheetName("정산관리");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 정산내역 상세조회
	 * @param bsnsCnvnId
	 * @param taskReqstWctId
	 * @return
	 */
	@GetMapping("/{bsnsCnvnId}")
	public ExcclcDto get(HttpServletRequest request,  @PathVariable("bsnsCnvnId") String bsnsCnvnId, String taskReqstWctId) {
		return excclcService.get(request, bsnsCnvnId, taskReqstWctId);
	}

	/**
	 * 정산내역 저장
	 * @param bsnsCnvnId
	 * @param taskReqstWctId
	 * @param info
	 * @param fileList
	 * @return
	 */
	@PutMapping("/{bsnsCnvnId}")
	public ExcclcDto modify(HttpServletRequest request
							, @PathVariable("bsnsCnvnId") String bsnsCnvnId
							, @RequestPart(value = "info") ExcclcParam info
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		excclcService.modify(bsnsCnvnId, info, fileList);
		return excclcService.get(request, bsnsCnvnId, info.getTaskReqstWctId());
	}
}
