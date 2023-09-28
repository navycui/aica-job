package aicluster.mvn.api.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.resource.dto.AlrsrcRedstbParam;
import aicluster.mvn.api.resource.service.AlrsrcDstbService;

@RestController
@RequestMapping("api/resources/distribution")
public class AlrsrcDstbController {

	@Autowired
	AlrsrcDstbService service;

	/**
	 * 자원할당 수정
	 * @param alrsrcId
	 * @param param
	 */
	@PutMapping("/{alrsrcId}")
	void modify(@PathVariable String alrsrcId, @RequestBody AlrsrcRedstbParam param) {
		param.setAlrsrcId(alrsrcId);

		service.modify(param);
	}

}
