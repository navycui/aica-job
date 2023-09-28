package aicluster.tsp.api.front.mypage.resrce.controller;


import aicluster.tsp.api.front.mypage.resrce.param.MyResrceDetailParam;
import aicluster.tsp.api.front.mypage.resrce.param.MyResrceListParam;
import aicluster.tsp.api.front.mypage.resrce.param.MyResrceSearchParam;
import aicluster.tsp.api.front.mypage.resrce.service.MyResrceService;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/front/mypage/resrce")
@RequiredArgsConstructor
@Api(tags = "Front > 마이페이지 > 실증자원사용")
public class MyResrceController {

    @Autowired
    private MyResrceService service;

    @ApiOperation(value = "실증자원사용 목록 조회", notes = "실증자원 목록 조회")
    @GetMapping("")
    public CorePagination<MyResrceListParam> getResrceList(MyResrceSearchParam param, CorePaginationParam cpParam){
        return service.getResrceList(param, cpParam);
    }

    @ApiOperation(value = "실증자원사용 상세정보 조회", notes = "실증자원 상세정보 조회")
    @GetMapping("/{reqstId}")
    public MyResrceDetailParam getResrceDetail(@PathVariable(value = "reqstId") String reqstId){
        return service.getResrceDetail(reqstId);
    }

    @ApiOperation(value = "실증자원사용 상세정보 처리", notes = "실증자원 상세정보 처리")
    @PutMapping("/{reqstId}/{sttus}")
    public void putResrceDetail(@PathVariable(value = "reqstId") String reqstId, @PathVariable(value = "sttus") String sttus){
        service.putResrceDetail(reqstId, sttus);
    }
}
