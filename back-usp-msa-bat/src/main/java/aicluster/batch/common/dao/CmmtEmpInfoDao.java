package aicluster.batch.common.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface CmmtEmpInfoDao {

	int updateToDormantAdmin(String day365);
}
