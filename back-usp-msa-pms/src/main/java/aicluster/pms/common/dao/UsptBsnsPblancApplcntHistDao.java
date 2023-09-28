package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancApplcntHist;

@Repository
public interface UsptBsnsPblancApplcntHistDao {
	void insert(UsptBsnsPblancApplcntHist info);
	void insertList(List<UsptBsnsPblancApplcntHist> list);
	List<UsptBsnsPblancApplcntHist> selectList(String applyId);
}
