package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancAppnTask;

@Repository
public interface UsptBsnsPblancAppnTaskDao {
	void insert(UsptBsnsPblancAppnTask info);
	int deleteAll(UsptBsnsPblancAppnTask info);
	List<UsptBsnsPblancAppnTask> selectList(String pblancId);

	/** 공고 지원분야 목록 조회 */
	List<UsptBsnsPblancAppnTask> selectAppnTaskList();
}
