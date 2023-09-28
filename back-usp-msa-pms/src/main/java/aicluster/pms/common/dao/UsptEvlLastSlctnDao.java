package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.selection.dto.SelectionListParam;
import aicluster.pms.common.dto.DspthTrgetListDto;
import aicluster.pms.common.dto.SelectionListDto;
import aicluster.pms.common.dto.SlctnDetailDto;
import aicluster.pms.common.dto.SlctnTrgetListDto;
import aicluster.pms.common.entity.UsptEvlLastSlctn;

@Repository
public interface UsptEvlLastSlctnDao {
	int insert(UsptEvlLastSlctn usptEvlLastSlctn);
	int update(UsptEvlLastSlctn info);
	UsptEvlLastSlctn select(String evlLastSlctnId);
	Long selectListCount(SelectionListParam param);
	List<SelectionListDto> selectList(SelectionListParam param);
	SlctnDetailDto selectSlctnDetail(@Param("evlLastSlctnId") String evlLastSlctnId, @Param("insiderId") String insiderId);
	List<SlctnTrgetListDto> selectSlctnTrgetList(@Param("evlLastSlctnId") String evlLastSlctnId, @Param("insiderId") String insiderId);

	/**
	 * 통보대상 목록 조회
	 * @param evlLastSlctnId
	 * @param slctn
	 * @param insiderId
	 * @return
	 */
	List<DspthTrgetListDto> selectDspthTrgetList(
			@Param("evlLastSlctnId") String evlLastSlctnId
			, @Param("slctn") Boolean slctn
			, @Param("insiderId") String insiderId);
}
