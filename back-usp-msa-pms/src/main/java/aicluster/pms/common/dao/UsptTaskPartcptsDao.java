package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptTaskPartcpts;

@Repository
public interface UsptTaskPartcptsDao {
	int delete(UsptTaskPartcpts usptTaskPartcpts);

	int insert(UsptTaskPartcpts usptTaskPartcpts);

	int update(UsptTaskPartcpts usptTaskPartcpts);

	List<UsptTaskPartcpts> selectList(UsptTaskPartcpts usptTaskPartcpts);

	int deleteBsnsPlanDocIdAll(String  bsnsPlanDocId);

}

