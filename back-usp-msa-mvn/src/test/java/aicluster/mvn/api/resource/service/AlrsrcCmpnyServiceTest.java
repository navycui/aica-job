package aicluster.mvn.api.resource.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.security.Code;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyBsnsParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyListParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyPeriodParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyStatusParam;
import aicluster.mvn.api.resource.dto.AlrsrcDstbInsListItem;
import aicluster.mvn.common.dto.AlrsrcCmpnyListItemDto;
import aicluster.mvn.common.dto.AlrsrcCmpnySlctnDto;
import aicluster.mvn.common.entity.UsptResrceAsgnEntrps;
import aicluster.mvn.common.entity.UsptResrceAsgnHist;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlrsrcCmpnyServiceTest extends TestServiceSupport {

	@Autowired
	private AlrsrcCmpnyService service;

	private AlrsrcCmpnyBsnsParam bsnsParam;


	@BeforeAll
	void set() {
        setTokenMemberType(Code.memberType.내부사용자);
	}

	@BeforeEach
	void dataInit() {

		bsnsParam = new AlrsrcCmpnyBsnsParam();

		bsnsParam.setEvlLastSlctnId("evllastslctn-4a7d61b79ccf42649b878cbc3a2d88b3");

		log.info(String.format("최종선정대상ID 길이 : [%d]", string.length(bsnsParam.getEvlLastSlctnId())));

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

	}

	@Test
	void test()
	{
		// 자원할당업체 등록
		service.add(bsnsParam);

		// 자원할당업체 목록 조회
		AlrsrcCmpnyListParam param = new AlrsrcCmpnyListParam();
		param.setAlrsrcBgngDay(string.toDate("20220601"));
		param.setAlrsrcEndDay(string.toDate("20220630"));
		CorePagination<AlrsrcCmpnyListItemDto> list = service.getList(param, new CorePaginationParam());
		assertNotNull(list);

		// 자원할당업체 상세조회
		Random random = new Random();
		int selIdx = random.nextInt(list.getList().size());
		String selAlrsrcId = list.getList().get(selIdx).getAlrsrcId();
		UsptResrceAsgnEntrps selCmpnyDto = service.get(selAlrsrcId);
		assertNotNull(selCmpnyDto);

		// 이용기간 변경
		AlrsrcCmpnyPeriodParam periodParam = new AlrsrcCmpnyPeriodParam();
		periodParam.setAlrsrcId(selAlrsrcId);
		periodParam.setAlrsrcEndDay(string.toDate("20221031"));
		service.modifyPeriod(periodParam);
		UsptResrceAsgnEntrps periodCmpnyDto = service.get(selAlrsrcId);
		assertNotNull(periodCmpnyDto);


		// 이용종료
		AlrsrcCmpnyStatusParam statusParam = new AlrsrcCmpnyStatusParam();
		statusParam.setAlrsrcId(selAlrsrcId);
		statusParam.setAlrsrcSt(CodeExt.alrsrcSt.이용종료);
		statusParam.setReasonCn("자원재고 부족으로 인한 일부 업체 이용종료");
		service.modifyStatus(statusParam);
		UsptResrceAsgnEntrps closeCmpnyDto = service.get(selAlrsrcId);
		assertNotNull(closeCmpnyDto);

		// 자원할당업체 이력조회
		JsonList<UsptResrceAsgnHist> hist = service.getHist(selAlrsrcId);
		assertNotNull(hist);

		// 자원할당사업별 자원할당업체 목록 조회
		JsonList<AlrsrcCmpnySlctnDto> bsnsSlctnCmpnyList = service.getBsnsSlctn(bsnsParam.getEvlLastSlctnId());
		assertNotNull(bsnsSlctnCmpnyList);

		// 조회결과 데이터 로그 출력
		log.info("목록 조회 결과 : \n" + json.toString(list));
		log.info("상세 조회 결과 : \n" + json.toString(selCmpnyDto));
		log.info("이용기간 변경 결과 : \n" + json.toString(periodCmpnyDto));
		log.info("이용종료 결과 : \n" + json.toString(closeCmpnyDto));
		log.info("이력 조회 결과 : \n" + json.toString(hist));
		log.info("자원할당사업별 자원할당업체 목록 조회 결과 : \n" + json.toString(bsnsSlctnCmpnyList));
	}
}
