package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.IoeSetupDto;
import aicluster.pms.common.entity.UsptBsnsIoeSetup;

@Repository
public interface UsptBsnsIoeSetupDao {
	void insert(UsptBsnsIoeSetup info);
	void insertList(List<UsptBsnsIoeSetup> list);
	int deleteAll(String stdrBsnsCd);
	List<IoeSetupDto> selectList(String stdrBsnsCd);
}
