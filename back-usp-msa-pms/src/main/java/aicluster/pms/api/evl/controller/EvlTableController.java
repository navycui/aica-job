package aicluster.pms.api.evl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.evl.service.EvlTableService;
import aicluster.pms.common.entity.UsptEvlTable;
import aicluster.pms.common.entity.UsptEvlTableSearchParam;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/evl-table")
public class EvlTableController {

	@Autowired
	private EvlTableService evlTableService;

	/*평가표 목록조회*/
	@GetMapping("")
	public CorePagination<UsptEvlTable> getList(UsptEvlTableSearchParam usptEvlTableParam, @RequestParam(defaultValue = "1") Long page) {
		return evlTableService.getList(usptEvlTableParam, page);
	}

	/*평가표 목록 엑셀다운로드*/
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(UsptEvlTableSearchParam usptEvlTableParam){
		List<UsptEvlTable> list = evlTableService.getListExcelDwld(usptEvlTableParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가표 목록");
		ExcelSheet<UsptEvlTable> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("평가표명", "평가방식", "의견작성", "사용" , "등록일");
		sheet.setProperties("evlTableNm", "evlMthdNm", "indvdlzOpinionWritngNm", "enabledNm", "rgsde");
		sheet.setTitle("평가표 목록");
		sheet.setSheetName("평가표 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/*평가표 기본정보 등록*/
	@PostMapping("")
	public UsptEvlTable add(@RequestBody UsptEvlTable usptEvlTable) {
		log.debug("evlTable add");
		return evlTableService.add(usptEvlTable);
	}

	/*평가표 상세조회*/
	@GetMapping("/{evlTableId}")
	public UsptEvlTable get(@PathVariable String evlTableId) {
		return evlTableService.get(evlTableId);
	}

	/*평가표 수정 관리*/
	@PutMapping("/{evlTableId}")
	public UsptEvlTable modify(@PathVariable String evlTableId, @RequestBody UsptEvlTable param) {
		if (param == null) {
			throw new InvalidationException("입력정보가 없습니다.");
		}

		if (param.getUsptEvlIemList().size() < 1){
			throw new InvalidationException("입력한 평가항목 정보가 없습니다.");
		}

		//이미 평가진행이 된 평가표는 수정할 수 없음 로직 추가
		int modifyImpossibleCnt =  evlTableService.getModifyEnableCnt(evlTableId); //1 이상이면 수정 불가

		if(modifyImpossibleCnt > 0) {
			throw new InvalidationException("이미 평가 진행이 된 평가표는 수정이 불가합니다.");
		}

		param.setEvlTableId(evlTableId);
		evlTableService.modify(param);

		return evlTableService.get(evlTableId);//재조회하여 리턴

	}

	/*평가표 사용 미사용 처리*/
	@PutMapping("/{evlTableId}/enable")
	public UsptEvlTable modifyEnable(@PathVariable String evlTableId, @RequestBody UsptEvlTable param) {
		if (param == null) {
			throw new InvalidationException("입력정보가 없습니다.");
		}

		param.setEvlTableId(evlTableId);

		UsptEvlTable result = evlTableService.get(evlTableId);

		if(result == null) {
			throw new InvalidationException("평가표 정보가 없습니다.");
		}else {
			if(param.getEnabled() == result.getEnabled()){
				if(param.getEnabled()) {
					throw new InvalidationException("평가표가 이미 사용으로 설정되어 있습니다.");
				}else {
					throw new InvalidationException("평가표가 이미 미사용으로 설정되어 있습니다.");
				}
			}

		}

		result.setEnabled(param.getEnabled());
		result.setEvlTableId(evlTableId);

		evlTableService.modifyEnable(result);

		return evlTableService.get(evlTableId);//재조회하여 리턴

	}

	//평가표 선택 팝업
	@GetMapping("/search")
	public CorePagination<UsptEvlTable> getEvlTableList(String evlTableNm, @RequestParam(defaultValue = "1") Long page) {
		UsptEvlTableSearchParam usptEvlTableParam = new UsptEvlTableSearchParam();

		usptEvlTableParam.setEvlTableNm(evlTableNm);
		usptEvlTableParam.setEnabled(true);//사용인것만 조회하기
//		usptEvlTableParam.setEvlMthdCd(evlMthdCd);

		return evlTableService.getList(usptEvlTableParam, page);
	}
}
