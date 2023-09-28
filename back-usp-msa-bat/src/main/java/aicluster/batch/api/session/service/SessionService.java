package aicluster.batch.api.session.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.batch.BatchReturn;
import aicluster.framework.common.dao.CmmtSesionInfoDao;
import bnet.library.util.CoreUtils.date;

@Service
public class SessionService {
	@Autowired
	private CmmtSesionInfoDao cmmtSessionDao;

	public BatchReturn removeExpired() {
		Date now = new Date();
		cmmtSessionDao.deleteExpired();

		String msg = "만료일시가 " + date.format(now, "yyyy-MM-dd HH:mm:ss.SSS") + " 이전인 데이터 모두 삭제";
		return BatchReturn.success(msg);
	}
}
