package aicluster.tsp.api.front.notice.service;

import aicluster.tsp.api.front.notice.param.TspNoticeListParam;
import aicluster.tsp.common.dao.TsptNoticeDao;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TspNoticeService {
    @Autowired
    TsptNoticeDao tsptNoticeDao;

    public static final long ITEMS_PER_PAGE = 5;

    public CorePagination<TspNoticeListParam> getNoticeInfo(CorePaginationParam cpParam, TspNoticeListParam search)
    {
        if (CoreUtils.string.isBlank(cpParam.getPage().toString()) || cpParam.getPage() < 1)
            cpParam.setPage(1L);
        if (CoreUtils.string.isBlank(cpParam.getItemsPerPage().toString()) || cpParam.getItemsPerPage() < 1)
            cpParam.setItemsPerPage(ITEMS_PER_PAGE);

        long totalItems = tsptNoticeDao.getNoticeCount(search);

        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);
        Long beginRowNum = info.getBeginRowNum();

        List<TspNoticeListParam> list = tsptNoticeDao.getNoticeList(beginRowNum, cpParam.getItemsPerPage(), search);
        CorePagination<TspNoticeListParam> pagination = new CorePagination<>(info, list);

        return pagination;
    }
}
