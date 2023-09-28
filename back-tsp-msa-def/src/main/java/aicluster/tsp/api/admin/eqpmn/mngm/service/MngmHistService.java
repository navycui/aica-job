package aicluster.tsp.api.admin.eqpmn.mngm.service;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtHistParam;
import aicluster.tsp.common.dao.CmmtCodeDao;
import aicluster.tsp.common.dao.TsptEqpmnMngmHistDao;
import aicluster.tsp.common.entity.TsptEqpmnProcessHist;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Service
public class MngmHistService {

	@Autowired
	private TsptEqpmnMngmHistDao tsptEqpmnMngmHistDao;
	@Autowired
	private CmmtCodeDao cmmtCodeDao;


	//장비관리이력 조회
	public CorePagination<MngmMgtHistParam> getMgtList(String eqpmnId, String manageDiv, CorePaginationParam cpParam){
		// 엑셀다운로드 여부
		boolean isExcel = false;
		// 전체 건수 확인
		long totalItems = tsptEqpmnMngmHistDao.selectMgtHistCount(eqpmnId, manageDiv);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

		// 페이지 목록 조회
		List<MngmMgtHistParam> list = tsptEqpmnMngmHistDao.selectMgtHistList(
				eqpmnId, manageDiv, isExcel, info.getBeginRowNum(), cpParam.getItemsPerPage());

		// 코드 리스트
		CorePagination<MngmMgtHistParam> pagination = new CorePagination<>(info, list);

		// 목록 조회
		return pagination;
	}

	//장비관리이력 엑셀 다운로드
	public ModelAndView getMgtListExcelDwld(String eqpmnId, String manageDiv) {
		// 엑셀다운로드 여부
		boolean isExcel = true;
		// 페이지 목록 조회
		List<MngmMgtHistParam> list = tsptEqpmnMngmHistDao.selectMgtHistList(
				eqpmnId, manageDiv,	isExcel);
//		List<EqpmnCodeDto> list2 = cmmtCodeDao.selectCodeNameList("EQPMN_ST");
//		for (int i = 0; list.size() > i; i++){
//			for (int j = 0; list2.size() > j; j++) {
//				if ( list2.get(j).getCode().equals(list.get(i).getManageDiv()) ){
//					list.get(i).setManageDiv(list2.get(j).getCodeNm());
//				}
//			}
//		}
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("장비관리이력");
		ExcelSheet<MngmMgtHistParam> sheet = new ExcelSheet<>();
		ExcelMergeRows mergeRegions = new ExcelMergeRows();
		sheet.setMergeRegions(mergeRegions);
		sheet.addRows(list);
		sheet.setHeaders("구분", "시작일", "종료일", "내역", "교정실시기관", "처리자명", "처리ID");
		sheet.setProperties("exManageDiv", "exManageBeginDt", "exManageEndDt", "manageResult", "crrcInstt", "mberNm", "opetrId");
		sheet.setTitle("처리이력 조회");
		sheet.setSheetName("처리이력");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}
	//장비처리이력 조회
	public CorePagination<TsptEqpmnProcessHist> getProHistList(String eqpmnId, CorePaginationParam cpParam){
		// 전체 건수 확인
		long totalItems = tsptEqpmnMngmHistDao.selectProHistCnt(eqpmnId);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

		// 페이지 목록 조회
		List<TsptEqpmnProcessHist> list = tsptEqpmnMngmHistDao.selectProHistList(eqpmnId, info.getBeginRowNum(), cpParam.getItemsPerPage());
		CorePagination<TsptEqpmnProcessHist> pagination = new CorePagination<>(info, list);

		// 목록 조회
		return pagination;
	}
}

