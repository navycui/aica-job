package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.ApplyAttachDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplyAttach;

@Repository
public interface UsptBsnsPblancApplyAttachDao {
	void insert(UsptBsnsPblancApplyAttach info);
	void insertList(List<UsptBsnsPblancApplyAttach> list);
	int update(UsptBsnsPblancApplyAttach info);
	UsptBsnsPblancApplyAttach select(
			@Param("applyId") String applyId
			, @Param("atchmnflSetupId") String atchmnflSetupId);
	List<ApplyAttachDto> selectList(
			@Param("applyId") String applyId
			, @Param("bsnsCd") String bsnsCd);
}
