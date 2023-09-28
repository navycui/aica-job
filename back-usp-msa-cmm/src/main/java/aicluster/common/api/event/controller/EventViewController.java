package aicluster.common.api.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.event.dto.EventViewGetListParam;
import aicluster.common.api.event.service.EventViewService;
import aicluster.common.common.dto.EventListItem;
import aicluster.common.common.dto.EventPrevNextItem;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/events-view")
public class EventViewController {
	@Autowired
	private EventViewService eventViewService;

	/**
	 * 오늘의 이벤트 목록 조회
	 *
	 * @param searchCnt: 조회건수
	 * @return
	 */
	@GetMapping("/today")
	public JsonList<EventListItem> getTodayList(Long searchCnt){
		return eventViewService.getTodayList(searchCnt);
	}

	/**
	 * 게시 이벤트 목록 조회
	 *
	 * @param param(beginDay:조회기간 시작일, endDay:조회기간 종료일, searchType:조회유형, searchCn:조회내용)
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<EventListItem> getList(EventViewGetListParam param, CorePaginationParam pageParam){
		return eventViewService.getList(param, pageParam);

	}

	/**
	 * 게시 이벤트에 대한 이전글/다음글 조회
	 *
	 * @param eventId : 이벤트ID
	 * @param param(beginDay:조회기간 시작일, endDay:조회기간 종료일, searchType:조회유형, searchCn:조회내용)
	 * @return EventPrevNextItem
	 */
	@GetMapping("/{eventId}/previous-next")
	public EventPrevNextItem getPreviousNext(@PathVariable String eventId, EventViewGetListParam param) {
		return eventViewService.getPrevNext(eventId, param);
	}

}
