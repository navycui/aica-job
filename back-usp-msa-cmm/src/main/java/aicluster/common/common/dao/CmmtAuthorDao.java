package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtAuthor;

@Repository
public interface CmmtAuthorDao {

	CmmtAuthor select(String authorityId);

}
