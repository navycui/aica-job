package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancDetail;

@Repository
public interface UsptBsnsPblancDetailDao {
	void insert(UsptBsnsPblancDetail info);
	int update(UsptBsnsPblancDetail info);
	int delete(UsptBsnsPblancDetail info);
	List<UsptBsnsPblancDetail> selectList(String pblancId);
}
