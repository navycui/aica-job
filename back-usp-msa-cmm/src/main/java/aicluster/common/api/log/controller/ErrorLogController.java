package aicluster.common.api.log.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.log.service.AccessLogService;
import aicluster.common.api.log.service.ErrorLogService;
import aicluster.common.common.dto.ErrorLogListItem;
import aicluster.common.common.dto.LogTrendItem;
import aicluster.common.common.entity.LogtErrorLog;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;

@RestController
@RequestMapping("/api/log/errors")
public class ErrorLogController {

	@Autowired
	private ErrorLogService service;

	/**
	 * 에러 로그 추이
	 * @param apiSystemId
	 * @param searchType
	 * @param beginDt
	 * @param endDt
	 * @return
	 */
	@GetMapping("/trend")
	public JsonList<LogTrendItem> search(String apiSystemId, String searchType, Date beginDt, Date endDt) {
		if (string.isBlank(apiSystemId)) {
			throw new InvalidationException("API시스템ID를 입력하세요.");
		}
		if (string.isBlank(searchType)) {
			throw new InvalidationException("조회구분을 입력하세요.");
		}
		if (!array.contains(AccessLogService.searchType.list, searchType)) {
			throw new InvalidationException("조회구분이 올바르지 않습니다.");
		}
		if (beginDt == null || endDt == null) {
			throw new InvalidationException("조회기간을 입력하세요.");
		}
		if (string.equals(searchType, AccessLogService.searchType.월별조회)) {
			String beginMonth = date.format(beginDt, "yyyyMM");
			String endMonth = date.format(endDt, "yyyyMM");
			return service.getTrendMonthly(apiSystemId, beginMonth, endMonth);
		}
		else {
			String beginDay = date.format(beginDt, "yyyyMMdd");
			String endDay = date.format(endDt, "yyyyMMdd");
			return service.getTrendDaily(apiSystemId, beginDay, endDay);
		}
	}

	/**
	 * 에러 로그 목록 조회
	 * @param apiSystemId
	 * @param beginDt
	 * @param endDt
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<ErrorLogListItem> getList(String apiSystemId, Date beginDt, Date endDt, Long page, Long itemsPerPage) {
		if (string.isBlank(apiSystemId)) {
			throw new InvalidationException("API시스템ID를 입력하세요.");
		}
		if (beginDt == null || endDt == null) {
			throw new InvalidationException("조회기간을 입력하세요.");
		}
		String beginDay = date.format(beginDt, "yyyyMMdd");
		String endDay = date.format(endDt, "yyyyMMdd");
		return service.getList(apiSystemId, beginDay, endDay, page, itemsPerPage);
	}

	/**
	 * 에러 로그 상세조회
	 * @param logId
	 * @return
	 */
	@GetMapping("/{logId}")
	public LogtErrorLog getDetail(@PathVariable String logId) {
		return service.get(logId);
	}
}
