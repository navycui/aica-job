package aicluster.mvn.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.dao.FwCodeDao;
import aicluster.framework.common.entity.CodeDto;
import aicluster.mvn.api.facility.dto.MvnFcListParam;
import aicluster.mvn.api.status.dto.MvnStatusListParam;
import aicluster.mvn.common.dto.MvnFcOfficeRoomDto;
import aicluster.mvn.common.dto.MvnFcStatsListItemDto;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.support.TestDaoSupport;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePaginationInfo;

public class UsptMvnFcltyInfoDaoTest extends TestDaoSupport {

    @Autowired
    private UsptMvnFcltyInfoDao dao;

    @Autowired
    private FwCodeDao codeDao;

    private List<UsptMvnFcltyInfo> insMvnFcs;

	@BeforeEach
    void init() {
        Date now = new Date();

        List<CodeDto> codes = codeDao.selectList("MVN_FC_DTYPE");

        String[] arrBnoCd = {"A", "B", "C"};

        insMvnFcs = new ArrayList<UsptMvnFcltyInfo>();
        Random random = new Random();
        for (int i=0; i<10; i++) {

            String mvnFcNm = "";
            String mvnFcType = "";
            String mvnFcDtype = "";
            String bnoCd = "";
            String roomNo = "";
            String reserveType = "";
            if (i<5) {
                int k = random.nextInt(3);
                mvnFcNm = "사무실";
                mvnFcType = "OFFICE";
                bnoCd = arrBnoCd[k];
                roomNo = "10" + (i+1) + "호";
            }
            else {
                int k = random.nextInt(codes.size());
                mvnFcNm = codes.get(k).getCodeNm();
                mvnFcType = "SHARE";
                mvnFcDtype = codes.get(k).getCode();
                reserveType = "IMME";
            }

            UsptMvnFcltyInfo mvnFc = UsptMvnFcltyInfo.builder()
                                    .mvnFcId(string.getNewId(prefixId.시설ID))
                                    .mvnFcNm(mvnFcNm + string.leftPad(Integer.toString(i+1), 3, "0"))
                                    .mvnFcType(mvnFcType)
                                    .mvnFcDtype(mvnFcDtype)
                                    .mvnFcCn(mvnFcNm + " 설명입니다.")
                                    .bnoCd(bnoCd)
                                    .roomNo(roomNo)
                                    .mvnFcCapacity(30L)
                                    .mvnFcar("10")
                                    .reserveType(reserveType)
                                    .hr24Yn(false)
                                    .utztnBeginHh("09")
                                    .utztnEndHh("18")
                                    .mainFc("이것저것")
                                    .enabled(true)
                                    .imageFileId("")
                                    .imageAltCn("")
                                    .mvnSt("EMPTY")
                                    .mvnStDt(now)
                                    .creatorId("SYSTEM")
                                    .createdDt(now)
                                    .updaterId("SYSTEM")
                                    .updatedDt(now)
                                    .build();
            insMvnFcs.add(mvnFc);
        }
    }

    @Test
    void test() {
        Random random = new Random();

        // insert
        for (UsptMvnFcltyInfo mvnFc : insMvnFcs) {
            dao.insert(mvnFc);
        }

        // 목록 조회
        MvnFcListParam listParam = MvnFcListParam.builder().mvnFcType("SHARE").enabled(true).build();
        long totalCnt = dao.selectList_count(listParam);
        System.out.println(String.format("기본조회대상 건수 : [%d]", totalCnt));
        CorePaginationInfo info = new CorePaginationInfo(1L, 3L, totalCnt);
        List<UsptMvnFcltyInfo> list = dao.selectList(listParam, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());
        for (UsptMvnFcltyInfo mvnFc : list) {
            System.out.println(mvnFc.toString());
        }

        int selIdx = random.nextInt(10);
        // update
        UsptMvnFcltyInfo uptMvnFc = insMvnFcs.get(selIdx);
        uptMvnFc.setMvnFcNm(uptMvnFc.getMvnFcNm() + "~~수정");
        uptMvnFc.setEnabled(false);
        dao.update(uptMvnFc);

        // select
        UsptMvnFcltyInfo selMvnFc = dao.select(uptMvnFc.getMvnFcId());
        System.out.println(selMvnFc.toString());

        // selectCurrStateList
        long currTotalCnt = dao.selectCurrStateList_count( MvnStatusListParam.builder().bnoCd("A").build() );
        System.out.println(String.format("입주현황 조회대상 건수 : [%d]", currTotalCnt));
        CorePaginationInfo currInfo = new CorePaginationInfo(1L, 3L, currTotalCnt);
        List<MvnFcStatsListItemDto> currList = dao.selectCurrStateList( MvnStatusListParam.builder().bnoCd("A").build(), currInfo.getBeginRowNum(), currInfo.getItemsPerPage(), currInfo.getTotalItems());
        for (MvnFcStatsListItemDto item : currList) {
            System.out.println(item.toString());
        }

        // selectEnable
        int senIdx = random.nextInt(10);
        String senMvnFcType = insMvnFcs.get(senIdx).getMvnFcType();
        String senMvnFcId = insMvnFcs.get(senIdx).getMvnFcId();
        UsptMvnFcltyInfo senMvnFc = dao.selectEnable(senMvnFcType, senMvnFcId);
        if (senMvnFc != null) {
            System.out.println(senMvnFc.toString());
        }
        else {
            System.out.println(String.format("[%s][%s]건이 없습니다.", senMvnFcType, senMvnFcId));
        }

        // selectEnableShareList
        long shareTotalCnt = dao.selectEnableShareList_count("MEETING");
        System.out.println(String.format("공유시설 조회대상 건수 : [%d]", shareTotalCnt));
        CorePaginationInfo shareInfo = new CorePaginationInfo(1L, 3L, shareTotalCnt);
        List<UsptMvnFcltyInfo> shareList = dao.selectEnableShareList("MEETING", shareInfo.getBeginRowNum(), shareInfo.getItemsPerPage(), shareInfo.getTotalItems());
        for (UsptMvnFcltyInfo item : shareList) {
            System.out.println(item.toString());
        }

        // selectBnoRoomCodeList
        List<MvnFcOfficeRoomDto> bnoList = dao.selectBnoRoomCodeList();
        for (MvnFcOfficeRoomDto item : bnoList) {
            System.out.println(item.toString());
        }

        // selectEnableOfficeList
        boolean srchYn = random.nextBoolean();
        String bnoRoomNo = "";
        if (srchYn) {
            int ofeIdx = random.nextInt(bnoList.size());
            bnoRoomNo = bnoList.get(ofeIdx).getBnoRoomNo();
        }
        long officeTotalCnt = dao.selectEnableOfficeList_count(bnoRoomNo, null, "");
        System.out.println(String.format("사무실 조회대상 건수 : [%d]", officeTotalCnt));
        CorePaginationInfo officeInfo = new CorePaginationInfo(1L, 3L, officeTotalCnt);
        List<UsptMvnFcltyInfo> officeList = dao.selectEnableOfficeList(bnoRoomNo, null, "", officeInfo.getBeginRowNum(), officeInfo.getItemsPerPage(), officeInfo.getTotalItems());
        for (UsptMvnFcltyInfo item : officeList) {
            System.out.println(item.toString());
        }

        // delete
        for (UsptMvnFcltyInfo mvnFc : insMvnFcs) {
            dao.delete(mvnFc.getMvnFcId());
        }
    }

}
