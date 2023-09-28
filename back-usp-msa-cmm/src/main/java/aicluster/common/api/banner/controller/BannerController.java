package aicluster.common.api.banner.controller;

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

import aicluster.common.api.banner.dto.BannerGetListParam;
import aicluster.common.api.banner.dto.BannerParam;
import aicluster.common.api.banner.service.BannerService;
import aicluster.common.common.entity.CmmtBanner;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/banners")
public class BannerController {

	@Autowired
	private BannerService service;

	/**
	 * 배너 목록조회
	 *
	 * @param systemId
	 * @return
	 */
	@GetMapping("")
	public CorePagination<CmmtBanner> getList(BannerGetListParam param, CorePaginationParam pageParam){
		return service.getList(param, pageParam);
	}

	/**
	 * 배너 상세조회
	 *
	 * @param bannerId
	 * @return
	 */
	@GetMapping("/{bannerId}")
	public CmmtBanner get(@PathVariable String bannerId) {
		return service.get(bannerId);

	}

	/**
	 * 배너 수정
	 *
	 * @param bannerId
	 * @param cmmtBanner
	 * @param pcImageFile
	 * @param mobileImageFile
	 * @return
	 */
	@PutMapping("/{bannerId}")
	public CmmtBanner modify(
				@PathVariable String bannerId,
				@RequestPart(value = "banner", required = true) BannerParam param,
				@RequestPart(value = "pcImageFile", required = false) MultipartFile pcImageFile,
				@RequestPart(value = "mobileImageFile", required = false) MultipartFile mobileImageFile) {
		param.setBannerId(bannerId);
		return service.modify(param, pcImageFile, mobileImageFile);
	}

	/**
	 * 배너 추가
	 *
	 * @param cmmtBanner
	 * @param pcImageFile
	 * @param mobileImageFile
	 * @return
	 */
	@PostMapping("")
	public CmmtBanner add(
			@RequestPart(value = "banner", required = true) BannerParam param,
			@RequestPart(value = "pcImageFile", required = false) MultipartFile pcImageFile,
			@RequestPart(value = "mobileImageFile", required = false) MultipartFile mobileImageFile) {
		return service.add(param, pcImageFile, mobileImageFile);
	}

	/**
	 * 배너 삭제
	 *
	 * @param bannerId
	 */
	@DeleteMapping("/{bannerId}")
	public void remove(@PathVariable String bannerId) {
		service.remove(bannerId);
	}

	/**
	 * 배너 이미지 다운로드
	 *
	 * @param bannerId
	 * @param imageType
	 */
	@GetMapping("/{bannerId}/images/{platformType}")
	public void downloadImage(
			HttpServletResponse response,
			@PathVariable String bannerId,
			@PathVariable String platformType) {
		service.downloadImage(response, bannerId,platformType);
	}

	/**
	 * 전시 배너 목록조회
	 *
	 * @param systemId
	 * @return
	 */
	@GetMapping("/now/{systemId}")
	public JsonList<CmmtBanner> getTodayList(@PathVariable String systemId){
		return service.getTodayList(systemId);

	}
	
	/**
	 * 배너 전시상태변경
	 * 
	 * @param bannerId
	 * @param useAt
	 * @return
	 */
	@PutMapping("/{bannerId}/status")
	public CmmtBanner modifyUseAt(@PathVariable String bannerId, Boolean useAt) {
		return service.modifyStatus(bannerId, useAt);
	}
}
