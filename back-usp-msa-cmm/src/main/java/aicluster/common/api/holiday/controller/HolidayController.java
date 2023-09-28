package aicluster.common.api.holiday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.holiday.dto.YearHolidaysDto;
import aicluster.common.api.holiday.service.HolidayService;
import aicluster.common.common.entity.CmmtRestde;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

	@Autowired
	private HolidayService holidayService;

	/**
	 * 휴일 정보 목록 조회
	 *
	 * @param year
	 * @param ymdNm
	 * @return
	 */
	@GetMapping("")
	public YearHolidaysDto getList(String year, String ymdNm) {
		return holidayService.getlist(year, ymdNm);
	}

	/**
	 * 휴일 정보 등록
	 *
	 * @param holiday
	 * @return
	 */
	@PostMapping("")
	public CmmtRestde add(@RequestBody(required = false) CmmtRestde holiday) {
		return holidayService.addHoliday(holiday);
	}

	/**
	 * 휴일 정보 조회
	 *
	 * @param day
	 * @return
	 */
	@GetMapping("/{ymd}")
	public CmmtRestde get(@PathVariable String ymd) {
		return holidayService.getHoliday(ymd);
	}

	/**
	 * 휴일 정보 수정
	 *
	 * @param holiday
	 * @return
	 */
	@PutMapping("/{ymd}")
	public CmmtRestde modify(@PathVariable String ymd, @RequestBody(required = false) CmmtRestde holiday) {
		holiday.setYmd(ymd);
		return holidayService.modifyHoliday(holiday);
	}

	/**
	 * 휴일 정보 삭제
	 *
	 * @param ymd
	 */
	@DeleteMapping("/{ymd}")
	public void remove(@PathVariable String ymd) {
		holidayService.removeHoliday(ymd);
	}

}
