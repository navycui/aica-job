package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtRestdeExcl;

@Repository
public interface CmmtRestdeExclDao {

	List<CmmtRestdeExcl> selectlist(
			@Param("year") String year,
			@Param("ymdNm") String ymdNm);

	void insert(CmmtRestdeExcl holidayExcl);

	CmmtRestdeExcl select(String ymd);

	void update(CmmtRestdeExcl holidayExcl);

	void delete(String ymd);

}
