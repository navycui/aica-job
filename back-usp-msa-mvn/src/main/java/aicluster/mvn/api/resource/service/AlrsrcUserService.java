package aicluster.mvn.api.resource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.resource.dto.AlrsrcUserListParam;
import aicluster.mvn.common.dao.UsptResrceAsgnEntrpsDao;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbDao;
import aicluster.mvn.common.dto.AlrsrcDstbUserDto;
import aicluster.mvn.common.dto.AlrsrcUserDto;
import aicluster.mvn.common.dto.AlrsrcUserListItemDto;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class AlrsrcUserService {

	@Autowired
	private UsptResrceAsgnEntrpsDao cmpnyDao;

	@Autowired
	private UsptResrceAsgnDstbDao dstbDao;

	public CorePagination<AlrsrcUserListItemDto> getList(AlrsrcUserListParam param, CorePaginationParam pageParam)
	{
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		// 로그인 사용자 memberId 값을 검색 Dto 세팅
		param.setCmpnyId(worker.getMemberId());

		// 검색조건 필수값 검증
		if ( string.isBlank(param.getAlrsrcBgngDay()) || string.isBlank(param.getAlrsrcEndDay()) ) {
			throw new InvalidationException(String.format(validateMessageExt.날짜없음, "이용기간"));
		}
		if ( date.getDiffDays(string.toDate(param.getAlrsrcBgngDay()), string.toDate(param.getAlrsrcEndDay())) < 0 ) {
			throw new InvalidationException(String.format(validateMessageExt.일시_큰비교오류, "이용기간 시작일", "종료일", "날짜"));
		}

		long totalItems = cmpnyDao.selectUserList_count( param );
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<AlrsrcUserListItemDto> list = cmpnyDao.selectUserList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

		return new CorePagination<>(info, list);
	}

	public AlrsrcUserDto get(String alrsrcId)
	{
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		AlrsrcUserDto alrsrcCmpnyUser = cmpnyDao.selectUser( worker.getMemberId(), alrsrcId );
		if (alrsrcCmpnyUser == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 정보"));
		}

		List<AlrsrcDstbUserDto> alrsrcDstbUserList = dstbDao.selectUserList( alrsrcId );
		alrsrcCmpnyUser.setAlrsrcDstbUserList(alrsrcDstbUserList);

		return alrsrcCmpnyUser;
	}

}
