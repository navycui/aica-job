package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.common.dto.MvnCmpnyAlarmDto;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import aicluster.mvn.common.dto.MvnUserDto;
import aicluster.mvn.common.dto.UserMvnCmpnyDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;

@Repository
public interface UsptMvnEntrpsInfoDao {

    void insertList(List<UsptMvnEntrpsInfo> list);

    UsptMvnEntrpsInfo select(String mvnId);

    void update(UsptMvnEntrpsInfo mvnCmpny);

    Long selectList_count(@Param("param") MvnCmpnyListParam param);

    List<MvnCmpnyListItemDto> selectList(
    		@Param("param") MvnCmpnyListParam param,
    		@Param("beginRowNum") Long beginRowNum,
    		@Param("itemsPerPage") Long itemsPerPage,
    		@Param("totalItems") Long totalItems
    		);

    UsptMvnEntrpsInfo select_mvnFcId(String mvnFcId);

    UserMvnCmpnyDto selectUser(@Param("cmpnyId") String cmpnyId, @Param("mvnGoingYn") boolean mvnGoingYn);

    List<UsptMvnEntrpsInfo> selectSyncTgrtList();

    List<MvnCmpnyAlarmDto> selectSendTgrtList(@Param("mvnIds") List<String> mvnIds);

    Long selectGoing_count(@Param("cmpnyId") String cmpnyId, @Param("mvnId") String mvnId);

    MvnUserDto selectMvnUser(String mvnId);
    
    String selectPblancBsnsEndDt(String lastSlctnTrgetId);
}
