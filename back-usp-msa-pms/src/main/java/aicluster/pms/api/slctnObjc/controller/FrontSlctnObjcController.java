package aicluster.pms.api.slctnObjc.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.slctnObjc.dto.FrontSlctnObjcReqstListParam;
import aicluster.pms.api.slctnObjc.service.FrontSlctnObjcService;
import aicluster.pms.common.dto.FrontSlctnObjcDto;
import aicluster.pms.common.dto.FrontSlctnObjcReqstListDto;
import aicluster.pms.common.dto.RejectDto;
import bnet.library.util.pagination.CorePagination;

/**
 * 결과이의신청
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/slctn-objc")
public class FrontSlctnObjcController {

	@Autowired
	private FrontSlctnObjcService frontSlctnObjcService;

	/**
	 * 결과 이의신청 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<FrontSlctnObjcReqstListDto> getList(FrontSlctnObjcReqstListParam param, @RequestParam(defaultValue = "1") Long page) {
		return frontSlctnObjcService.getList(param, page);
	}

	/**
	 * 결과 이의신청 상세조회
	 * @param objcReqstId
	 * @return
	 */
	@GetMapping("/{objcReqstId}")
	public FrontSlctnObjcDto get(@PathVariable("objcReqstId") String objcReqstId) {
		return frontSlctnObjcService.get(objcReqstId);
	}

	/**
	 * 결과이의신청 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	@GetMapping("/{objcReqstId}/objc/atchmnfl")
	public void getObjcAtchmnflAll(HttpServletResponse response, @PathVariable("objcReqstId") String objcReqstId) {
		frontSlctnObjcService.getObjcAtchmnflAll(response, objcReqstId);
	}


	/**
	 * 결과이의신청 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	@GetMapping("/{objcReqstId}/objc/atchmnfl/{attachmentId}")
	public void getObjcAtchmnfl(HttpServletResponse response, @PathVariable("objcReqstId") String objcReqstId, @PathVariable("attachmentId") String attachmentId) {
		frontSlctnObjcService.getObjcAtchmnfl(response, objcReqstId, attachmentId);
	}


	/**
	 * 심의결과 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	@GetMapping("/{objcReqstId}/dlbrt/atchmnfl")
	public void getDlbrtAtchmnfl(HttpServletResponse response, @PathVariable("objcReqstId") String objcReqstId) {
		frontSlctnObjcService.getDlbrtAtchmnflAll(response, objcReqstId);
	}


	/**
	 * 심의결과 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	@GetMapping("/{objcReqstId}/dlbrt/atchmnfl/{attachmentId}")
	public void getDlbrtAtchmnfl(HttpServletResponse response, @PathVariable("objcReqstId") String objcReqstId, @PathVariable("attachmentId") String attachmentId) {
		frontSlctnObjcService.getDlbrtAtchmnfl(response, objcReqstId, attachmentId);
	}


	/**
	 * 반려사유 조회
	 * @param objcReqstId
	 * @return
	 */
	@GetMapping("/{objcReqstId}/reject-reason")
	public RejectDto getRejectReason(@PathVariable("objcReqstId") String objcReqstId){
		return frontSlctnObjcService.getRejectReason(objcReqstId);
	}

	/**
	 * 신청취소
	 * @param objcReqstId
	 * @return
	 */
	@PutMapping("/{objcReqstId}/cancel")
	public void modifyCancel(@PathVariable("objcReqstId") String objcReqstId){
		frontSlctnObjcService.modifyCancel(objcReqstId);
	}
}
