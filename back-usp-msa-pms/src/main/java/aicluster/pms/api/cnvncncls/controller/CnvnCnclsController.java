package aicluster.pms.api.cnvncncls.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.cnvncncls.dto.CnvnCnclsDto;
import aicluster.pms.api.cnvncncls.dto.CnvnCnclsParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsDto;
import aicluster.pms.api.cnvncncls.service.CnvnCnclsService;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 전자협약 관리_admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/cnvn-cncls")
public class CnvnCnclsController {

	@Autowired
	private CnvnCnclsService cnvnCnclsService;


	/**
	 * 전자협약 관리 목록조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptBsnsCnvn> getList(CnvnCnclsParam cnvnCnclsParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	getList : {}", cnvnCnclsParam);
		return cnvnCnclsService.getList(cnvnCnclsParam, page );
	}

	/**
	 * 전자협약 관리 목록 엑셀 다운로드
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcel(CnvnCnclsParam cnvnCnclsParam){
		log.debug("#####	getListExcel : {}", cnvnCnclsParam);
		List<UsptBsnsCnvn> list = cnvnCnclsService.getListExcel(cnvnCnclsParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전자협약관리 목록");
		ExcelSheet<UsptBsnsCnvn> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "협약상태",   "접수번호",    "과제명"	, "사업년도"	, "사업명"	,"공고명"	, "회원명"	, "협약일",  "해약일");
		sheet.setProperties("rn"	, "cnvnSttusNm"	,"receiptNo"	, "taskNmKo"	, "bsnsYear"	, "bsnsNm"	, "pblancNm"	, "memberNm"	, "cnvnDe" , "cnvnTrmnatDe");
		sheet.setTitle("사업계획서접수 관리 목록");
		sheet.setSheetName("사업계획서접수 관리 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 전자협약 관리 상세 조회
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/detail-info")
	public CnvnCnclsDto getBsnsCnvnInfo(CnvnCnclsParam cnvnCnclsParam){
		log.debug("#####	getBsnsCnvnInfo : {}", cnvnCnclsParam);
		return cnvnCnclsService.getBsnsCnvnInfo(cnvnCnclsParam);
	}


	/**
	 * 전자협약 관리 해지정보 조회
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/detail-info/trmnat")
	public CnvnCnclsDto getBsnsCnvnTrmnatInfo(CnvnCnclsParam cnvnCnclsParam){
		log.debug("#####	getBsnsCnvnTrmnatInfo : {}", cnvnCnclsParam);
		return cnvnCnclsService.getBsnsCnvnTrmnatInfo(cnvnCnclsParam);
	}

	/**
	 * 전자협약 관리 협약서 생성
	 * @param CnvnCnclsParam
	 * @return
	 */
	@PutMapping("/cnvn-creat")
	public void modifyCnvnCncls( @RequestBody CnvnCnclsParam cnvnCnclsParam) {
		log.debug("#####	modifyCnvnCncls : {}", cnvnCnclsParam);
		cnvnCnclsService.modifyCnvnCncls(cnvnCnclsParam);
	}

	/**
	 * 전자협약 상태 변경 및 협약완료
	 * @param CnvnCnclsParam
	 * @return
	 */
	@PutMapping("/request-sign")
	public void modifyRequestSign ( @RequestBody CnvnCnclsParam cnvnCnclsParam) {
		log.debug("#####	modifyPrtcmpnySign : {}", cnvnCnclsParam);
		cnvnCnclsService.modifyRequestSign(cnvnCnclsParam);
	}

	/**
	 *  전자협약 관리 협약서 다운 정보 조회
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/cnclsDoc")
	public FrontCnvnCnclsDto getCnclsDocInfo(CnvnCnclsParam cnvnCnclsParam){
		log.debug("#####	getCnclsDocInfo : {}", cnvnCnclsParam);
		return cnvnCnclsService.getCnclsDocInfo(cnvnCnclsParam);
	}

	/**
	 * 전자협약 관리 협약첨부파일 일괄 협약첨부파일 일괄 다운2022.11.15 삭제
	 * @param CnvnCnclsParam
	 * @return
	 */
//	@GetMapping("/file-dwln")
//	public void getCnvnFileDown(HttpServletResponse response, CnvnCnclsParam cnvnCnclsParam){
//		log.debug("#####	getCnvnFileDown : {}", cnvnCnclsParam);
//		cnvnCnclsService.getCnvnFileDown(response, cnvnCnclsParam);
//	}

	/**
	 * 전자협약 관리  및 해지 협약 첨부파일 단건
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/file-dwln-one")
	public void getCnvnFileDownOne(HttpServletResponse response, CnvnCnclsParam cnvnCnclsParam){
		log.debug("#####	getCnvnFileDownOne : {}", cnvnCnclsParam);
		cnvnCnclsService.getCnvnFileDownOne(response, cnvnCnclsParam);
	}

	/**
	 * 전자협약 관리
	 * @param CnvnCnclsParam
	 * @return
	 */
	@GetMapping("/file-dwln/trmnat")
	public void getCnvnTrmnatFileDown(HttpServletResponse response, CnvnCnclsParam cnvnCnclsParam){
		log.debug("#####	getCnvnFileDown : {}", cnvnCnclsParam);
		cnvnCnclsService.getCnvnTrmnatFileDown(response, cnvnCnclsParam);
	}

}