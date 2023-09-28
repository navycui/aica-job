package aicluster.batch.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.LmsEduCateT;

@Repository
public interface LmsEduCateTDao {
	List<LmsEduCateT> selectList();
}
