package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancApplyChklst;

@Repository
public interface UsptBsnsPblancApplyChklstDao {
	void insert(UsptBsnsPblancApplyChklst info);
	void insertList(List<UsptBsnsPblancApplyChklst> list);
	int update(UsptBsnsPblancApplyChklst info);
	UsptBsnsPblancApplyChklst select(String applyId);
	List<UsptBsnsPblancApplyChklst> selectList(String applyId);
}
