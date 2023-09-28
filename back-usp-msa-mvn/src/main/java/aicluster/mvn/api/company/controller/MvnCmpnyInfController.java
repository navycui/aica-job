package aicluster.mvn.api.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.company.dto.MvnCmpnyAlarmParam;
import aicluster.mvn.api.company.dto.MvnCmpnyAllocateParam;
import aicluster.mvn.api.company.dto.MvnCmpnyCheckoutParam;
import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyPrcsDto;
import aicluster.mvn.api.company.service.MvnCmpnyInfService;
import aicluster.mvn.common.dto.MvnCmpnyDto;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/company")
public class MvnCmpnyInfController {

	@Autowired
	private MvnCmpnyInfService service;

	/**
	 * 입주업체 목록조회
	 *
	 * @param mvnCmpnySt: 입주업체상태
	 * @param mvnAllocateSt: 입주배정상태
	 * @param bnoRoomNo: 건물동호수
	 * @param cmpnyNm: 업체명
	 * @param page
	 * @param itemsPerPage
	 * @return CorePagination<MvnCompanyListItemDto>
	 */
	@GetMapping("")
	CorePagination<MvnCmpnyListItemDto> getList(MvnCmpnyListParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}

	/**
	 * 입주업체 상세조회
	 *
	 * @param mvnId: 입주ID
	 * @return UsptMvnCompany: 입주업체정보
	 */
	@GetMapping("/{mvnId}")
	MvnCmpnyDto get(@PathVariable String mvnId) {
		return service.get(mvnId);
	}

	/**
	 * 입주업체 사무실배정
	 *
	 * @param mvnId: 입주ID
	 * @param mvnFcId: 입주시설ID
	 * @param mvnBeginDay: 입주시작일
	 * @param mvnEndDay: 입주종료일
	 * @param equipPvdtl: 장비제공내역
	 */
	@PutMapping("/{mvnId}/allocation")
	void modifyAllocate(@PathVariable String mvnId, @RequestBody MvnCmpnyAllocateParam param) {
		param.setMvnId(mvnId);

		service.modifyAllocate(param);
	}

	/**
	 * 입주업체 퇴실처리
	 *
	 * @param mvnId: 입주ID
	 * @param checkoutPlanDay: 퇴실예정일
	 * @param checkoutReason: 퇴실사유
	 * @param equipRtdtl: 장비반납내역
	 */
	@PutMapping("/{mvnId}/checkout")
	void modifyCheckout(@PathVariable String mvnId, @RequestBody MvnCmpnyCheckoutParam param) {
		param.setMvnId(mvnId);
		param.setCheckoutReqId(null);

		service.modifyCheckout(param);
	}

	/**
	 * 입주관리사업 최종선정업체에 대한 입주업체관리 동기화
	 *
	 * @return 동기화건수
	 */
	@PostMapping("/sync")
	MvnCmpnyPrcsDto add() {
		return service.add();
	}

	/**
	 * 입주업체사업 사무실배정 알림 이메일 발송
	 *
	 * @param mvnIds: 입주ID 배열
	 * @return 이메일 발송건수
	 */
	@PostMapping("/alarm")
	MvnCmpnyPrcsDto alarm(@RequestBody MvnCmpnyAlarmParam param) {
		return service.alarm(param.getMvnIds());
	}
}