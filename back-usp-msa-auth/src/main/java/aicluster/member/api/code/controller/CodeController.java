package aicluster.member.api.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.code.service.CodeService;
import aicluster.member.common.entity.CmmtCode;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/codegroups")
public class CodeController {


	@Autowired
	private CodeService codeService;

	/**
	 * 코드목록 조회
	 *
	 * @param codeGroup
	 * @return
	 */
	@GetMapping("/{codeGroup}/codes")
	public JsonList<CmmtCode> getList(@PathVariable("codeGroup") String codeGroup) {
		return codeService.getList(codeGroup);
	}

	/**
	 * 코드 등록
	 *
	 * @param codeGroup
	 * @param requestCode
	 * @return
	 */
	@PostMapping("/{codeGroup}/codes")
	public JsonList<CmmtCode> add(@PathVariable("codeGroup") String codeGroup,@RequestBody CmmtCode requestCode) {
		requestCode.setCodeGroup(codeGroup);
		return codeService.add(requestCode);
	}

	/**
	 * 코드 수정
	 *
	 * @param codeGroup
	 * @param code
	 * @param requestCode
	 * @return
	 */
	@PutMapping("/{codeGroup}/codes/{code}")
	public JsonList<CmmtCode> modify(@PathVariable("codeGroup") String codeGroup,@PathVariable("code") String code,@RequestBody CmmtCode requestCode) {
		requestCode.setCodeGroup(codeGroup);
		requestCode.setCode(code);
		return codeService.modify(requestCode);
	}

	/**
	 * 코드 삭제
	 *
	 * @param codeGroup
	 * @param code
	 */
	@DeleteMapping("/{codeGroup}/codes/{code}")
	public void remove(@PathVariable("codeGroup") String codeGroup,@PathVariable("code") String code) {
		codeService.remove(codeGroup,code);
	}

	/**
	 * 사용상태 코드 목록 조회
	 * @param codeGroup
	 * @return
	 */
	@GetMapping("/{codeGroup}/codes/enabled")
	public JsonList<CmmtCode> getEnabledList(@PathVariable("codeGroup") String codeGroup) {
		return codeService.getEnabledList(codeGroup);
	}
}
