package aicluster.pms.api.reprt.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.reprt.dto.FrontReprtListParam;
import aicluster.pms.api.reprt.dto.ReprtMakeupDto;
import aicluster.pms.api.reprt.service.FrontReprtService;
import aicluster.pms.common.dto.FrontReprtDto;
import aicluster.pms.common.dto.FrontReprtListDto;
import aicluster.pms.common.entity.UsptReprt;
import bnet.library.util.pagination.CorePagination;

/**
 * 보고서제출
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/reprt-presentn")
public class FrontReprtController {

	@Autowired
	private FrontReprtService frontReprtService;

	/**
	 * 보고서 제출 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<FrontReprtListDto> getList(FrontReprtListParam param, @RequestParam(defaultValue = "1") Long page) {
		return frontReprtService.getList(param, page);
	}



	/**
	 * 보고서 보완요청 조회
	 * @param reprtId
	 * @return
	 */
	@GetMapping("/{reprtId}/makeup")
	public ReprtMakeupDto getMakeup(@PathVariable("reprtId") String reprtId) {
		return frontReprtService.getMakeup(reprtId);
	}


	/**
	 * 보고서 보완요청 첨부파일 다운로드
	 * @param response
	 * @param reprtId
	 * @param attachmentId
	 */
	@GetMapping("/{reprtId}/makeup/atchmnfl/{attachmentId}")
	public void getMakeupAtchmnfl(HttpServletResponse response, @PathVariable("reprtId") String reprtId, @PathVariable("attachmentId") String attachmentId) {
		frontReprtService.getMakeupAtchmnfl(response, reprtId, attachmentId);
	}


	/**
	 * 보고서 상세조회
	 * @param reprtId
	 * @return
	 */
	@GetMapping("/{reprtId}")
	public FrontReprtDto get(@PathVariable("reprtId") String reprtId) {
		return frontReprtService.get(reprtId);
	}


	/**
	 * 보고서 첨부파일 다운로드
	 * @param response
	 * @param reprtId
	 * @param attachmentId
	 */
	@GetMapping("/{reprtId}/atchmnfl/{attachmentId}")
	public void getAtchmnfl(HttpServletResponse response, @PathVariable("reprtId") String reprtId, @PathVariable("attachmentId") String attachmentId) {
		frontReprtService.getAtchmnfl(response, reprtId, attachmentId);
	}

	/**
	 * 보고서 제출
	 * @param reprtId
	 */
	@PutMapping("/{reprtId}")
	public FrontReprtDto modify(@PathVariable("reprtId") String reprtId
							, @RequestPart("info") UsptReprt info
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontReprtService.modify(reprtId, info.getReprtSumryCn(), fileList);
		return frontReprtService.get(reprtId);
	}

}
