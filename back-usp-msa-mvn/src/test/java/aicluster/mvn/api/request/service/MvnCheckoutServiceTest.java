package aicluster.mvn.api.request.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.security.Code;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.company.dto.MvnCmpnyAllocateParam;
import aicluster.mvn.api.company.dto.MvnCmpnyCheckoutParam;
import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyPrcsDto;
import aicluster.mvn.api.company.service.MvnCmpnyInfService;
import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.api.request.dto.MvnCheckoutListParam;
import aicluster.mvn.api.request.dto.MvnCheckoutStatusParam;
import aicluster.mvn.common.dto.CheckoutReqDto;
import aicluster.mvn.common.dto.CheckoutReqListItemDto;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import aicluster.mvn.common.entity.UsptMvnChcktHist;
import aicluster.mvn.common.entity.UsptMvnChcktReqst;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MvnCheckoutServiceTest extends TestServiceSupport {
	@Autowired
	private MvnCheckoutService service;
	@Autowired
	private MvnCmpnyInfService cmpnyService;
    @Autowired
    MvnFcService mvnFcService;
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

	@BeforeAll
    void init() {
        setTokenMemberType(Code.memberType.내부사용자);
    }

	@Test
	void test() {
		// 입주업체 세팅
		setCmpny();

		// 일반회원 처리(1)
		user1Test();

		// 업무담당자 처리(1)
		admin1Test();

		// 일반회원 처리(2)
		user2Test();

		// 업무담당자 처리(2)
		admin2Test();
	}

	void setCmpny() {
		System.out.println("##################################### [ setCmpny(1) ] ################################################################");
		BnMember worker = SecurityUtils.getCurrentMember();
		System.out.println(worker.toString());

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

	void user1Test() {
		System.out.println("##################################### [ userTest(1) ] ################################################################");
		login(false);
		BnMember worker = SecurityUtils.getCurrentMember();
		System.out.println(worker.toString());

		// 퇴실신청 등록(add)
		UsptMvnChcktReqst insChkoutReq = service.add(MvnCmpnyCheckoutParam.builder().mvnId(mvnCompany.getMvnId()).checkoutPlanDay(string.toDate("20220501")).checkoutReason("퇴실신청 테스트 등록").build());

		// 퇴실신청 조회
		UsptMvnChcktReqst selChkoutReq = service.getUser(mvnCompany.getMvnId());

		assertNotEquals("등록한 정보와 조회한 정보가 다릅니다.", insChkoutReq.getCheckoutReqId(), selChkoutReq.getCheckoutReqId());

		// 퇴실신청 신청취소(modifyStatus)
		MvnCheckoutStatusParam chcktSttusParam1 = MvnCheckoutStatusParam.builder()
													.checkoutReqId(selChkoutReq.getCheckoutReqId())
													.checkoutReqSt(CodeExt.checkoutReqSt.신청취소)
													.build();
		service.modifyStatus(chcktSttusParam1);
		selChkoutReq = service.getUser(mvnCompany.getMvnId());
		System.out.println(String.format("신청취소 결과 : [%s]", selChkoutReq.toString()));

		// 퇴실신청 수정(modify)
		UsptMvnChcktReqst uptChkoutReq = service.modify( MvnCmpnyCheckoutParam.builder().checkoutReqId(selChkoutReq.getCheckoutReqId()).checkoutPlanDay(string.toDate("20220502")).checkoutReason("퇴실신청 테스트 수정(1)").build() );
		System.out.println(String.format("수정한 결과 : [%s]", uptChkoutReq.toString()));
	}

	void admin1Test() {
		System.out.println("##################################### [ adminTest(1) ] ################################################################");
		login(true);
		BnMember worker = SecurityUtils.getCurrentMember();
		System.out.println(worker.toString());

		// 퇴실신청 목록(getList)
		CorePagination<CheckoutReqListItemDto> list = service.getList( new MvnCheckoutListParam(), new CorePaginationParam() );
        System.out.println(String.format("현재 페이지 \t: [%d]", list.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", list.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t: [%d]", list.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", list.getList().toString()));

		// 퇴실신청 상세조회(get)
        Random random = new Random();
        int selIdx;
        if (list.getItemsPerPage() > list.getTotalItems()) {
            selIdx = random.nextInt(list.getTotalItems().intValue());
        }
        else {
            selIdx = random.nextInt(list.getItemsPerPage().intValue());
        }
        String selChkoutId = list.getList().get(selIdx).getCheckoutReqId();
        CheckoutReqDto chkoutReq = service.get(selChkoutId);
        System.out.println(String.format("단건조회결과 \t: [%s]", chkoutReq.toString()));

		// 퇴실신청 보완(modifyStatus)
		MvnCheckoutStatusParam chcktSttusParam2 = MvnCheckoutStatusParam.builder()
													.checkoutReqId(selChkoutId)
													.checkoutReqSt(CodeExt.checkoutReqSt.보완)
													.makeupReqCn("보완요청 내용입니다.")
													.build();
		service.modifyStatus(chcktSttusParam2);
		chkoutReq = service.get(selChkoutId);
		System.out.println(String.format("보완요청 결과 : [%s]", chkoutReq.toString()));
	}

	void user2Test() {
		System.out.println("##################################### [ userTest(2) ] ################################################################");
		login(false);
		BnMember worker = SecurityUtils.getCurrentMember();
		System.out.println(worker.toString());

		// 현재 유효한 퇴실신청 정보(getUser)
		UsptMvnChcktReqst selChkoutReq = service.getUser(mvnCompany.getMvnId());
        System.out.println(String.format("사용자신청현황 \t: [%s]", selChkoutReq.toString()));

		// 퇴실신청 수정(modify)
		UsptMvnChcktReqst uptChkoutReq = service.modify( MvnCmpnyCheckoutParam.builder().checkoutReqId(selChkoutReq.getCheckoutReqId()).checkoutPlanDay(string.toDate("20220502")).checkoutReason("보완요청 내용 수정").build() );
		System.out.println(String.format("보완수정한 결과 : [%s]", uptChkoutReq.toString()));
	}

	void admin2Test() {
		System.out.println("##################################### [ adminTest(2) ] ################################################################");
		login(true);
		BnMember worker = SecurityUtils.getCurrentMember();
		System.out.println(worker.toString());

		// 퇴실신청 목록(getList)
		CorePagination<CheckoutReqListItemDto> list = service.getList( new MvnCheckoutListParam(), new CorePaginationParam());
        System.out.println(String.format("현재 페이지 \t: [%d]", list.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", list.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t: [%d]", list.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", list.getList().toString()));

		// 퇴실신청 상세조회(get)
        Random random = new Random();
        int selIdx;
        if (list.getItemsPerPage() > list.getTotalItems()) {
            selIdx = random.nextInt(list.getTotalItems().intValue());
        }
        else {
            selIdx = random.nextInt(list.getItemsPerPage().intValue());
        }
        String selChkoutId = list.getList().get(selIdx).getCheckoutReqId();
        CheckoutReqDto chkoutReq = service.get(selChkoutId);
        System.out.println(String.format("단건조회결과 \t: [%s]", chkoutReq.toString()));

		// 퇴실신청 승인
		MvnCheckoutStatusParam chcktSttusParam3 = MvnCheckoutStatusParam.builder()
													.checkoutReqId(selChkoutId)
													.checkoutReqSt(CodeExt.checkoutReqSt.승인)
													.equipRtdtl("책상3, 의자3")
													.build();
		service.modifyStatus(chcktSttusParam3);
		chkoutReq = service.get(selChkoutId);
		System.out.println(String.format("승인처리 결과 : [%s]", chkoutReq.toString()));

		// 퇴실신청 이력
		JsonList<UsptMvnChcktHist> histList = service.getHist(selChkoutId);
		System.out.println(json.toString(histList));
	}
}
