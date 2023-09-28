package aicluster.tsp.api.front.usereqst.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.common.param.CommonCalcParam;
import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtRntfeeParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtSelectParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtUseDateParam;
import aicluster.tsp.common.dao.TsptFrontUseReqstDao;
import aicluster.tsp.common.dto.FrontEqpmnSelectDto;
import aicluster.tsp.common.dto.FrontEqpmnSelectListDto;
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

import java.util.List;

@Slf4j
@Service
public class FrontUseReqstService {
    public static final long ITEMS_PER_PAGE = 5;

    @Autowired
    private CommonService commonService;
    @Autowired
    private TsptFrontUseReqstDao tsptFrontUseReqstDao;

    public CorePagination<FrontEqpmnSelectListDto> getEstmtEqpmnList(UseReqstEstmtSelectParam param, CorePaginationParam cpParam){
        BnMember worker = TspUtils.getMember();

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(param.getEqpmnNmKorean()) && param.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        // 전체 건수 확인
        long totalItems =tsptFrontUseReqstDao.selectUseReqstEqpmnListCnt(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<FrontEqpmnSelectListDto> list = tsptFrontUseReqstDao.selectUseReqstEqpmnList(info.getBeginRowNum(), info.getItemsPerPage(), param);
        CorePagination<FrontEqpmnSelectListDto> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    public FrontEqpmnSelectDto getEstmtEqpmnInfo(String eqpmnId){
        FrontEqpmnSelectDto data = tsptFrontUseReqstDao.selectUseReqstEqpmnInfo(eqpmnId);

        if(data == null){
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "장비"));
        }

        // 1일 가용시간 계산
        data.setUsefulHour(commonService.getUseFulHour(data.getUsefulBeginHour(), data.getUsefulEndHour()).getUsefulHour());

        return data;
    }

    public void postUseReqst(UseReqstEstmtParam param){

        BnMember worker = TspUtils.getMember();

        // 예상사용료
        Integer expectRntfee = 0;
        CommonCalcParam calc = commonService.getWorkingRntfee(param.getEqpmnId(), param.getUseBeginDt(), param.getUseEndDt(), param.getTkoutAt());
        expectRntfee = calc.getRntfee();

        param.setEstmtId(CoreUtils.string.getNewId("reqst-"));
        param.setReqstSttus(TspCode.reqstSttus.APPLY.toString());
        param.setCreatrId(worker.getMemberId());
        param.setRntfee(expectRntfee);
        param.setExpectRntfee(expectRntfee);
        param.setExpectUsgtm(calc.getTotalMin());
        if (!param.getTkoutAt()) {
            param.setTkinAt(true);
        }

        tsptFrontUseReqstDao.insertUseReqst(param);
    }

    // 예상 사용금액
    public UseReqstEstmtRntfeeParam getExpectRntfee(UseReqstEstmtParam param){
        CommonCalcParam calc = commonService.getWorkingRntfee(param.getEqpmnId(), param.getUseBeginDt(), param.getUseEndDt(), param.getTkoutAt());

        UseReqstEstmtRntfeeParam result = new UseReqstEstmtRntfeeParam();
        // 1시간 사용료
        result.setRntfeeHour(calc.getRntfeeHour());
        // 1일 가용시간
        result.setUsefulHour(calc.getUsefulHour());
        // 예상 사용료
        result.setExpectRntfee(calc.getRntfee());
        // 총 사용시간
        result.setExpectUsgtm(calc.getTotalHrs());

        return result;
    }



    public CommonReturnList<UseReqstEstmtUseDateParam> getEqpmnUseDateList(UseReqstEstmtUseDateParam param){

        param.setReqstSttus(TspCode.reqstSttus.APPROVE.toString());
        List<UseReqstEstmtUseDateParam> list = tsptFrontUseReqstDao.selectEqpmnUseDateList(param);

        return new CommonReturnList<>(list);

    }

}
