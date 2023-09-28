package aicluster.tsp.api.front.notice.controller;

import aicluster.tsp.api.front.notice.param.TspNoticeListParam;
import aicluster.tsp.api.front.notice.service.TspNoticeService;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/front/notice")
@RequiredArgsConstructor
@Api(tags = "Front > 공지사항")

public class TspNoticeController {
    @Autowired
    TspNoticeService service;

    @ApiOperation(value = "공지사항", notes = "공지사항 전체페이지")
    @GetMapping("")
    public CorePagination<TspNoticeListParam> getEqpmnInfo(CorePaginationParam cpParam, TspNoticeListParam search){
        return service.getNoticeInfo(cpParam, search);
    }
}
