package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.CmmtInsider;

@Repository
public interface CmmtInsiderDao {

	int selectListCnt(@Param("deptNm") String deptNm, @Param("memberNm") String memberNm);
	List<CmmtInsider> selectList(@Param("deptNm") String deptNm, @Param("memberNm") String memberNm,
										@Param("beginRowNum") Long beginRowNum, @Param("itemsPerPage") Long itemsPerPage);

}
