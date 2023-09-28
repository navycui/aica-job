package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEvlMfcmmResult;

@Repository
public interface UsptEvlMfcmmResultDao {
	//평가결과 등록
	int insert(UsptEvlMfcmmResult usptEvlMfcmmResult);

	//평가결과 수정
	int update(UsptEvlMfcmmResult usptEvlMfcmmResult);

	//위원평가상태코드 모두 update처리
	int updateMfcmmEvlSttusAll(UsptEvlMfcmmResult usptEvlMfcmmResult);

	//평가결과 조회
	List<UsptEvlMfcmmResult> selectList(UsptEvlMfcmmResult usptEvlMfcmmResult);

}
