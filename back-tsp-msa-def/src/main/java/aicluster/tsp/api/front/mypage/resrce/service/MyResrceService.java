package aicluster.tsp.api.front.mypage.resrce.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.front.mypage.resrce.param.MyResrceDetailParam;
import aicluster.tsp.api.front.mypage.resrce.param.MyResrceListParam;
import aicluster.tsp.api.front.mypage.resrce.param.MyResrceSearchParam;
import aicluster.tsp.common.dao.TsptFrontMyResrceDao;
import aicluster.tsp.common.entity.TsptApplcnt;
import aicluster.tsp.common.entity.TsptResrceUseReqst;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyResrceService {

    @Autowired
    private TsptFrontMyResrceDao tsptFrontMyResrceDao;

    public CorePagination<MyResrceListParam> getResrceList(MyResrceSearchParam param, CorePaginationParam cpParam){
        BnMember worker = TspUtils.getMember();
        param.setMberId(worker.getMemberId());
        // 전체 건수 확인
        long totalItems = tsptFrontMyResrceDao.getResrceCnt(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<TsptResrceUseReqst> tsptResrceUseReqst = tsptFrontMyResrceDao.getResrceList(info.getBeginRowNum(), cpParam.getItemsPerPage(), param);
        List<MyResrceListParam> list = new ArrayList<>();
        for (int i = 0; tsptResrceUseReqst.size()>i; i++){
            list.add(MyResrceListParam.builder()
                            .reqstId(tsptResrceUseReqst.get(i).getReqstId())
                            .reqstSttus(tsptResrceUseReqst.get(i).getReqstSttus())
                            .useSttus(tsptResrceUseReqst.get(i).getUseSttus())
                            .gpuAt(tsptResrceUseReqst.get(i).isGpuAt())
                            .cpuCo(tsptResrceUseReqst.get(i).getCpuCo())
                            .dataStorgeAt(tsptResrceUseReqst.get(i).isDataStorgeAt())
                            .rsndqf(tsptResrceUseReqst.get(i).getRsndqf())
                            .useBeginDt(tsptResrceUseReqst.get(i).getUseBeginDt())
                            .useEndDt(tsptResrceUseReqst.get(i).getUseEndDt())
                            .creatDt(tsptResrceUseReqst.get(i).getCreatDt())
                    .build());
        }

        CorePagination<MyResrceListParam> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    public MyResrceDetailParam getResrceDetail(String reqstId){
        TsptResrceUseReqst tsptResrceUseReqst = tsptFrontMyResrceDao.getResrceInfo(reqstId);
        TsptApplcnt tsptApplcnt = tsptFrontMyResrceDao.getTsptApplcnt(tsptResrceUseReqst.getApplcntId());

        MyResrceDetailParam param = new MyResrceDetailParam();
        param.setAttachMentParam(tsptFrontMyResrceDao.selectMyResrceDetailAttachMentParam(tsptResrceUseReqst.getAtchmnflGroupId()));
        param.setRceptNo(tsptResrceUseReqst.getRceptNo());
        param.setCreatDt(tsptResrceUseReqst.getCreatDt());
        param.setUseSttus(tsptResrceUseReqst.getUseSttus());
        param.setReqstSttus(tsptResrceUseReqst.getReqstSttus());
        param.setUseEndDt(tsptResrceUseReqst.getUseEndDt());
        param.setUseRturnDt(tsptResrceUseReqst.getUseRturnDt());
        param.setMberDiv(tsptApplcnt.getMberDiv());
        param.setEntrprsNm(tsptApplcnt.getEntrprsNm());
        param.setUserNm(tsptApplcnt.getUserNm());
        param.setOfcps(tsptApplcnt.getOfcps());
        param.setCttpc(tsptApplcnt.getCttpc());
        param.setEmail(tsptApplcnt.getEmail());
        param.setPartcptnDiv(tsptApplcnt.getPartcptnDiv());
        param.setGpuAt(tsptResrceUseReqst.isGpuAt());
        param.setCpuCo(tsptResrceUseReqst.getCpuCo());
        param.setDataStorgeAt(tsptResrceUseReqst.isDataStorgeAt());
        param.setUseprps(tsptResrceUseReqst.getUseprps());
        param.setAtchmnflGroupId(tsptResrceUseReqst.getAtchmnflGroupId());
        param.setPartcptnDiv(tsptResrceUseReqst.getPartcptnDiv());
        return param;
    }

    public void putResrceDetail(String reqstId, String sttus){
        MyResrceDetailParam param = getResrceDetail(reqstId);
        if (CoreUtils.string.equals(param.getReqstSttus(), TspCode.reqstSttus.APPROVE.toString())){
            if (CoreUtils.string.equals(param.getUseSttus(), TspCode.reqUsage.USE.toString())){
                throw new InvalidationException(String.format(TspCode.validateMessage.허용불가,"상태","상태"));
            }
        }
        if (CoreUtils.string.equals(param.getReqstSttus(), TspCode.reqstSttus.REJECT.toString())){
            throw new InvalidationException(String.format(TspCode.validateMessage.허용불가,"상태","상태"));
        }
        tsptFrontMyResrceDao.putResrceDetail(reqstId, sttus);
    }
}
