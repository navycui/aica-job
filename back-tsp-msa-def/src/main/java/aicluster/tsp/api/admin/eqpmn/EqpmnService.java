package aicluster.tsp.api.admin.eqpmn;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.admin.eqpmn.dscnt.param.DscntApplyParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.CmmtCodeDao;
import aicluster.tsp.common.dao.TsptEqpmnDao;
import aicluster.tsp.common.dao.TsptEqpmnDscntDao;
import aicluster.tsp.common.dto.EqpmnAllListDto;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Service
public class EqpmnService {

    public static final long ITEMS_PER_PAGE = 5;

    @Autowired
    private TsptEqpmnDao tsptEqpmnDao;

    @Autowired
    private TsptEqpmnDscntDao tsptEqpmnDscntDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CmmtCodeDao cmmtCodeDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EnvConfig config;

    /**
     * 장비 추가
     */

    public EqpmnParam insert(EqpmnParam param) {
        EqpmnParam tsptEqpmn = new EqpmnParam();
        BnMember worker = TspUtils.getMember();
        //장비
        tsptEqpmn.setEqpmnId(CoreUtils.string.getNewId("eqpmn-")); // 장비ID
        tsptEqpmn.setAssetsNo(param.getAssetsNo()); // 자산번호
        tsptEqpmn.setEqpmnSttus(TspCode.eqpmnSt.가용);
        tsptEqpmn.setEqpmnNmKorean(param.getEqpmnNmKorean()); // 장비명(국문)
        tsptEqpmn.setEqpmnNmEngl(param.getEqpmnNmEngl()); // 장비명(영문)
        tsptEqpmn.setModelNm(param.getModelNm()); // 모델명
        tsptEqpmn.setEqpmnClId(param.getEqpmnClId()); // 장비분류ID
        tsptEqpmn.setEqpmnStndrd(param.getEqpmnStndrd()); // 장비규격
        tsptEqpmn.setSumry(param.getSumry()); // 요약
        tsptEqpmn.setSpecComposition(param.getSpecComposition()); // 제원
        tsptEqpmn.setAsstnMhrls(param.getAsstnMhrls()); // 보조기기
        tsptEqpmn.setRealmPrpos(param.getRealmPrpos()); // 장비용도
        tsptEqpmn.setSrcelct(param.getSrcelct()); // 전원
        tsptEqpmn.setMnlAt(param.isMnlAt()); // 메뉴얼유무
        tsptEqpmn.setSwAt(param.isSwAt()); // 소프트웨어유무
        tsptEqpmn.setLegacyItlpc(param.getLegacyItlpc()); // 설치장소
        tsptEqpmn.setPchrgAt(param.isPchrgAt()); // 유료여부
        tsptEqpmn.setSpcmnt(param.getSpcmnt()); // 특기사항
        tsptEqpmn.setImageId(param.getImageId());
        tsptEqpmn.setAsEntrprsNm(param.getAsEntrprsNm()); // AS업체
        tsptEqpmn.setAsChargerNm(param.getAsChargerNm()); // AS담당자
        tsptEqpmn.setAsChargerCttpc(param.getAsChargerCttpc()); // AS연락처(전화)
        tsptEqpmn.setPurchsDt(param.getPurchsDt()); // 구입일
        tsptEqpmn.setStrNm(param.getStrNm()); // 구입처
        tsptEqpmn.setPurchsPc(param.getPurchsPc()); // 구입가격(원)
        tsptEqpmn.setMakr(param.getMakr()); // 제조사
        tsptEqpmn.setTkoutAt(param.isTkoutAt()); // 반출여부

        // Note: 테스트기간동안 고정적인 updateID로 들어가도록 함
        tsptEqpmn.setUpdusrId(worker.getLoginId()); // 생성자ID
        tsptEqpmn.setCreatrId(worker.getLoginId()); // 수정자ID

        tsptEqpmn.setRntfeeHour(param.isPchrgAt() ? param.getRntfeeHour() : 0); // 시간당 사용료
        tsptEqpmn.setUsefulBeginHour(param.getUsefulBeginHour()); // 1일 가용시작시간
        tsptEqpmn.setUsefulEndHour(param.getUsefulEndHour()); // 1일 가용종료시간
        tsptEqpmn.setUseRateInctvSetupAt(param.isUseRateInctvSetupAt()); // 사용율 저조설정
        tsptEqpmn.setHldyInclsAt(param.isHldyInclsAt()); // 법정공휴일 휴일포함
        tsptEqpmn.setTkoutHldyInclsAt(param.isTkoutHldyInclsAt()); // 반출시 휴일포함
        tsptEqpmn.setNttkoutHldyInclsAt(param.isNttkoutHldyInclsAt()); // 미반출시 휴일포함
        if ( param.getUsefulBeginHour().equals(12) ) {
            throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "1일 가용시작시간이", "점심시간(12~13)"));
        }
        if ( param.getUsefulEndHour().equals(13) ) {
            throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "1일 가용종료시간이", "점심시간(12~13)"));
        }
        if(string.isNotBlank(tsptEqpmn.getEqpmnId())) {
            //자산번호, 장비명(국문), 분류 필수입력항목 미입력시 예외처리
            if(CoreUtils.string.isBlank(tsptEqpmn.getAssetsNo())) {
                throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "자산번호"));
            }
            else if(CoreUtils.string.isBlank(tsptEqpmn.getEqpmnNmKorean())) {
                throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "장비명(국문)"));
            }
            else if(CoreUtils.string.isBlank(tsptEqpmn.getEqpmnClId())) {
                throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "분류"));
            }
            else if(CoreUtils.string.isBlank(tsptEqpmn.getRntfeeHour().toString())) {
                if (tsptEqpmn.isPchrgAt()) {
                    throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "시간당 사용료"));
                }
                tsptEqpmn.setRntfeeHour(0);
            }
            else if(CoreUtils.string.isBlank(tsptEqpmn.getUsefulBeginHour().toString())||CoreUtils.string.isBlank(tsptEqpmn.getUsefulEndHour().toString())){
                throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "1일간 가용시간"));
            }
            // 입력값 검증 결과 메시지 처리 및 DB insert
            tsptEqpmnDao.insertEquipment(tsptEqpmn);
        }
            for(String dscnt : param.getDscntId())
            {
                if(string.isNotBlank(dscnt))
                {
                    DscntApplyParam dscntApplyParam = new DscntApplyParam();
                    dscntApplyParam.setDscntId(dscnt);
                    dscntApplyParam.setEqpmnId(tsptEqpmn.getEqpmnId());
                    dscntApplyParam.setCreatrId(worker.getLoginId());
                    dscntApplyParam.setUpdusrId(worker.getLoginId());
                    tsptEqpmnDscntDao.postDscntApplyList(dscntApplyParam);
                }else{
                    throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "할인ID"));
                }
            }
        return tsptEqpmn;
    }
//
//
//    //장비삭제
//    public void delete(String eqpmnId) {
//        // 장비사용신청(랜탈을 했는지 여부)를 확인해서 한건이라도 있으면 삭제 불가
//        if(tsptEqpmnRentalDao.selectRentalIsEqpmnCount(eqpmnId) != 0)
//        {
//            throw new InvalidationException("사용신청 이력이 있습니다.");
//            // 사용이력이 없다면 테이블에서 삭제
//        }else {
//            tsptEqpmnDao.deleteEquipment(eqpmnId);
//        }
//        insertHistory(eqpmnId, "삭제");
//
//    }
//
////    public void modify() {
////    }

    /**
     * 장비 목록 조회
     */

    public CorePagination<EqpmnAllListDto> getlist(EqpmnAllListParam eqpmnAllListParam, CorePaginationParam cpParam){
        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (string.isNotBlank(eqpmnAllListParam.getEqpmnNmKorean()) && eqpmnAllListParam.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (string.isNotBlank(eqpmnAllListParam.getModelNm()) && eqpmnAllListParam.getModelNm().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (string.isNotBlank(eqpmnAllListParam.getAssetsNo()) && eqpmnAllListParam.getAssetsNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        boolean isExcel = false;
        // 전체 건수 확인
        long totalItems = tsptEqpmnDao.selectEquipmentCount(eqpmnAllListParam);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<EqpmnAllListDto> list = tsptEqpmnDao.selectEquipmentList(eqpmnAllListParam, isExcel, info.getBeginRowNum(), cpParam.getItemsPerPage());
        CorePagination<EqpmnAllListDto> pagination = new CorePagination<>(info, list);
		// 목록 조회
		return pagination;
    }

    //엑셀 다운로드
    public ModelAndView getListExcelDwld(EqpmnAllListParam eqpmnAllListParam) {
    	boolean isExcel = true;
        List<EqpmnAllListDto> list = tsptEqpmnDao.selectEquipmentList(eqpmnAllListParam, isExcel);
//        List<EqpmnCodeDto> list2 = cmmtCodeDao.selectCodeNameList("EQPMN_ST");
//        for (int i = 0; list.size() > i; i++){
//            for (int j = 0; list2.size() > j; j++) {
//                if ( list2.get(j).getCode().equals(list.get(i).getEqpmnSttus()) ){
//                    list.get(i).setEqpmnSttus(list2.get(j).getCodeNm());
//                }
//                if (list.get(i).getTkoutAt()){
//                    list.get(i).setExTkoutAt("반출");
//                }else list.get(i).setExTkoutAt("미반출");
//                if (list.get(i).getDisuseAt()){
//                    list.get(i).setExDisuseAt("불용");
//                }else list.get(i).setExDisuseAt("정상");
//            };
//        };
        ExcelWorkbook wb = new ExcelWorkbook();
        wb.setFilename("장비목록");
        ExcelSheet<EqpmnAllListDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("장비상태", "자산번호", "장비명(국문)", "분류", "모델명", "반출여부", "불용여부", "등록일");
        sheet.setProperties("exEqpmnSttus", "assetsNo", "eqpmnNmKorean", "eqpmnClNm", "modelNm", "exTkoutAt", "exDisuseAt", "exCreatDt");
        sheet.setTitle("장비목록");
        sheet.setSheetName("장비목록");
        wb.addSheet(sheet);
        return ExcelView.getView(wb);
    }
//
//    private void insertHistory(String eqpmnId, String text){
//        TsptEqpmnHist hist = new TsptEqpmnHist();
//        hist.setHistId(CoreUtils.string.getNewId("eqpmnHist-"));
//        hist.setEqpmnId(eqpmnId);
//        // Note: 현재는 고정 ID로 넣어어지도록 수정
//        hist.setWorkerId("member-449a4df86c8648a1b6a1faef1b1f64fc");
//        hist.setWorkTypeNm("장비 " + text);
//        hist.setWorkCn("장비를 " + text + " 하였습니다.");
//        if(tsptEqpmnHistDao.insertEquipmentHistory(hist) != 1) {
//            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "장비 - " + text));
//        }
//    }
//
    public String imageUpload(MultipartFile image) {
        if( image != null) {
            CmmtAtchmnfl attachment = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), image, null, 0);
            return attachment.getAttachmentId();
//            return attachment.getAttachmentId();
        } else {
            throw new InvalidationException("이미지 업로드 실패");
        }
    }
}

