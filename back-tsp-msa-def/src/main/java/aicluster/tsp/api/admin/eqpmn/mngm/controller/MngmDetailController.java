package aicluster.tsp.api.admin.eqpmn.mngm.controller;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmDetailParam;
import aicluster.tsp.api.admin.eqpmn.mngm.service.MngmDetailService;
import aicluster.tsp.api.common.param.CommonReturn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/api/admin/eqpmns/mngms/details")
@RequiredArgsConstructor
@Api(tags = "장비관리 > 상세정보" )
public class MngmDetailController {
	@Autowired
	private MngmDetailService service;

	@ApiOperation(value = "장비 상세 정보 조회", notes = "상세 정보 조회")
	@GetMapping("/{eqpmnId}")
	public MngmDetailParam getDetail(@PathVariable("eqpmnId") String eqpmnId){
		return service.getDetail(eqpmnId);
	}

	@ApiOperation(value = "장비 상세 정보 수정", notes = "상세 정보 수정")
	@PutMapping("/{eqpmnId}")
	public MngmDetailParam modifyDetail(@PathVariable("eqpmnId") String eqpmnId, @RequestBody(required = false) MngmDetailParam param) {
		param.setEqpmnId(eqpmnId);
		return service.modifyDetail(param);
	}

	@ApiOperation(value = "장비 상세정보 이미지 업로드", notes = "장비 상세정보 이미지 업로드")
	@PostMapping("/{eqpmnId}/image-upload")
	public CommonReturn<String> imageUpload(@PathVariable("eqpmnId") String eqpmnId,
											@RequestPart(value = "image", required = false) MultipartFile image) {
		return new CommonReturn<>(service.imageUpload(eqpmnId, image));
	}
}
