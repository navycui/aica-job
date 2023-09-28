package aicluster.batch.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.CmmtRestde;

@Repository
public interface CmmtRestdeDao {

	void deleteNotUserDsgn(String yyyymm);

	void insertList(@Param("list") List<CmmtRestde> list);

}
