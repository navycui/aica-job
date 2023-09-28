package aicluster.common.api.log.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.log.service.AccessLogService;
import aicluster.common.common.dto.LogTrendItem;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/log/accesses")
public class AccessLogController {

	@Autowired
	private AccessLogService service;

	/**
	 * 접속 로그 추이
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
}
