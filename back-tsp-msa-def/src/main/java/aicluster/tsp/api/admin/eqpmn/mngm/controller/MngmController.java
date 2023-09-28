package aicluster.tsp.api.admin.eqpmn.mngm.controller;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtHistParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtParam;
import aicluster.tsp.api.admin.eqpmn.mngm.service.MngmService;
import aicluster.tsp.api.common.param.CommonRequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/eqpmns/mngms")
@RequiredArgsConstructor
@Api(tags = "장비관리 > 장비정보")
public class MngmController {

	@Autowired
	private MngmService mngmService;

	@ApiOperation(value = "장비정보상세 관리정보 조회", notes = "장비정보상세 관리정보 조회.")
	@GetMapping("/{eqpmnId}")
	public MngmMgtParam getInfoList(@PathVariable("eqpmnId") String eqpmnId) {
		return mngmService.getInfo(eqpmnId);
	}

	@ApiOperation(value = "관리정보조회 관리정보 저장", notes = "관리정보조회 관리정보 저장.")
	@PutMapping("/{eqpmnId}")
	public MngmMgtParam modifyInfo(@PathVariable("eqpmnId") String eqpmnId, @RequestBody MngmParam dto) {
		dto.setEqpmnId(eqpmnId);
		return mngmService.modifyInfo(dto);
	}

	@ApiOperation(value = "관리정보조회 관리정보 삭제", notes = "관리정보조회 관리정보 삭제.")
	@DeleteMapping("/{eqpmnId}")
	public void deleteInfo(@PathVariable("eqpmnId") String eqpmnId) {
		mngmService.deleteInfo(eqpmnId);
	}

	@ApiOperation(value = "관리정보조회 관리정보 불용", notes = "관리정보조회 관리정보 불용.")
	@PutMapping("/unavilable/{eqpmnId}")
	public MngmMgtParam unavilableInfo(@PathVariable("eqpmnId") String eqpmnId, @RequestBody CommonRequestParam param) {
		return mngmService.unavilableInfo(eqpmnId, param.getParam());
	}

	@ApiOperation(value = "수리/교정/점검 조회", notes = "수리/교정/점검 조회")
	@GetMapping("/procs/{eqpmnId}")
	public MngmMgtHistParam getInfoView(@PathVariable("eqpmnId") String eqpmnId, @RequestParam String manageDiv) {
		return mngmService.getInfoView(eqpmnId, manageDiv);
	}
	@ApiOperation(value = "수리/교정 등록", notes = "수리/교정 등록")
	@PostMapping("/procs/{eqpmnId}")
	public MngmMgtParam insertInfoRegister(@PathVariable("eqpmnId") String eqpmnId, @RequestBody MngmMgtHistParam dto) {
		dto.setEqpmnId(eqpmnId);
		return mngmService.insertInfoRegister(dto);
	}
	@ApiOperation(value = "수리/교정/점검 완료", notes = "수리/교정/점검 완료")
	@PutMapping("/procs/{eqpmnId}")
	public MngmMgtParam updateInfoFinish(@PathVariable("eqpmnId") String eqpmnId, @RequestBody MngmMgtHistParam dto) {
		dto.setEqpmnId(eqpmnId);
		return mngmService.updateInfoFinish(dto);
	}
}
