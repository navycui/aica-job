package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.entity.UsptMvnFcltyRestde;

@Repository
public interface UsptMvnFcltyRestdeDao {
    void insertList(List<UsptMvnFcltyRestde> list);
    void delete(String mvnFcId);
    List<UsptMvnFcltyRestde> selectList(String mvnFcId);
}
