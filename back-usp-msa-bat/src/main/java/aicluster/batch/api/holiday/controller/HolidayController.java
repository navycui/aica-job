package aicluster.batch.api.holiday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.holiday.dto.HolidaySaveParam;
import aicluster.batch.api.holiday.service.HolidayService;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {

	@Autowired
	private HolidayService service;

	@PutMapping("")
	public void save(@RequestBody HolidaySaveParam param) {
		service.updateHolidays(param.getYear(), param.getMonth(), param.getCnt());
	}
}
