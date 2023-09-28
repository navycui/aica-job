package aicluster.tsp.api.front.svcintro.controller;


import aicluster.tsp.api.front.svcintro.param.EqpmnIntroDetailParam;
import aicluster.tsp.api.front.svcintro.param.EqpmnIntroParam;
import aicluster.tsp.api.front.svcintro.param.EqpmnIntroSearchParam;
import aicluster.tsp.api.front.svcintro.service.EqpmnIntroService;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/front/eqpmn-info")
@RequiredArgsConstructor
@Api(tags = "Front > 실증장비소개")
public class EqpmnIntroController {

    @Autowired
    EqpmnIntroService service;

    @ApiOperation(value = "실증장비 소개(전체)", notes = "실증장비 소개 전체페이지")
    @GetMapping("")
    public CorePagination<EqpmnIntroParam> getEqpmnInfo(CorePaginationParam cpParam, EqpmnIntroSearchParam search){
        return service.getEqpmnInfo(cpParam, search);
    }

    @ApiOperation(value = "실증장비 소개 상세페이지", notes = "실증장비 소개 상세페이지")
    @GetMapping("/{eqpmnId}")
    public EqpmnIntroDetailParam getEqpmnInfoDetail(@PathVariable("eqpmnId") String eqpmnId){
        return service.getEqpmnInfoDetail(eqpmnId);
    }

}
