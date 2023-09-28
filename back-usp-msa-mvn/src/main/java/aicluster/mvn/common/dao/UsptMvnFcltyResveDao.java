package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.reservation.dto.MvnFcRsvtListParam;
import aicluster.mvn.common.dto.MvnFcReserveStCountDto;
import aicluster.mvn.common.dto.MvnFcRsvtListItemDto;
import aicluster.mvn.common.entity.UsptMvnFcltyResve;

@Repository
public interface UsptMvnFcltyResveDao {

	void insert(UsptMvnFcltyResve mvnFcRsvt);

	UsptMvnFcltyResve select(String reserveId);

	void update(UsptMvnFcltyResve mvnFcRsvt);

	Long selectList_count(@Param("param") MvnFcRsvtListParam param);

	List<MvnFcRsvtListItemDto> selectList(
			@Param("param") MvnFcRsvtListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	/* 파라미터와 Query가 맞지 않아 수정함
	Long selectUserList_count(
			@Param("mvnFcId") String mvnFcId,
			@Param("rsvtDay") String rsvtDay,
			@Param("rsvtBgngTm") String rsvtBgngTm,
			@Param("rsvtEndTm") String rsvtEndTm);
	*/
	Long selectUserList_count(
			@Param("rsvctmId") String rsvctmId,
			@Param("reserveSt") String reserveSt,
			@Param("rsvtBeginDay") String rsvtBeginDay,
			@Param("rsvtEndDay") String rsvtEndDay,
			@Param("mvnFcNm") String mvnFcNm);

	/* 파라미터와 Query가 맞지 않아 수정함
	List<MvnFcRsvtListItemDto> selectUserList(
			@Param("mvnFcId") String mvnFcId,
			@Param("rsvtDay") String rsvtDay,
			@Param("rsvtBgngTm") String rsvtBgngTm,
			@Param("rsvtEndTm") String rsvtEndTm,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);
	*/
	List<MvnFcRsvtListItemDto> selectUserList(
			@Param("rsvctmId") String rsvctmId,
			@Param("reserveSt") String reserveSt,
			@Param("rsvtBeginDay") String rsvtBeginDay,
			@Param("rsvtEndDay") String rsvtEndDay,
			@Param("mvnFcNm") String mvnFcNm,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	Long selectCount_validRsvtDt(
			@Param("mvnFcId") String mvnFcId,
			@Param("rsvtDay") String rsvtDay,
			@Param("rsvtBgngTm") String rsvtBgngTm,
			@Param("rsvtEndTm") String rsvtEndTm);

	List<UsptMvnFcltyResve> selectCutoffTime(
			@Param("mvnFcId") String mvnFcId,
			@Param("rsvtDay") String rsvtDay);

	MvnFcReserveStCountDto selectReserveSt_count();
}
