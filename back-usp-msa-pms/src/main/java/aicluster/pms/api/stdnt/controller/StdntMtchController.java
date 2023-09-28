package aicluster.pms.api.stdnt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.stdnt.dto.StdntListParam;
import aicluster.pms.api.stdnt.dto.StdntMtchListParam;
import aicluster.pms.api.stdnt.service.StdntMtchService;
import aicluster.pms.common.dto.MtchStdnListDto;
import aicluster.pms.common.dto.StdnListDto;
import aicluster.pms.common.dto.StdntMtchDto;
import aicluster.pms.common.dto.StdntMtchListDto;
import aicluster.pms.common.entity.UsptStdntMtch;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 교육생 매칭 관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/stdnt-mtch")
public class StdntMtchController {

	@Autowired
	private StdntMtchService stdntMtchService;


	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<StdntMtchListDto> getList(StdntMtchListParam param, @RequestParam(defaultValue = "1") Long page){
		return stdntMtchService.getList(param, page, false);
	}

	/**
	 * 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(StdntMtchListParam param){
		List<StdntMtchListDto> list = stdntMtchService.getListExcelDwld(param, false);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("교육생매칭 목록");
		ExcelSheet<StdntMtchListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "사업자명", "접수번호", "과제명", "책임자", "교육생수");
		sheet.setProperties("processSttus", "memberNm", "receiptNo", "taskNm", "rspnberNm", "stdntMtchcnt");
		sheet.setTitle("교육생매칭 목록");
		sheet.setSheetName("교육생매칭 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 상세 기업정보 조회
	 * @param lastSlctnTrgetId
	 * @return
	 */
	@GetMapping("/{lastSlctnTrgetId}")
	public StdntMtchDto get(HttpServletRequest request, @PathVariable("lastSlctnTrgetId") String lastSlctnTrgetId){
		return stdntMtchService.get(request, lastSlctnTrgetId);
	}


	/**
	 * 상세 매칭교육생 목록 조회
	 * @param lastSlctnTrgetId
	 * @return
	 */
	@GetMapping("/{lastSlctnTrgetId}/mtch-stdnt")
	public CorePagination<MtchStdnListDto> getMtchStdntList(@PathVariable("lastSlctnTrgetId") String lastSlctnTrgetId
															, @RequestParam(defaultValue = "1") Long page, Long itemsPerPage){
		return stdntMtchService.getMtchStdntList(lastSlctnTrgetId, page, itemsPerPage);
	}


	/**
	 * 상세 매칭교육생 목록 엑셀 다운로드
	 * @param lastSlctnTrgetId
	 * @return
	 */
	@GetMapping("/{lastSlctnTrgetId}/mtch-stdnt/excel-dwld")
	public ModelAndView getMtchStdntListExcelDwld(@PathVariable("lastSlctnTrgetId") String lastSlctnTrgetId){
		List<MtchStdnListDto> list = stdntMtchService.getMtchStdntListExcelDwld(lastSlctnTrgetId);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("매칭교육생 목록");
		ExcelSheet<MtchStdnListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("접수번호", "이름", "희망직무", "최종학교", "졸업여부", "군필여부");
		sheet.setProperties("receiptNo", "memberNm", "hopeDty", "schulNm", "msvcType", "grdtnDiv");
		sheet.setTitle("매칭교육생 목록");
		sheet.setSheetName("매칭교육생 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 매칭교육생 삭제
	 * @param lastSlctnTrgetId
	 * @param deleteMtchStdntList
	 */
	@DeleteMapping("/{lastSlctnTrgetId}/mtch-stdnt")
	public void removeMtchStdnt(@PathVariable("lastSlctnTrgetId") String lastSlctnTrgetId, @RequestBody List<UsptStdntMtch> deleteMtchStdntList){
		stdntMtchService.removeMtchStdnt(lastSlctnTrgetId, deleteMtchStdntList);
	}


	/**
	 * 교육생 목록 조회
	 * @param lastSlctnTrgetId
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("/{lastSlctnTrgetId}/stdnt")
	public CorePagination<StdnListDto> getStdntList(@PathVariable("lastSlctnTrgetId") String lastSlctnTrgetId
												, StdntListParam param
												, @RequestParam(defaultValue = "1") Long page){
		param.setLastSlctnTrgetId(lastSlctnTrgetId);
		return stdntMtchService.getStdntList(param, page);
	}


	/**
	 * 교육생 매칭 등록
	 * @param lastSlctnTrgetId
	 * @param mtchStdntList
	 */
	@PostMapping("/{lastSlctnTrgetId}/mtch-stdnt")
	public void add(@PathVariable("lastSlctnTrgetId") String lastSlctnTrgetId, @RequestBody List<UsptStdntMtch> mtchStdntList){
		stdntMtchService.add(lastSlctnTrgetId, mtchStdntList);
	}

}
