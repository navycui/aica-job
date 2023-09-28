package aicluster.tsp.api.admin.eqpmn.dscnt.controller;

import aicluster.tsp.api.admin.eqpmn.dscnt.param.DscntParam;
import aicluster.tsp.api.admin.eqpmn.dscnt.service.DscntService;
import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.common.entity.TsptEqpmnDscnt;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/eqpmns/dscnt")
@RequiredArgsConstructor
@Api(tags = "할인")
public class DscntController {
    @Autowired
    private DscntService service;


    @ApiOperation(value = "장비 할인 등록", notes = "장비에 대한 할인을 등록합니다.")
    @PostMapping("")
    public TsptEqpmnDscnt postDscntList(@RequestBody(required = false) DscntParam param) {
        return service.postDscntList(param);
    }


    @ApiOperation(value = "장비 할인 조회", notes = "장비에 대한 할인 리스트를 조회합니다.")
    @GetMapping("")
    public CorePagination<DscntParam> getDscntList(
            CorePaginationParam corePaginationParam,
            DscntParam search) {
        return service.getDscntList(search, corePaginationParam);
    }



    @ApiOperation(value = "사용여부 수정", notes = "할인등록에 있는 할인 리스트의 사용 여부를 수정합니다.")
    @PutMapping("")
    public CommonReturnList<DscntParam> updateUsgstt(@RequestBody List<DscntParam> dscntParam) {
        return new CommonReturnList<>(service.updateUsgstt(dscntParam));
    }

    @ApiOperation(value = "장비 할인 검색", notes = "장비에 대한 사용적용된 할인 리스트를 조회합니다.")
    @GetMapping("/apply")
    public CorePagination<DscntParam> getDscntApplyList(DscntParam apply, CorePaginationParam corePaginationParam) {
        return service.getDscntApplyList(apply, corePaginationParam);
    }

}
