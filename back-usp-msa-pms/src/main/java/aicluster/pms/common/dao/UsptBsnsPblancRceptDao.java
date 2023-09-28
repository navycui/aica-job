package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancRcept;

@Repository
public interface UsptBsnsPblancRceptDao {
	List<UsptBsnsPblancRcept> selectList(UsptBsnsPblancRcept usptBsnsPblancRcept);
	long selectListCount(String pblancId);
	void insert(UsptBsnsPblancRcept info);
	int update(UsptBsnsPblancRcept info);
}
