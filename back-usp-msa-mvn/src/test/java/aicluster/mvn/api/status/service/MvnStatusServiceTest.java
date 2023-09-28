package aicluster.mvn.api.status.service;

import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.security.Code;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.company.dto.MvnCmpnyAllocateParam;
import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyPrcsDto;
import aicluster.mvn.api.company.service.MvnCmpnyInfService;
import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.api.status.dto.MvnStatusListParam;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import aicluster.mvn.common.dto.MvnFcStatsListItemDto;
import aicluster.mvn.common.dto.UserMvnCmpnyDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MvnStatusServiceTest extends TestServiceSupport {
	@Autowired
	private MvnStatusService service;
	@Autowired
	private MvnCmpnyInfService cmpnyService;
	@Autowired
	private MvnFcService mvnFcService;
	@Autowired
	private FwUserDao userDao;

	private UsptMvnEntrpsInfo mvnCompany;

	private void login(boolean adminYn) {
		if (BooleanUtils.isTrue(adminYn)) {
			resetAccessToken("insider-test", Code.memberType.내부사용자);
		}
		else {
			UserDto user = userDao.select(mvnCompany.getCmpnyId());
			resetAccessToken(user.getMemberId(), user.getMemberType());
		}
	}

	private void mvnAllocate() {
		// 데이터 생성
        MvnCmpnyPrcsDto prcsDto = cmpnyService.add();
        System.out.println(String.format("동기화 수행건 : [%d]", prcsDto.getPrcsCnt()));

        // 목록조회
        CorePagination<MvnCmpnyListItemDto> cmpnyList = cmpnyService.getList(MvnCmpnyListParam.builder().build(), new CorePaginationParam());
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
        this.mvnCompany = cmpnyService.get(selMvnId);
        System.out.println(String.format("단건조회결과 \t: [%s]", this.mvnCompany.toString()));

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
        cmpnyService.modifyAllocate(new MvnCmpnyAllocateParam(this.mvnCompany.getMvnId(), mvnOffice.getMvnFcId(), string.toDate("20220429"), string.toDate(this.mvnCompany.getMvnEndDay()), "책상4, 의자4"));
        this.mvnCompany = cmpnyService.get(selMvnId);
        mvnOffice = mvnFcService.get(mvnOffice.getMvnFcId());
        System.out.println(String.format("배정결과 \t: \n입주업체 [%s] \n입주시설 [%s]", this.mvnCompany.toString(), mvnOffice.toString()));
	}

	@Test
	@Order(0)
	void test1() {
		// 업무담당자 로그인
		login(true);

		// 입주배정 수행
		mvnAllocate();

		// 입주현황 목록조회
		CorePagination<MvnFcStatsListItemDto> list = service.getList( MvnStatusListParam.builder().build(), new CorePaginationParam() );
        System.out.println(String.format("현재 페이지 \t: [%d]", list.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", list.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t: [%d]", list.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", list.getList().toString()));

        assertNotNull(list);
	}

	@Test
	@Order(1)
	void test2() {
		// 업무담당자 로그인
		login(true);

		// 입주배정 수행
		mvnAllocate();

		// 입주업체 로그인
		login(false);

		// 입주업체 로그인 정보
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		System.out.println(worker.toString());

		// 입주업체 정보 조회
		UserMvnCmpnyDto userMvn = service.getUser(null);
		System.out.println(String.format("조회 결과 : [%s]", userMvn.toString()));

		assertNotNull(userMvn);
	}
}
