package aicluster.mvn.api.resource.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.security.Code;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyBsnsParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyListParam;
import aicluster.mvn.api.resource.dto.AlrsrcDstbInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcRedstbParam;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbDao;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoDao;
import aicluster.mvn.common.dto.AlrsrcCmpnyListItemDto;
import aicluster.mvn.common.entity.UsptResrceAsgnEntrps;
import aicluster.mvn.common.entity.UsptResrceAsgnDstb;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlrsrcDstbServiceTest extends TestServiceSupport {

	@Autowired
	private AlrsrcDstbService service;

	@Autowired
	private AlrsrcCmpnyService cmpnyService;

	@Autowired
	private UsptResrceAsgnDstbDao dstbDao;
	@Autowired
	private UsptResrceInvntryInfoDao fninfDao;

	private AlrsrcCmpnyBsnsParam bsnsParam;

	private UsptResrceAsgnEntrps selCmpnyDto;

	@BeforeAll
	void set() {
		setTokenMemberType(Code.memberType.내부사용자);
	}

	@BeforeEach
	void dataInit() {
		bsnsParam = new AlrsrcCmpnyBsnsParam();

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

		// 자원할당업체 목록 조회
		AlrsrcCmpnyListParam param = new AlrsrcCmpnyListParam();
		param.setAlrsrcBgngDay(string.toDate("20220601"));
		param.setAlrsrcEndDay(string.toDate("20220630"));
		CorePagination<AlrsrcCmpnyListItemDto> list = cmpnyService.getList(param, new CorePaginationParam());

		// 자원할당업체 상세조회
		Random random = new Random();
		int selIdx = random.nextInt(list.getList().size());
		String selAlrsrcId = list.getList().get(selIdx).getAlrsrcId();
		selCmpnyDto = cmpnyService.get(selAlrsrcId);
	}

	@Test
	void test() throws InterruptedException {

		AlrsrcRedstbParam param;

		// 동일 자원재할당
		log.info("################################################### 동일 자원재할당 시작 #############################################");
		List<AlrsrcDstbInsListItem> sameDstbList = new ArrayList<>();
		List<UsptResrceInvntryInfo> bfFninfDto = fninfDao.selectList();

		List<UsptResrceAsgnDstb> cmpnyDstbs = selCmpnyDto.getAlrsrcDstbList();
		for (UsptResrceAsgnDstb cmpnyDstb : cmpnyDstbs) {

			AlrsrcDstbInsListItem dstbItem = AlrsrcDstbInsListItem.builder()
													.rsrcId(cmpnyDstb.getRsrcId())
													.build();

			UsptResrceInvntryInfo fninf = fninfDao.select(cmpnyDstb.getRsrcId());
			switch (fninf.getRsrcGroupCd()) {
				case "GPU" :
					dstbItem.setRsrcDstbQy(Long.valueOf(cmpnyDstb.getRsrcDstbQy().toString())-1L);
					break;
				case "STORAGE" :
					if (string.equals(cmpnyDstb.getRsrcId(), "rsrc-1856e1f4254c4d62ba76cc677a929379")) {
						dstbItem.setRsrcDstbQy(Long.valueOf(cmpnyDstb.getRsrcDstbQy().toString())-10L);
					}
					else {
						dstbItem.setRsrcDstbQy(Long.valueOf(cmpnyDstb.getRsrcDstbQy().toString())+100L);
					}
					break;
				default :
					dstbItem.setRsrcUseYn(true);
					break;
			}
			dstbItem.setRsrcDstbCn(cmpnyDstb.getRsrcDstbCn());

			sameDstbList.add(dstbItem);
		}

		param = AlrsrcRedstbParam.builder().alrsrcId(selCmpnyDto.getAlrsrcId()).alrsrcDstbInsList(sameDstbList).build();
		service.modify(param);

		// 10초 지연 실행
		TimeUnit.SECONDS.sleep(10);

		List<UsptResrceAsgnDstb> uptDstbDto = dstbDao.selectList(selCmpnyDto.getAlrsrcId());

		// 10초 지연 실행
		TimeUnit.SECONDS.sleep(10);

		List<UsptResrceInvntryInfo> afFninfDto1 = fninfDao.selectList();

		log.info("################################################### 동일 자원재할당 종료 #############################################");

		log.info("################################################### 동일 자원재할당 검증 S. #############################################");
		log.info(cmpnyDstbs.toString());
		log.info(uptDstbDto.toString());
		log.info(bfFninfDto.toString());
		log.info(afFninfDto1.toString());
		log.info("################################################### 동일 자원재할당 검증 E. #############################################");

		// 신규 자원재할당
		log.info("################################################### 신규 자원재할당 시작 #############################################");
		List<AlrsrcDstbInsListItem> newDstbList = new ArrayList<>();
		AlrsrcDstbInsListItem newGpuDstb = null;
		AlrsrcDstbInsListItem newStorageDstb = null;
		for (UsptResrceInvntryInfo fninf : afFninfDto1) {
			boolean isSameRsrc = uptDstbDto.stream().filter(obj->obj.getRsrcId().equals(fninf.getRsrcId())).findFirst().isPresent();
			if (!isSameRsrc) {
				if (string.equals(fninf.getRsrcGroupCd(), "GPU") && fninf.getInvtQy() > 0) {
					if (newGpuDstb == null) {
						int rsrcDstbQy = fninf.getInvtQy() - 1;
						newGpuDstb = AlrsrcDstbInsListItem.builder().rsrcId(fninf.getRsrcId()).rsrcUseYn(null).rsrcDstbQy(Long.valueOf(Integer.toString(rsrcDstbQy))).rsrcDstbCn(null).build();
					}
				}
				else if (string.equals(fninf.getRsrcGroupCd(), "STORAGE") && fninf.getInvtQy() > 0) {
					if (newStorageDstb == null) {
						int rsrcDstbQy = fninf.getInvtQy() - 1;
						newStorageDstb = AlrsrcDstbInsListItem.builder().rsrcId(fninf.getRsrcId()).rsrcUseYn(null).rsrcDstbQy(Long.valueOf(Integer.toString(rsrcDstbQy))).rsrcDstbCn(null).build();
					}
				}
			}
		}
		newDstbList.add(newGpuDstb);
		newDstbList.add(newStorageDstb);
		newDstbList.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-e5859e4146274945a2ee9cfb399e4df2").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());
		newDstbList.add(AlrsrcDstbInsListItem.builder().rsrcId("rsrc-2d77e0eea549411a83accc94257fa26d").rsrcUseYn(false).rsrcDstbQy(null).rsrcDstbCn(null).build());

		param = AlrsrcRedstbParam.builder().alrsrcId(selCmpnyDto.getAlrsrcId()).alrsrcDstbInsList(newDstbList).build();
		service.modify(param);

		// 10초 지연 실행
		TimeUnit.SECONDS.sleep(10);

		List<UsptResrceAsgnDstb> newDstbDto = dstbDao.selectList(selCmpnyDto.getAlrsrcId());

		// 10초 지연 실행
		TimeUnit.SECONDS.sleep(10);

		List<UsptResrceInvntryInfo> afFninfDto2 = fninfDao.selectList();

		log.info("################################################### 신규 자원재할당 종료 #############################################");

		log.info("################################################### 신규 자원재할당 검증 S. #############################################");
		log.info(uptDstbDto.toString());
		log.info(newDstbDto.toString());
		log.info(afFninfDto1.toString());
		log.info(afFninfDto2.toString());
		log.info("################################################### 신규 자원재할당 검증 E. #############################################");
	}
}
