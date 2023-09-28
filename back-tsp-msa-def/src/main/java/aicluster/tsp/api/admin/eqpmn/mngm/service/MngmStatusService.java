package aicluster.tsp.api.admin.eqpmn.mngm.service;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmStatusParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmSearchParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.CmmtCodeDao;
import aicluster.tsp.common.dao.TsptEqpmnDao;
import aicluster.tsp.common.dao.TsptEqpmnMngmMgtDao;
import aicluster.tsp.common.util.TspCode;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import bnet.library.view.ExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class MngmStatusService {

  public static final long ITEMS_PER_PAGE = 5;

  @Autowired
  private TsptEqpmnDao tsptEqpmnDao;
  @Autowired
  private TsptEqpmnMngmMgtDao tsptEqpmnMngmMgtDao;

  @Autowired
  CommonService commonService;
  @Autowired
  private CmmtCodeDao cmmtCodeDao;

  public CorePagination<MngmStatusParam> getList(String status, MngmSearchParam search,  CorePaginationParam corePaginationParam) {

    // 검색 글자수 제한
    Integer searchString = 100;
    if (searchString > Integer.MAX_VALUE) {
      throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
    }
    if (CoreUtils.string.isNotBlank(search.getUserNm()) && search.getUserNm().length() > searchString){
      throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
    }
    if (CoreUtils.string.isNotBlank(search.getEqpmnNmKorean()) && search.getEqpmnNmKorean().length() > searchString){
      throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
    }
    if (CoreUtils.string.isNotBlank(search.getAssetsNo()) && search.getAssetsNo().length() > searchString){
      throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
    }

    // 전체 건수 조회
    long totalItems = tsptEqpmnDao.selectEqpmnStatusCount(status, search);

    // 조회할 페이지 구간 정보 확인
    CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);

    List<MngmStatusParam> list = tsptEqpmnDao.selectEqpmnStatusList(status, search,info.getBeginRowNum(), corePaginationParam.getItemsPerPage(), false);

    return new CorePagination<>(info, list);
  }

  public ModelAndView getListExcelDownload(String status, MngmSearchParam search) {
    boolean isExcel = true;

    ExcelWorkbook wb = new ExcelWorkbook();
    // 페이지 목록 조회
    List<MngmStatusParam> list = tsptEqpmnDao.selectEqpmnStatusList(status, isExcel, search);
//    List<EqpmnCodeDto> list2 = cmmtCodeDao.selectCodeNameList("MEMBER_TYPE");
//    for (int i = 0; list.size() > i; i++){
//      for (int j = 0; list2.size() > j; j++) {
//        if ( list2.get(j).getCode().equals(list.get(i).getMberDiv()) ){
//          list.get(i).setMberDiv(list2.get(j).getCodeNm());
//        }
//      }
//    }
    wb.setFilename("장비사용_현황");
    ExcelSheet<MngmStatusParam> sheet = new ExcelSheet<>();
    ExcelMergeRows mergeRegions = new ExcelMergeRows();
    sheet.setMergeRegions(mergeRegions);
    sheet.addRows(list);
    sheet.setHeaders("번호","자산번호", "장비명", "구분", "이름", "업체명",
      "사용 시작일", "사용 종료일", "사용 시간");
    sheet.setProperties("number", "assetsNo", "eqpmnNmKorean", "exMberDiv", "userNm", "entrprsNm",
      "exUseBeginDt", "exUseEndDt", "exExpectUsgtm");
    sheet.setTitle("장비사용_현황 조회");
    sheet.setSheetName("장비사용_현황 조회");
    wb.addSheet(sheet);
    return ExcelView.getView(wb);
  }
}

