package aicluster.member.api.member.controller;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.member.api.member.service.MemberService;
import aicluster.member.common.dto.MemberStatsDto;
import aicluster.member.common.dto.MemberStatsListItemDto;
import bnet.library.excel.report.ExcelReportProvider;
import bnet.library.util.CoreUtils.date;
import bnet.library.view.ReportExcelView;

@RestController
@RequestMapping("/api/ust/user-stats")
public class UstController {

	@Autowired
	private MemberService service;

	/**
	 * 사용자현황 목록 조회
	 * @param searchType
	 * @param memberType
	 * @param beginDay
	 * @param endDay
	 * @return
	 */
	@GetMapping("")
	public MemberStatsDto getList(String searchType, String memberType, String beginDay, String endDay) {
		return service.getStatsList(searchType, memberType, beginDay, endDay);
	}

	/**
	 * 사용자현황 엑셀 다운로드
	 * @param searchType
	 * @param memberType
	 * @param beginDay
	 * @param endDay
	 * @return
	 */
	@GetMapping("/excel")
	public ModelAndView getExcel(String searchType, String memberType, String beginDay, String endDay) {
		Date now = new Date();
		String excelName = "사용자현황_"+date.format(now, "yyyyMMddHHmm");

		String searchTypeNm = null;
		if ("DAY".equals(searchType)) {
			searchTypeNm = "집계일";
		}
		else if ("MONTH".equals(searchType)) {
			searchTypeNm = "집계월";
		}

		// 엑셀 출력 데이터 조회
		MemberStatsDto statsDto = service.getStatsList(searchType, memberType, beginDay, endDay);

		List<MemberStatsListItemDto> statsList = statsDto.getStatsList();

		statsDto.setStatsList(null);

		// Excel 설정
		Workbook workbook = ExcelReportProvider.excelReport()
				.custom()
				.templatePath("/report/member-stats.xlsx") // src/main/resources/report/member-stats.xlsx
				.targetCell("사용자현황!A1")                    // 엑셀 메모 위치
				.addData("searchType", searchTypeNm)				// 검색구분 정보 설정
				.addData("summary", statsDto)				// 요약 정보 설정
				.addData("list", statsList)                // 목록 정보 설정
				.getWorkbook();                           // workbook 생성
		return ReportExcelView.modelAndView(workbook, excelName);  // "사용자현황_[YYYYMMDDHH24MM].xlsx" 파일명으로 엑셀 다운로드
	}
}
