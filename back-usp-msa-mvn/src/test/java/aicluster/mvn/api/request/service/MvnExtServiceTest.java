package aicluster.mvn.api.request.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.security.Code;
import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.api.company.service.MvnCmpnyInfService;
import aicluster.mvn.api.request.dto.MvnExtDlbrtParam;
import aicluster.mvn.api.request.dto.MvnExtListParam;
import aicluster.mvn.api.request.dto.MvnExtParam;
import aicluster.mvn.api.request.dto.MvnExtStatusParam;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import aicluster.mvn.common.dto.MvnEtReqDto;
import aicluster.mvn.common.dto.MvnEtReqListItemDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnEtHist;
import aicluster.mvn.common.entity.UsptMvnEtReqst;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

public class MvnExtServiceTest extends TestServiceSupport {

	@Autowired
	MvnExtService service;
	@Autowired
	MvnCmpnyInfService cmpnyService;
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

	@BeforeEach
	void init() {
		login(true);

		// 입주업체 목록 조회
        CorePagination<MvnCmpnyListItemDto> cmpnyList = cmpnyService.getList(MvnCmpnyListParam.builder().mvnCmpnySt(CodeExt.mvnCmpnySt.입주중).build(), new CorePaginationParam());
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
	}

	@Test
	void test() {
		// 업체담당자 입주연장신청
		userAdd();

		// 업무담당자 보완처리
		adminSplmnt();

		// 업체담당자 보완제출
		userSplmnt();

		// 업무담당자 심의결과 등록
		adminDlbrt();
	}

	private void userAdd()
	{
		login(false);

		UsptMvnEtReqst mvnEtReq = null;

		// 입주연장신청 등록
		MvnExtParam insParam = MvnExtParam.builder()
									.mvnId(mvnCompany.getMvnId())
									.mvnEtEndDay(date.addMonths(string.toDate(mvnCompany.getMvnEndDay()), 2))
									.mvnEtReqCn("연장신청 테스트입니다.")
									.build();

		mvnEtReq = service.add(insParam, null);
		System.out.println(String.format("등록결과 : [%s]", mvnEtReq.toString()));
		assertNotNull(mvnEtReq);

		// 신청취소 처리
		service.modifyState(new MvnExtStatusParam(mvnEtReq.getMvnEtReqId(), CodeExt.mvnEtReqSt.취소, null));
		mvnEtReq = service.getUser(mvnCompany.getMvnId());
		System.out.println(String.format("취소결과 : [%s]", mvnEtReq.toString()));
		assertNotNull(mvnEtReq);

		// 입주연장신청 수정
		MvnExtParam uptParam = MvnExtParam.builder()
									.mvnEtReqId(mvnEtReq.getMvnEtReqId())
									.mvnEtEndDay(date.addMonths(string.toDate(mvnCompany.getMvnEndDay()), 1))
									.mvnEtReqCn("연장신청 수정 테스트입니다.")
									.build();
		mvnEtReq = service.modify(uptParam, null);
		System.out.println(String.format("수정결과 : [%s]", mvnEtReq.toString()));
		assertNotNull(mvnEtReq);
	}

	private void adminSplmnt()
	{
		login(true);

		MvnEtReqDto viewDto = null;

		// 목록조회
		CorePagination<MvnEtReqListItemDto> list = service.getList(MvnExtListParam.builder().build(), new CorePaginationParam());
		System.out.println(String.format("목록조회결과(1) : [%s]", list.getList().toString()));
		assertTrue(list.getTotalItems() == 1L);
		assertTrue(list.getList().size() == 1L);

		MvnEtReqListItemDto mvnEtReqDto = list.getList().get(0);
		System.out.println(String.format("선택된 결과 : [%s]", mvnEtReqDto));
		viewDto = service.get(mvnEtReqDto.getMvnEtReqId());
		System.out.println(String.format("상세조회결과(1) : [%s]", viewDto.toString()));
		assertNotNull(viewDto);

		// 보완처리
		service.modifyState(new MvnExtStatusParam(viewDto.getMvnEtReqId(), CodeExt.mvnEtReqSt.보완, "보완요청내용"));
		viewDto = service.get(mvnEtReqDto.getMvnEtReqId());
		System.out.println(String.format("보완처리결과 : [%s]", viewDto.toString()));
		assertNotNull(viewDto);
	}

	private void userSplmnt()
	{
		login(false);

		UsptMvnEtReqst mvnEtReq = null;

		mvnEtReq = service.getUser(mvnCompany.getMvnId());
		System.out.println(String.format("최근조회결과 : [%s]", mvnEtReq.toString()));
		assertNotNull(mvnEtReq);

		// 입주연장신청 보완
		MvnExtParam uptParam = MvnExtParam.builder()
									.mvnEtReqId(mvnEtReq.getMvnEtReqId())
									.mvnEtEndDay(date.addMonths(string.toDate(mvnCompany.getMvnEndDay()), -1))
									.mvnEtReqCn("연장신청 보완 테스트입니다.")
									.build();
		mvnEtReq = service.modify(uptParam, null);
		System.out.println(String.format("보완결과 : [%s]", mvnEtReq.toString()));
		assertNotNull(mvnEtReq);
	}

	private void adminDlbrt()
	{
		login(true);

		MvnEtReqDto viewDto = null;

		// 목록조회
		CorePagination<MvnEtReqListItemDto> list = service.getList(MvnExtListParam.builder().build(), new CorePaginationParam());
		System.out.println(String.format("목록조회결과(2) : [%s]", list.getList().toString()));

		String mvnEtReqId = list.getList().get(0).getMvnEtReqId();
		viewDto = service.get(mvnEtReqId);
		System.out.println(String.format("상세조회결과(2) : [%s]", viewDto.toString()));

		// 접수완료
		service.modifyState(new MvnExtStatusParam(viewDto.getMvnEtReqId(), CodeExt.mvnEtReqSt.접수완료, null));
		viewDto = service.get(mvnEtReqId);
		System.out.println(String.format("접수처리결과 : [%s]", viewDto.toString()));
		assertNotNull(viewDto);

		// 심의결과 등록
		MvnExtDlbrtParam param = MvnExtDlbrtParam.builder()
										.mvnEtReqId(viewDto.getMvnEtReqId())
										.mvnEtReqSt(CodeExt.mvnEtReqSt.연장승인)
										.dlbrtDay(string.toDate("20220510"))
										.dlbrtAprvEndDay(date.addDays(string.toDate(viewDto.getMvnEtEndDay()), -10))
										.dlbrtResultDtlCn("신청일보다 10일 이전으로 연장승인되었습니다.")
										.build();
		service.modifyDlbrt(param, null);
		viewDto = service.get(mvnEtReqId);
		System.out.println(String.format("승인처리결과 : [%s]", viewDto.toString()));
		assertNotNull(viewDto);

		// 이력조회
		JsonList<UsptMvnEtHist> hist = service.getHist(mvnEtReqId);
		System.out.println(String.format("이력조회결과 : [%s]", hist.toString()));
		assertTrue(hist.getList().size() > 1L);
	}
}
