package aicluster.batch.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptMvnEntrpsInfo;

@Repository
public interface UsptMvnEntrpsInfoDao {
    UsptMvnEntrpsInfo select(String mvnId);
    void update(UsptMvnEntrpsInfo mvnCmpny);
}
