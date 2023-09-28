package aicluster.common.api.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.LogtDeUnitConnectMberLogDao;
import aicluster.common.common.dao.LogtElapseTimeLogDao;
import aicluster.common.common.dao.LogtIndvdlinfoConectLogDao;
import aicluster.common.common.dto.DayMemberSummary;
import aicluster.common.common.dto.ElapsedTimeLogSummary;
import aicluster.common.common.entity.LogtIndvdlinfoConectLog;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class LogService {

	@Autowired
	private LogtIndvdlinfoConectLogDao logtIndvdlinfoLogDao;
	@Autowired
	private LogtElapseTimeLogDao logtElapsedTimeLogDao;
	@Autowired
	private LogtDeUnitConnectMberLogDao logtDayMemberLogDao;

	public CorePagination<LogtIndvdlinfoConectLog> getIndvdlinfoList(String beginDay, String endDay, String workerLoginId, Long page,
			Long itemsPerPage) {
		if (page == null) {
			page = 1L;
		}
		if (itemsPerPage == null) {
			itemsPerPage = 20L;
		}
		long totalItems = logtIndvdlinfoLogDao.selectList_count(beginDay, endDay, workerLoginId);
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		List<LogtIndvdlinfoConectLog> list = logtIndvdlinfoLogDao.selectList(beginDay, endDay, workerLoginId, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());
		CorePagination<LogtIndvdlinfoConectLog> dto = new CorePagination<>(info, list);
		return dto;
	}

	public List<LogtIndvdlinfoConectLog> getExcelIndvdlinfoList(String beginDay, String endDay, String workerLoginId)
	{
		long totalItems = logtIndvdlinfoLogDao.selectList_count(beginDay, endDay, workerLoginId);
		return logtIndvdlinfoLogDao.selectList(beginDay, endDay, workerLoginId, 1L, totalItems, totalItems);
	}

	public ElapsedTimeLogSummary getElapsedTimeSummary() {
		return logtElapsedTimeLogDao.selectElapsedTimeLogSummary();
	}

	public DayMemberSummary getDayMemberSummary() {
		return logtDayMemberLogDao.selectDayMemberSummary();
	}

}
