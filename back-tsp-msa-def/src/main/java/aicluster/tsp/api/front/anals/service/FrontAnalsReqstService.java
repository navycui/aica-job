package aicluster.tsp.api.front.anals.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.api.front.anals.param.FrontAnalsReqstParam;
import aicluster.tsp.api.front.anals.param.FrontAnalsTmplatParam;
import aicluster.tsp.common.dao.TsptFrontAnalsReqstDao;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FrontAnalsReqstService {
    public static final long ITEMS_PER_PAGE = 5;

    @Autowired
    private TsptFrontAnalsReqstDao tsptFrontAnalsReqstDao;

    public void postAnals(FrontAnalsReqstParam param){

        BnMember worker = TspUtils.getMember();

        Long enabledCount = tsptFrontAnalsReqstDao.selectAnalsReqst(param);

        // 테스트를 위해 제거
//        if(enabledCount < 1){
//            // 사용가능한 리소스 부족
//            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
//        }
        //param.setReqstId(CoreUtils.string.getNewId("reqst-"));
        param.setUseSttus(TspCode.analsUseSttus.APPLY.toString());
        param.setCreatrId(worker.getMemberId());

        tsptFrontAnalsReqstDao.insertAnalsReqst(param);
    }

    // 예상 사용금액
//    public UseReqstEstmtRntfeeParam getExpectRntfee(UseReqstEstmtParam param){
//        CommonCalcParam calc = commonService.getWorkingRntfee(param.getEqpmnId(), param.getUseBeginDt(), param.getUseEndDt(), param.getTkoutAt());
//
//        UseReqstEstmtRntfeeParam result = new UseReqstEstmtRntfeeParam();
//        // 1시간 사용료
//        result.setRntfeeHour(calc.getRntfeeHour());
//        // 1일 가용시간
//        result.setUsefulHour(calc.getUsefulEndHour() - calc.getUsefulBeginHour());
//        // 예상 사용료
//        result.setExpectRntfee(calc.getRntfee());
//        // 총 사용시간
//        result.setExpectUsgtm(calc.getTotalHrs());
//
//        return result;
//    }
//
//
//
    public CommonReturnList<FrontAnalsTmplatParam> getAnalsTmplat(FrontAnalsTmplatParam param){

        List<FrontAnalsTmplatParam> data = tsptFrontAnalsReqstDao.selectAnalsTmplat(param);

        return new CommonReturnList<>(data);

    }

}
