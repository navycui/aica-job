package aicluster.mvn.api.resource.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.security.Code;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyBsnsParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcDstbInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcUserListParam;
import aicluster.mvn.common.dto.AlrsrcUserDto;
import aicluster.mvn.common.dto.AlrsrcUserListItemDto;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlrsrcUserServiceTest extends TestServiceSupport {

	@Autowired
	private AlrsrcUserService service;

	@Autowired
	private AlrsrcCmpnyService cmpnyService;

	@Autowired
	private FwUserDao userDao;

	@BeforeAll
	void set() {
        setTokenMemberType(Code.memberType.내부사용자);
	}


	@BeforeEach
	void dataInit() {
		AlrsrcCmpnyBsnsParam bsnsParam = new AlrsrcCmpnyBsnsParam();

		bsnsParam.setEvlLastSlctnId("evllastslctn-4a7d61b79ccf42649b878cbc3a2d88b3");

		// 자원할당업체 정보
		List<AlrsrcCmpnyInsListItem> bsnsCmpnys = new ArrayList<>();
		bsnsCmpnys.add(AlrsrcCmpnyInsListItem.builder().lastSlctnTrgetId("evllastslctntrget-a4de44b79ccf42649b878ddc3a2d88a1").bsnsSlctnId("bsnsslctn-54ee22c79ccf42649b878ddc3a2dada1").receiptNo("BA00000015").memberId("member-9f1fae272be44afaa9a2bee4e9711059").bsnsBgnde(string.toDate("20220501")).bsnsEndde(string.toDate("20221231")).build());
		bsnsCmpnys.add(AlrsrcCmpnyInsListItem.builder().lastSlctnTrgetId("evllastslctntrget-a4de44b79ccf42649b878ddc3a2d88a2").bsnsSlctnId("bsnsslctn-54ee22c79ccf42649b878ddc3a2dada2").receiptNo("BA00000016").memberId("member-38a5be040139493ab0bb7795d8cac045").bsnsBgnde(string.toDate("20220501")).bsnsEndde(string.toDate("20221231")).build());
		bsnsCmpnys.add(AlrsrcCmpnyInsListItem.builder().lastSlctnTrgetId("evllastslctntrget-a4de44b79ccf42649b878ddc3a2d88a3").bsnsSlctnId("bsnsslctn-54ee22c79ccf42649b878ddc3a2dada3").receiptNo("BA00000017").memberId("member-ccb9c698b4af410c85d293f9802e80d8").bsnsBgnde(string.toDate("20220501")).bsnsEndde(string.toDate("20221231")).build());
		bsnsCmpnys.add(AlrsrcCmpnyInsListItem.builder().lastSlctnTrgetId("evllastslctntrget-a4de44b79ccf42649b878ddc3a2d88a4").bsnsSlctnId("bsnsslctn-54ee22c79ccf42649b878ddc3a2dada4").receiptNo("BA00000018").memberId("member-65d316888db2492bbcc89b213d3cb738").bsnsBgnde(string.toDate("20220501")).bsnsEndde(string.toDate("20221231")).build());
		bsnsCmpnys.add(AlrsrcCmpnyInsListItem.builder().lastSlctnTrgetId("evllastslctntrget-a4de44b79ccf42649b878ddc3a2d88a5").bsnsSlctnId("bsnsslctn-54ee22c79ccf42649b878ddc3a2dada5").receiptNo("BA00000019").memberId("member-e5594c93600049698759d281776d4eca").bsnsBgnde(string.toDate("20220501")).bsnsEndde(string.toDate("20221231")).build());

		// 자원할당업체 배분정보
		List<AlrsrcDstbInsListItem> bsnsCmpnyDstbs = new ArrayList<>();
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-92bb8977721941b0aac746d367830633").rsrcUseYn(null).rsrcDstbQy(3L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-1856e1f4254c4d62ba76cc677a929379").rsrcUseYn(null).rsrcDstbQy(10L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-e5859e4146274945a2ee9cfb399e4df2").rsrcUseYn(true).rsrcDstbQy(null).rsrcDstbCn("SaaS1").build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-2d77e0eea549411a83accc94257fa26d").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());
		bsnsCmpnys.get(0).setAlrsrcDstbInsList(bsnsCmpnyDstbs);

		bsnsCmpnyDstbs = new ArrayList<>();
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-049a138e7aab4257bfb977cd6179070b").rsrcUseYn(null).rsrcDstbQy(1L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-584074920611411192e2dbb0ee53c632").rsrcUseYn(null).rsrcDstbQy(15L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-e5859e4146274945a2ee9cfb399e4df2").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-2d77e0eea549411a83accc94257fa26d").rsrcUseYn(true).rsrcDstbQy(null).rsrcDstbCn("데이터").build());
		bsnsCmpnys.get(1).setAlrsrcDstbInsList(bsnsCmpnyDstbs);

		bsnsCmpnyDstbs = new ArrayList<>();
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-38dca927d9104ce4875afe04de2e8230").rsrcUseYn(null).rsrcDstbQy(2L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-1856e1f4254c4d62ba76cc677a929379").rsrcUseYn(null).rsrcDstbQy(25L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-e5859e4146274945a2ee9cfb399e4df2").rsrcUseYn(true).rsrcDstbQy(null).rsrcDstbCn("SaaS2").build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-2d77e0eea549411a83accc94257fa26d").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());
		bsnsCmpnys.get(2).setAlrsrcDstbInsList(bsnsCmpnyDstbs);

		bsnsCmpnyDstbs = new ArrayList<>();
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-fbcdd9909ee0446aa3ee07bb39fa48de").rsrcUseYn(null).rsrcDstbQy(2L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-1856e1f4254c4d62ba76cc677a929379").rsrcUseYn(null).rsrcDstbQy(15L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-e5859e4146274945a2ee9cfb399e4df2").rsrcUseYn(true).rsrcDstbQy(null).rsrcDstbCn("SaaS3").build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-2d77e0eea549411a83accc94257fa26d").rsrcUseYn(true).rsrcDstbQy(null).rsrcDstbCn("데이터").build());
		bsnsCmpnys.get(3).setAlrsrcDstbInsList(bsnsCmpnyDstbs);

		bsnsCmpnyDstbs = new ArrayList<>();
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-0a9f48fe8e4c4615b2bddd23e3452bf6").rsrcUseYn(null).rsrcDstbQy(2L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-584074920611411192e2dbb0ee53c632").rsrcUseYn(null).rsrcDstbQy(1024L).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-e5859e4146274945a2ee9cfb399e4df2").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());
		bsnsCmpnyDstbs.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-2d77e0eea549411a83accc94257fa26d").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());
		bsnsCmpnys.get(4).setAlrsrcDstbInsList(bsnsCmpnyDstbs);

		bsnsParam.setAlrsrcCmpnyInsList(bsnsCmpnys);

		// 자원할당업체 등록
		cmpnyService.add(bsnsParam);

		UserDto user = userDao.select("member-65d316888db2492bbcc89b213d3cb738");
		resetAccessToken(user.getMemberId(), user.getMemberType());
	}

	@Test
	void test() {
		AlrsrcUserListParam param = AlrsrcUserListParam.builder()
										.alrsrcBgngDay(string.toDate("20220101"))
										.alrsrcEndDay(string.toDate("20221231"))
										.build();

		log.info("################################################### 사용자 자원할당내역 목록 시작 #############################################");
		CorePagination<AlrsrcUserListItemDto> list = service.getList(param, new CorePaginationParam());
		assertNotNull(list);
		assertTrue(list.getTotalItems() > 0);
		log.info("################################################### 사용자 자원할당내역 목록 종료 #############################################");

		log.info("################################################### 사용자 자원할당내역 상세 시작 #############################################");
		String alrsrcId = list.getList().get(0).getAlrsrcId();
		AlrsrcUserDto dtlDto = service.get(alrsrcId);
		assertNotNull(dtlDto);
		assertTrue(string.isNotBlank(dtlDto.getAlrsrcId()));
		log.info("################################################### 사용자 자원할당내역 상세 종료 #############################################");

		log.info(String.format("목록정보 : [%s]", json.toString(list)));
		log.info(String.format("상세정보 : [%s]", json.toString(dtlDto)));
	}
}
