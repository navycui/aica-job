package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.event.dto.EventGetListParam;
import aicluster.common.api.event.dto.EventViewGetListParam;
import aicluster.common.common.dto.EventListItem;
import aicluster.common.common.dto.EventPrevNextItem;
import aicluster.common.common.entity.CmmtEvent;

@Repository
public interface CmmtEventDao {

	long selectList_count(@Param("param") EventGetListParam param);

	CmmtEvent select(String eventId);

	List<EventListItem> selectList_today();

	List<EventListItem> selectList(
			@Param("param") EventGetListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	void insert(CmmtEvent cmmtEvent);

	void update(CmmtEvent cmmtEvent);

	void delete(String eventId);

	List<EventListItem> selectList_time(Long searchCnt);

	long selectList_posting_count(
			@Param("param") EventViewGetListParam param);

	List<EventListItem> selectList_posting(
			@Param("param") EventViewGetListParam param,
			@Param("beginRowNum")Long beginRowNum,
			@Param("itemsPerPage")Long itemsPerPage,
			@Param("totalItems")Long totalItems);

	EventPrevNextItem selectPrevNext(
			@Param("eventId") String eventId,
			@Param("param") EventViewGetListParam param);
}
