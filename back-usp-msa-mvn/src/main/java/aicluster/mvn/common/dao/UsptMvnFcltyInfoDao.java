package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.facility.dto.MvnFcListParam;
import aicluster.mvn.api.status.dto.MvnStatusListParam;
import aicluster.mvn.common.dto.MvnFcCodeDto;
import aicluster.mvn.common.dto.MvnFcOfficeRoomDto;
import aicluster.mvn.common.dto.MvnFcStatsListItemDto;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;

@Repository
public interface UsptMvnFcltyInfoDao {
    void insert(UsptMvnFcltyInfo mvnFc);
    UsptMvnFcltyInfo select(String mvnFcId);
    void update(UsptMvnFcltyInfo mvnFc);
    void delete(String mvnFcId);
    Long selectList_count(@Param("param") MvnFcListParam param);
    List<UsptMvnFcltyInfo> selectList(
        @Param("param") MvnFcListParam param,
        @Param("beginRowNum") Long beginRowNum,
        @Param("itemsPerPage") Long itemsPerPage,
        @Param("totalItems") Long totalItems
    );
    Long selectCurrStateList_count(@Param("param") MvnStatusListParam param);
    List<MvnFcStatsListItemDto> selectCurrStateList(
    	@Param("param") MvnStatusListParam param,
        @Param("beginRowNum") Long beginRowNum,
        @Param("itemsPerPage") Long itemsPerPage,
        @Param("totalItems") Long totalItems
    );
    UsptMvnFcltyInfo selectEnable(
        @Param("mvnFcType") String mvnFcType,
        @Param("mvnFcId") String mvnFcId
    );
    Long selectEnableShareList_count(String mvnFcDtype);
    List<UsptMvnFcltyInfo> selectEnableShareList(
        @Param("mvnFcDtype") String mvnFcDtype,
        @Param("beginRowNum") Long beginRowNum,
        @Param("itemsPerPage") Long itemsPerPage,
        @Param("totalItems") Long totalItems
    );
    Long selectEnableOfficeList_count(
        @Param("bnoRoomNo") String bnoRoomNo,
        @Param("mvnFcCapacity") Long mvnFcCapacity,
        @Param("mvnFcar") String mvnFcar
    );
    List<UsptMvnFcltyInfo> selectEnableOfficeList(
        @Param("bnoRoomNo") String bnoRoomNo,
        @Param("mvnFcCapacity") Long mvnFcCapacity,
        @Param("mvnFcar") String mvnFcar,
        @Param("beginRowNum") Long beginRowNum,
        @Param("itemsPerPage") Long itemsPerPage,
        @Param("totalItems") Long totalItems
    );
    List<MvnFcOfficeRoomDto> selectBnoRoomCodeList();
    List<MvnFcCodeDto> selectCapacityCodeList();
    List<MvnFcCodeDto> selectFcarCodeList();
}
