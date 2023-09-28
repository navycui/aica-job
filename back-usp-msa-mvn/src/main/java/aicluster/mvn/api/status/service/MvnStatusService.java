package aicluster.mvn.api.status.service;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.framework.security.Code.validateMessage;
import aicluster.mvn.api.status.dto.MvnStatusListParam;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dto.MvnFcStatsListItemDto;
import aicluster.mvn.common.dto.UserMvnCmpnyDto;
import aicluster.mvn.common.util.MvnUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class MvnStatusService {
	@Autowired
	private UsptMvnFcltyInfoDao mvnFcDao;
	@Autowired
	private UsptMvnEntrpsInfoDao mvnCmpnyDao;
	@Autowired
	private MvnUtils mvnUtils;

	/**
	 * 입주시설 입주현황 목록 조회
	 * (업무담당자가 입주시설(사무실) 별 입주업체 현황을 목록조회한다.)
	 *
	 * @param param : 조회조건 (mvnSt:입주상태, bnoCd:건물동코드(BNO), mvnFcCapPd:수용범위, mvnFcarPd:시설면적범위, mvnFcCapacity:시설수용인원, mvnFcar:시설면적)
	 * @param pageParam : 페이징조건
	 * @return CorePagination<MvnFcStatsListItemDto>
	 */
	public CorePagination<MvnFcStatsListItemDto> getList(MvnStatusListParam param, CorePaginationParam pageParam)
	{
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

        // 입력값 유효성 검증
        mvnUtils.validateCode("MVN_ST", param.getMvnSt(), "입주상태");
        mvnUtils.validateCode("BNO", param.getBnoCd(), "건물동");
        mvnUtils.validateCode("CAPACITY_PERIOD", param.getMvnFcCapPd(), "수용범위");
        mvnUtils.validateCode("AREA_PERIOD", param.getMvnFcarPd(), "면적범위");

        long totalItems = mvnFcDao.selectCurrStateList_count( param );
        CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
        List<MvnFcStatsListItemDto> list = mvnFcDao.selectCurrStateList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

		// log 정보생성(마스킹 출력이므로 이력 생성하지 않는다.)
        for (MvnFcStatsListItemDto item : list) {
    		if (!string.equals(worker.getMemberId(), item.getCmpnyId())) {
//    			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
//    										.memberId(worker.getMemberId())
//    										.memberIp(webutils.getRemoteIp(request))
//    										.workTypeNm("조회")
//    										.workCn("입주현황 목록페이지 담당자정보 조회 업무")
//    										.trgterId(item.getCmpnyId())
//    										.email(item.getEmail())
//    										.birthday(null)
//    										.mobileNo(item.getMobileNo())
//    										.build();
//    			indvdlInfSrchUtils.insert(logParam);
    		}
        }

        return new CorePagination<>(info, list);
	}

	/**
	 * 사용자에 대한 입주현황 조회
	 * (로그인 회원에 대한 입주현황을 조회한다.)
	 *
	 * @param allInqireYn : 전제조회여부
	 * @return UserMvnCmpnyDto
	 */
	public UserMvnCmpnyDto getUser(Boolean allInqireYn)
	{
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		boolean mvnGoingYn = false;   /* 입주중여부(기본값 false) */

		// 전체조회여부 값이 'false'이면 입주중여부는 true로 정의한다.
		if (BooleanUtils.isFalse(allInqireYn)) {
			mvnGoingYn = true;
		}

		// 입주중이거나 입주종료 상태인 입주업체정보 중 최근 수정한 데이터를 조회한다.
		UserMvnCmpnyDto userMvnCmpny = mvnCmpnyDao.selectUser( worker.getMemberId(), mvnGoingYn );
		if (userMvnCmpny == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "사용자의 입주정보"));
		}

		return userMvnCmpny;
	}
}
