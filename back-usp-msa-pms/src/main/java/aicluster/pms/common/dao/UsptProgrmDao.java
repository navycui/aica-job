package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptProgrm;

@Repository
public interface UsptProgrmDao {
	void insert(UsptProgrm info);
	int update(UsptProgrm info);
	int delete(UsptProgrm info);
	List<UsptProgrm> selectList(String memberId);
}
