package aicluster.tsp.api.admin.eqpmn.dscnt.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.admin.eqpmn.dscnt.param.DscntParam;
import aicluster.tsp.common.dao.TsptEqpmnDscntDao;
import aicluster.tsp.common.entity.TsptEqpmnDscnt;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DscntService {

    public static final long ITEMS_PER_PAGE = 5;

    @Autowired
    private TsptEqpmnDscntDao tsptEqpmnDscntDao;

    //할인등록
    public TsptEqpmnDscnt postDscntList(DscntParam param) {
        // 로그인 사용자 정보 추출
        BnMember worker = TspUtils.getMember();
        TsptEqpmnDscnt tsptEqpmnDscnt = new TsptEqpmnDscnt();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        Date now = new Date();
        tsptEqpmnDscnt = TsptEqpmnDscnt.builder()
                .dscntId(CoreUtils.string.getNewId("dscnt-"))
                .dscntResn(param.getDscntResn())
                .dscntRate(param.getDscntRate())
                .useSttus(param.getUseSttus())
                .creatrId(worker.getMemberId())
                .updusrId(worker.getMemberId())
                .build();

        if(CoreUtils.string.isNotBlank(tsptEqpmnDscnt.getDscntId())) {
            if(CoreUtils.string.isBlank(tsptEqpmnDscnt.getDscntResn())) {
                inputValidateErrs.add("dscntResn", String.format(TspCode.validateMessage.입력없음, "할인사유"));
            }else if(tsptEqpmnDscnt.getDscntRate()>100||tsptEqpmnDscnt.getDscntRate()<0){
                inputValidateErrs.add("dscntRate", String.format(TspCode.validateMessage.허용불가, "할인율", "할인율"));
            }

            if (inputValidateErrs.size() > 0) {
                throw inputValidateErrs;
            }else {
                tsptEqpmnDscntDao.postDscntList(tsptEqpmnDscnt);
            }
        }
        return tsptEqpmnDscnt;
    }
    //할인조회
    public CorePagination<DscntParam> getDscntList(DscntParam search, CorePaginationParam corePaginationParam){

        if (CoreUtils.string.isBlank(corePaginationParam.getPage().toString()) || corePaginationParam.getPage() < 1)
            corePaginationParam.setPage(1L);
        if (CoreUtils.string.isBlank(corePaginationParam.getItemsPerPage().toString()) || corePaginationParam.getItemsPerPage() < 1)
            corePaginationParam.setItemsPerPage(ITEMS_PER_PAGE);

        long totalItems = tsptEqpmnDscntDao.selectDscntCount(search);

        CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);
        Long beginRowNum = info.getBeginRowNum();

        List<DscntParam> list = tsptEqpmnDscntDao.selectDscntList(search, beginRowNum, corePaginationParam.getItemsPerPage());
        CorePagination<DscntParam> pagination = new CorePagination<>(info, list);

        return pagination;
    }

    //할인검색
    public CorePagination<DscntParam> getDscntApplyList(DscntParam apply, CorePaginationParam corePaginationParam){

        if (CoreUtils.string.isBlank(corePaginationParam.getPage().toString()) || corePaginationParam.getPage() < 1)
            corePaginationParam.setPage(1L);
        if (CoreUtils.string.isBlank(corePaginationParam.getItemsPerPage().toString()) || corePaginationParam.getItemsPerPage() < 1)
            corePaginationParam.setItemsPerPage(ITEMS_PER_PAGE);

        long totalItems = tsptEqpmnDscntDao.selectApplyCount(apply);

        CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);
        Long beginRowNum = info.getBeginRowNum();

        List<DscntParam> list = tsptEqpmnDscntDao.selectApplyList(apply, beginRowNum, corePaginationParam.getItemsPerPage());
        CorePagination<DscntParam> pagination = new CorePagination<>(info, list);
        return pagination;
    }

    // 사용상태 수정
    public List<DscntParam> updateUsgstt(List<DscntParam> dscntParam)
    {
        // 로그인 사용자 정보 추출
        BnMember worker = TspUtils.getMember();
        List<DscntParam> updateParam = new ArrayList<>();
        Date now = new Date();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        for(DscntParam param : dscntParam)
        {
           if(CoreUtils.string.isBlank(param.getDscntId()))
                inputValidateErrs.add("dscntId",String.format(TspCode.validateMessage.허용불가, "할인ID", "할인ID"));
           else {
               param.setUseSttus(param.getUseSttus());
               param.setUpdusrId(worker.getMemberId());
               param.setUpdtDt(now);
               updateParam.add(param);
               tsptEqpmnDscntDao.updateUsgstt(param);
           }
        }
        return updateParam;
    }

}
