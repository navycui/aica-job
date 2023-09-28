package aicluster.mvn.api.facility.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.mvn.api.facility.dto.MvnFcListParam;
import aicluster.mvn.api.facility.dto.MvnFcParam;
import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/facilities")
public class MvnFcController {

	@Autowired
	private MvnFcService service;

	@Autowired
	private AttachmentService arthService;

	@Autowired
	private EnvConfig config;

	/**
	 * 입주시설 목록조회
	 *
	 * @param mvnFcNm: 입주시설명
	 * @param mvnFcType: 입주시설유형
	 * @param mvnFcDtype: 입주시설상세유형
	 * @param enabled: 입주시설 사용여부
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	CorePagination<UsptMvnFcltyInfo> getList(MvnFcListParam param,	CorePaginationParam pageParam)
	{
		return service.getList(param, pageParam);
	}

	/**
	 * 입주시설정보 등록
	 *
	 * @param param: 입주시설 등록정보
	 * @param imageFile: 입주시설 이미지파일
	 * @return UsptMvnFc
	 */
	@PostMapping("")
	UsptMvnFcltyInfo add(
			@RequestPart(value = "mvnFc", required = true) MvnFcParam param,
			@RequestPart(value = "imageFile", required = false) MultipartFile imageFile)
	{
		return service.add(param, imageFile);
	}

	/**
	 * 입주시설 상세조회
	 *
	 * @param mvnFcId: 입주시설ID
	 * @return UsptMvnFc
	 */
	@GetMapping("/{mvnFcId}")
	UsptMvnFcltyInfo get(@PathVariable String mvnFcId)
	{
		return service.get(mvnFcId);
	}

	/**
	 * 입주시설정보 수정
	 *
	 * @param mvnFcId: 입주시설ID
	 * @param param: 입주시설등록정보
	 * @param imageFile: 입주시설 이미지파일
	 * @return
	 */
	@PutMapping("/{mvnFcId}")
	UsptMvnFcltyInfo modify(@PathVariable String mvnFcId,
			@RequestPart(value = "mvnFc", required = true) MvnFcParam param,
			@RequestPart(value = "imageFile", required = false) MultipartFile imageFile)
	{
		param.setMvnFcId(mvnFcId);
		return service.modify(param, imageFile);
	}

	/**
	 * 입주시설정보 삭제
	 *
	 * @param mvnFcId: 입주시설ID
	 */
	@DeleteMapping("/{mvnFcId}")
	void remove(@PathVariable String mvnFcId)
	{
		service.delete(mvnFcId);
	}

	/**
	 * 입주시설 이미지파일 다운로드
	 *
	 * @param response
	 * @param mvnFcId: 입주시설ID
	 */
	@GetMapping("/{mvnFcId}/image")
	void downloadImage(HttpServletResponse response, @PathVariable String mvnFcId)
	{
		UsptMvnFcltyInfo mvnFc = service.selectInfo(mvnFcId);

		arthService.downloadFile_contentType(response, config.getDir().getUpload(), mvnFc.getImageFileId());
	}

	/**
	 * 입주시설 이미지파일 삭제
	 *
	 * @param mvnFcId: 입주시설ID
	 */
	@DeleteMapping("/{mvnFcId}/image")
	void removeImage(@PathVariable String mvnFcId)
	{
		service.deleteImage(mvnFcId);
	}

	/**
	 * 입주시설 사용여부 변경
	 *
	 * @param mvnFcId: 입주시설
	 * @param enabled: 입주시설 사용여부
	 * @return
	 */
	@PutMapping("/{mvnFcId}/use")
	UsptMvnFcltyInfo modifyEnabled(@PathVariable String mvnFcId, Boolean enabled)
	{
		return service.modifyEnabled(mvnFcId, enabled);
	}
}
