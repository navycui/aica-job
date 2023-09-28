package aicluster.mvn.api.request.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.mvn.api.request.dto.MvnExtDlbrtParam;
import aicluster.mvn.api.request.dto.MvnExtListParam;
import aicluster.mvn.api.request.dto.MvnExtParam;
import aicluster.mvn.api.request.dto.MvnExtStatusParam;
import aicluster.mvn.api.request.service.MvnExtService;
import aicluster.mvn.common.dto.MvnEtReqDto;
import aicluster.mvn.common.dto.MvnEtReqListItemDto;
import aicluster.mvn.common.entity.UsptMvnEtHist;
import aicluster.mvn.common.entity.UsptMvnEtReqst;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/extension")
public class MvnExtController {
	@Autowired
	MvnExtService service;

	@Autowired
	private AttachmentService atchService;

	@Autowired
	private EnvConfig config;

	/**
	 * 입주연장신청 등록
	 *
	 * @param param : 입주연장신청 데이터
	 * @param attachments : 첨부파일 목록
	 * @return UsptMvnEtReq : 입주연장신청 정보
	 * @throws ParseException
	 */
	@PostMapping("/user/{mvnId}")
	UsptMvnEtReqst add(
			@PathVariable String mvnId,
			@RequestPart(value = "mvnEtReq", required = true) MvnExtParam param,
			@RequestPart(value = "attachments", required = false) List<MultipartFile> attachments ) throws ParseException {
		param.setMvnId(mvnId);
		param.setMvnEtReqId(null);

		return service.add(param, attachments);
	}

	/**
	 * 사용자의 최근 요청한 입주연장신청 정보 조회
	 *
	 * @param mvnId : 입주ID
	 * @return
	 */
	@GetMapping("/user/{mvnId}")
	UsptMvnEtReqst getUser(@PathVariable String mvnId) {
		return service.getUser(mvnId);
	}

	/**
	 * 입주담당자 입주연장 신청정보 수정
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @param param : 입주연장신청 정보
	 * @param attachments : 입주연장신청 첨부파일 목록
	 * @return
	 */
	@PutMapping("/user/{mvnId}/{mvnEtReqId}")
	UsptMvnEtReqst modify(
			@PathVariable String mvnId,
			@PathVariable String mvnEtReqId,
			@RequestPart(value = "mvnEtReq", required = true) MvnExtParam param,
			@RequestPart(value = "attachments", required = false) List<MultipartFile> attachments ) {
		param.setMvnId(mvnId);
		param.setMvnEtReqId(mvnEtReqId);

		return service.modify(param, attachments);
	}

	/**
	 * 입주연장신청 목록 조회
	 *
	 * @param param : 조회 조건
	 * @param pageParam : 페이징 조건
	 * @return CorePagination<MvnEtReqListItemDto> : 입주연장신청 목록
	 */
	@GetMapping("")
	CorePagination<MvnEtReqListItemDto> getList(MvnExtListParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}

	/**
	 * 입주연장신청 상세조회
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @return MvnEtReqDto : 입주연장신청 정보(입주업체 및 입주시설(사무실) 정보 포함)
	 */
	@GetMapping("/{mvnEtReqId}")
	MvnEtReqDto get(@PathVariable String mvnEtReqId) {
		return service.get(mvnEtReqId);
	}

	/**
	 * 입주연장신청 상태 변경
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @param mvnEtReqSt : 입주연장신청 상태
	 * @param makeupReqCn : 보완내용
	 */
	@PutMapping("/{mvnEtReqId}/udpate-status")
	void modifyState(
			@PathVariable String mvnEtReqId,
			@RequestBody MvnExtStatusParam param ) {
		param.setMvnEtReqId(mvnEtReqId);

		service.modifyState(param);
	}

	/**
	 * 입주연장신청 심의결과 등록
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @param param : 심의결과 정보
	 * @param attachments : 심의결과 첨부파일
	 */
	@PutMapping("/{mvnEtReqId}/deliberate")
	void modifyDlbrt(
			@PathVariable String mvnEtReqId,
			@RequestPart(value = "dlbrt", required = true) MvnExtDlbrtParam param,
			@RequestPart(value = "dlbrtAttachments", required = false) List<MultipartFile> attachments ) {
		param.setMvnEtReqId(mvnEtReqId);

		service.modifyDlbrt(param, attachments);
	}

	/**
	 * 입주연장신청 이력 목록 조회
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @return List<UsptMvnEtHist> : 입주연장신청 이력목록
	 */
	@GetMapping("/{mvnEtReqId}/hist")
	JsonList<UsptMvnEtHist> getHist(@PathVariable String mvnEtReqId) {
		return service.getHist(mvnEtReqId);
	}

	/**
	 * 입주연장신청 신청 첨부파일 목록 조회
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @return List<CmmtAtchmnfl> : 파일정보 목록
	 */
	@GetMapping("/{mvnEtReqId}/req-files")
	JsonList<CmmtAtchmnfl> getReqAtchList(@PathVariable String mvnEtReqId) {
		UsptMvnEtReqst mvnEtReq = service.select_chkLogin(mvnEtReqId);

		List<CmmtAtchmnfl> list = atchService.getFileInfos_group(mvnEtReq.getAttachmentGroupId());

		return new JsonList<>(list);
	}

	/**
	 * 입주연장신청 신청 첨부파일 다운로드
	 *
	 * @param request
	 * @param response
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @param attachmentId : 첨부파일ID
	 */
	@GetMapping("/{mvnEtReqId}/req-files/{attachmentId}")
	void downloadFile(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String mvnEtReqId,
			@PathVariable String attachmentId) {
		CmmtAtchmnfl attachment = service.selectReqFile(mvnEtReqId, attachmentId);

		atchService.downloadFile(response, config.getDir().getUpload(), attachment.getAttachmentId());
	}

	/**
	 * 입주연장신청 첨부파일 삭제
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @param attachmentId : 첨부파일ID
	 */
	@DeleteMapping("/{mvnEtReqId}/req-files/{attachmentId}")
	void removeReqFile(
			@PathVariable String mvnEtReqId,
			@PathVariable String attachmentId) {
		service.removeReqFile(mvnEtReqId, attachmentId);
	}

	/**
	 * 입주연장신청 심의결과 첨부파일 목록 조회
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @return List<CmmtAtchmnfl> : 파일정보 목록
	 */
	@GetMapping("/{mvnEtReqId}/dlbrt-files")
	JsonList<CmmtAtchmnfl> getDlbrtAtchList(@PathVariable String mvnEtReqId) {
		UsptMvnEtReqst mvnEtReq = service.select_chkLogin(mvnEtReqId);

		List<CmmtAtchmnfl> list = atchService.getFileInfos_group(mvnEtReq.getDlbrtAtchGroupId());

		return new JsonList<>(list);
	}

	/**
	 * 입주연장신청 심의결과 첨부파일 다운로드
	 *
	 * @param request
	 * @param response
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @param attachmentId : 첨부파일ID
	 */
	@GetMapping("/{mvnEtReqId}/dlbrt-files/{attachmentId}")
	void downloadDlbrtFile(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String mvnEtReqId,
			@PathVariable String attachmentId) {
		CmmtAtchmnfl attachment = service.selectDlbrtFile(mvnEtReqId, attachmentId);

		atchService.downloadFile(response, config.getDir().getUpload(), attachment.getAttachmentId());
	}

	/**
	 * 입주연장신청 심의결과 첨부파일 삭제
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @param attachmentId : 첨부파일ID
	 */
	@DeleteMapping("/{mvnEtReqId}/dlbrt-files/{attachmentId}")
	void removeDlbrtFile(
			@PathVariable String mvnEtReqId,
			@PathVariable String attachmentId) {
		service.removeDlbrtFile(mvnEtReqId, attachmentId);
	}

	/**
	 * 입주연장신청 신청 첨부파일 일괄 다운로드
	 *
	 * @param response
	 * @param mvnEtReqId : 입주연장신청 ID
	 */
	@GetMapping("/{mvnEtReqId}/req-files-zip")
	void downloadFileZip( HttpServletResponse response, @PathVariable String mvnEtReqId) {
		MvnEtReqDto etReqDto = service.getFilesDownloadInfo(mvnEtReqId);

		String zipFilename = etReqDto.getMvnCompany().getCmpnyNm() + "_입주연장신청첨부";
		atchService.downloadGroupFiles(response, config.getDir().getUpload(), etReqDto.getAttachmentGroupId(), zipFilename);
	}

	/**
	 * 입주연장신청 심의결과 첨부파일 일괄 다운로드
	 *
	 * @param response
	 * @param mvnEtReqId : 입주연장신청 ID
	 */
	@GetMapping("/{mvnEtReqId}/dlbrt-files-zip")
	void downloadDlbrtFileZip(HttpServletResponse response, @PathVariable String mvnEtReqId) {
		MvnEtReqDto etReqDto = service.getFilesDownloadInfo(mvnEtReqId);

		String zipFilename = etReqDto.getMvnCompany().getCmpnyNm() + "_입주연장심의결과첨부";
		atchService.downloadGroupFiles(response, config.getDir().getUpload(), etReqDto.getDlbrtAtchGroupId(), zipFilename);
	}
}
