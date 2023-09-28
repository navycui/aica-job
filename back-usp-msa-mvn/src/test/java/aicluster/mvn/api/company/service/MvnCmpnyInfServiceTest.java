package aicluster.mvn.api.company.service;

import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.security.Code;
import aicluster.mvn.api.company.dto.MvnCmpnyAllocateParam;
import aicluster.mvn.api.company.dto.MvnCmpnyCheckoutParam;
import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyPrcsDto;
import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.common.dao.UsptMvnChcktReqstDao;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import aicluster.mvn.common.entity.UsptMvnChcktReqst;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

public class MvnCmpnyInfServiceTest extends TestServiceSupport {

    @Autowired
    MvnCmpnyInfService service;
    @Autowired
    MvnFcService mvnFcService;
    @Autowired
    UsptMvnChcktReqstDao checkoutReqDao;

    @BeforeAll
    void set() {
        setTokenMemberType(Code.memberType.내부사용자);
    }

    @Test
    void test() {
        // 데이터 생성
        MvnCmpnyPrcsDto prcsDto = service.add();
        System.out.println(String.format("동기화 수행건 : [%d]", prcsDto.getPrcsCnt()));

        // 목록조회
        CorePagination<MvnCmpnyListItemDto> cmpnyList = service.getList(MvnCmpnyListParam.builder().build(), new CorePaginationParam());
        System.out.println(String.format("현재 페이지 \t: [%d]", cmpnyList.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", cmpnyList.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t: [%d]", cmpnyList.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", cmpnyList.getList().toString()));

        if (cmpnyList.getTotalItems() <= 0) return;

        // 상세조회
        Random random1 = new Random();
        int selIdx;
        if (cmpnyList.getItemsPerPage() > cmpnyList.getTotalItems()) {
            selIdx = random1.nextInt(cmpnyList.getTotalItems().intValue());
        }
        else {
            selIdx = random1.nextInt(cmpnyList.getItemsPerPage().intValue());
        }
        String selMvnId = cmpnyList.getList().get(selIdx).getMvnId();
        UsptMvnEntrpsInfo selCmpny = service.get(selMvnId);
        System.out.println(String.format("단건조회결과 \t: [%s]", selCmpny.toString()));

        // 입주시설 사무실 조회
        Random random2 = new Random();
        CorePagination<UsptMvnFcltyInfo> officeList = mvnFcService.getEnableOfficeList(null, null, null, new CorePaginationParam());
        int allOfficeIdx;
        if (officeList.getItemsPerPage() > officeList.getTotalItems()) {
            allOfficeIdx = random2.nextInt(officeList.getTotalItems().intValue());
        }
        else {
            allOfficeIdx = random2.nextInt(officeList.getItemsPerPage().intValue());
        }
        UsptMvnFcltyInfo mvnOffice = officeList.getList().get(allOfficeIdx);
        System.out.println(String.format("선택된 입주시설(사무실) 정보 : \n[%s]", mvnOffice.toString()));

        // 입주배정
        Date now = new Date();
        service.modifyAllocate(new MvnCmpnyAllocateParam(selCmpny.getMvnId(), mvnOffice.getMvnFcId(), now, string.toDate(selCmpny.getMvnEndDay()), "책상4, 의자4"));
        selCmpny = service.get(selMvnId);
        mvnOffice = mvnFcService.get(mvnOffice.getMvnFcId());
        System.out.println(String.format("배정결과 \t: \n입주업체 [%s] \n입주시설 [%s]", selCmpny.toString(), mvnOffice.toString()));

        // 입주배정 알람
        MvnCmpnyPrcsDto alarmDto = service.alarm(array.toList(new String[]{selCmpny.getMvnId()}));
        System.out.println(String.format("알람 발송결과 \t: [%d]", alarmDto.getPrcsCnt()));

        // 퇴실처리
        Date checkoutPlanDt = date.addWeeks(string.toDate(selCmpny.getMvnBeginDay()), 0);  // 15주 후 날짜
        System.out.println(String.format("퇴실예정일 생성 결과 : [%s]=>[%s]", selCmpny.getMvnBeginDay(), date.format(checkoutPlanDt, "yyyyMMdd")));
        service.modifyCheckout( MvnCmpnyCheckoutParam.builder().mvnId(selCmpny.getMvnId()).checkoutPlanDay(checkoutPlanDt).checkoutReason("퇴실합니다.").equipRtdtl("책상4, 의자4").build() );
        selCmpny = service.get(selMvnId);
        mvnOffice = mvnFcService.get(mvnOffice.getMvnFcId());
        UsptMvnChcktReqst checkoutReq = checkoutReqDao.select_mvnId( selCmpny.getMvnId() );
        System.out.println(String.format("퇴실결과 \t: \n입주업체 [%s] \n입주시설 [%s] \n퇴실신청 [%s]", selCmpny.toString(), mvnOffice.toString(), checkoutReq.toString()));
    }

}
