package aicluster.tsp.api.admin.eqpmn.category.controller;

import aicluster.tsp.api.admin.eqpmn.category.param.EquipmentCategoryParam;
import aicluster.tsp.api.admin.eqpmn.category.service.EquipmentCategoryService;
import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.common.entity.TsptEqpmnCl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/admin/eqpmns")
@RequiredArgsConstructor
@Api(tags = "장비분류" )
public class EquipmentCategoryController {
	/*
	* 장비 분류
	* */

	@Autowired
	private EquipmentCategoryService service;

	/**
	 * 조회 : 클릭 후 나오는 카테고리 리스트
	 * */
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK", response = TsptEqpmnCl.class)
	})
	@GetMapping("/categories/{eqpmnClId}")
	@ApiOperation(value = "장비 분류 조회", notes = "분류 ID를 부모로 가지고 있는 장비분류 리스트를 조회")
	public CommonReturnList<TsptEqpmnCl> getList(@PathVariable("eqpmnClId") String eqpmnClId){

		return new CommonReturnList<>(service.selectList(eqpmnClId));
	}

	/**
	 * 조회 : 모든 카테고리의 리스트
	 * */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = TsptEqpmnCl.class)
	})
	@GetMapping("/categories")
	@ApiOperation(value = "장비 분류 전체 조회", notes = "ROOT 부터 시작되서 하위에 있는 모든 리스트를 조회")
	public CommonReturnList<TsptEqpmnCl> selectTreeList()
	{
		return new CommonReturnList<>(service.selectAllList());
	}

	/**
	 * 저장 : 저장 버튼 클릭해서 저장
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = TsptEqpmnCl.class)
	})
	@PutMapping("/categories/{eqpmnClId}")
	@ApiOperation(value = "장비 분류 저장", notes = "장비분류 추가 및 수정")
	public CommonReturnList<TsptEqpmnCl> save(@RequestBody(required = false) List<EquipmentCategoryParam> param, @PathVariable("eqpmnClId") String eqpmnClId) {
		return new CommonReturnList<>(service.save(param, eqpmnClId));
	}
	/**
	 * 삭제 : 삭제 버튼 클릭으로 데이터 삭제
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = TsptEqpmnCl.class)
	})
	@DeleteMapping("/categories")
	@ApiOperation(value = "장비 분류 삭제", notes = "선택된 장비분류 삭제")
	public CommonReturnList<TsptEqpmnCl> delete(@RequestBody(required = false) List<EquipmentCategoryParam> param) {
		return new CommonReturnList<>(service.delete(param));
	}
}
