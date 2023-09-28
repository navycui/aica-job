package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.BhExmntDto;
import aicluster.pms.common.entity.UsptBsnsBhExmnt;

@Repository
public interface UsptBsnsBhExmntDao {
	void insert(UsptBsnsBhExmnt info);
	void insertList(List<UsptBsnsBhExmnt> list);
	int update(UsptBsnsBhExmnt info);
	int delete(UsptBsnsBhExmnt info);
	List<BhExmntDto> selectList(String bsnsCd);
}
