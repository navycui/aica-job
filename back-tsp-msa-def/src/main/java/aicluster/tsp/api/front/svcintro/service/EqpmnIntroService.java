package aicluster.tsp.api.front.svcintro.service;

import aicluster.tsp.api.front.svcintro.param.EqpmnIntroDetailParam;
import aicluster.tsp.api.front.svcintro.param.EqpmnIntroParam;
import aicluster.tsp.api.front.svcintro.param.EqpmnIntroSearchParam;
import aicluster.tsp.common.dao.TsptFrontEqpmnDao;
import aicluster.tsp.common.util.TspCode;
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
public class EqpmnIntroService {

    public static final long ITEMS_PER_PAGE = 5;


    @Autowired
    TsptFrontEqpmnDao tsptFrontEqpmnDao;
    public CorePagination<EqpmnIntroParam> getEqpmnInfo(CorePaginationParam cpParam, EqpmnIntroSearchParam search)
    {
//        BnMember worker = TspUtils.getMember();

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(search.getEqpmnNmKorean()) && search.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        if (CoreUtils.string.isBlank(cpParam.getPage().toString()) || cpParam.getPage() < 1)
            cpParam.setPage(1L);
        if (CoreUtils.string.isBlank(cpParam.getItemsPerPage().toString()) || cpParam.getItemsPerPage() < 1)
            cpParam.setItemsPerPage(ITEMS_PER_PAGE);

        long totalItems = tsptFrontEqpmnDao.selectEqpmnInfoCount(search);

        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);
        Long beginRowNum = info.getBeginRowNum();

        List<EqpmnIntroParam> list = tsptFrontEqpmnDao.selectEqpmnInfoList(search, beginRowNum, cpParam.getItemsPerPage(), false);
        CorePagination<EqpmnIntroParam> pagination = new CorePagination<>(info, list);

        return pagination;
    }

    public EqpmnIntroDetailParam getEqpmnInfoDetail(String eqpmnId)
    {
//        BnMember worker = TspUtils.getMember();
        EqpmnIntroDetailParam data = tsptFrontEqpmnDao.selectEqpmnInfoDetail(eqpmnId);

        if(data == null){
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "장비"));
        }
        return data;
    }
}
