package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.dto.LogTrendItem;

@Repository
public interface LogtLoginLogDao {

	List<LogTrendItem> selectTrendDaily(
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);

	List<LogTrendItem> selectTrendMonthly(
			@Param("beginMonth") String beginMonth,
			@Param("endMonth") String endMonth);
}
