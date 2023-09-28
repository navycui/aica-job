package aicluster.pms.api.cnvntrmnat.controller;

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
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.cnvntrmnat.dto.CnvnTrmnatDto;
import aicluster.pms.api.cnvntrmnat.dto.CnvnTrmnatHistDto;
import aicluster.pms.api.cnvntrmnat.dto.CnvnTrmnatInputParam;
import aicluster.pms.api.cnvntrmnat.dto.CnvnTrmnatParam;
import aicluster.pms.api.cnvntrmnat.service.CnvnTrmnatService;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 전자협약 해지 관리_admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/cnvn-trmnat")
public class CnvnTrmnatController {

	@Autowired
	private CnvnTrmnatService cnvnTrmnatService;

	/**
	 * 전자협약 해지 관리 목록조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptBsnsCnvn> getList(CnvnTrmnatParam cnvnTrmnatParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getList : {}", cnvnTrmnatParam);
		return cnvnTrmnatService.getList(cnvnTrmnatParam, page );
	}

	/**
	 * 전자협약 해지 관리 목록 엑셀 다운로드
	 * @param cnvnTrmnatParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcel(CnvnTrmnatParam cnvnTrmnatParam){
		log.debug("#####	getListExcel : {}", cnvnTrmnatParam);
		List<UsptBsnsCnvn> list = cnvnTrmnatService.getListExcel(cnvnTrmnatParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전자협약해지 관리 목록");
		ExcelSheet<UsptBsnsCnvn> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "접수번호"		, "과제명"			, "사업년도"	, "사업자명/이름"		, "회원명"			, "협약시작일"	,  "협약종료일"	, "해지사유"				, "해지일");
		sheet.setProperties("rn"	, "receiptNo"	, "taskNmKo"	, "bsnsYear"	, "bsnsNm"	, "memberNm"	, "cnvnBgnde" 	, "cnvnEndde"	,"cnvnTrmnatDivNm"	, "cnvnTrmnatDe");
		sheet.setTitle("사업계획서접수 관리 목록");
		sheet.setSheetName("사업계획서접수 관리 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 전자협약 해지 관리 과제검색 팝업
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/task")
	public CorePagination<UsptBsnsCnvn> getCnvnTrmnatTask(CnvnTrmnatParam cnvnTrmnatParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getCnvnTrmnatTask : {}", cnvnTrmnatParam);
		return cnvnTrmnatService.getCnvnTrmnatTask(cnvnTrmnatParam, page);
	}

	/**
	 * 전자협약 해지 등록_저장
	 * @param CnvnCnclsParam
	 * @return
	 */
	@PutMapping("/save")
	public void modifyCnvnTrmnat( @RequestPart(value = "info") CnvnTrmnatInputParam cnvnTrmnatInputParam
				, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyCnvnTrmnat : {}", cnvnTrmnatInputParam);
		cnvnTrmnatService.modifyCnvnTrmnat(cnvnTrmnatInputParam, fileList);
	}

	/**
	 * 전자협약 해지 취소
	 * @param CnvnCnclsParam
	 * @return
	 */
	@PutMapping("/cancel")
	public void modifyCnvnTrmnatCancel( @RequestBody CnvnTrmnatParam cnvnTrmnatParam) {

		log.debug("#####	modifyCnvnTrmnatCancel : {}", cnvnTrmnatParam);
		cnvnTrmnatService.modifyCnvnTrmnatCancel(cnvnTrmnatParam);
	}

	/**
	 * 전자협약 해지 관리 상세 조회
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/detail-info")
	public CnvnTrmnatDto getCnvnTrmnatInfo(CnvnTrmnatParam cnvnTrmnatParam){
		log.debug("#####	getCnvnTrmnatInfo : {}", cnvnTrmnatParam);
		return cnvnTrmnatService.getCnvnTrmnatInfo(cnvnTrmnatParam);
	}

	/**
	 * 전자협약 해지 처리이력
	 * @param bsnsCnvnId
	 * @return
	 */
	@GetMapping("/hist")
	public CnvnTrmnatHistDto getCnvnTrmnatHist(CnvnTrmnatParam cnvnTrmnatParam){
		log.debug("#####	getCnvnTrmnatHist : {}", cnvnTrmnatParam);
		return cnvnTrmnatService.getCnvnTrmnatHist(cnvnTrmnatParam);
	}
	/**
	 * 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		cnvnTrmnatService.getOneFileDown(response, attachmentId);
	}

}