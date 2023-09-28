package aicluster.pms.api.rslt.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.api.rslt.dto.BsnsMvnRsltParam;
import aicluster.pms.api.rslt.dto.FrontRsltListParam;
import aicluster.pms.api.rslt.dto.RsltDto;
import aicluster.pms.api.rslt.dto.RsltHistDto;
import aicluster.pms.api.rslt.dto.RsltIdDto;
import aicluster.pms.api.rslt.dto.RsltIdxParam;
import aicluster.pms.api.rslt.dto.RsltMakeupDto;
import aicluster.pms.api.rslt.service.FrontRsltService;
import aicluster.pms.common.dto.FrontRsltListDto;
import aicluster.pms.common.dto.RsltHistPresentnDto;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;


/**
 * 성과관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/rslt")
public class FrontRsltController {

	@Autowired
	private FrontRsltService frontrsltService;


	/**
	 * 성과목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<FrontRsltListDto> getList(FrontRsltListParam param, @RequestParam(defaultValue = "1") Long page) {
		return frontrsltService.getList(param, page);
	}



	/**
	 * 성과 보완요청 조회
	 * @param rsltId
	 * @return
	 */
	@GetMapping("/{rsltId}/makeup")
	public RsltMakeupDto getMakeup(@PathVariable("rsltId") String rsltId) {
		return frontrsltService.getMakeup(rsltId);
	}


	/**
	 * 성과 보완요청 첨부파일 다운로드
	 * @param response
	 * @param rsltId
	 * @param attachmentId
	 */
	@GetMapping("/{rsltId}/makeup/atchmnfl/{attachmentId}")
	public void getMakeupAtchmnfl(HttpServletResponse response, @PathVariable("rsltId") String rsltId, @PathVariable("attachmentId") String attachmentId) {
		frontrsltService.getMakeupAtchmnfl(response, rsltId, attachmentId);
	}


	/**
	 * 성과 상세조회
	 * @param rsltId
	 * @return
	 */
	@GetMapping("/{rsltId}")
	public RsltDto get(@PathVariable("rsltId") String rsltId) {
		return frontrsltService.get(rsltId);
	}

	/**
	 * 성과 제출
	 * @param rsltId
	 */
	@PutMapping("/{rsltId}")
	public RsltDto modify(@PathVariable("rsltId") String rsltId
						, @RequestPart("infoList") List<RsltIdxParam> infoList
						, @RequestPart(value="deleteAttachFileList", required = false) List<CmmtAtchmnfl> deleteAttachFileList
						, @RequestPart(value="rsltIdxFileList", required = false) List<MultipartFile> rsltIdxFileList
						, @RequestPart(value="attachFileList", required = false) List<MultipartFile> attachFileList) {
		frontrsltService.modify(rsltId, infoList, deleteAttachFileList, rsltIdxFileList, attachFileList);
		return frontrsltService.get(rsltId);
	}


	/**
	 * 제출이력 제출일 목록 조회
	 * @param rsltId
	 * @return
	 */
	@GetMapping("/{rsltId}/presentn-dt")
	public JsonList<RsltHistPresentnDto> getPresentnDtList(@PathVariable("rsltId") String rsltId) {
		return frontrsltService.getPresentnDtList(rsltId);
	}


	/**
	 * @param rsltId
	 * @return
	 */
	@GetMapping("/{rsltId}/hist")
	public RsltHistDto getHist(@PathVariable("rsltId") String rsltId, String rsltHistId) {
		return frontrsltService.getHist(rsltId, rsltHistId);
	}


	/**
	 * 성과 제출이력 첨부파일 일괄 다운로드
	 * @param response
	 * @param rsltId
	 * @param attachmentId
	 */
	@GetMapping("/{rsltId}/hist/atchmnfl")
	public void getAtchmnflAll(HttpServletResponse response, @PathVariable("rsltId") String rsltId, String rsltHistId) {
		frontrsltService.getAtchmnflAll(response, rsltId, rsltHistId);
	}


	/**
	 * 성과 제출이력 첨부파일 다운로드
	 * @param response
	 * @param rsltId
	 * @param attachmentId
	 */
	@GetMapping("/{rsltId}/hist/atchmnfl/{attachmentId}")
	public void getAtchmnfl(HttpServletResponse response
							, @PathVariable("rsltId") String rsltId
							, @PathVariable("attachmentId") String attachmentId
							, String rsltHistId) {
		frontrsltService.getAtchmnfl(response, rsltId, attachmentId, rsltHistId);
	}



	/**
	 * 입주성과 성과ID 생성 요청
	 * @param param
	 * @return
	 */
	@PostMapping("/mvn/rsltId")
	public RsltIdDto getBsnsMvnRsltId(@RequestBody BsnsMvnRsltParam param) {
		String rsltId = frontrsltService.getBsnsMvnRsltId(param);
		RsltIdDto dto = new RsltIdDto();
		dto.setRsltId(rsltId);
		return dto;
	}
}
