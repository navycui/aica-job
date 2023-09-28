package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.holiday.dto.MonthHolidaysDto;
import aicluster.common.common.dto.HldySmmryDto;
import aicluster.common.common.entity.CmmtRestde;

@Repository
public interface CmmtRestdeDao {

	List<CmmtRestde> selectList(
			@Param("year") String year,
			@Param("ymdNm") String ymdNm);

	void insert(CmmtRestde holiday);

	CmmtRestde select(String ymd);

	void update(CmmtRestde holiday);

	void delete(String ymd);

	List<HldySmmryDto> selectSummary(String year);

	List<MonthHolidaysDto> selectMonthList(String ym);
}
