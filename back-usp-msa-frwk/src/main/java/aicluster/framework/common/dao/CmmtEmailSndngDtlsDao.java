package aicluster.framework.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtEmailSndngDtls;

@Repository("FwCmmtEmailSndngDtlsDao")
public interface CmmtEmailSndngDtlsDao {
	void insert(CmmtEmailSndngDtls cmmtEmail);
}
