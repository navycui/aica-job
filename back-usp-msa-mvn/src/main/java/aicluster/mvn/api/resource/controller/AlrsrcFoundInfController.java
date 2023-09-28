package aicluster.mvn.api.resource.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.resource.dto.AlrsrcFoundInfParam;
import aicluster.mvn.api.resource.service.AlrsrcFoundInfService;
import aicluster.mvn.common.dto.AlrsrcFninfRsrcDto;
import aicluster.mvn.common.dto.HistDtListItemDto;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import aicluster.mvn.common.entity.UsptResrceInvntryInfoHist;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("api/resources/found-inf")
public class AlrsrcFoundInfController {

	@Autowired
	AlrsrcFoundInfService service;

	/**
	 * 자원할당재고 목록 조회
	 * @return
	 */
	@GetMapping("")
	JsonList<UsptResrceInvntryInfo> getList() {
		return service.getList();
	}

	/**
	 * 자원할당재고 등록
	 * @param alrsrcFninfList
	 */
	@PostMapping("")
	void add(@RequestBody List<AlrsrcFoundInfParam> alrsrcFninfList) {
		service.add(alrsrcFninfList);
	}


	/**
	 * 자원할당재고 상세 조회
	 * @param rsrcId
	 * @return
	 */
	@GetMapping("/{rsrcId}")
	UsptResrceInvntryInfo get(@PathVariable String rsrcId) {
		return service.get(rsrcId);
	}

	/**
	 * 자원할당재고 수정
	 * @param alrsrcFninfList
	 */
	@PutMapping("")
	void modify(@RequestBody List<AlrsrcFoundInfParam> alrsrcFninfList) {
		service.modify(alrsrcFninfList);
	}

	/**
	 * 자원할당재고 삭제
	 * @param alrsrcFninfList
	 */
	@DeleteMapping("")
	void remove(@RequestBody List<AlrsrcFoundInfParam> alrsrcFninfList) {
		service.remove(alrsrcFninfList);
	}

	/**
	 * 자원할당재고 처리이력일자 목록조회
	 * @param pageParam
	 * @return
	 */
	@GetMapping("/hist/date")
	CorePagination<HistDtListItemDto> getHistDays(CorePaginationParam pageParam) {
		return service.getHistDays(pageParam);
	}

	/**
	 * 자원할당재고 처리이력 상세조회
	 * @param histDt
	 * @return
	 */
	@GetMapping("/hist/{histDt}")
	JsonList<UsptResrceInvntryInfoHist> getHist(@PathVariable Date histDt) {
		String fmtHistDt = date.format(histDt, "yyyyMMdd");
		return service.getHist(fmtHistDt);
	}

	/**
	 * 자원 코드성 목록 조회
	 * @param rsrcGroupCd
	 * @return
	 */
	@GetMapping("/codes")
	JsonList<AlrsrcFninfRsrcDto> getCodeList(String rsrcGroupCd) {
		return service.getCodeList(rsrcGroupCd);
	}
}
