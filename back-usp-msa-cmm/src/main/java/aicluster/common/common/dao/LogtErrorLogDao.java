package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.dto.ErrorLogListItem;
import aicluster.common.common.dto.LogTrendItem;
import aicluster.common.common.entity.LogtErrorLog;

@Repository
public interface LogtErrorLogDao {

	List<LogTrendItem> selectTrendDaily(
			@Param("apiSystemId") String apiSystemId,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);

	List<LogTrendItem> selectTrendMonthly(
			@Param("apiSystemId") String apiSystemId,
			@Param("beginMonth") String beginMonth,
			@Param("endMonth") String endMonth);

	long selectList_count(
			@Param("apiSystemId") String apiSystemId,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);

	List<ErrorLogListItem> selectList(
			@Param("apiSystemId") String apiSystemId,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	LogtErrorLog select(String logId);
}
