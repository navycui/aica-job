package aicluster.tsp.api.admin.dashboard.service;

import aicluster.tsp.common.dao.CmmtBoardArticleDao;
import aicluster.tsp.common.dto.DashboardDto;
import aicluster.tsp.common.entity.CmmtBoardArticle;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

	@Autowired
	private CmmtBoardArticleDao cmmtBoardArticleDao;

	public CorePagination<CmmtBoardArticle> selectDashboardList(Long page, Long itemsPerPage) {

		// 건수 조회
		Long totalItems = cmmtBoardArticleDao.selectDashboardListCount();
		// 조회

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		List<CmmtBoardArticle> list = cmmtBoardArticleDao.selectDashboardList(info.getBeginRowNum()
				,info.getItemsPerPage(),info.getTotalItems());

		//CorePagination 생성하여 출력
		CorePagination<CmmtBoardArticle> vo = new CorePagination<>(info,list);
		return vo;
	}

	public DashboardDto selectCount() {
		return cmmtBoardArticleDao.selectDashboardCount();
	}
}

