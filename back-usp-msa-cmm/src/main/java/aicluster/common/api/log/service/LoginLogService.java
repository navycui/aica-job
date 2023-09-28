package aicluster.common.api.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.LogtLoginLogDao;
import aicluster.common.common.dto.LogTrendItem;
import bnet.library.util.dto.JsonList;

@Service
public class LoginLogService {
	public static class searchType {
		public static final String 일별조회 = "DAY";
		public static final String 월별조회 = "MONTH";
		public static final String[] list = {일별조회, 월별조회};
	}

	@Autowired
	private LogtLoginLogDao dao;

	public JsonList<LogTrendItem> getTrendMonthly(String beginMonth, String endMonth) {
		List<LogTrendItem> list = dao.selectTrendMonthly(beginMonth, endMonth);

		return new JsonList<>(list);
	}

	public JsonList<LogTrendItem> getTrendDaily(String beginDay, String endDay) {
		List<LogTrendItem> list = dao.selectTrendDaily(beginDay, endDay);

		return new JsonList<>(list);
	}
}
