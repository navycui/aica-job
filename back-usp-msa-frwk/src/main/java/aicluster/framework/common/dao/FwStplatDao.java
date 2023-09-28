package aicluster.framework.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtStplat;

@Repository
public interface FwStplatDao {

	CmmtStplat select(
		@Param("termsType") String termsType,
		@Param("beginDay") String beginDay,
		@Param("required") boolean required);

	List<CmmtStplat> select_today(String termsType);
}
