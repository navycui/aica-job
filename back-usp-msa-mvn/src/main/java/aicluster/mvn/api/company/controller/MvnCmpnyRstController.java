package aicluster.mvn.api.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.company.dto.MvnCmpnyPrfrmListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyRstParam;
import aicluster.mvn.api.company.service.MvnCmpnyRstService;
import aicluster.mvn.common.dto.CmpnyPrfrmListItemDto;
import aicluster.mvn.common.dto.MvnCmpnyPrfrmDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("api/company-result")
public class MvnCmpnyRstController {

	@Autowired
	private MvnCmpnyRstService service;

	/**
	 * 입주업체성과 제출정보 목록조회
	 * @param param
	 * @param pageParam
	 * @return
	 */
	@GetMapping("")
	public CorePagination<CmpnyPrfrmListItemDto> getList(MvnCmpnyPrfrmListParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}

	/**
	 * 입주업체성과 제출정보 상세조회
	 * @param mvnId
	 * @param sbmsnYm
	 * @return
	 */
	@GetMapping("/{mvnId}/{sbmsnYm}")
	MvnCmpnyPrfrmDto get(@PathVariable String mvnId, @PathVariable String sbmsnYm) {
		return service.get(mvnId, sbmsnYm);
	}

	/**
	 * 입주업체성과 제출정보 등록
	 * @param mvnId
	 * @param sbmsnYm
	 * @param param
	 */
	@PostMapping("/{mvnId}/{sbmsnYm}")
	void add(@PathVariable String mvnId, @PathVariable String sbmsnYm, @RequestBody MvnCmpnyRstParam param) {
		param.setMvnId(mvnId);
		param.setSbmsnYm(sbmsnYm);

		service.add(param);
	}

	/**
	 * 입주업체성과 제출정보 삭제
	 * @param mvnId
	 * @param sbmsnYm
	 */
	@DeleteMapping("/{mvnId}/{sbmsnYm}")
	void remove(@PathVariable String mvnId, @PathVariable String sbmsnYm) {
		service.remove(mvnId, sbmsnYm);
	}
}
