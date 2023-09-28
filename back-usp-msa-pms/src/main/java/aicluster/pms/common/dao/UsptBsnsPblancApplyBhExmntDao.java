package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.ApplyBhExmntDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplyBhExmnt;

@Repository
public interface UsptBsnsPblancApplyBhExmntDao {
	int save(UsptBsnsPblancApplyBhExmnt info);
	List<ApplyBhExmntDto> selectList(@Param("applyId") String applyId, @Param("bsnsCd") String bsnsCd);
}
