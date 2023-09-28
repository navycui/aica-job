package aicluster.tsp.common.dao;

import aicluster.tsp.api.front.mypage.resrce.param.MyResrceSearchParam;
import aicluster.tsp.api.front.notice.param.TspNoticeListParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptNoticeDao {
    Long getNoticeCount(@Param("param") TspNoticeListParam param);

    List<TspNoticeListParam> getNoticeList(Long beginRowNum, Long itemsPerPage, @Param("param") TspNoticeListParam param);
}
