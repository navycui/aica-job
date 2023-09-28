package aicluster.common.api.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.event.dto.EventViewGetListParam;
import aicluster.common.common.dao.CmmtEventDao;
import aicluster.common.common.dto.EventListItem;
import aicluster.common.common.dto.EventPrevNextItem;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventViewService {

	@Autowired
	private CmmtEventDao cmmtEventDao;

	/**
	 * 목록 검색조건 검증
	 *
	 * @param param(beginDay:조회기간 시작일, endDay:조회기간 종료일, searchType:조회유형, searchCn:조회내용)
	 */
	private void validSrchParam(EventViewGetListParam param)
	{
		String[] arrSortType = {CodeExt.eventSortType.생성일시, CodeExt.eventSortType.조회건수};
		if (!string.containsIgnoreCase(arrSortType, param.getSortType())) {
			log.error(String.format("정렬기준 검증 오류 : [%s]", param.getSortType()));
			throw new InvalidationException(String.format(validateMessageExt.허용불가, "정렬기준", param.getSortType()));
		}
		else {
			if (string.equals(param.getSortType(), CodeExt.eventSortType.생성일시)) {
				param.setSortType("creat_dt");
			}
			else if (string.equals(param.getSortType(), CodeExt.eventSortType.조회건수)) {
				param.setSortType("rdcnt");
			}
		}
	}

	/**
	 * 오늘 게시 중인 이벤트 목록
	 *
	 * @param searchCnt: 조회건수
	 * @return 이벤트 목록
	 */
	public JsonList<EventListItem> getTodayList(Long searchCnt) {
		if(searchCnt == null || searchCnt < 1) {
			searchCnt = 4L;
		}
		List<EventListItem> list = cmmtEventDao.selectList_time(searchCnt);
		return new JsonList<>(list);
	}

	/**
	 * 게시 중인 이벤트 목록 조회
	 *
	 * @param param(beginDay:조회기간 시작일, endDay:조회기간 종료일, searchType:조회유형, searchCn:조회내용)
	 * @param pageParam
	 * @return 페이징 목록
	 */
	public CorePagination<EventListItem> getList(EventViewGetListParam param, CorePaginationParam pageParam)
	{
		// 검색 조건 검증
		validSrchParam(param);

		// 건수 조회
		long totalItems = cmmtEventDao.selectList_posting_count(param);

		// 목록 조회
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<EventListItem> list = cmmtEventDao.selectList_posting(param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);

		// CorePagination 생성하여 출력
		return new CorePagination<>(info, list);
	}

	/**
	 * 게시 이벤트 목록조회의 검색조건을 기준으로 이벤트에 대한 이전글/다음글 정보를 조회힌다.
	 *
	 * @param eventId : 이벤트ID
	 * @param param(beginDay:조회기간 시작일, endDay:조회기간 종료일, searchType:조회유형, searchCn:조회내용)
	 * @return EventPrevNextItem
	 */
	public EventPrevNextItem getPrevNext(String eventId, EventViewGetListParam param)
	{
		// 검색 조건 검증
		validSrchParam(param);

		return cmmtEventDao.selectPrevNext(eventId, param);
	}

}
