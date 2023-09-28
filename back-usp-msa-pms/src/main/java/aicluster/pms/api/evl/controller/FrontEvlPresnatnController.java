package aicluster.pms.api.evl.controller;

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

import aicluster.pms.api.evl.dto.FrontEvlPresnatnParam;
import aicluster.pms.api.evl.service.FrontEvlPresnatnService;
import aicluster.pms.common.dto.FrontEvlPresnatnListDto;
import aicluster.pms.common.entity.UsptEvlTrgetPresentnHist;
import bnet.library.util.pagination.CorePagination;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/front/evl-presnatn")
public class FrontEvlPresnatnController {

	@Autowired
	private FrontEvlPresnatnService frontEvlPresnatnService;

	/**
	 * 발표자료 대상 목록조회
	 *
	 */
	@GetMapping("/target-list")
	public CorePagination<FrontEvlPresnatnListDto> getPresnatnTargetList(FrontEvlPresnatnParam frontEvlTargetListParam, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	evlPresnatnListDto : {}", frontEvlTargetListParam.toString());
		return frontEvlPresnatnService.getPresnatnTargetList(frontEvlTargetListParam, page);
	}

	/**
	 * 보완사유 조회 팝업
	 *
	 */
	@GetMapping("/splemnt-resn")
	public UsptEvlTrgetPresentnHist getSplemntResnDetail(FrontEvlPresnatnParam frontEvlTargetListParamt) {
		log.debug("#####	getSplemntResnDetail : {}", frontEvlTargetListParamt.toString());
		return frontEvlPresnatnService.getSplemntResnDetail(frontEvlTargetListParamt);
	}

	/**
	 * 발표자료 대상 상세 조회
	 *
	 */
	@GetMapping("/detail")
	public FrontEvlPresnatnListDto getPresnatnTargetDetailList(FrontEvlPresnatnParam frontEvlTargetListParamt) {
		log.debug("#####	getPresnatnTargetDetailList : {}", frontEvlTargetListParamt);
		return frontEvlPresnatnService.getPresnatnTargetDetailList(frontEvlTargetListParamt);
	}

	/**
	 * 발표자료 제출
	 * evlTrgetId   , attachmentGroupId
	 */
	@PutMapping("/presentn")
	public FrontEvlPresnatnListDto modifyPresnatn(@RequestPart(value = "info") FrontEvlPresnatnListDto frontEvlPresnatnListDto
		, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		log.debug("#####	modifyPresnatn : {}", frontEvlPresnatnListDto);
		frontEvlPresnatnService.modifyPresnatn(frontEvlPresnatnListDto, fileList);

		//재조회
		FrontEvlPresnatnParam frontEvlTargetListParamt = new FrontEvlPresnatnParam();
		frontEvlTargetListParamt.setEvlTrgetId(frontEvlPresnatnListDto.getEvlTrgetId());
		 return frontEvlPresnatnService.getPresnatnTargetDetailList(frontEvlTargetListParamt);
	}

	/**
	 * 발표자료 -파일 단건 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		frontEvlPresnatnService.getOneFileDown(response, attachmentId);
	}

}
