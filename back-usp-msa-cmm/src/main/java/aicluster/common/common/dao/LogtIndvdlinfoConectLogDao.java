package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.LogtIndvdlinfoConectLog;

@Repository
public interface LogtIndvdlinfoConectLogDao {
	Long selectList_count(
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay,
			@Param("workerLoginId") String workerLoginId);

	List<LogtIndvdlinfoConectLog> selectList(
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay,
			@Param("workerLoginId") String workerLoginId,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);
}
