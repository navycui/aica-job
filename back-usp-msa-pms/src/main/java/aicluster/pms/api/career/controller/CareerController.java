package aicluster.pms.api.career.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.career.dto.CareerDto;
import aicluster.pms.api.career.service.CareerService;

/**
 * 나의경력관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/career")
public class CareerController {

	@Autowired
	private CareerService careerService;

	/**
	 * 조회
	 * @return
	 */
	@GetMapping("")
	public CareerDto get() {
		return careerService.get();
	}

	/**
	 * 수정
	 * @param param
	 */
	@PutMapping("")
	public void modify(@RequestBody CareerDto param) {
		careerService.modify(param);
	}

}
