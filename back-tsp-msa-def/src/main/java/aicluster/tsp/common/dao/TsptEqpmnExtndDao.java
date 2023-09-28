package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndDetailProcessParam;
import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndListParam;
import aicluster.tsp.common.dto.EqpmnExtndDetailDto;
import aicluster.tsp.common.dto.EqpmnExtndListDto;
import aicluster.tsp.common.entity.TsptEqpmnExtnHist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnExtndDao {
	// 기간연장 전체 건수 확인
	long selectEqpmnExtndCount(@Param("param") ExtndListParam param);
	// 기간연장 페이지 목록 조회
	List<EqpmnExtndListDto> selectEqpmnExtndList(boolean isExcel, Long beginRowNum, Long itemsPerPage, @Param("param") ExtndListParam param);
	// 페이지 목록 조회 엑셀 다운로드
	List<EqpmnExtndListDto> selectEqpmnExtndList(boolean isExcel, @Param("param") ExtndListParam param);
	// 기간연장 상세정보조회
	EqpmnExtndDetailDto selectEqpmnExtndDetail(String etReqstId);
	// 기간연장 상세정보 할인 리스트 조회
	List<EqpmnExtndDetailDto.DetailDscntParam> selectEqpmnExtndDetailDscnt(String eqpmnId);

	// 기간연장 상세정보 처리
	void updateEqpmnExtndDetail(String etReqstId, @Param("param") ExtndDetailProcessParam param, String pymntMth);


	// 기간연장 처리이력 등록
	void insertExtndHist(TsptEqpmnExtnHist hist);
	// 기간연장 처리이력 전체 건수 확인
	long selectExtndHistCnt(String etReqstId);
	// 기간연장 처리이력 페이지 목록 조회
	List<TsptEqpmnExtnHist> selectExtndHistList( String etReqstId, Long beginRowNum, Long itemsPerPage);
}
