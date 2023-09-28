package aicluster.member.api.member.controller;



import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.api.member.service.MemberService;
import aicluster.member.common.dto.MemberExcelDto;
import bnet.library.excel.report.ExcelReportProvider;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.pagination.CorePaginationParam;
import bnet.library.view.ReportExcelView;

//@Slf4j
@RestController
@RequestMapping("/api/members-excel")
public class MemberExcelController {

	@Autowired
	private MemberService memberService;

	/**
	 * 회원목록 엑셀다운로드
	 *
	 * @param loginId
	 * @param memberNm
	 * @param memberSt
	 * @param memberType
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public ModelAndView getExcel(String workCn)
	{
		Date now = new Date();
		String excelName = "회원목록_"+date.format(now, "yyyyMMddHHmm");

		// 엑셀 출력 데이터 조회
		List<MemberExcelDto> list = memberService.getExcelData(workCn, excelName+".xlsx");

		// Excel 설정
		Workbook workbook = ExcelReportProvider.excelReport()
				.custom()
				.templatePath("/report/member-list.xlsx") // src/main/resources/report/member-list.xlsx
				.targetCell("회원목록!A1")                    // 엑셀 메모 위치
				.addData("list", list)                // 목록 정보 설정
				.getWorkbook();                           // workbook 생성
		return ReportExcelView.modelAndView(workbook, excelName);  // "회원목록_[YYYYMMDDHH24MM].xlsx" 파일명으로 엑셀 다운로드
	}
}
