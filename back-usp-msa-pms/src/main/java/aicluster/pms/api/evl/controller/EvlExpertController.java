package aicluster.pms.api.evl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.evl.service.EvlExpertService;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertParam;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/evl-expert")
public class EvlExpertController {

	@Autowired
	private EvlExpertService evlExpertService;

	//전문가 관리 목록조회
	@GetMapping("/list")
	public CorePagination<UsptExpert> getExpertList(UsptExpertParam usptExpert, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	usptExpert : {}", usptExpert.toString());
		return evlExpertService.getExpertList(usptExpert, page);
	}


	//전문가 목록 엑셀 다운로드
	@GetMapping("/list/excel-dwld")
	public ModelAndView getEvlOnlineTargetListExcel(UsptExpertParam usptExpert){
		List<UsptExpert> list = evlExpertService.getExpertListExcel(usptExpert);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전문가 목록");
		ExcelSheet<UsptExpert> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "전문가명"	,"성별"		, "내/외국인"	, "직장명"	, "직위"	,"휴대폰"	, "이메일"	, "생년월일");
		sheet.setProperties("rn"	, "expertNm"	,"genderNm"	, "nativeNm"	, "wrcNm"	, "ofcpsNm"	, "mbtlnum"	, "email"	, "brthdy");
		sheet.setTitle("전문가 목록");
		sheet.setSheetName("전문가 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	//평가계획 기본정보 등록
	@PostMapping("")
	public JsonList<UsptExpert> add(@RequestBody(required = false) List<UsptExpert> usptExpertList) {
		log.debug("UsptExpert insert");
		log.debug(usptExpertList.toString());
		return new JsonList<>(evlExpertService.add(usptExpertList));
	}


}
