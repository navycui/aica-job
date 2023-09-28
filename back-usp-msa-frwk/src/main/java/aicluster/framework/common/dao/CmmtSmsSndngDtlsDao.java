package aicluster.framework.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtSmsSndngDtls;

@Repository("FwCmmtSmsSndngDtlsDao")
public interface CmmtSmsSndngDtlsDao {
	void insert(CmmtSmsSndngDtls cmmtSms);
}
