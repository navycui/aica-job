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

import aicluster.common.api.holiday.service.ExceptHolidayService;
import aicluster.common.common.entity.CmmtRestdeExcl;

@RestController
@RequestMapping("/api/except-holidays")
public class ExceptHolidayController {

	@Autowired
	private ExceptHolidayService exceptHolidayService;

	/**
	 * 휴일 제외 정보 등록
	 *
	 * @param exctHoliday
	 * @return
	 */
	@PostMapping("")
	public CmmtRestdeExcl add(@RequestBody(required = false) CmmtRestdeExcl exctHoliday) {
		return exceptHolidayService.addExctHoliday(exctHoliday);
	}

	/**
	 * 휴일 제외 정보 조회
	 *
	 * @param ymd
	 * @return
	 */
	@GetMapping("/{ymd}")
	public CmmtRestdeExcl get(@PathVariable String ymd) {
		return exceptHolidayService.getExctHoliday(ymd);
	}

	/**
	 * 휴일 제외 정보 수정
	 *
	 * @param exctHoliday
	 * @return
	 */
	@PutMapping("/{ymd}")
	public CmmtRestdeExcl modify(@PathVariable String ymd, @RequestBody(required = false) CmmtRestdeExcl exctHoliday) {
		exctHoliday.setYmd(ymd);
		return exceptHolidayService.modifyExctHoliday(exctHoliday);
	}

	/**
	 * 휴일 제외 정보 삭제
	 * @param ymd
	 */
	@DeleteMapping("/{ymd}")
	public void remove(@PathVariable String ymd) {
		exceptHolidayService.removeExctHoliday(ymd);
	}

}
