package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsTaxitmSetup;

@Repository
public interface UsptBsnsTaxitmSetupDao {
	void insert(UsptBsnsTaxitmSetup info);
	void insertList(List<UsptBsnsTaxitmSetup> list);
	int deleteAll(String stdrBsnsCd);
}
