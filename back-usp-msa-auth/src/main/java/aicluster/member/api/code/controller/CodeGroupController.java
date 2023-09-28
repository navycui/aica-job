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

import aicluster.member.api.code.dto.CodeGroupDto;
import aicluster.member.api.code.service.CodeGroupService;
import aicluster.member.common.entity.CmmtCodeGroup;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/codegroups")
public class CodeGroupController {

	@Autowired
	private CodeGroupService codeGroupService;

	/**
	 * 코드그룹 목록조회
	 *
	 * @param codeGroup
	 * @param codeGroupNm
	 * @return
	 */
	@GetMapping("")
	public JsonList<CmmtCodeGroup> getlist(String codeGroup, String codeGroupNm){
		return codeGroupService.getList(codeGroup, codeGroupNm);
	}

	/**
	 * 코드그룹 등록
	 *
	 * @param dto
	 * @return
	 */
	@PostMapping("")
	public CmmtCodeGroup add(@RequestBody(required = false) CodeGroupDto dto) {
		CmmtCodeGroup cmmtCodeGroup = codeGroupService.addGroup(dto);
		return cmmtCodeGroup;
	}

	/**
	 * 코드그룹 수정
	 *
	 * @param codeGroup
	 * @param dto
	 * @return
	 */
	@PutMapping("/{codeGroup}")
	public CmmtCodeGroup modify(@PathVariable String codeGroup, @RequestBody(required = false) CodeGroupDto dto) {
		dto.setCodeGroup(codeGroup);
		CmmtCodeGroup cmmtCodeGroup = codeGroupService.modifyGroup(dto);
		return cmmtCodeGroup;
	}

	/**
	 * 코드그룹 삭제
	 *
	 * @param codeGroup
	 */
	@DeleteMapping("/{codeGroup}")
	public void remove(@PathVariable String codeGroup) {
		codeGroupService.removeGroup(codeGroup);
	}


}
