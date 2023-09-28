package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptRsltIdxIemHist;

@Repository
public interface UsptRsltIdxIemHistDao {
	void insert(UsptRsltIdxIemHist info);
	List<UsptRsltIdxIemHist> selectList(String rsltHistId);
}
