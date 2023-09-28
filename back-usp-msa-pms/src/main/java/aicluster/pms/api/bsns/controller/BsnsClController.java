package aicluster.pms.api.bsns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.bsns.service.BsnsClService;
import aicluster.pms.common.entity.UsptBsnsCl;
import bnet.library.util.dto.JsonList;
import lombok.RequiredArgsConstructor;

/**
 * 기준사업 분류
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/bsns-cl")
@RequiredArgsConstructor
public class BsnsClController {

	@Autowired
	private BsnsClService bsnsClService;


	/**
	 * 산업분류 목록 조회
	 * @param parentBsnsClId
	 * @return
	 */
	@GetMapping("")
	public JsonList<UsptBsnsCl> getList(String parentBsnsClId) {
		return bsnsClService.getList(parentBsnsClId, null);
	}


	/**
	 * 산업분류 저장/수정
	 * @param infoList
	 * @return
	 */
	@PostMapping("")
	public JsonList<UsptBsnsCl> add(@RequestBody List<UsptBsnsCl> infoList) {
		bsnsClService.add(infoList);
		if(infoList != null) {
			return bsnsClService.getList(infoList.get(0).getParentBsnsClId(), null);
		} else {
			return bsnsClService.getList(null, null);
		}
	}


	/**
	 * 산업분류 삭제
	 * @param infoList
	 * @return
	 */
	@DeleteMapping("")
	public JsonList<UsptBsnsCl> remove(@RequestBody List<UsptBsnsCl> infoList) {
		bsnsClService.remove(infoList);
		if(infoList != null) {
			return bsnsClService.getList(infoList.get(0).getParentBsnsClId(), null);
		} else {
			return bsnsClService.getList(null, null);
		}
	}


	/**
	 * 사용여부가 사용인 산업분류 목록 조회
	 * @param parentBsnsClId
	 * @return
	 */
	@GetMapping("/enabled")
	public JsonList<UsptBsnsCl> getEnabledList(String parentBsnsClId) {
		return bsnsClService.getList(parentBsnsClId, "Y");
	}

}
