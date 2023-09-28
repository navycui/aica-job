package aicluster.mvn.api.reservation.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.security.Code;
import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtDto;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtListParam;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtModifyStateParam;
import aicluster.mvn.api.reservation.dto.UserRsvtListParam;
import aicluster.mvn.common.dao.UsptMvnFcltyResveDao;
import aicluster.mvn.common.dto.MvnFcCutoffTimeDto;
import aicluster.mvn.common.dto.MvnFcRsvtListItemDto;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyResve;
import aicluster.mvn.common.entity.UsptMvnFcltyResveHist;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.reserveSt;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MvnFcRsvtServiceTest extends TestServiceSupport{
	@Autowired
	private MvnFcRsvtService service;
	@Autowired
	private UsptMvnFcltyResveDao dao;
	@Autowired
	private MvnFcService fcService;

	private List<UsptMvnFcltyInfo> selMvnFcList;

	@Data
	private class RsvtFcTmpItem {
		private String  rsvctmId      ;                 /** 예약자ID(회원ID) */
		private String  rsvtDay       ;                 /** 예약일 */
		private String  rsvtBgngTm    ;                 /** 예약시작시각 : HH24MISS 형식 */
		private String  rsvtEndTm     ;                 /** 예약종료시각 : HH24MISS 형식 */
		private Integer rsvtNope      ;                 /** 예약인원수 */
		private Boolean mvnYn         ;                 /** 입주여부 */
		private Date    rsvtReqDt     ;                 /** 예약요청일시 */
		private String  reserveSt     ;                 /** 예약상태(G:RESERVE_ST) */
		private Date    reserveStDt   ;                 /** 예약상태변경일시 */

		public RsvtFcTmpItem(String rsvctmId, String rsvtDay, String rsvtBgngTm, String rsvtEndTm, Integer rsvtNope,
				Boolean mvnYn, Date rsvtReqDt, String reserveSt, Date reserveStDt) {
			this.rsvctmId = rsvctmId;
			this.rsvtDay = rsvtDay;
			this.rsvtBgngTm = rsvtBgngTm;
			this.rsvtEndTm = rsvtEndTm;
			this.rsvtNope = rsvtNope;
			this.mvnYn = mvnYn;
			this.rsvtReqDt = rsvtReqDt;
			this.reserveSt = reserveSt;
			this.reserveStDt = reserveStDt;
		}
	}

	@BeforeEach
	void init() {
		this.selMvnFcList = new ArrayList<UsptMvnFcltyInfo>();
		CorePagination<UsptMvnFcltyInfo> fcList= fcService.getEnableShareList(null, new CorePaginationParam());
		Random random = new Random();
		int firstIdx = random.nextInt(fcList.getList().size());
		selMvnFcList.add(fcList.getList().get(firstIdx));

		int secondIdx = random.nextInt(fcList.getList().size());
		if ( firstIdx == secondIdx ) secondIdx = random.nextInt(fcList.getList().size());
		selMvnFcList.add(fcList.getList().get(secondIdx));

		// 승인형 설비 선택
		String fcApprId = "mvnfc-33d1ea3e33644b7b8381d4c9e301f82f";
		boolean fcApprYn = selMvnFcList.stream().filter(obj -> obj.getMvnFcId().equals(fcApprId)).findFirst().isPresent();
		if (!fcApprYn) {
			selMvnFcList.add(fcService.get(fcApprId));
		}

		List<RsvtFcTmpItem> tmpList = new ArrayList<>();
		tmpList.add(new RsvtFcTmpItem("member-cf191939537f4c3f9e056c1a13cfc521","20220502","0900","1100",5,false,string.toDate("20220422"),reserveSt.승인,string.toDate("20220426")));
		tmpList.add(new RsvtFcTmpItem("member-38a5be040139493ab0bb7795d8cac045","20220502","1300","1500",5,false,string.toDate("20220425"),reserveSt.신청,string.toDate("20220425")));
		if (!fcApprYn) {
			tmpList.add(new RsvtFcTmpItem("member-38a5be040139493ab0bb7795d8cac045","20220630","1300","1500",5,false,string.toDate("20220620"),reserveSt.신청,string.toDate("20220620")));
		}

		Date now = new Date();
		int i = 0;
		for (UsptMvnFcltyInfo selMvnFc : selMvnFcList) {
			UsptMvnFcltyResve rsvt = UsptMvnFcltyResve.builder()
								.reserveId(string.getNewId("test-"))
								.mvnFcId(selMvnFc.getMvnFcId())
								.rsvctmId(tmpList.get(i).getRsvctmId())
								.rsvtDay(tmpList.get(i).getRsvtDay())
								.rsvtBgngTm(tmpList.get(i).getRsvtBgngTm())
								.rsvtEndTm(tmpList.get(i).getRsvtEndTm())
								.rsvtNope(tmpList.get(i).getRsvtNope())
								.utztnPurpose("회의11")
								.mvnYn(tmpList.get(i).getMvnYn())
								.rsvtReqDt(tmpList.get(i).getRsvtReqDt())
								.reserveSt(tmpList.get(i).getReserveSt())
								.reserveStDt(tmpList.get(i).getReserveStDt())
								.creatorId("SYSTEM")
								.createdDt(now)
								.updaterId("SYSTEM")
								.updatedDt(now)
								.build();

			dao.insert(rsvt);

			i++;
		}
	}

	@Test
	void test() {

		reserve();

		reserveManagement();
	}

	private void login(boolean adminYn) {
		if (BooleanUtils.isTrue(adminYn)) {
			resetAccessToken("insider-test", Code.memberType.내부사용자);
		}
		else {
			resetAccessToken("member-cf191939537f4c3f9e056c1a13cfc521", Code.memberType.개인사업자);
		}
	}

	private void reserve()
	{
		login(false);

		for (UsptMvnFcltyInfo selMvnFc : selMvnFcList) {
			MvnFcCutoffTimeDto dto = service.getFacCutoffTimeList(selMvnFc.getMvnFcId(), "20220502");
			log.info(dto.toString());
		}

		UserRsvtListParam param = UserRsvtListParam.builder().srchBeginDay("20220501").srchEndDay("20220630").build();
		CorePagination<MvnFcRsvtListItemDto> rsvtList = service.getUserRsvtList(param, new CorePaginationParam());
		assertNotNull(rsvtList);
		log.info("사용자 목록조회 결과 : " + rsvtList.toString());

		Random random = new Random();
		int selIdx = random.nextInt(rsvtList.getList().size());
		String reserveId = rsvtList.getList().get(selIdx).getReserveId();
		MvnFcRsvtDto detail = service.getUserRsvt(reserveId);
		assertNotNull(detail);
		log.info("사용자 상세조회 결과 : " + detail.toString());
	}

	private void reserveManagement()
	{
		login(true);

		MvnFcRsvtListParam param = MvnFcRsvtListParam.builder().rsvtBeginDay("20220101").rsvtEndDay("20220630").build();
		CorePagination<MvnFcRsvtListItemDto> list = service.getRsvtSpaceList(param, new CorePaginationParam());
		assertNotNull(list);
		log.info("시설예약 목록조회 결과 : " + list.toString());

		Random random = new Random();
		int selIdx = random.nextInt(list.getList().size());
		String reserveId = list.getList().get(selIdx).getReserveId();
		MvnFcRsvtDto detail = service.getRsvtSpace(reserveId);
		assertNotNull(detail);
		log.info("시설예약 상세조회 결과 : " + detail.toString());

		param = MvnFcRsvtListParam.builder().reserveType(CodeExt.reserveType.승인형).reserveSt(CodeExt.reserveSt.신청).rsvtBeginDay("20220502").rsvtEndDay("20220630").build();
		CorePagination<MvnFcRsvtListItemDto> sttuslist = service.getRsvtSpaceList(param, new CorePaginationParam());
		selIdx = random.nextInt(sttuslist.getList().size());
		String selReserveId = sttuslist.getList().get(selIdx).getReserveId();
		MvnFcRsvtModifyStateParam modifyParam = MvnFcRsvtModifyStateParam.builder().reserveSt(CodeExt.reserveSt.승인).build();
		UsptMvnFcltyResve rsvtSttus = service.modifyState(selReserveId, modifyParam);
		assertNotNull(rsvtSttus);
		log.info("시설예약 상태변경 결과 : " + rsvtSttus.toString());

		MvnFcCutoffTimeDto curoffTimeDto = service.getFacCutoffTimeList(sttuslist.getList().get(selIdx).getMvnFcId(), sttuslist.getList().get(selIdx).getRsvtDay());
		assertNotNull(curoffTimeDto);
		log.info("시설예약 예약불가시간조회 결과 : " + curoffTimeDto.toString());

		JsonList<UsptMvnFcltyResveHist> histList = service.getReserveHistList(selReserveId);
		assertNotNull(histList);
		log.info("시설예약 이력조회 결과 : " + histList.toString());
	}
}
