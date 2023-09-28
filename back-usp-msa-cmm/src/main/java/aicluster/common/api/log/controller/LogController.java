package aicluster.common.api.log.controller;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.common.api.log.service.LogService;
import aicluster.common.common.dto.DayMemberSummary;
import aicluster.common.common.dto.ElapsedTimeLogSummary;
import aicluster.common.common.entity.LogtIndvdlinfoConectLog;
import bnet.library.excel.report.ExcelReportProvider;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ReportExcelView;

@RestController
@RequestMapping("/api/log/")
public class LogController {

	@Autowired
	private LogService service;

	/**
	 * 개인정보 조회로그 조회
	 * @param beginDt
	 * @param endDt
	 * @param workerLoginId
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("/indvdlinfos")
	public CorePagination<LogtIndvdlinfoConectLog> getIndvdlinfoList(Date beginDt, Date endDt, String workerLoginId, Long page, Long itemsPerPage) {
		if (beginDt == null || endDt == null) {
			throw new InvalidationException("기간을 입력하세요.");
		}

		String beginDay = date.format(beginDt, "yyyyMMdd");
		String endDay = date.format(endDt, "yyyyMMdd");
		return service.getIndvdlinfoList(beginDay, endDay, workerLoginId, page, itemsPerPage);
	}

	/**
	 * 개인정보 조회로그 엑셀다운로드
	 * @param beginDt
	 * @param endDt
	 * @param workerLoginId
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("/indvdlinfos-excel")
	public ModelAndView downloadIndvdlinfoList(Date beginDt, Date endDt, String workerLoginId, Long page, Long itemsPerPage) {
		if (beginDt == null || endDt == null) {
			throw new InvalidationException("기간을 입력하세요.");
		}

		String beginDay = date.format(beginDt, "yyyyMMdd");
		String endDay = date.format(endDt, "yyyyMMdd");

		Date now = new Date();
		String excelName = "개인정보로그_"+date.format(now, "yyyyMMddHHmm");

		// 엑셀 출력 데이터 조회
		List<LogtIndvdlinfoConectLog> list = service.getExcelIndvdlinfoList(beginDay, endDay, workerLoginId);

		// Excel 설정
		Workbook workbook = ExcelReportProvider.excelReport()
				.custom()
				.templatePath("/report/indvdlinfos-stats.xlsx") // src/main/resources/report/indvdlinfos-stats.xlsx
				.targetCell("개인정보로그!A1")                    // 엑셀 메모 위치
				.addData("list", list)                // 목록 정보 설정
				.getWorkbook();                           // workbook 생성
		return ReportExcelView.modelAndView(workbook, excelName);  // "개인정보로그_[YYYYMMDDHH24MM].xlsx" 파일명으로 엑셀 다운로드
	}

	/**
	 * 접속소요시간 로그 조회
	 * @return
	 */
	@GetMapping("/elapsedtimes")
	public ElapsedTimeLogSummary getElapsedTimeSummary() {
		return service.getElapsedTimeSummary();
	}

	/**
	 * 일별 회원접속로그
	 * @return
	 */
	@GetMapping("/daymems")
	public DayMemberSummary getDayMembers() {
		return service.getDayMemberSummary();
	}
}
