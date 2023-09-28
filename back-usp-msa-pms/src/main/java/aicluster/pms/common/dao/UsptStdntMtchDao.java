package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.stdnt.dto.StdntListParam;
import aicluster.pms.common.dto.MtchStdnListDto;
import aicluster.pms.common.dto.StdnListDto;
import aicluster.pms.common.entity.UsptStdntMtch;

@Repository
public interface UsptStdntMtchDao {
	void insert(UsptStdntMtch info);
	int delete(UsptStdntMtch info);

	/**
	 * 매칭교육생 목록 총건수 조회
	 * @param lastSlctnTrgetId
	 * @return
	 */
	Long selectMtchStdnListCount(String lastSlctnTrgetId);
	/**
	 * 매칭교육생 목록 조회
	 * @param lastSlctnTrgetId
	 * @return
	 */
	List<MtchStdnListDto> selectMtchStdnList(
			@Param("lastSlctnTrgetId") String lastSlctnTrgetId
			, @Param("itemsPerPage") Long itemsPerPage
			, @Param("beginRowNum") Long beginRowNum);


	/**
	 * 교육생 목록 총건수 조회
	 * @param param
	 * @return
	 */
	Long selectStdnListCount(StdntListParam param);
	/**
	 * 교육생 목록 조회
	 * @param param
	 * @return
	 */
	List<StdnListDto> selectStdnList(StdntListParam param);
}
