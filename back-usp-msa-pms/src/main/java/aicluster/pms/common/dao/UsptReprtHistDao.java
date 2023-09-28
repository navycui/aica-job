package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptReprtHist;

@Repository
public interface UsptReprtHistDao {
	void insert(UsptReprtHist info);
	List<UsptReprtHist> selectList(String reprtId);
}
