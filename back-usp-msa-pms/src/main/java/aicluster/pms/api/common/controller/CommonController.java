package aicluster.pms.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.api.common.dto.StreamDocsResponseDto;
import aicluster.pms.api.common.service.CommonService;
import aicluster.pms.common.dto.BsnsNmDto;
import aicluster.pms.common.entity.UsptBsnsPblancAppnTask;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/common")
public class CommonController {

	@Autowired
	private CommonService commonService;

	/**
	 * 사업연도 목록 조회
	 * @return
	 */
	@GetMapping("/bsnsYearList")
	public JsonList<String> getBsnsYearList() {
		return new JsonList<>(commonService.getBsnsYearList());
	}

	/**
	 * 사업 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	@GetMapping("/bsnsNmList")
	public JsonList<BsnsNmDto> getBsnsNmList(String bsnsYear) {
		return new JsonList<>(commonService.getBsnsNmList(bsnsYear));
	}


	/**
	 * 공고 지원분야 목록 조회
	 * @return
	 */
	@GetMapping("/appnTaskList")
	public JsonList<UsptBsnsPblancAppnTask> getAppnTaskList(){
		return new JsonList<>(commonService.getAppnTaskList());
	}

	/**
	 * PDF View
	 * @param fileInfo
	 * @return
	 */
	@GetMapping("/pdf/view")
	public StreamDocsResponseDto getPdfView(CmmtAtchmnfl fileInfo) {
		return commonService.getPdfView(fileInfo);
	}

}
