package aicluster.pms.api.bsnsapply.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.bsns.dto.Chklst;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplicantDto;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyDto;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyInfoDto;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyListParam;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyParam;
import aicluster.pms.api.bsnsapply.dto.FrontReqReasonDto;
import aicluster.pms.api.bsnsapply.dto.PdfGatewayDto;
import aicluster.pms.api.bsnsapply.service.FrontBsnsApplyService;
import aicluster.pms.common.dto.FrontBsnsApplyListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplyChklst;
import aicluster.pms.common.util.Code;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;

/**
 * 사업신청
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/bsns-apply")
public class FrontBsnsApplyController {

	@Autowired
	private FrontBsnsApplyService frontBsnsApplyService;

	/**
	 * 필수확인사항 조회
	 * @param pblancId
	 * @return
	 */
	@GetMapping("/{pblancId}/chklst")
	public JsonList<Chklst> getChklst(@PathVariable("pblancId") String pblancId) {
		return frontBsnsApplyService.getChklst(pblancId);
	}

	/**
	 * 신청자정보 조회
	 * @param pblancId
	 * @return
	 */
	@GetMapping("/{pblancId}/applicant")
	public FrontBsnsApplicantDto getApplicant(@PathVariable("pblancId") String pblancId) {
		return frontBsnsApplyService.getApplicant(pblancId);
	}

	/**
	 * 신청정보
	 * @param pblancId
	 * @return
	 */
	@GetMapping("/{pblancId}/apply")
	public FrontBsnsApplyDto getApply(@PathVariable("pblancId") String pblancId) {
		return frontBsnsApplyService.getApply(pblancId);
	}

	/**
	 * 서식파일 다운로드
	 * @param response
	 * @param pblancId
	 * @param attachmentId
	 */
	@GetMapping("/{pblancId}/atchmnfl/{attachmentId}")
	public void getFileDownload(HttpServletResponse response, @PathVariable("pblancId") String pblancId, @PathVariable("attachmentId") String attachmentId ) {
		frontBsnsApplyService.getFileDownload(response, pblancId, attachmentId);
	}

	/**
	 * 임시저장
	 * @param pblancId
	 * @param frontBsnsApplyParam
	 * @param fileList
	 */
	@PostMapping("/{pblancId}/tmp-save")
	public void addTmpSave(@PathVariable("pblancId") String pblancId
							, @RequestPart(value = "info") FrontBsnsApplyParam frontBsnsApplyParam
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontBsnsApplyService.add(Code.rceptSttus.임시저장 , pblancId, frontBsnsApplyParam, fileList);
	}

	/**
	 * 저장
	 * @param pblancId
	 * @param frontBsnsApplyParam
	 * @param fileList
	 */
	@PostMapping("/{pblancId}/save")
	public void add(@PathVariable("pblancId") String pblancId
							, @RequestPart(value = "info") FrontBsnsApplyParam frontBsnsApplyParam
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontBsnsApplyService.add(Code.rceptSttus.신청 , pblancId, frontBsnsApplyParam, fileList);
	}


	/**
	 * 사업신청 목록 조회
	 * @param frontBsnsApplyListParam
	 * @param page
	 * @return
	 */
	@GetMapping("/mgnt")
	public CorePagination<FrontBsnsApplyListDto> getList(FrontBsnsApplyListParam frontBsnsApplyListParam, @RequestParam(defaultValue = "1") Long page){
		return frontBsnsApplyService.getList(frontBsnsApplyListParam, page);
	}

	/**
	 * 사업신청 보완요청/반려 사유 확인
	 * @param applyId
	 * @return
	 */
	@GetMapping("/mgnt/{applyId}/reason")
	public FrontReqReasonDto getReason(@PathVariable("applyId") String applyId){
		return frontBsnsApplyService.getReason(applyId);
	}

	/**
	 * 사업신청 필수확인사항 조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/mgnt/{applyId}/chklst")
	public JsonList<UsptBsnsPblancApplyChklst> getApplyChklst(@PathVariable("applyId") String applyId){
		return frontBsnsApplyService.getApplyChklst(applyId);
	}

	/**
	 * 사업신청 신청자 정보 조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/mgnt/{applyId}/applicant")
	public FrontBsnsApplicantDto getApplyApplicant(@PathVariable("applyId") String applyId){
		return frontBsnsApplyService.getApplyApplicant(applyId);
	}

	/**
	 * 사업신청 신청정보 조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/mgnt/{applyId}/apply")
	public FrontBsnsApplyInfoDto getApplyInfo(@PathVariable("applyId") String applyId){
		return frontBsnsApplyService.getApplyInfo(applyId);
	}


	/**
	 * 사업신청 임시저장
	 * @param applyId
	 * @return
	 */
	@PutMapping("/mgnt/{applyId}/tmp-save")
	public void modifyApplyTmpSave(@PathVariable("applyId") String applyId
			, @RequestPart(value = "info") FrontBsnsApplyParam frontBsnsApplyParam
			, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontBsnsApplyService.modify(Code.rceptSttus.임시저장 , applyId, frontBsnsApplyParam, fileList);
	}


	/**
	 * 사업신청 신청
	 * @param applyId
	 * @param frontBsnsApplyParam
	 * @param fileList
	 */
	@PutMapping("/mgnt/{applyId}/save")
	public void modifyApplySave(@PathVariable("applyId") String applyId
			, @RequestPart(value = "info") FrontBsnsApplyParam frontBsnsApplyParam
			, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontBsnsApplyService.modify(Code.rceptSttus.신청 , applyId, frontBsnsApplyParam, fileList);
	}


	/**
	 * 사업신청 취소
	 * @param applyId
	 * @return
	 */
	@PutMapping("/mgnt/{applyId}/cancel")
	public void modifyApplyCancel(@PathVariable("applyId") String applyId){
		frontBsnsApplyService.modifyApplyCancel(applyId);
	}



	/**
	 * PDF-GATEWAY를 위한 첨부파일 다운로드
	 * @param response
	 * @param attachmentId
	 */
	@GetMapping("/pdf-gateway/file-download/{attachmentId}")
	public void getFileDownloadForPdfGateway(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId) {
		frontBsnsApplyService.getFileDownloadForPdfGateway(response, attachmentId);
	}


	/**
	 * PDF-GATEWAY의 파일변환 결과 저장
	 * @param response
	 * @param attachmentId
	 */
	@GetMapping("/pdf-gateway/result")
	public void modifyPdfGatewayResult(PdfGatewayDto resultInfo) {
		frontBsnsApplyService.modifyPdfGatewayResult(resultInfo);
	}

}
