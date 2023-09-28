package aicluster.common.api.event.service;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.api.event.dto.EventViewGetListParam;
import aicluster.common.common.dto.EventListItem;
import aicluster.common.common.dto.EventPrevNextItem;
import aicluster.common.support.TestServiceSupport;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventViewServiceTest extends TestServiceSupport {

	@Autowired
	private EventViewService service;

	@Test
	void test() {
		// 오늘의 게시물 조회
		JsonList<EventListItem> todayList = service.getTodayList(4L);
		assertNotNull(todayList);
		log.info(String.format("### 오늘의 게시물 조회 결과 ###\n[%s]", todayList.getList().toString()));

		// 게시된 이벤트 목록 조회
		EventViewGetListParam param = new EventViewGetListParam();
		param.setBeginDay(string.toDate("20220501"));
		param.setEndDay(string.toDate("20221230"));

		CorePaginationParam pageParam = new CorePaginationParam();
		pageParam.setPage(1L);
		pageParam.setItemsPerPage(3L);

		CorePagination<EventListItem> postingList = service.getList(param, pageParam);

		assertNotNull(postingList);
		log.info(String.format("### 게시 이벤트 목록 조회 결과 ###\n[%s]", postingList.getList().toString()));

		if (!postingList.getList().isEmpty()) {
			param.setSortType(null);

			// 게시된 이벤트의 이전글/다음글 조회
			String eventId = postingList.getList().get(0).getEventId();
			EventPrevNextItem prevNextItem = service.getPrevNext(eventId, param);
			assertNotNull(prevNextItem);

			log.info(String.format("### 이전글/다음글 조회 결과 ###\n[%s]", prevNextItem.toString()));
		}
	}
}
