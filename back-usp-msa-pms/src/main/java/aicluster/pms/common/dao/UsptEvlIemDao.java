package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.EvlIemByCmitDto;
import aicluster.pms.common.entity.UsptEvlIem;

@Repository
public interface UsptEvlIemDao {

	List<UsptEvlIem> selectList(UsptEvlIem usptEvlIem);

	UsptEvlIem select(String evlIemId);

	void insert(UsptEvlIem usptEvlIem);

	void update(UsptEvlIem usptEvlIem);

	void delete(UsptEvlIem usptEvlIem);
}
