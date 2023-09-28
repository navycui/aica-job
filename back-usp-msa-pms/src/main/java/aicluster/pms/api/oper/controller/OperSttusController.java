package aicluster.pms.api.oper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.oper.service.OperSttusService;
import aicluster.pms.common.dto.BsnsPortListDto;
import aicluster.pms.common.dto.EntrprsSttusListDto;
import aicluster.pms.common.dto.EqpmnResultDto;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.view.ExcelView;

/**
 * 운영관리 현황
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/oper-sttus")
public class OperSttusController {

	@Autowired
	private OperSttusService operSttusService;

	/**
	 * 기업현황 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	@GetMapping("/entrprs")
	public JsonList<EntrprsSttusListDto> getEntrprsSttusList(String bsnsYear) {
		return new JsonList<>(operSttusService.getEntrprsSttusList(bsnsYear));
	}


	/**
	 * 기업현황 목록 엑셀 다운로드
	 * @param bsnsYear
	 * @return
	 */
	@GetMapping("/entrprs/excel-dwld")
	public ModelAndView getEntrprsSttusExcelDwld(String bsnsYear){
		List<EntrprsSttusListDto> list = operSttusService.getEntrprsSttusList(bsnsYear);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename(string.toDownloadFilename("기업현황"));
		ExcelSheet<EntrprsSttusListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("기업유형", "총기업수" , "최종선정", "사업중", "사업종료");
		sheet.setProperties("memberTypeNm", "totCnt", "lastSlctnCnt", "bsnsIng", "bsnsEnd");
		sheet.setTitle("기업현황");
		sheet.setSheetName("기업현황");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 사업지원현황 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	@GetMapping("/bsns-port")
	public JsonList<BsnsPortListDto> getBsnsPortSttusList(String bsnsYear) {
		return new JsonList<>(operSttusService.getBsnsPortSttusList(bsnsYear));
	}


	/**
	 * 사업지원현황 목록 엑셀 다운로드
	 * @param bsnsYear
	 * @return
	 */
	@GetMapping("/bsns-port/excel-dwld")
	public ModelAndView getBsnsPortSttusExcelDwld(String bsnsYear){
		List<BsnsPortListDto> list = operSttusService.getBsnsPortSttusList(bsnsYear);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename(string.toDownloadFilename("사업지원현황"));
		ExcelSheet<BsnsPortListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("유형", "과제수" , "정부지원금", "자기부담금", "협약 합계(정부지원금+자기부담금)");
		sheet.setProperties("memberTypeNm", "taskCnt", "excutSbsidy", "excutBsnmAlotm", "excutCnvnTotamt");
		sheet.setTitle("사업지원현황");
		sheet.setSheetName("사업지원현황");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 실증장비사용현황 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	@GetMapping("/eqpmn")
	public JsonList<EqpmnResultDto> getEqpmnList(String bgnde, String endde, String  selectStdr) {
		return new JsonList<>(operSttusService.getEqpmnList(bgnde, endde, selectStdr));
	}


	/**
	 * 실증장비사용현황 목록 엑셀 다운로드
	 * @param bgnde
	 * @param endde
	 * @param selectStdr
	 * @return
	 */
	@GetMapping("/eqpmn/excel-dwld")
	public ModelAndView getEqpmnExcelDwld(String bgnde, String endde, String  selectStdr) {
		List<EqpmnResultDto> list = operSttusService.getEqpmnList(bgnde, endde, selectStdr);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename(string.toDownloadFilename("실증장비사용현황"));
		ExcelSheet<EqpmnResultDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("집계일", "견적신청" , "사용신청", "승인", "사용중", "사용종료", "사용장비수", "반출장비수", "반입장비수", "사용총액", "미수금");
		sheet.setProperties("fmtStatsDay", "estmtReqst", "useReqst", "confm", "use", "useEnd", "useEqpmnCo", "tkoutEqpmnCo", "tkinEqpmnCo", "useTotamt", "npy");
		sheet.setTitle("실증장비사용현황");
		sheet.setSheetName("실증장비사용현황");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


}
