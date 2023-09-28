package aicluster.member.api.insider.controller;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.member.api.insider.dto.InsiderListParam;
import aicluster.member.api.insider.service.InsiderService;
import aicluster.member.common.dto.InsiderExcelDto;
import bnet.library.excel.report.ExcelReportProvider;
import bnet.library.util.CoreUtils.date;
import bnet.library.view.ReportExcelView;

@RestController
@RequestMapping("/api/insiders-excel")
public class InsiderExcelController {

	@Autowired
	private InsiderService insiderService;

	/**
	 * 내부사용자 목록 엑셀다운로드
	 * @param param
	 * @return
	 */
	@GetMapping("")
	public ModelAndView getExcel(String workCn) {
		Date now = new Date();
		String excelName = "관리자계정목록_"+date.format(now, "yyyyMMddHHmm");

		List<InsiderExcelDto> list = insiderService.getExcelData(workCn, excelName+".xlsx");

		// Excel 설정
		Workbook workbook = ExcelReportProvider.excelReport()
				.custom()
				.templatePath("/report/insider-list.xlsx") // src/main/resources/report/insider-list.xlsx
				.targetCell("관리자목록!A1")                    // 엑셀 메모 위치
				.addData("list", list)                // 목록 정보 설정
				.getWorkbook();                           // workbook 생성
		return ReportExcelView.modelAndView(workbook, excelName);  // "회원목록_[YYYYMMDDHH24MM].xlsx" 파일명으로 엑셀 다운로드
	}
}
