package aicluster.pms.api.pblanc.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.pblanc.dto.FrontBsnsPblancListParam;
import aicluster.pms.api.pblanc.dto.FrontMainRecomendParam;
import aicluster.pms.api.pblanc.service.FrontBsnsPblancService;
import aicluster.pms.common.dto.FrontBsnsPblancDto;
import aicluster.pms.common.dto.FrontBsnsPblancListDto;
import aicluster.pms.common.dto.FrontMainListDto;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;

/**
 * 모집공고
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/bsns-pblanc")
public class FrontBsnsPblancController {

	@Autowired
	private FrontBsnsPblancService frontBsnsPblancService;

	/**
	 * 모집공고 목록 조회
	 * @param frontBsnsPblancListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<FrontBsnsPblancListDto> getList(FrontBsnsPblancListParam frontBsnsPblancListParam, @RequestParam(defaultValue = "1") Long page) {
		return frontBsnsPblancService.getList(frontBsnsPblancListParam, page);
	}

	/**
	 * 마감임박 공고 목록 조회
	 * @param frontBsnsPblancListParam
	 * @return
	 */
	@GetMapping("/closeing")
	public JsonList<FrontBsnsPblancListDto> getCloseingList(FrontBsnsPblancListParam frontBsnsPblancListParam) {
		return frontBsnsPblancService.getCloseingList(frontBsnsPblancListParam);
	}

	/**
	 * 모집공고 상세조회
	 * @param pblancId
	 * @param frontBsnsPblancListParam
	 * @return
	 */
	@GetMapping("/{pblancId}")
	public FrontBsnsPblancDto get(@PathVariable("pblancId") String pblancId, FrontBsnsPblancListParam frontBsnsPblancListParam) {
		return frontBsnsPblancService.get(pblancId, frontBsnsPblancListParam);
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param pblancId
	 * @param attachmentId
	 */
	@GetMapping("/{pblancId}/atchmnfl/{attachmentId}")
	public void getFileDownload(HttpServletResponse response, @PathVariable("pblancId") String pblancId, @PathVariable("attachmentId") String attachmentId ) {
		frontBsnsPblancService.getFileDownload(response, pblancId, attachmentId);
	}

	/**
	 * 썸네일 이미지 파일 다운로드
	 * @param response
	 * @param pblancId
	 */
	@GetMapping("/{pblancId}/thumbnail")
	public void downloadThumbnail(HttpServletResponse response, @PathVariable("pblancId") String pblancId) {
		frontBsnsPblancService.downloadThumbnail(response, pblancId);
	}

	/**
	 * 메인 인기공고 목록 조회
	 * @return
	 */
	@GetMapping("/main/popular")
	public JsonList<FrontMainListDto> getFrontMainPopularList() {
		return frontBsnsPblancService.getFrontMainPopularList();
	}


	/**
	 * 메인 나에게 맞는 사업 추천 목록 조회
	 * @return
	 */
	@GetMapping("/main/recomend")
	public JsonList<FrontMainListDto> getFrontMainRecomendList(FrontMainRecomendParam param) {
		return frontBsnsPblancService.getFrontMainRecomendList(param);
	}
}
