package aicluster.common.api.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.LogtErrorLogDao;
import aicluster.common.common.dto.ErrorLogListItem;
import aicluster.common.common.dto.LogTrendItem;
import aicluster.common.common.entity.LogtErrorLog;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class ErrorLogService {
	public static class searchType {
		public static final String 일별조회 = "DAY";
		public static final String 월별조회 = "MONTH";
		public static final String[] list = {일별조회, 월별조회};
	}

	@Autowired
	private LogtErrorLogDao dao;

	public JsonList<LogTrendItem> getTrendMonthly(String apiSystemId, String beginMonth, String endMonth) {
		List<LogTrendItem> list = dao.selectTrendMonthly(apiSystemId, beginMonth, endMonth);

		return new JsonList<>(list);
	}

	public JsonList<LogTrendItem> getTrendDaily(String apiSystemId, String beginDay, String endDay) {
		List<LogTrendItem> list = dao.selectTrendDaily(apiSystemId, beginDay, endDay);

		return new JsonList<>(list);
	}

	public CorePagination<ErrorLogListItem> getList(String apiSystemId, String beginDay, String endDay, Long page,
			Long itemsPerPage) {
		if (string.length(beginDay) != 8 || string.length(endDay) != 8) {
			throw new InvalidationException("기간이 올바르지 않습니다.");
		}
		if (page == null) {
			page = 1L;
		}
		if (itemsPerPage == null) {
			itemsPerPage = 20L;
		}

		long totalItems = dao.selectList_count(apiSystemId, beginDay, endDay);
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		List<ErrorLogListItem> list = dao.selectList(apiSystemId, beginDay, endDay, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());

		CorePagination<ErrorLogListItem> dto = new CorePagination<>(info, list);
		return dto;
	}

	public LogtErrorLog get(String logId) {
		LogtErrorLog log = dao.select(logId);
		if (log == null) {
			throw new InvalidationException("자료가 없습니다.");
		}
		return log;
	}
}
