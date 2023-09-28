package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.common.dto.HistDtListItemDto;
import aicluster.mvn.common.entity.UsptResrceInvntryInfoHist;

@Repository
public interface UsptResrceInvntryInfoHistDao {
	void insertList(List<UsptResrceInvntryInfoHist> list);
	List<UsptResrceInvntryInfoHist> selectList_histDt(String histDt);
	Long selectHistDtGroupList_count();
	List<HistDtListItemDto> selectHistDtGroupList(
			@Param("beginRowNum") Long beginRowNum ,         /* 시작 Row 위치 */
			@Param("itemsPerPage") Long itemsPerPage,         /* 페이지        */
			@Param("totalItems") Long totalItems            /* 전체 건수     */
			);
}
