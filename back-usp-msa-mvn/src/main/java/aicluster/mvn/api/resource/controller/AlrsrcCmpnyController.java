package aicluster.mvn.api.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.resource.dto.AlrsrcCmpnyBsnsParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyListParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyPeriodParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyStatusParam;
import aicluster.mvn.api.resource.service.AlrsrcCmpnyService;
import aicluster.mvn.common.dto.AlrsrcCmpnyListItemDto;
import aicluster.mvn.common.dto.AlrsrcCmpnySlctnDto;
import aicluster.mvn.common.entity.UsptResrceAsgnEntrps;
import aicluster.mvn.common.entity.UsptResrceAsgnHist;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("api/resources/company")
public class AlrsrcCmpnyController {

	@Autowired
	private AlrsrcCmpnyService service;

	/**
	 * 자원할당업체 목록조회
	 * @param param
	 * @param pageParam
	 * @return
	 */
	@GetMapping("")
	CorePagination<AlrsrcCmpnyListItemDto> getList(AlrsrcCmpnyListParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}

	/**
	 * 자원할당업체 상세조회
	 * @param alrsrcId
	 * @return
	 */
	@GetMapping("/{alrsrcId}")
	UsptResrceAsgnEntrps get(@PathVariable String alrsrcId) {
		return service.get(alrsrcId);
	}

	/**
	 * 자원할당업체 등록 동기화
	 * @param param
	 */
	@PostMapping("")
	void add(@RequestBody AlrsrcCmpnyBsnsParam param) {
		service.add(param);
	}

	/**
	 * 자원할당업체 이용기간 변경
	 * @param alrsrcId
	 * @param param
	 */
	@PutMapping("/{alrsrcId}/update-period")
	void modifyPeriod(@PathVariable String alrsrcId, @RequestBody AlrsrcCmpnyPeriodParam param) {
		param.setAlrsrcId(alrsrcId);
		service.modifyPeriod(param);
	}

	/**
	 * 자원할당업체 상태변경
	 * @param alrsrcId
	 * @param param
	 */
	@PutMapping("/{alrsrcId}/update-status")
	void modifyStatus(@PathVariable String alrsrcId, @RequestBody AlrsrcCmpnyStatusParam param) {
		param.setAlrsrcId(alrsrcId);
		service.modifyStatus(param);
	}

	/**
	 * 자원할당업체 처리이력 목록조회
	 * @param alrsrcId
	 * @return
	 */
	@GetMapping("/{alrsrcId}/hist")
	JsonList<UsptResrceAsgnHist> getHist(@PathVariable String alrsrcId) {
		return service.getHist(alrsrcId);
	}

	/**
	 * 자원할당사업별 자원할당업체 목록 조회
	 * @param evlLastSlctnId
	 * @return
	 */
	@GetMapping("/bsns-slctn/{evlLastSlctnId}")
	JsonList<AlrsrcCmpnySlctnDto> getBsnsSlctn(@PathVariable String evlLastSlctnId) {
		return service.getBsnsSlctn(evlLastSlctnId);
	}
}
