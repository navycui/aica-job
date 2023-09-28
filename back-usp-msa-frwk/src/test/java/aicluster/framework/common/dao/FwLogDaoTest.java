package aicluster.framework.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.entity.LogtConectLog;
import aicluster.framework.common.entity.LogtBatchLog;
import aicluster.framework.common.entity.LogtErrorLog;
import aicluster.framework.common.entity.LogtLoginLog;
import aicluster.framework.support.TestDaoSupport;
import bnet.library.util.CoreUtils;

//@Slf4j
public class FwLogDaoTest extends TestDaoSupport {
	@Autowired
	private FwLogDao dao;

	@BeforeEach
	public void init() {

	}

	@Test
	public void test() {
		List<LogtConectLog> accessLogs = new ArrayList<>();
		LogtConectLog accessLog = null;
		for(int i = 0; i < 5; i++) {
			accessLog = new LogtConectLog();
			accessLog.setDeptNm("부서명-" + i);
			accessLog.setLogDt(new Date());
			accessLog.setLogId(CoreUtils.string.getNewId("accesslog-"));
			accessLog.setLoginId("loginid-" + i);
			accessLog.setMemberId("memberid-" + i);
			accessLog.setMemberIp("127.0.0." + i);
			accessLog.setMemberNm("memberNm" + i);
			accessLog.setPositionNm("직급" + i);
			accessLog.setApiSystemId("systemId" + i);
			accessLog.setUrl("/member/access/url" + i);
			accessLogs.add(accessLog);
		}

		dao.insertLogtAccessLogs(accessLogs);

		List<LogtBatchLog> batchLogs = new ArrayList<>();
		LogtBatchLog batchLog = null;
		for(int i = 0; i < 5; i++) {
			batchLog = new LogtBatchLog();
			batchLog.setBatchMethod("method" + i);
			batchLog.setBatchName("name" + i);
			batchLog.setBatchSt("batchSt" + i);
			batchLog.setBeginDt(new Date());
			batchLog.setElapsedTime(i + 1000L);
			batchLog.setErrorCode("InvalidationException");
			batchLog.setErrorMsg("블라블라블라~");
			batchLog.setLogId(CoreUtils.string.getNewId("batchlog-"));
			batchLog.setResultCn("총 5건 처리");
			batchLog.setApiSystemId("systemId" + i);
			batchLogs.add(batchLog);
		}
		dao.insertLogtBatchLogs(batchLogs);

		List<LogtErrorLog> errorLogs = new ArrayList<>();
		LogtErrorLog errorLog = null;
		for(int i = 0; i < 5; i++) {
			errorLog = new LogtErrorLog();
			errorLog.setDeptNm("deptnm" + i);
			errorLog.setErrorCode("InvalidationException");
			errorLog.setErrorMsg("errormsg");
			errorLog.setLogDt(new Date());
			errorLog.setLogId(CoreUtils.string.getNewId("logid-"));
			errorLog.setLoginId("loginid-" + i);
			errorLog.setMemberId("memberid-" + i);
			errorLog.setMemberIp("127.0.0." + i);
			errorLog.setMemberNm("membernm-" + i);
			errorLog.setPositionNm("positionnm-" + i);
			errorLog.setApiSystemId("systemId" + i);
			errorLog.setUrl("/member/error/url" + i);

			errorLogs.add(errorLog);
		}
		dao.insertLogtErrorLogs(errorLogs);

		List<LogtLoginLog> loginLogs = new ArrayList<>();
		LogtLoginLog loginLog = null;
		for(int i = 0; i < 5; i++) {
			loginLog = new LogtLoginLog();
			loginLog.setDeptNm("deptnm-" + i);
			loginLog.setLogDt(new Date());
			loginLog.setLogId(CoreUtils.string.getNewId("loginlog-"));
			loginLog.setLoginId("loginid-" + i);
			loginLog.setMemberId("memberid-" + i);
			loginLog.setMemberIp("127.0.0." + i);
			loginLog.setMemberNm("membernm-" + i);
			loginLog.setPositionNm("positionnm-" + i);
			loginLog.setApiSystemId("systemId" + i);
			loginLogs.add(loginLog);
		}
		dao.insertLogtLoginLogs(loginLogs);
	}
}
