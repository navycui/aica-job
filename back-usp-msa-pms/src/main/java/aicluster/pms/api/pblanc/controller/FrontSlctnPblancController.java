package aicluster.pms.api.pblanc.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.pblanc.service.FrontSlctnPblancService;
import aicluster.pms.common.dto.FrontSlctnPblancDto;
import aicluster.pms.common.dto.FrontSlctnPblancListDto;
import bnet.library.util.pagination.CorePagination;

/**
 * 선정결과공고
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/slctn-pblanc")
public class FrontSlctnPblancController {

	@Autowired
	private FrontSlctnPblancService frontSlctnPblancService;

	/**
	 * 선정결과 공고 목록 조회
	 * @param slctnPblancNm
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<FrontSlctnPblancListDto> getList(String slctnPblancNm , @RequestParam(defaultValue = "1") Long page, Long itemsPerPage) {
		return frontSlctnPblancService.getList(slctnPblancNm, page, itemsPerPage);
	}

	/**
	 * 선정결과 공고 상세조회
	 * @param slctnPblancId
	 * @param slctnPblancNm
	 * @return
	 */
	@GetMapping("/{slctnPblancId}")
	public FrontSlctnPblancDto get(@PathVariable("slctnPblancId") String slctnPblancId, String slctnPblancNm) {
		return frontSlctnPblancService.get(slctnPblancId, slctnPblancNm);
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param slctnPblancId
	 * @param attachmentId
	 */
	@GetMapping("/{slctnPblancId}/atchmnfl/{attachmentId}")
	public void getFileDownload(HttpServletResponse response, @PathVariable("slctnPblancId") String slctnPblancId, @PathVariable("attachmentId") String attachmentId ) {
		frontSlctnPblancService.getFileDownload(response, slctnPblancId, attachmentId);
	}
}
