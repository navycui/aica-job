package aicluster.mvn.api.facility.service;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.security.Code;
import aicluster.mvn.api.facility.dto.MvnFcListParam;
import aicluster.mvn.api.facility.dto.MvnFcParam;
import aicluster.mvn.api.facility.dto.MvnFcRsvtDdParam;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.support.TestServiceSupport;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

public class MvnFcServiceTest extends TestServiceSupport {

    @Autowired
    private MvnFcService service;

    private MvnFcParam param;

    @BeforeAll
    void set() {
    	setTokenMemberType(Code.memberType.내부사용자);
    }

    @BeforeEach
    void init() {
        param = MvnFcParam.builder()
                .mvnFcNm("소회의실1")
                .mvnFcType("SHARE")
                .mvnFcDtype("MEETING")
                .mvnFcCn("소회의실(1) 입니다.")
                .bnoCd("A")
                .roomNo("소회의실1")
                .mvnFcCapacity(5L)
                .mvnFcar("10")
                .reserveType("IMME")
                .hr24Yn(false)
                .utztnBeginHh("09")
                .utztnEndHh("18")
                .mainFc("프로젝트 빔")
                .imageAltCn("없음")
                .build();

        MvnFcRsvtDdParam ddParam1 = MvnFcRsvtDdParam.builder()
				.beginDay(string.toDate("20220505"))
				.endDay(string.toDate("20220505"))
				.reason("어린이날")
				.build();
        MvnFcRsvtDdParam ddParam2 = MvnFcRsvtDdParam.builder()
				.beginDay(string.toDate("20220509"))
				.endDay(string.toDate("20220509"))
				.reason("부처님오신날 대체휴일")
				.build();
        param.setMvnFcRsvtDdList(new ArrayList<MvnFcRsvtDdParam>());
        param.getMvnFcRsvtDdList().add(ddParam1);
        param.getMvnFcRsvtDdList().add(ddParam2);

        System.out.println(param.getMvnFcRsvtDdList().toString());
    }

    @Test
    void test() {
        UsptMvnFcltyInfo mvnFc = service.add(param, null);
        System.out.println(String.format("입력결과 \t\t: [%s]", mvnFc.toString()));

        CorePagination<UsptMvnFcltyInfo> mvnFcList = service.getList(MvnFcListParam.builder().mvnFcNm("회의").build(), new CorePaginationParam());
        System.out.println(String.format("현재 페이지 \t: [%d]", mvnFcList.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", mvnFcList.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t: [%d]", mvnFcList.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", mvnFcList.getList().toString()));

        param.setMvnFcId(mvnFc.getMvnFcId());
        param.setMvnFcNm(mvnFc.getMvnFcNm() + "~~수정");
        mvnFc = service.modify(param, null);
        System.out.println(String.format("수정결과 \t\t: [%s]", mvnFc.toString()));

        mvnFc = service.modifyEnabled(mvnFc.getMvnFcId(), false);
        System.out.println(String.format("사용결과 \t\t: [%s]", mvnFc.toString()));

        CorePagination<UsptMvnFcltyInfo> shareList = service.getEnableShareList("MEETING", new CorePaginationParam());
        System.out.println(String.format("현재 페이지 \t: [%d]", shareList.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", shareList.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t\t: [%d]", shareList.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", shareList.getList().toString()));

        CorePagination<UsptMvnFcltyInfo> officeList = service.getEnableOfficeList("", null, "", new CorePaginationParam());
        System.out.println(String.format("현재 페이지 \t: [%d]", officeList.getPage()));
        System.out.println(String.format("페이지당 건수 \t: [%d]", officeList.getItemsPerPage()));
        System.out.println(String.format("전체 건수 \t\t: [%d]", officeList.getTotalItems()));
        System.out.println(String.format("목록조회결과  \t: [%s]", officeList.getList().toString()));

        service.delete(mvnFc.getMvnFcId());

        mvnFc = service.get(mvnFcList.getList().get(1).getMvnFcId());
        System.out.println(String.format("단건조회결과 \t: [%s]", mvnFc.toString()));

        service.deleteImage(mvnFc.getMvnFcId());
        System.out.println(String.format("이미지삭제결과 \t: [%s]", mvnFc.toString()));
    }
}
