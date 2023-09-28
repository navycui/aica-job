package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtCodeGroup;

@Repository
public interface CmmtCodeGroupDao {
	void insert(CmmtCodeGroup codeGroup);

	CmmtCodeGroup select(String codeGroup);

	void update(CmmtCodeGroup codeGroup);

	void delete(String codeGroup);
}
