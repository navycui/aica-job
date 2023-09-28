package aicluster.mvn.api.reservation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.reservation.dto.MvnFcRsvctmDto;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtDto;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtListParam;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtModifyStateParam;
import aicluster.mvn.api.reservation.dto.UserRsvtListParam;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyResveDao;
import aicluster.mvn.common.dao.UsptMvnFcltyResveHistDao;
import aicluster.mvn.common.dto.CutoffTimeDto;
import aicluster.mvn.common.dto.MvnFcCutoffTimeDto;
import aicluster.mvn.common.dto.MvnFcReserveStCountDto;
import aicluster.mvn.common.dto.MvnFcRsvtListItemDto;
import aicluster.mvn.common.dto.UserMvnCmpnyDto;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyResve;
import aicluster.mvn.common.entity.UsptMvnFcltyResveHist;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.searchType;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class MvnFcRsvtService {

	@Autowired
	private UsptMvnFcltyInfoDao usptMvnFcDao;

	@Autowired
	private UsptMvnEntrpsInfoDao mvnCmpnyDao;

	@Autowired
	private UsptMvnFcltyResveDao usptMvnFcRsvtDao;

	@Autowired
	private UsptMvnFcltyResveHistDao usptMvnFcRsvtHistDao;

	@Autowired
	private FwUserDao userDao;

	public CorePagination<MvnFcRsvtListItemDto> getRsvtSpaceList(MvnFcRsvtListParam param, CorePaginationParam pageParam)
	{
		// 내부사용자
		SecurityUtils.checkWorkerIsInsider();

		// 기간은 필수입력
		Date beginDt = string.toDate(param.getRsvtBeginDay());
		String beginDay = date.format(beginDt, "yyyyMMdd");
		Date endDt = string.toDate(param.getRsvtEndDay());
		String endDay = date.format(endDt, "yyyyMMdd");

		if (string.isBlank(beginDay) || string.isBlank(endDay)) {
			throw new InvalidationException("예약기간을 모두 입력하세요.");
		}

		param.setRsvtBeginDay(beginDay);
		param.setRsvtEndDay(endDay);

		if ( string.isNotBlank(param.getSearchCn()) ) {
			if ( string.equals(param.getSearchType(), searchType.시설명) ) {
				param.setMvnFcNm(param.getSearchCn());
			}
			if ( string.equals(param.getSearchType(), searchType.사업자명) ) {
				param.setMemberNm(param.getSearchCn());
			}
		}

		// 총 건수 조회
		long totalItems = usptMvnFcRsvtDao.selectList_count(param);

		// 목록 조회
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<MvnFcRsvtListItemDto> list = usptMvnFcRsvtDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());

		return new CorePagination<>(info, list);
	}

	public MvnFcRsvtDto getRsvtSpace(String reserveId)
	{
		// 내부사용자
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/*
		 * 예약정보 조회
		 */
		UsptMvnFcltyResve rsvt = usptMvnFcRsvtDao.select(reserveId);
		if (rsvt == null) {
			throw new InvalidationException("예약정보가 없습니다.");
		}

		// 시설정보 조회
		UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(rsvt.getMvnFcId());
		if (mvnFc == null) {
			throw new InvalidationException("시설정보가 없습니다.");
		}

		// 예약자ID로 입주중인 입주업체정보 중 데이터를 조회한다.
		UserMvnCmpnyDto userMvnCmpny = mvnCmpnyDao.selectUser( rsvt.getRsvctmId(), true );

		// 신청자정보 조회
		UserDto userDto = userDao.select(rsvt.getRsvctmId());
		if (userDto == null) {
			throw new InvalidationException("신청자 정보가 없습니다.");
		}

		MvnFcRsvctmDto rsvctm = MvnFcRsvctmDto.builder()
									.memberId(userDto.getMemberId())
									.memberNm(userDto.getMemberNm())
									.nickname(userDto.getNickname())
									.loginId(userDto.getLoginId())
									.encEmail(userDto.getEncEmail())
									.encMobileNo(userDto.getEncMobileNo())
									.build();

		if (userMvnCmpny == null) {
			rsvctm.setIsMvn(false);
			rsvctm.setBnoRoomNm(null);
		}
		else {
			rsvctm.setIsMvn(true);
			rsvctm.setBnoRoomNm(userMvnCmpny.getBnoNm() + " " + userMvnCmpny.getRoomNo());
		}

		MvnFcRsvtDto dto = MvnFcRsvtDto.builder()
									.mvnFcRsvt(rsvt)
									.mvnFc(mvnFc)
									.rsvctm(rsvctm)
									.build();

		// log 정보생성(마스킹 출력이므로 이력 생성하지 않는다.)
		if (!string.equals(worker.getMemberId(), rsvctm.getMemberId())) {
//			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
//										.memberId(worker.getMemberId())
//										.memberIp(webutils.getRemoteIp(request))
//										.workTypeNm("조회")
//										.workCn("시설예약 상세페이지 예약자정보 조회 업무")
//										.trgterId(rsvctm.getMemberId())
//										.email(rsvctm.getEmail())
//										.birthday(null)
//										.mobileNo(rsvctm.getMobileNo())
//										.build();
//			indvdlInfSrchUtils.insert(logParam);
		}

		return dto;
	}

	public UsptMvnFcltyResve modifyState(String reserveId, MvnFcRsvtModifyStateParam param)
	{
		// 내부사용자
		BnMember worker = SecurityUtils.checkLogin();
		Date now = new Date();

		/*
		 * 입력검사
		 */

		if (param == null) {
			throw new InvalidationException("예약상태를 입력하세요.");
		}

		// 예약상태 검사
		if (string.isBlank(param.getReserveSt())) {
			throw new InvalidationException("예약상태를 입력하세요.");
		}
		// 반려인 경우, 반려사유 검사
		if (string.equals(param.getReserveSt(), CodeExt.reserveSt.반려) && string.isBlank(param.getRejectReasonCn())) {
			throw new InvalidationException("반려사유를 입력하세요.");
		}

		// 반려가 아니면, 반려사유를 null로 변경
		if (!string.equals(param.getReserveSt(), CodeExt.reserveSt.반려)) {
			param.setRejectReasonCn(null);
		}

		// 예약정보 조회
		UsptMvnFcltyResve rsvt = usptMvnFcRsvtDao.select(reserveId);
		if (rsvt == null) {
			throw new InvalidationException("예약정보가 없습니다.");
		}

		// 시설정보 조회
		UsptMvnFcltyInfo fc = usptMvnFcDao.select(rsvt.getMvnFcId());
		if (fc == null) {
			throw new InvalidationException("예약 시설정보가 없습니다.");
		}

		/*
		 * 예약상태별 처리
		 */

		String reserveSt = param.getReserveSt();
		String workTypeNm = null;
		String workCn = null;

		// 변경 상태별 권한 점검
		if (string.equals(reserveSt, CodeExt.reserveSt.예약취소)) {
			// 예약 취소는 작성자만 할 수 있다.
			if ( !string.equals(rsvt.getRsvctmId(), worker.getMemberId()) ) {
				throw new InvalidationException(String.format(CodeExt.validateMessageExt.행위오류, "예약자만", "예약취소"));
			}
		}
		else {
			// 예약 취소 외의 상태 변경은 내부사용자만 할 수 있다.
			SecurityUtils.checkWorkerIsInsider();
		}

		// 예약상태 = 반려
		if (string.equals(reserveSt, CodeExt.reserveSt.반려)) {
			if (!string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.신청)) {
				throw new InvalidationException("반려는 신청상태인 경우만 할 수 있습니다.");
			}
			workTypeNm = "반려";
			workCn = "반려 처리하였습니다.";
		}
		// 예약상태 = 승인
		else if (string.equals(reserveSt, CodeExt.reserveSt.승인)) {
			if (!string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.신청)) {
				throw new InvalidationException("승인은 신청상태인 경우만 할 수 있습니다.");
			}
			workTypeNm = "승인";
			workCn = "승인 처리하였습니다.";
		}
		// 예약상태 = 승인취소
		else if (string.equals(reserveSt, CodeExt.reserveSt.승인취소)) {
			// 시설의 예약유형 = 승인형, 예약상태 = 승인
			boolean fcAprv = string.equals(fc.getReserveType(), CodeExt.reserveType.승인형);
			boolean rsvtAprv = string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.승인) || string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.이용대기) || string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.이용중);
			if (!fcAprv || !rsvtAprv) {
				throw new InvalidationException("승인취소는 시설의 예약유형이 승인형이고, 예약상태가 승인상태인 경우만 가능합니다.");
			}
			// 예약상태를 신청상태로 변경한다.
			reserveSt = CodeExt.reserveSt.신청;

			workTypeNm = "승인취소";
			workCn = "승인취소하여 신청상태로 변경하였습니다.";
		}
		// 예약상태 = 예약취소
		else if (string.equals(reserveSt, CodeExt.reserveSt.예약취소)) {
			// 예약취소 할 수 있는 현재 예약 상태 검증
			boolean imme = string.equals(fc.getReserveType(), CodeExt.reserveType.즉시예약형);
			if (imme) {
				if (!string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.승인) && !string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.이용대기)) {
					throw new InvalidationException("예약취소는 시설이 즉시예약형인 경우 예약상태가 승인상태여야 합니다.");
				}
			}
			else {
				if (!string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.신청) && !string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.반려)) {
					throw new InvalidationException("예약취소는 시설이 승인형인 경우 예약상태가 신청 또는 반려 상태여야 합니다.");
				}
			}

			workTypeNm = "예약취소";
			workCn = "예약취소 처리하였습니다.";
		}
		// 예약상태 = 이용종료
		else if (string.equals(reserveSt, CodeExt.reserveSt.이용종료)) {
			// 예약상태가 승인이고, 현재 이용중인 경우
			String nowTm = date.format(now, "yyyyMMddHHmm");
			String beginTm = rsvt.getRsvtDay() + rsvt.getRsvtBgngTm();
			String endTm = rsvt.getRsvtDay() + rsvt.getRsvtEndTm();
			boolean aprv = string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.승인) || string.equals(rsvt.getReserveSt(), CodeExt.reserveSt.이용중);
			boolean going = string.compare(nowTm, beginTm) >= 0 && string.compare(nowTm, endTm) <= 0;
			if (!aprv || !going) {
				throw new InvalidationException("이용종료는 이용중인 상태에서만 할 수 있습니다.");
			}

			workTypeNm = "이용종료";
			workCn = "이용종료 처리하였습니다.";
		}
		// 그 외는 오류
		else {
			throw new InvalidationException("예약상태 값이 올바르지 않습니다.");
		}

		/*
		 * 예약정보 변경
		 */
		rsvt.setReserveSt(reserveSt);
		rsvt.setReserveStDt(now);
		rsvt.setRejectReasonCn(param.getRejectReasonCn());
		rsvt.setUpdatedDt(now);
		rsvt.setUpdaterId(worker.getMemberId());

		usptMvnFcRsvtDao.update(rsvt);

		/*
		 * 예약처리이력 추가
		 */
		String histId = string.getNewId(prefixId.이력ID);
		UsptMvnFcltyResveHist hist = UsptMvnFcltyResveHist.builder()
				.histId(histId)
				.histDt(now)
				.reserveId(reserveId)
				.workTypeNm(workTypeNm)
				.workCn(workCn)
				.workerId(worker.getMemberId())
				.build();

		usptMvnFcRsvtHistDao.insert(hist);

		/*
		 * 결과 조회
		 */
		rsvt = usptMvnFcRsvtDao.select(reserveId);

		return rsvt;
	}

	public MvnFcCutoffTimeDto getFacCutoffTimeList(String mvnFcId, String ymd)
	{
		// 시설예약 정보 중 '신청', '승인' 상태인 예약정보 목록 조회
		List<UsptMvnFcltyResve> rsvtList = usptMvnFcRsvtDao.selectCutoffTime(mvnFcId, ymd);

		// 시설예약 정보를 기반으로 각 건별 예약된 시간대를 생성한다.
		List<CutoffTimeDto> timeList = new ArrayList<>();
		for (UsptMvnFcltyResve rsvt : rsvtList) {
			int time = string.toInt(rsvt.getRsvtBgngTm());
			int endTime = string.toInt(rsvt.getRsvtEndTm());
			while (time <= endTime) {
				CutoffTimeDto item = CutoffTimeDto.builder()
											.time(String.format("%04d", time))
											.build();
				timeList.add(item);
				time += 100;
			}
		}

		// 출력 VO 생성
		MvnFcCutoffTimeDto output = MvnFcCutoffTimeDto.builder()
												.mvnFcId(mvnFcId)
												.ymd(ymd)
												.build();

		// 예약된 시간대가 있는 경우
		if (timeList.size() > 0) {
			output.setCutoffTimeList(timeList);
		}

		// Output VO를 생성하여 리턴한다.
		return output;
	}

	public CorePagination<MvnFcRsvtListItemDto> getUserRsvtList(UserRsvtListParam param, CorePaginationParam pageParam)
	{
		// 작업자가 회원이어야 한다.
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		/*
		 * 입력검사
		 */
		Date beginDt = string.toDate(param.getSrchBeginDay());
		String beginDay = date.format(beginDt, "yyyyMMdd");
		Date endDt = string.toDate(param.getSrchEndDay());
		String endDay = date.format(endDt, "yyyyMMdd");

		if (string.isBlank(beginDay) || string.isBlank(endDay)) {
			throw new InvalidationException("조회기간을 모두 입력하세요.");
		}

		// 전체건수 조회
		Long totalItems = usptMvnFcRsvtDao.selectUserList_count(worker.getMemberId(), param.getReserveSt(), beginDay, endDay, param.getMvnFcNm());

		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);

		// 목록조회
		List<MvnFcRsvtListItemDto> list = usptMvnFcRsvtDao.selectUserList(
				worker.getMemberId(),
				param.getReserveSt(),
				beginDay,
				endDay,
				param.getMvnFcNm(),
				info.getBeginRowNum(),
				info.getItemsPerPage(),
				info.getTotalItems());

		// return value
		CorePagination<MvnFcRsvtListItemDto> dto = new CorePagination<>(info, list);
		return dto;
	}

	public MvnFcRsvtDto getUserRsvt(String reserveId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		/*
		 * 예약정보 조회
		 */
		UsptMvnFcltyResve rsvt = usptMvnFcRsvtDao.select(reserveId);
		if (rsvt == null) {
			throw new InvalidationException("예약정보가 없습니다.");
		}

		// 작업자 본인의 예약정보가 아니면, 권한없음.
		if (!string.equals(rsvt.getRsvctmId(), worker.getMemberId())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 시설정보 조회
		UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(rsvt.getMvnFcId());
		if (mvnFc == null) {
			throw new InvalidationException("시설정보가 없습니다.");
		}

//		// 신청자정보 조회
//		UserDto userDto = userDao.select(rsvt.getRsvctmId());
//		if (userDto == null) {
//			throw new InvalidationException("신청자 정보가 없습니다.");
//		}
//
//		MvnFcRsvctmDto rsvctm = MvnFcRsvctmDto.builder()
//									.memberId(userDto.getMemberId())
//									.memberNm(userDto.getMemberNm())
//									.nickname(userDto.getNickname())
//									.loginId(userDto.getLoginId())
//									.encEmail(userDto.getEncEmail())
//									.encMobileNo(userDto.getEncMobileNo())
//									.build();

		MvnFcRsvtDto dto = MvnFcRsvtDto.builder()
				.mvnFcRsvt(rsvt)
				.mvnFc(mvnFc)
//				.rsvctm(rsvctm)
				.build();
		return dto;
	}

	public JsonList<UsptMvnFcltyResveHist> getReserveHistList(String reserveId) {
		// 작업자 확인
		SecurityUtils.checkWorkerIsInsider();

		// 예약건 확인
		UsptMvnFcltyResve rsvt = usptMvnFcRsvtDao.select(reserveId);
		if (rsvt == null) {
			throw new InvalidationException("예약건이 존재하지 않습니다.");
		}

		// 처리이력 조회
		List<UsptMvnFcltyResveHist> list = usptMvnFcRsvtHistDao.selectList(reserveId);

		return new JsonList<>(list);
	}

	public MvnFcReserveStCountDto getStatusCount() {
		// 내부사용자 로그인 검증
		SecurityUtils.checkWorkerIsInsider();

		// 시설예약상태별 건수 조회
		return usptMvnFcRsvtDao.selectReserveSt_count();
	}
}
