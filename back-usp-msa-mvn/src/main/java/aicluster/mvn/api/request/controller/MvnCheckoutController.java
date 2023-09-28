package aicluster.mvn.api.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.company.dto.MvnCmpnyCheckoutParam;
import aicluster.mvn.api.request.dto.MvnCheckoutListParam;
import aicluster.mvn.api.request.dto.MvnCheckoutStatusParam;
import aicluster.mvn.api.request.service.MvnCheckoutService;
import aicluster.mvn.common.dto.CheckoutReqDto;
import aicluster.mvn.common.dto.CheckoutReqListItemDto;
import aicluster.mvn.common.entity.UsptMvnChcktHist;
import aicluster.mvn.common.entity.UsptMvnChcktReqst;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/checkouts")
public class MvnCheckoutController {
    @Autowired
    MvnCheckoutService service;

    /**
     * 퇴실신청 등록
     * (업체담당자가 퇴실신청한다.)
     *
     * @param param: 퇴실신청 등록정보 (checkoutPlanDay: 퇴실예정일, checkoutReason: 퇴실사유)
     * @return UsptCheckoutReq
     */
    @PostMapping("")
    UsptMvnChcktReqst add( @RequestBody MvnCmpnyCheckoutParam param )
    {
        param.setCheckoutReqId(null);

        return service.add(param);
    }

    /**
     * 퇴실신청 목록조회
     *
     * @param param : 조회조건 (checkoutReqSt:퇴실신청상태, bnoRoomNo:입주호실, memberNm:회원명(업체명))
     * @param pageParam : 페이징조건
     * @return CorePagination<CheckoutReqListItemDto>
     */
    @GetMapping("")
    CorePagination<CheckoutReqListItemDto> getList( MvnCheckoutListParam param, CorePaginationParam pageParam )
    {
        return service.getList( param, pageParam );
    }

    /**
     * 퇴실신청 상세조회
     *
     * @param checkoutReqId: 퇴실신청ID
     * @return 퇴실신청정보
     */
    @GetMapping("/{checkoutReqId}")
    CheckoutReqDto get(@PathVariable String checkoutReqId)
    {
        return service.get(checkoutReqId);
    }

    /**
     * 퇴실신청 상태변경
     *
     * @param checkoutReqId: 퇴실신청ID
     * @param checkoutReqSt: 퇴실신청상태
     * @param equipRtdtl: 장비제공내역(checkoutReqSt: 승인(APRV)인 경우 사용)
     * @param makeupReqCn: 보완요청내용(checkoutReqSt: 보완(SPLMNT)인 경우 사용)
     */
    @PutMapping("/{checkoutReqId}/update-status")
    void modifyStatus(@PathVariable String checkoutReqId, @RequestBody MvnCheckoutStatusParam param)
    {
    	param.setCheckoutReqId(checkoutReqId);
        service.modifyStatus(param);
    }

    /**
     * 퇴실신청이력 목록조회
     *
     * @param checkoutReqId: 퇴실신청ID
     * @return 퇴실신청이력 목록
     */
    @GetMapping("/{checkoutReqId}/hist")
    JsonList<UsptMvnChcktHist> getHistList(@PathVariable String checkoutReqId)
    {
        return service.getHist(checkoutReqId);
    }

    /**
     * 입주업체에 대한 퇴실신청정보조회
     * (입주업체의 업체담당자가 조회한다.)
     *
     * @param mvnId: 입주ID
     * @return UsptCheckoutReq
     */
    @GetMapping("/user/last-view/{mvnId}")
    UsptMvnChcktReqst getUser( @PathVariable String mvnId )
    {
        return service.getUser(mvnId);
    }

    /**
     * 퇴실신청정보 수정
     * (업체담당자가 퇴실신청정보를 수정한다.)
     *
     * @param checkoutReqId : 퇴실신청ID
     * @param param: 퇴실신청 등록정보 (checkoutPlanDay: 퇴실예정일, checkoutReason: 퇴실사유)
     * @return UsptCheckoutReq
     */
    @PutMapping("/user/{checkoutReqId}")
    UsptMvnChcktReqst modify( @PathVariable String checkoutReqId, @RequestBody MvnCmpnyCheckoutParam param )
    {
    	param.setCheckoutReqId(checkoutReqId);

        return service.modify(param);
    }
}
