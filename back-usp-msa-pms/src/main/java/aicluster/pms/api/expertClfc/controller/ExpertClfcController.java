package aicluster.pms.api.expertClfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.expertClfc.dto.ExpertClChargerParam;
import aicluster.pms.api.expertClfc.dto.ExpertClfcParam;
import aicluster.pms.api.expertClfc.service.ExpertClfcService;
import aicluster.pms.common.entity.UsptExpertCl;
import aicluster.pms.common.entity.UsptExpertClCharger;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문가분류 관리
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/expert-clfc")
public class ExpertClfcController {

	@Autowired
	private ExpertClfcService expertClfcService;


	/**
	 * 전문가분류 등록(CODE에서)
	 * @return List<UsptExpertCl>
	 */
	@PutMapping("/allcode/modify")
	public JsonList<UsptExpertCl> addExpertClfcFromCode() {
		//등록
		expertClfcService.addExpertClfcFromCode();
		ExpertClfcParam expertClfcParam = new ExpertClfcParam();
		expertClfcParam.setExpertClId("ALL");
		return new JsonList<>(expertClfcService.getExpertClfcMyTreeList(expertClfcParam));
	}

	/**
	 * 내담당목록 트리 조회
	 * @return List<UsptExpertCl>
	 */
	@GetMapping("/mytree")
	public JsonList<UsptExpertCl> getExpertClfcMyTreeList(ExpertClfcParam expertClfcParam) {
		log.debug("#####	getExpertClfcMyTreeList : {}", expertClfcParam);
		return new JsonList<>(expertClfcService.getExpertClfcMyTreeList(expertClfcParam));
	}


	/**
	 * 다음 트리 목록 조회(다음 depth)
	 * @param expertClId
	 * @return List<ExpertClDto>
	 */
	@GetMapping("/nextdepth")
	public JsonList<UsptExpertCl> getNextDepthList(ExpertClfcParam expertClfcParam) {
		log.debug("#####	getNextDepthList : {}", expertClfcParam);
		return new JsonList<>(expertClfcService.getNextDepthList(expertClfcParam));
	}

	/**
	 * 전문가분류 등록,수정
	 * @param
	 * @param expertClfcParamList
	 * @return
	 */
	@PutMapping("/save")
	public void modifyExpertClfc(@RequestBody(required = false) List<UsptExpertCl> usptExpertClList) {
		log.debug("#####	modifyExpertClfc : {}", usptExpertClList);
		expertClfcService.modifyExpertClfc(usptExpertClList);
	}

	/**
	 * 전문가분류 삭제
	 * @param expertClId(조회용)
	 * @param expertClfcParamList
	 * @return List<ExpertClDto>
	 */
	@DeleteMapping("/delete")
	public void removeExpertClfc(@RequestBody(required = false) List<UsptExpertCl> usptExpertClList	) {
		log.debug("#####	removeExpertClfc : {}", usptExpertClList);
		expertClfcService.deleteExpertClfc(usptExpertClList);
	}


	/**
	 * 전문가단 담당자 목록 조회
	 * @param expertClId
	 * @return List<ExpertClMapngDto>
	 */

	@GetMapping("/expertcl-charger")
	public JsonList<UsptExpertClCharger> getExpertClChargerList(ExpertClfcParam expertClfcParam) {
		log.debug("#####	getExpertClChargerList : {}", expertClfcParam);
		return new JsonList<>(expertClfcService.getExpertClChargerList(expertClfcParam));
	}

	/**
	 * 전문가단 담당자 등록
	 * @param usptExpertClChargerList
	 * @return
	 */
	@PostMapping("/expertcl-charger/save")
	public void addExpertClCharger( @RequestBody(required = false)  List<UsptExpertClCharger> usptExpertClChargerList) {
		log.debug("#####	addExpertClCharger : {}", usptExpertClChargerList);
		expertClfcService.addExpertClfcMapng(usptExpertClChargerList);
	}

	/**
	 * 전문가단 담당자 삭제
	 * @param usptExpertClChargerList
	 * @return
	 */
	@DeleteMapping("/expertcl-charger/delete")
	public void removeExpertClCharger( @RequestBody(required = false) List<UsptExpertClCharger> usptExpertClChargerList) {
		log.debug("#####	removeExpertClCharger : {}", usptExpertClChargerList);
		expertClfcService.removeExpertClCharger(usptExpertClChargerList);
	}

	/**
	 * 전문가단 담당자 추가 목록 조회(팝업)
	 * @param wrcNm
	 * @param expertNm
	 * @return List<ExpertClMapngDto>
	 */

	@GetMapping("/popup")
	public CorePagination<UsptExpertClCharger> getExpertClChargerPopupList( ExpertClChargerParam inputParam, @RequestParam(required = false) Long page){
		log.debug("#####	getExpertClChargerPopupList : {}", inputParam);
		return expertClfcService.getExpertClChargerPopupList(inputParam, page);
	}

	/**
	 * 전문가분류 tree 목록 조회(팝업)
	 * @param
	 * @param
	 * @return List<ExpertClMapngDto>
	 */
	@GetMapping("/mytree/popup")
	public JsonList<UsptExpertCl>  getExpertClfcMyTreeListPopup( ){
		log.debug("#####	getExpertClfcMyTreeListPopup : {}");
		return new JsonList<>(expertClfcService.getExpertClfcMyTreeListPopup());
	}
}