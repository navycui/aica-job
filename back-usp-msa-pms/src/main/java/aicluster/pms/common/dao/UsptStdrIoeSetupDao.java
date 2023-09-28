package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.IoeSetupDto;
import aicluster.pms.common.entity.UsptStdrIoeSetup;

@Repository
public interface UsptStdrIoeSetupDao {
	void insert(UsptStdrIoeSetup info);
	int deleteAll(String stdrBsnsCd);
	List<UsptStdrIoeSetup> selectList(String stdrBsnsCd);
	List<IoeSetupDto> selectViewList(String stdrBsnsCd);
}
