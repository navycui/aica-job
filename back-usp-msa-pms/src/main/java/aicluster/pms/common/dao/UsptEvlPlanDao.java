package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.EvlPlanPblancStepDto;
import aicluster.pms.common.entity.UsptEvlPlan;

@Repository
public interface UsptEvlPlanDao {

	List<UsptEvlPlan> selectList(UsptEvlPlan usptEvlPlan);

	Long selectListCount(UsptEvlPlan usptEvlPlan);

	Long selectListExistCount(UsptEvlPlan usptEvlPlan);

	UsptEvlPlan select(String evlPlanId);

	int insert(UsptEvlPlan usptEvlPlan);

	int update(UsptEvlPlan usptEvlPlan);

	int updateEnable(UsptEvlPlan usptEvlPlan);

	/**평가시 평가상태 코드만 변경*/
	int updateEvlSttusCd(UsptEvlPlan usptEvlPlan);
	/**평가시 평가위원회ID로 평가계획 ID 조회*/
	String selectEvlPlanIdByEvlCmitId(String evlCmitId);

	/**
	 * 선정공고대상 목록 조회
	 * @param pblancId
	 * @param rceptOdr
	 * @return
	 */
	List<EvlPlanPblancStepDto> selectPblancEvlStepList(
			@Param("pblancId") String pblancId
			, @Param("rceptOdr") Integer rceptOdr);

	/**
	 * 선정공고대상 평가단계 조회
	 * @param pblancId
	 * @param rceptOdr
	 * @param evlStepId
	 * @return
	 */
	EvlPlanPblancStepDto selectEvlStep(
			@Param("pblancId") String pblancId
			, @Param("rceptOdr") Integer rceptOdr
			, @Param("evlStepId") String evlStepId);

	/**
	 * 선정공고대상 평가최종선정 조회
	 * @param pblancId
	 * @param rceptOdr
	 * @param evlLastSlctnId
	 * @return
	 */
	EvlPlanPblancStepDto selectEvlLastSlctn(
			@Param("pblancId") String pblancId
			, @Param("rceptOdr") Integer rceptOdr
			, @Param("evlLastSlctnId") String evlLastSlctnId);
}
