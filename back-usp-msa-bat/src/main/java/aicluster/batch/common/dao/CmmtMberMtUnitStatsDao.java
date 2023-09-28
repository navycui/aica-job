package aicluster.batch.common.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface CmmtMberMtUnitStatsDao {

	int upsert(String ym);

}
