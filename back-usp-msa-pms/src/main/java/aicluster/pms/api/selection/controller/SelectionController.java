package aicluster.pms.api.selection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.selection.dto.LastSelectionDspthParam;
import aicluster.pms.api.selection.dto.LastSelectionParam;
import aicluster.pms.api.selection.dto.SelectionDto;
import aicluster.pms.api.selection.dto.SelectionListParam;
import aicluster.pms.api.selection.service.SelectionService;
import aicluster.pms.common.dto.LastSlctnProcessHistDto;
import aicluster.pms.common.dto.SelectionListDto;
import aicluster.pms.common.dto.SlctnDetailDto;
import aicluster.pms.common.dto.SlctnTrgetListDto;
import aicluster.pms.common.util.Code;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 선정관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/selection")
public class SelectionController {

	@Autowired
	private SelectionService selectionService;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<SelectionListDto> getList(SelectionListParam param, @RequestParam(defaultValue = "1") Long page) {
		return selectionService.getList(param, page);
	}

	/**
	 * 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getExcelDwld(SelectionListParam param){
		List<SelectionListDto> list = selectionService.getListExcelDwld(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("선정관리");
		ExcelSheet<SelectionListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "사업연도" , "공고명", "모집유형", "접수차수", "담당부서", "담당자", "선정일", "통보상태");
		sheet.setProperties("processSttusNm", "bsnsYear", "pblancNm", "ordtmRcrit", "rceptOdr", "chrgDeptNm", "chargerNm", "lastSlctnDate", "dspthSttusNm");
		sheet.setTitle("선정관리 목록");
		sheet.setSheetName("선정관리");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 선정 상세조회
	 * @param evlLastSlctnId
	 * @return
	 */
	@GetMapping("/{evlLastSlctnId}")
	public SelectionDto get(@PathVariable("evlLastSlctnId") String evlLastSlctnId){
		return selectionService.get(evlLastSlctnId);
	}


	/**
	 * 선정대상 목록 엑셀 다운로드
	 * @param evlLastSlctnId
	 * @return
	 */
	@GetMapping("/{evlLastSlctnId}/slctn-trget/excel-dwld")
	public ModelAndView getSlctnTrgetExcelDwld(@PathVariable("evlLastSlctnId") String evlLastSlctnId){
		SelectionDto dto = selectionService.get(evlLastSlctnId);
		SlctnDetailDto orgInfo = dto.getSlctnInfo();
		if(orgInfo.getLastSlctn() == null || !orgInfo.getLastSlctn()) {
			throw new InvalidationException("최종선정 처리 후 엑셀 다운로드가 가능합니다.");
		}

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("최종선정대상목록");
		ExcelSheet<SlctnTrgetListDto> sheet = new ExcelSheet<>();
		sheet.addRows(dto.getSlctnTrgetList());
		if(!orgInfo.getWctMdatDlbrt() || CoreUtils.string.equals(Code.bsnsType.자원할당사업, orgInfo.getBsnsTypeCd())) {
			sheet.setHeaders("번호", "접수번호" , "사업자명/이름", "선정상태");
			sheet.setProperties("rn", "receiptNo", "memberNm", "slctnAt");
		} else {
			sheet.setHeaders("번호", "접수번호" , "사업자명/이름", "총사업비", "정부보조금", "자부담금(현금)", "자부담금(현물)", "선정상태");
			sheet.setProperties("rn", "receiptNo", "memberNm", "totalWct", "sportBudget", "alotmCash", "alotmActhng", "slctnAt");
		}
		sheet.setTitle("최종선정대상목록");
		sheet.setSheetName("최종선정대상목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 최종선정 처리
	 * @param evlLastSlctnId
	 * @param paramList
	 * @return
	 */
	@PutMapping("/{evlLastSlctnId}/last-selection")
	public JsonList<SlctnTrgetListDto> modifyLastSelection(@PathVariable("evlLastSlctnId") String evlLastSlctnId, @RequestBody List<LastSelectionParam> lastSelectionList) {
		return new JsonList<>(selectionService.modifyLastSelection(evlLastSlctnId, lastSelectionList));
	}



	/**
	 * 통보
	 * @param evlLastSlctnId
	 * @param param
	 */
	@PutMapping("/{evlLastSlctnId}/dspth")
	public void modifyDspth(@PathVariable("evlLastSlctnId") String evlLastSlctnId, @RequestBody LastSelectionDspthParam param) {
		selectionService.modifyDspth(evlLastSlctnId, param);
	}


	/**
	 * 처리이력 목록 조회
	 * @param evlLastSlctnId
	 * @return
	 */
	@GetMapping("/{evlLastSlctnId}/hist")
	public JsonList<LastSlctnProcessHistDto> getHist(@PathVariable("evlLastSlctnId") String evlLastSlctnId){
		return selectionService.getHist(evlLastSlctnId);
	}


}
