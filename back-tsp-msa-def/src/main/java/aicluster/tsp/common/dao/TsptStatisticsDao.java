package aicluster.tsp.common.dao;

import aicluster.tsp.common.dto.StatisticsDto;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TsptStatisticsDao {

	List<StatisticsDto> selectStatistics(Date beginDt, Date endDt);

	Integer selectEntrprsCount(Date beginDt, Date endDt);
}
