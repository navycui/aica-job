package aicluster.tsp.api.front.mypage.anals.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.front.mypage.anals.param.FrontMypageAnalsReqstParam;
import aicluster.tsp.common.dao.TsptAnalsDao;
import aicluster.tsp.common.dao.TsptFrontAnalsReqstDao;
import aicluster.tsp.common.dto.FrontMyAnalsReqstDto;
import aicluster.tsp.common.entity.TsptAnalsUntReqstHist;
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
public class FrontMypageAnalsReqstService {
    @Autowired
    private TsptFrontAnalsReqstDao tsptFrontAnalsReqstDao;
    @Autowired
    private TsptAnalsDao tsptAnalsDao;


    public CorePagination<FrontMyAnalsReqstDto> getAnalsList(FrontMypageAnalsReqstParam param, CorePaginationParam cpParam){
        // 로그인 계정 셋팅
        BnMember worker = TspUtils.getMember();
        param.setCreatrId(worker.getMemberId());
        // 전체 건수 확인
        long totalItems = tsptFrontAnalsReqstDao.selectMyAnalsCnt(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<FrontMyAnalsReqstDto> list = tsptFrontAnalsReqstDao.selectMyAnalsList(info.getBeginRowNum(), cpParam.getItemsPerPage(), param);

        CorePagination<FrontMyAnalsReqstDto> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    public FrontMyAnalsReqstDto getAnalsDetail(String reqstId){
        FrontMyAnalsReqstDto dto = tsptFrontAnalsReqstDao.selectMyAnalsDetail(reqstId);

        return dto;
    }

    public void putUseCancel(String reqstId){
        BnMember worker = TspUtils.getMember();
        if (!getAnalsDetail(reqstId).getUseSttus().equals(TspCode.analsUseSttus.APPLY.toString())){
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "상태"));
        }
        tsptFrontAnalsReqstDao.updateMyAnalsCancel(reqstId, TspCode.analsUseSttus.CANCEL.toString(), worker.getMemberId());

        TsptAnalsUntReqstHist hist = TsptAnalsUntReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(reqstId)
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.analsUseSttus.CANCEL.toString())
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.valueOf(TspCode.analsUseSttus.CANCEL.toString()).getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptAnalsDao.insertAnalsReqstHist(hist);

    }
}
