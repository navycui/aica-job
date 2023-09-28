package aicluster.pms.api.stdnt.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.stdnt.dto.StdntCareerParam;
import aicluster.pms.api.stdnt.dto.StdntCareerReqParam;
import aicluster.pms.api.stdnt.service.StdntCareerService;
import aicluster.pms.common.dto.StdntCareerDto;
import aicluster.pms.common.dto.StdntCareerListDto;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 교육생경력 관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/stdnt-career")
public class StdntCareerController {

	@Autowired
	private StdntCareerService stdntCareerService;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<StdntCareerListDto> getList(StdntCareerParam param, @RequestParam(defaultValue = "1") Long page){
		return stdntCareerService.getList(param, page);
	}

	/**
	 * 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(StdntCareerParam param){
		List<StdntCareerListDto> list = stdntCareerService.getListExcelDwld(param);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("교육생이력 목록");
		ExcelSheet<StdntCareerListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("접수번호", "희망직무", "이름", "매칭여부", "매칭기관명", "사업연도", "공고명");
		sheet.setProperties("receiptNo", "hopeDty", "memberNm", "mtchAt", "mtchInstt", "bsnsYear", "pblancNm");
		sheet.setTitle("교육생이력 목록");
		sheet.setSheetName("교육생이력 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 일괄 등록요청
	 * @param param
	 * @return
	 */
	@PutMapping("/regist-req")
	public void addBndeRegistReq(@RequestBody StdntCareerReqParam param) {
		stdntCareerService.addRegistReq(param);
	}



	/**
	 * 교육생경력 상세조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/{applyId}")
	public StdntCareerDto get(HttpServletRequest request, @PathVariable("applyId") String applyId){
		return stdntCareerService.get(request, applyId);
	}


	/**
	 * 등록요청
	 * @param applyId
	 * @param param
	 * @return
	 */
	@PutMapping("/{applyId}/regist-req")
	public void addRegistReq(@PathVariable("applyId") String applyId, @RequestBody StdntCareerReqParam param) {
		List<String> applyIdList = new ArrayList<String>();
		applyIdList.add(applyId);
		param.setApplyIdList(applyIdList);
		stdntCareerService.addRegistReq(param);
	}

}
