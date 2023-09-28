package aicluster.batch.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.CmmtMberInfoHist;

@Repository
public interface CmmtMberInfoHistDao {
	void insert(CmmtMberInfoHist cmmtMemberHist);
	void insertList(@Param("list") List<CmmtMberInfoHist> list);
}

