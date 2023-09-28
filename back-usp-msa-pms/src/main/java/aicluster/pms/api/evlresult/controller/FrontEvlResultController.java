package aicluster.pms.api.evlresult.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.evlresult.dto.FrontEvlResultDto;
import aicluster.pms.api.evlresult.dto.FrontEvlResultListParam;
import aicluster.pms.api.evlresult.service.FrontEvlResultService;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqst;
import aicluster.pms.common.dto.FrontEvlResultListDto;
import bnet.library.util.pagination.CorePagination;

@RestController
@RequestMapping("/api/front/evl-result")
public class FrontEvlResultController {

	@Autowired
	private FrontEvlResultService frontEvlResultService;

	/**
	 * 평가결과 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<FrontEvlResultListDto> getList(FrontEvlResultListParam param, @RequestParam(defaultValue = "1") Long page) {
		return frontEvlResultService.getList(param, page);
	}


	/**
	 * 평가의견 확인
	 * @param evlTrgetId
	 * @return
	 */
	@GetMapping("/{evlTrgetId}")
	public FrontEvlResultDto get(@PathVariable("evlTrgetId") String evlTrgetId) {
		return frontEvlResultService.get(evlTrgetId);
	}


	/**
	 * 결과이의신청 등록
	 * @param evlTrgetId
	 * @param slctnObjcReqst
	 * @param fileList
	 */
	@PostMapping("/{evlTrgetId}/objc-reqst")
	public void add(@PathVariable("evlTrgetId") String evlTrgetId
					, @RequestPart(value = "objcReqstCn") SlctnObjcReqst slctnObjcReqst
					, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		frontEvlResultService.add(evlTrgetId, slctnObjcReqst, fileList);
	}
}
