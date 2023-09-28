package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.dto.InsiderHistDto;
import aicluster.member.common.entity.CmmtEmpInfoHist;

@Repository
public interface CmmtEmpInfoHistDao {
	void insert(CmmtEmpInfoHist insiderHist);

	void insertList(@Param("list") List<CmmtEmpInfoHist> list);

	long selectCount(String memberId);

	List<InsiderHistDto> selectList(String memberId);
}
