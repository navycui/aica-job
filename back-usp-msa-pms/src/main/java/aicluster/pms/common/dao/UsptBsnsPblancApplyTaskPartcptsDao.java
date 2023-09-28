package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;

@Repository
public interface UsptBsnsPblancApplyTaskPartcptsDao {
	void insert(UsptBsnsPblancApplyTaskPartcpts info);
	void insertList(List<UsptBsnsPblancApplyTaskPartcpts> list);
	int update(UsptBsnsPblancApplyTaskPartcpts info);
	int delete(UsptBsnsPblancApplyTaskPartcpts info);
	UsptBsnsPblancApplyTaskPartcpts select(@Param("applyId") String applyId, @Param("partcptsId") String partcptsId);
	List<UsptBsnsPblancApplyTaskPartcpts> selectList(String applyId);
}
