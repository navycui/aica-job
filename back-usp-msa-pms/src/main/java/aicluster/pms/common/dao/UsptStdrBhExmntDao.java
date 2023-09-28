package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.BhExmntDto;
import aicluster.pms.common.entity.UsptStdrBhExmnt;

@Repository
public interface UsptStdrBhExmntDao {
	void insert(UsptStdrBhExmnt info);
	int update(UsptStdrBhExmnt info);
	int delete(UsptStdrBhExmnt info);
	List<BhExmntDto> selectList(String stdrBsnsCd);
}
