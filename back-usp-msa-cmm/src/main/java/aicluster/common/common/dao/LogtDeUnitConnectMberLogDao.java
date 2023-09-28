package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.dto.DayMemberSummary;

@Repository
public interface LogtDeUnitConnectMberLogDao {
	DayMemberSummary selectDayMemberSummary();
}
