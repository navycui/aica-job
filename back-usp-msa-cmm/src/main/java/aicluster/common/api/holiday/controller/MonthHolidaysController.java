package aicluster.common.api.holiday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.holiday.dto.MonthHolidaysDto;
import aicluster.common.api.holiday.service.HolidayService;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/month-holidays")
public class MonthHolidaysController {

	@Autowired
	private HolidayService holidayService;

	/**
	 * 월별 휴일 목록 조회
	 *
	 * @param ym
	 * @return
	 */
	@GetMapping("/{ym}")
	public JsonList<MonthHolidaysDto> getList(@PathVariable String ym) {
		return holidayService.getMonthHolidaysList(ym);
	}

}
