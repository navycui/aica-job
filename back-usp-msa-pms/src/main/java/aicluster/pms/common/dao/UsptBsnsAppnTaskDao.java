package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsAppnTask;

@Repository
public interface UsptBsnsAppnTaskDao {
	void insert(UsptBsnsAppnTask info);
	void insertList(List<UsptBsnsAppnTask> list);
	int update(UsptBsnsAppnTask info);
	int delete(UsptBsnsAppnTask info);
	List<UsptBsnsAppnTask> selectList(String bsnsCd);
}
