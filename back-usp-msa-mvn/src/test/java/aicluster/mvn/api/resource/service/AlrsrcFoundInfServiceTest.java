package aicluster.mvn.api.resource.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.security.Code;
import aicluster.mvn.api.resource.dto.AlrsrcFoundInfParam;
import aicluster.mvn.common.dto.AlrsrcFninfRsrcDto;
import aicluster.mvn.common.dto.HistDtListItemDto;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import aicluster.mvn.common.entity.UsptResrceInvntryInfoHist;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlrsrcFoundInfServiceTest extends TestServiceSupport {
	@Autowired
	AlrsrcFoundInfService service;

	@BeforeAll
	void set() {
        setTokenMemberType(Code.memberType.내부사용자);
	}

	@Test
	void test() throws InterruptedException {
		/*
		 * RSRC_GROUP	GPU	가속기
		 * RSRC_GROUP	STORAGE	스토리지
		 * RSRC_GROUP	SAAS	SaaS
		 * RSRC_GROUP	DATALAKE	데이터레이크
		 */
		// 자원할당재고 일괄등록
		List<AlrsrcFoundInfParam> insParamList = new ArrayList<>();
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("GPU").rsrcTypeNm("A100").invtQy(8L).rsrcCalcQy(156L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("GPU").rsrcTypeNm("A100").invtQy(4L).rsrcCalcQy(78L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("GPU").rsrcTypeNm("T4").invtQy(8L).rsrcCalcQy(64L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("GPU").rsrcTypeNm("T4").invtQy(4L).rsrcCalcQy(32L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("GPU").rsrcTypeNm("T4").invtQy(2L).rsrcCalcQy(16L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("STORAGE").rsrcTypeNm("NAS").invtQy(50L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("SAAS").rsrcTypeNm("SaaS").invtQy(50L).build());
		insParamList.add(AlrsrcFoundInfParam.builder().rsrcGroupCd("DATALAKE").rsrcTypeNm("데이터레이크").invtQy(50L).build());
		service.add(insParamList);

		// 자원할당재고 목록 조회
		JsonList<UsptResrceInvntryInfo> fninfList = service.getList();
		assertNotNull(fninfList);
		log.info("입력결과 : " + fninfList.toString());

		// 1분 지연 실행
		TimeUnit.MINUTES.sleep(1);

		Random random = new Random();
		List<Integer> selIdxs;

		// 자원할당재고 일괄수정
		selIdxs = new ArrayList<>();
		List<AlrsrcFoundInfParam> uptParamList = new ArrayList<>();
		for (int i=0; i<3; i++) {
			int selIdx = random.nextInt(8);
			if (!selIdxs.contains(selIdx)) {
				UsptResrceInvntryInfo alrsrcFninf = fninfList.getList().get(selIdx);

				uptParamList.add(AlrsrcFoundInfParam.builder()
									.rsrcId(alrsrcFninf.getRsrcId())
									.rsrcGroupCd(alrsrcFninf.getRsrcGroupCd())
									.rsrcTypeNm(alrsrcFninf.getRsrcTypeNm())
									.rsrcTypeUnitCd(alrsrcFninf.getRsrcTypeUnitCd())
									.invtQy(10L)
									.rsrcCalcQy(alrsrcFninf.getRsrcCalcQy().longValue())
									.build());

				selIdxs.add(selIdx);
			}
		}
		service.modify(uptParamList);
		fninfList = service.getList();
		assertNotNull(fninfList);
		log.info("수정결과 : " + fninfList.toString());

		// 자원재고정보 조회
		String selRsrcId = fninfList.getList().get(random.nextInt(fninfList.getList().size())).getRsrcId();
		UsptResrceInvntryInfo fninf = service.get(selRsrcId);
		assertNotNull(fninf);
		log.info("단건조회결과 : " + fninf.toString());

		// 1분 지연 실행
		TimeUnit.MINUTES.sleep(1);

		// 자원재고정보 일괄삭제
		selIdxs = new ArrayList<>();
		List<AlrsrcFoundInfParam> delParamList = new ArrayList<>();
		for (int i=0; i<3; i++) {
			int selIdx = random.nextInt(8);
			if (!selIdxs.contains(selIdx)) {
				UsptResrceInvntryInfo alrsrcFninf = fninfList.getList().get(selIdx);

				delParamList.add(AlrsrcFoundInfParam.builder().rsrcId(alrsrcFninf.getRsrcId()).build());

				selIdxs.add(selIdx);
			}
		}
		service.remove(delParamList);
		fninfList = service.getList();
		assertNotNull(fninfList);
		log.info("삭제결과 : " + fninfList.toString());

		// 자원재고정보 이력일자 목록 조회
		CorePagination<HistDtListItemDto> histDayList = service.getHistDays(new CorePaginationParam());
		assertNotNull(histDayList);
		log.info("이력일자 목록 조회 : " + histDayList.toString());

		// 자원재고정보 이력 목록 조회
		HistDtListItemDto selHistDtDto = histDayList.getList().get(random.nextInt(histDayList.getList().size()));
		String selHistDt = date.format(selHistDtDto.getHistDt(), "yyyyMMdd") ;
		JsonList<UsptResrceInvntryInfoHist> histList = service.getHist(selHistDt);
		assertNotNull(histList);
		log.info("이력목록 조회 : " + histList.toString());

		// 자원 코드성 목록 조회
		JsonList<AlrsrcFninfRsrcDto> codeList = service.getCodeList("GPU");
		assertNotNull(codeList);
		log.info("GPU 가속기 코드목록 결과 : " + codeList.toString());
	}
}
