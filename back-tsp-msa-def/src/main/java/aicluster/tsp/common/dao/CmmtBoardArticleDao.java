package aicluster.tsp.common.dao;

import aicluster.tsp.common.dto.DashboardDto;
import aicluster.tsp.common.entity.CmmtBoardArticle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmmtBoardArticleDao {

	List<CmmtBoardArticle> selectDashboardList(
			@Param("beginRowNum")Long beginRowNum
			,@Param("itemsPerPage") Long itemsPerPage
			,@Param("totalItems") Long totalItems);

	Long selectDashboardListCount();

	DashboardDto selectDashboardCount();
}
