package aicluster.member.api.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.auth.service.RoleService;
import aicluster.member.common.entity.CmmtRole;
import bnet.library.util.dto.JsonList;
import lombok.RequiredArgsConstructor;

//@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RoleController {
	private final RoleService roleService;

	/**
	 * 역할 목록조회
	 * @return
	 */
	@GetMapping("/roles")
	public JsonList<CmmtRole> getList() {
		return roleService.getList();
	}

	/**
	 * 역할 추가
	 * @param roleId
	 * @param roleNm
	 * @return
	 */
	@PostMapping("/roles")
	public CmmtRole add(String roleId, String roleNm) {
		return roleService.insert(roleId, roleNm);
	}

	/**
	 * 역할 조회
	 * @param roleId
	 * @return
	 */
	@GetMapping("/roles/{roleId}")
	public CmmtRole get(@PathVariable("roleId") String roleId) {
		return roleService.select(roleId);
	}

	/**
	 * 역할 수정
	 * @param roleId
	 * @param roleNm
	 * @return
	 */
	@PutMapping("/roles/{roleId}")
	public CmmtRole modify(@PathVariable("roleId") String roleId, String roleNm) {
		return roleService.modify(roleId, roleNm);
	}

	/**
	 * 역할 삭제
	 * @param roleId
	 */
	@DeleteMapping("/roles/{roleId}")
	public void remove(@PathVariable("roleId") String roleId) {
		roleService.delete(roleId);
	}
}
