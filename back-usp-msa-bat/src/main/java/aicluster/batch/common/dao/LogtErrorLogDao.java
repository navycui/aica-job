package aicluster.batch.common.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogtErrorLogDao {

	long selectCount(
			@Param("apiSystemId") String apiSystemId,
			@Param("ymd") String ymd);

}
