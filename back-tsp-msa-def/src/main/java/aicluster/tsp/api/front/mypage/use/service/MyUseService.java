package aicluster.tsp.api.front.mypage.use.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.common.param.CommonCalcParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.front.mypage.use.param.*;
import aicluster.tsp.common.dao.TsptFrontMyUseDao;
import aicluster.tsp.common.dto.*;
import aicluster.tsp.common.entity.TsptEqpmnExtn;
import aicluster.tsp.common.entity.TsptEqpmnReprt;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MyUseService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private TsptFrontMyUseDao tsptFrontMyUseDao;
    Date now = new Date();
    /** 목록 조회 */
    public CorePagination<FrontMyUseListDto> getUseList(MyUseListSearchParam param, CorePaginationParam cpParam){
        // 로그인 계정 셋팅
        BnMember worker = TspUtils.getMember();

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(param.getEqpmnNmKorean()) && param.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        param.setMberId(worker.getMemberId());
        // 전체 건수 확인
        long totalItems = tsptFrontMyUseDao.selectMyUseCnt(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(),cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<FrontMyUseListDto> list = tsptFrontMyUseDao.selectMyUseList(info.getBeginRowNum(), cpParam.getItemsPerPage(), param);
        CorePagination<FrontMyUseListDto> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    /** 상세(승인전, 후) 조회 및 첨부파일 ID 조회 */
    public MyUseDetailParam getUseDetail(String reqstId){
        // 승인 전 조회
        MyUseDetailParam dto = tsptFrontMyUseDao.selectMyUseDetail(reqstId);
        dto.setPromsFileInfo(tsptFrontMyUseDao.selectPromsFileInfo(dto.getPromsAtchmnflId()));
        // 예상금액 계산
        CommonCalcParam calc =  commonService.getWorkingRntfee(dto.getEqpmnId(), dto.getUseBeginDt(), dto.getUseEndDt(), dto.isTkoutAt());
        // 예상금액결과값 dto에 추가
        dto.setExpectUsgtm(calc.getTotalHrs());
        dto.setExpectRntfee(calc.getRntfee());
        // 1일 가용시간
        dto.setUsefulHour(calc.getUsefulHour());
        // 첨부파일리스트 조회
        dto.setAttachMentParam(tsptFrontMyUseDao.selectMyUseDetailAttachMentParam(dto.getAtchmnflGroupId()));
        // 승인 후 조회
        if (Objects.equals(TspCode.reqstSttus.APPROVE.toString(), dto.getReqstSttus())) {
            dto.setApproveParam(tsptFrontMyUseDao.selectApproveParam(reqstId));
            if (dto.getApproveParam().isTkinAt()) {
                dto.getApproveParam().setTkinAtParam(tsptFrontMyUseDao.selectTkinParam(reqstId,"TKIN"));
            }
        }
        return dto;
    }

    public int datevalue(Date cdate) {

        return Integer.parseInt(CoreUtils.date.format(cdate,"yyyyMMdd"));
    }

    /** 상세 신청취소 */
    public void putUseCancel(String reqstId) {
        MyUseDetailParam dto = getUseDetail(reqstId);
        if (!(dto.getReqstSttus().equals(TspCode.reqstSttus.APPLY.toString()) || dto.getReqstSttus().equals(TspCode.reqstSttus.SPM_REQUEST.toString()))) {
            if (datevalue(now) > datevalue(dto.getUseBeginDt())) {
                throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "사용상태", "사용상태"));
            }
        }
        tsptFrontMyUseDao.putUseCancel(reqstId, TspCode.reqstSttus.CANCEL.toString());
    }

    /** 상세 사용종료 */
    public void putUseEnduse(String reqstId) {
        MyUseDetailParam dto = getUseDetail(reqstId);
        if (!dto.getReqstSttus().equals(TspCode.reqstSttus.APPROVE.toString())){
            throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "사용상태","사용상태"));
        }
        if (!dto.getUseSttus().equals(TspCode.eqpmnUsage.USE.toString())){
            throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "사용상태","사용상태"));
        }
        tsptFrontMyUseDao.putUseEnduse(reqstId, TspCode.eqpmnUsage.END_USE.toString());
    }

    /** 상세 기간연장내역 */
    public List<FrontMyUseExtndListDto> getUseExtndList(String reqstId){
        List<FrontMyUseExtndListDto> list = tsptFrontMyUseDao.getUseExtndList(reqstId);
        if (!(list.size() == 0)) {
            if (!list.get(0).isTkoutAt()) {
                throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "반출상태", TspCode.eqpmnTkoutSttus.NOT_TAKEN.getTitle()));
            }
//        dto.setUsgtm(Math.round(dto.getUsgtm()/60));
//        dto.setExpectUsgtm(Math.round(dto.getExpectUsgtm()/60));
            if (list.size() > 0) {
                for (int i = 0; list.size() > i; i++) {
                    list.get(i).setUsgtm(Math.round(list.get(i).getUsgtm() / 60));
                    list.get(i).setExpectUsgtm(Math.round(list.get(i).getExpectUsgtm() / 60));
                }
            }
            return list;
        }
        return list;
    }

    /** 상세 기간연장 조회 */
    public FrontMyUseExtndCalenDto getUseExtndReqst(String reqstId, String searchMonth){

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        // 해당 월 조회
        if ( CoreUtils.string.isBlank(searchMonth) ) {
            searchMonth = String.valueOf((now.getYear() + 1900) * 100 + now.getMonth() + 1);
        }
        String eqpmnId = tsptFrontMyUseDao.getEqpmnId(reqstId);
        List<List<Object>> listParam = tsptFrontMyUseDao.getUseExtndReqst(reqstId, TspCode.reqstSttus.APPROVE.toString(), eqpmnId, searchMonth);
        List<String> list = (ArrayList)listParam.get(1);

        FrontMyUseExtndCalenDto param = new FrontMyUseExtndCalenDto();
        param.setMyUseExtndReqstDt((ArrayList)listParam.get(2));
        param.setUseBeginDt(param.getMyUseExtndReqstDt().get(0).getUseBeginDt());
        param.setUseEndDt(param.getMyUseExtndReqstDt().get(0).getUseEndDt());

        param.setMyUseExtndReqstDt((ArrayList)listParam.get(0));
        for (int i = 0; param.getMyUseExtndReqstDt().size()>i; i++) {
            cal1.setTime(param.getMyUseExtndReqstDt().get(i).getUseBeginDt());
            cal2.setTime(param.getMyUseExtndReqstDt().get(i).getUseEndDt());
            String notReservation = CoreUtils.date.format(param.getMyUseExtndReqstDt().get(i).getUseBeginDt(), "yyyyMMdd");
            while (cal1.before(cal2)) {
                list.add(notReservation);
                cal1.add(Calendar.DATE, 1);
                notReservation = CoreUtils.date.format(cal1.getTime(), "yyyyMMdd");
            }
        }

        // 중복 날짜 제거
        Set<String> set = new HashSet<>(list);
        List<String> newList = new ArrayList<>(set);
        newList.remove(param.getUseEndDt());
        param.setNotReservation(newList);
        // 결과보고서에 필요.
        param.setMyUseExtndReqstDt((ArrayList)listParam.get(2));
        return param;
    }

    /** 상세 기간연장 신청 전 조회 */
    public MyUseExtndParam getUseExtndReqst(String reqstId, MyUseExtndReqstParam myUseExtndReqstParam){
        MyUseExtndParam param = tsptFrontMyUseDao.getUseExtndReqstInfo(reqstId);
        if (!param.getUseSttus().equals(TspCode.eqpmnUsage.USE.toString())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.허용불가, "장비사용상태","장비사용상태"));
        }
        // 예상금액 계산
        CommonCalcParam calc = commonService.getWorkingRntfee(param.getEqpmnId(), myUseExtndReqstParam.getUseBeginDt(), myUseExtndReqstParam.getUseEndDt(), param.isTkoutAt());
        CoreUtils.property.copyProperties(param, myUseExtndReqstParam);
        param.setRntfeeHour(calc.getRntfeeHour());
        param.setUsgtm(calc.getTotalMin());
        param.setExpectUsgtm(calc.getTotalMin());
        param.setExpectRntfee(calc.getRntfee());
        param.setUsefulHour(calc.getUsefulHour());
        param.setUsefulBeginHour(calc.getUsefulBeginHour());
        param.setUsefulEndHour(calc.getUsefulEndHour());
        param.setDscntAmount(calc.getRntfee() * param.getDscntRate() / 100);
        param.setRntfee(calc.getRntfee() - param.getDscntAmount());
        return param;
//        return postUseExtndReqst(reqstId, param);
    }

    /** 상세 기간연장 신청 */
    public void postUseExtndReqst(String reqstId, MyUseExtndParam param){
        // 로그인 계정 셋팅
        BnMember worker = TspUtils.getMember();
        TsptEqpmnExtn tsptEqpmnExtn = TsptEqpmnExtn.builder()
                .etReqstId(CoreUtils.string.getNewId("etReqst-"))
                .reqstId(reqstId)
                .reqstSttus(TspCode.reqstSttus.APPLY.toString())
                .useBeginDt(param.getUseBeginDt())
                .useEndDt(param.getUseEndDt())
                .rntfee(param.getRntfee())
                .dscntId(param.getDscntId())
                .usgtm(param.getUsgtm())
                .expectUsgtm(param.getExpectUsgtm())
                .dscntAmount(param.getDscntAmount())
                .expectRntfee(param.getExpectRntfee())
//                .creatrId("User006")
//                .updusrId("User006")
                .creatrId(worker.getMemberId())
                .updusrId(worker.getMemberId())
                .build();
        tsptFrontMyUseDao.postUseExtndReqst(tsptEqpmnExtn);
        param.setUsgtm(Math.round(param.getUsgtm()/60));
        param.setExpectUsgtm(Math.round(param.getExpectUsgtm()/60));
//        return param;

    }

    /** 상세 사용료부과내역 */
    public FrontMyUseExtndRntfeeDto getUseRntfee(String reqstId){
        FrontMyUseExtndRntfeeDto param = new FrontMyUseExtndRntfeeDto();
        param.setMyRntfee(tsptFrontMyUseDao.getUseRntfee(reqstId, TspCode.reqstSttus.APPROVE.toString()));
        param.setMyAddRntfee(tsptFrontMyUseDao.getUseAddRntfee(reqstId, TspCode.reqstSttus.APPROVE.toString()));
        return param;
    }

    /** 상세 결과보고서 */
    public FrontMyUseReprtDto getUseReprt(String reqstId){
        FrontMyUseReprtDto param = tsptFrontMyUseDao.getUseReprtInfo(reqstId);
        if (CoreUtils.string.isNotBlank(param.getReprtId())){
            param.setMyUseReprt(tsptFrontMyUseDao.getUseReprt(param.getReprtId()));
            if (CoreUtils.string.isNotBlank(param.getMyUseReprt().getAtchmnflGroupId())) {
                param.getMyUseReprt().setMyAttachMentParam(tsptFrontMyUseDao.selectMyUseDetailAttachMentParam(param.getMyUseReprt().getAtchmnflGroupId()));
            }
        }

        param.setMyUseExtndReqstDt(getUseExtndReqst(reqstId,"").getMyUseExtndReqstDt());
        param.setUseBeginDt(param.getMyUseExtndReqstDt().get(0).getUseBeginDt());
        param.setUseEndDt(param.getMyUseExtndReqstDt().get(param.getMyUseExtndReqstDt().size() - 1).getUseEndDt());
        return param;
    }

    /** 상세 결과보고서 임시저장_추가 */
    public FrontMyUseReprtDto postUseReprt(String reqstId, FrontMyUseReprtDto.MyUseReprt myUseReprt, String reprtSttus){
        // 로그인 계정 셋팅
        BnMember worker = TspUtils.getMember();
        TsptEqpmnReprt tsptEqpmnReprt = new TsptEqpmnReprt();
        CoreUtils.property.copyProperties(tsptEqpmnReprt, myUseReprt);
        tsptEqpmnReprt.setReqstId(reqstId);
        tsptEqpmnReprt.setReprtSttus(reprtSttus);
        if (CoreUtils.string.isBlank(myUseReprt.getReprtId())) {
            tsptEqpmnReprt.setReprtId(CoreUtils.string.getNewId("reprt-"));
            tsptEqpmnReprt.setCreatrId(worker.getMemberId());
            tsptEqpmnReprt.setUpdusrId(worker.getMemberId());
            tsptFrontMyUseDao.postUseReprt(tsptEqpmnReprt);
        }else {

            tsptEqpmnReprt.setUpdusrId(worker.getMemberId());
            tsptEqpmnReprt.setUpdtDt(now);
            tsptFrontMyUseDao.putUseReprt(tsptEqpmnReprt);
        }
        FrontMyUseReprtDto frontMyUseReprtDto =  getUseReprt(reqstId);
        return frontMyUseReprtDto;
    }

}
