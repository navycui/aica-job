package aicluster.batch.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptMvnFcltyInfo;

@Repository
public interface UsptMvnFcltyInfoDao {
    UsptMvnFcltyInfo select(String mvnFcId);
    void update(UsptMvnFcltyInfo mvnFc);
}
