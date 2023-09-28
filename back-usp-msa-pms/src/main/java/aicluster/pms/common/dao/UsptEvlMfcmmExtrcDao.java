package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.EvlCmitExtrcExcelResultDto;
import aicluster.pms.common.dto.EvlCmitExtrcResultDto;
import aicluster.pms.common.entity.UsptEvlMfcmmExtrc;

@Repository
public interface UsptEvlMfcmmExtrcDao {

	//추출 조건 기본 정보 저장
	void insert(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	//추출 조건 상세조회
	EvlCmitExtrcResultDto selectEvlCmitExtrcResult(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	EvlCmitExtrcExcelResultDto selectEvlCmitExtrcExcelResult(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc);

	void deleteByCmitId(String EvlCmitId);

	int selectMaxOdrNo(String mfcmmExtrcId);
}
