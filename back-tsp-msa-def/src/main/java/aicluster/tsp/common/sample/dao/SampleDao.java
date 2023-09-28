package aicluster.tsp.common.sample.dao;

import aicluster.tsp.common.sample.entity.CmmtCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleDao {
	List<CmmtCode> selectAll();
}
