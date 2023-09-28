package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrTaxitmSetup;

@Repository
public interface UsptStdrTaxitmSetupDao {
	void insert(UsptStdrTaxitmSetup info);
	int deleteAll(String stdrBsnsCd);
	List<UsptStdrTaxitmSetup> selectList(String stdrBsnsCd);
}
