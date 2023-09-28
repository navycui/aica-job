package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtParam;
import org.springframework.stereotype.Repository;


@Repository
public interface TsptEqpmnMngmDao {
	MngmMgtParam selectEqpmnMgtInfoFixSelect(String eqpmnId);
	MngmMgtParam.TkoutParam selectEqpmnMgtInfoTkout(String eqpmnId);
	MngmMgtParam.RepairParam selectEqpmnMgtInfoRepair(String eqpmnId);
	MngmMgtParam.CorrectParam selectEqpmnMgtInfoCorrect(String crrcId, String eqpmnId);
	void updateEqpmnMgtInfo(MngmMgtParam mngmMgtParam);
	// 장비 삭제
	void deleteEqpmn(String eqpmnId);
	// 장비 사용신청 이력 조회
	int countEqpmnUse(String eqpmnId);
	// 불용처리
	void updateUnavailable(String eqpmnId, boolean disuseAt);
}
