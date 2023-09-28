package aicluster.mvn.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyRestde;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.support.TestDaoSupport;
import bnet.library.util.CoreUtils.string;

public class UsptMvnFcltyRestdeDaoTest extends TestDaoSupport {

    @Autowired
    private UsptMvnFcltyInfoDao fcDao;
    @Autowired
    private UsptMvnFcltyRestdeDao dao;

    private UsptMvnFcltyInfo mvnFc;
    private List<UsptMvnFcltyRestde> mvnFcDds;

    @SuppressWarnings("serial")
	@BeforeEach
    void init() {
        Date now = new Date();

        mvnFc = UsptMvnFcltyInfo.builder()
                        .mvnFcId(string.getNewId(prefixId.시설ID))
                        .mvnFcNm("공유시설테스트")
                        .mvnFcType("SHARE")
                        .mvnFcDtype("MEETING")
                        .reserveType("IMME")
                        .hr24Yn(true)
                        .enabled(true)
                        .mvnSt("EMPTY")
                        .build();

        fcDao.insert(mvnFc);

        ArrayList<HashMap<String, String>> ddList = new ArrayList<>();
        ddList.add(new HashMap<String, String>(){{
            put("beginDay", "20220501");
            put("endDay", "20220501");
            put("reason", "근로자의 날");
        }});
        ddList.add(new HashMap<String, String>(){{
            put("beginDay", "20220606");
            put("endDay", "20220610");
            put("reason", "내부 공사");
        }});
        ddList.add(new HashMap<String, String>(){{
            put("beginDay", "20220816");
            put("endDay", "20220819");
            put("reason", "소방 훈련");
        }});

        mvnFcDds = new ArrayList<>();
        for (int i=0; i<ddList.size(); i++) {
            UsptMvnFcltyRestde mvnFcDd = UsptMvnFcltyRestde.builder()
                                                .mvnFcId(mvnFc.getMvnFcId())
                                                .beginDay(ddList.get(i).get("beginDay"))
                                                .endDay(ddList.get(i).get("endDay"))
                                                .reason(ddList.get(i).get("reason"))
                                                .creatorId("SYSTEM")
                                                .createdDt(now)
                                                .updaterId("SYSTEM")
                                                .updatedDt(now)
                                                .build();
            mvnFcDds.add(mvnFcDd);
        }
    }

    @Test
    void test() {
        // insert List
        dao.insertList(mvnFcDds);

        // select
        List<UsptMvnFcltyRestde> selList = dao.selectList(mvnFc.getMvnFcId());
        for(UsptMvnFcltyRestde item : selList) {
            System.out.println(item.toString());
        }

        // delete
        dao.delete(mvnFc.getMvnFcId());
    }

    @AfterEach
    void tear() {
        fcDao.delete(mvnFc.getMvnFcId());
    }


}
