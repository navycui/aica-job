package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEvlIemResult;

@Repository
public interface UsptEvlIemResultDao {
	int insert(UsptEvlIemResult usptEvlIemResult);

	int update(UsptEvlIemResult usptEvlIemResult);

	List<UsptEvlIemResult> selectList(UsptEvlIemResult usptEvlIemResult);

	int delete(String evlIemResultId);
}