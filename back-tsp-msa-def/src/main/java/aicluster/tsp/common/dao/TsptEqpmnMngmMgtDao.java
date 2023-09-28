package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmDetailParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtHistParam;
import aicluster.tsp.common.entity.TsptEqpmn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnMngmMgtDao {
	// 수리/교정 조회
	MngmMgtHistParam selectEqpmnMgtHist(String eqpmnId, String manageDiv, String repairId);
	// 수리/교정 등록
	void insertEqpmnMgtHist(MngmMgtHistParam dto);
	// 수리/교정 완료
	void updateEqpmnMgtHist(MngmMgtHistParam dto);
	// 장비정보 상세 조회
	MngmDetailParam selectEqpmnDetail(String eqpmnId);
	// 장비분류 전체 리스트 조회
	List<MngmDetailParam.EqpmnClParam> selectEqpmnCl();
	// 장비정보 상세 수정
	int updateEqpmnDetail(TsptEqpmn tsptEqpmn);
	// 이미지 장비 ID조회
	String selectEqpmnImageId(String eqpmnId);
	// 장비할인적용 초기화
	void deleteEqpmnDetailDscnt(String eqpmnId);
}