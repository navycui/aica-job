package aicluster.tsp.api.admin.statistics.controller;

import aicluster.tsp.api.admin.statistics.param.StatisticsParam;
import aicluster.tsp.api.admin.statistics.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
@Api(tags = "통계")
public class StatisticsController {
	
	@Autowired
	private StatisticsService statisticsService;

	@ApiOperation(value = "통계 조회", notes = "통계 조회.")
	@GetMapping("")
	public StatisticsParam getStatistics(StatisticsParam param) {
		return statisticsService.getStatistics(param);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(StatisticsParam param) {
		return statisticsService.getListExcelDownload(param);
	}
}
