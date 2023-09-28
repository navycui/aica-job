package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.EqpmnResultDto;

@Repository
public interface TsptEqpmnResultDao {

	/**
	 * 실증장비사용현황 일별 목록 조회
	 * @param bgnde	: 시작일
	 * @param endde : 종료일
	 * @return
	 */
	List<EqpmnResultDto> selectStatsList(String bgnde, String endde);

	/**
	 * 실증장비사용현황 월별 목록 조회
	 * @param bgnde	: 시작일
	 * @param endde : 종료일
	 * @return
	 */
	List<EqpmnResultDto> selectStatsYmList(String bgnde, String endde);
}
