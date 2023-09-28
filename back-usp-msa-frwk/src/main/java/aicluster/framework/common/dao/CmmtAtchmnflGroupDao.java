package aicluster.framework.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtAtchmnflGroup;

@Repository("FwCmmtAtchmnflGroupDao")
public interface CmmtAtchmnflGroupDao {
	CmmtAtchmnflGroup select(String attachmentGroupId);
	void insert( CmmtAtchmnflGroup attachmentGroup );
	void delete(String attachmentGroupId);
}
