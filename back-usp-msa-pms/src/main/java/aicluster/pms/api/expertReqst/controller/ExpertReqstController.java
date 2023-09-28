package aicluster.pms.api.expertReqst.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.expertReqst.dto.ExpertClIdDto;
import aicluster.pms.api.expertReqst.dto.ExpertClIdParntsDto;
import aicluster.pms.api.expertReqst.dto.ExpertReqstDto;
import aicluster.pms.api.expertReqst.dto.ExpertReqstListParam;
import aicluster.pms.api.expertReqst.service.ExpertReqstService;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertReqstHist;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문가 신청 관리 admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/expert-reqst")
public class ExpertReqstController {

	@Autowired
	private ExpertReqstService expertReqstService;

	/**
	 * 전문가신청관리 목록조회
	 * @param expertReqstListParam
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptExpert> getExpertReqstList(ExpertReqstListParam expertReqstListParam, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	getExpertReqstList : {}", expertReqstListParam);
		return expertReqstService.getExpertReqsList(expertReqstListParam, page );
	}

	/**
	 * 전문가신청관리 목록 엑셀 다운로드
	 * @param expertReqstListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getExpertReqstListExcel(ExpertReqstListParam expertReqstListParam){

		log.debug("#####	getExpertListExcel : {}", expertReqstListParam);

		List<UsptExpert> list = expertReqstService.getExpertReqstListExcel(expertReqstListParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전문가신청 목록");
		ExcelSheet<UsptExpert> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "이름",   "성별",    "내/외국인"	, "직장명"	, "직위");
		sheet.setProperties("rn"	, "expertNm"	,"genderNm"	, "nativeNm"	, "wrcNm"	, "ofcpsNm"	);
		sheet.setTitle("전문가신청 관리 목록");
		sheet.setSheetName("전문가 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 전문가 신청 상세정보
	 * @param expertId
	 * @return
	 */
	@GetMapping("/{expertId}")
	public ExpertReqstDto getExpertReqstInfo(HttpServletRequest request, @PathVariable("expertId") String expertId) {
		log.debug("#####	getExpertReqstInfo : {}", expertId);
		return expertReqstService.getExpertReqstInfo(request, expertId);
	}

	/**
	 * 전문가 신청 승인
	 * @param ExpertReqstDto
	 * @param fileList
	 */
	@PutMapping("/{expertId}/confm")
	public ExpertReqstDto modifyExpertReqst( @PathVariable("expertId") String expertId
			              , @RequestPart(value = "info", required = false) ExpertReqstDto expertReqstDto
			              , @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		return expertReqstService.modifyExpertReqstConfm(expertId, expertReqstDto, fileList);
	}



	/**
	 * 전문가신청관리 반려
	 * @param expertReqstListParam
	 * @return
	 */
	@PutMapping("/return")
	public ExpertReqstListParam modifyExpertReqstReturn(@RequestBody(required = false) ExpertReqstListParam expertReqstListParam){
		log.debug("#####	modifyExpertReqstReturn : {}", expertReqstListParam);
		 return expertReqstService.modifyExpertReqstReturn(expertReqstListParam);
	}

	/**
	 * 전문가 신청 상세_처리이력
	 * @param expertId
	 * @return
	 */
	@GetMapping("/{expertId}/hist")
	public JsonList<UsptExpertReqstHist> getExpertReqstHistList(@PathVariable("expertId") String expertId) {
		log.debug("#####	getExpertReqstHistList : {}", expertId);
		return new JsonList<>(expertReqstService.getExpertReqstHistList(expertId));
	}

	/**
	 * 전문가 분류조회_부모전문가분류 조회
	 * @return
	 */
	@GetMapping("/expert-clid/parnts")
	public JsonList<ExpertClIdParntsDto> selectParntsExpertClIdList() {
		log.debug("#####	selectParntsExpertClIdList : {}" );
		return new JsonList<>(expertReqstService.selectParntsExpertClIdList());
	}


	/**
	 * 전문가 분류조회_전문가분류보 조회
	 * @return
	 */
	@GetMapping("/expert-clid/{expertClId}")
	public JsonList<ExpertClIdDto> selectExpertClIdList(@PathVariable("expertClId") String expertClId) {
		return new JsonList<>(expertReqstService.selectExpertClIdList(expertClId));
	}

	/**
	 * 파일 일괄 다운
	 * @param bsnsPlanParam
	 * @return
	 */
	@GetMapping("/atchmnfl/group/{attachmentGroupId}")
	public void getAllFileDown(HttpServletResponse response, @PathVariable("attachmentGroupId") String attachmentGroupId){
		log.debug("#####	getAllFileDown : {}", attachmentGroupId);
		expertReqstService.getAllFileDown(response, attachmentGroupId);
	}

	/**
	 * 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		expertReqstService.getOneFileDown(response, attachmentId);
	}

}