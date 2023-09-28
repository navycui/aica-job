package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsns.dto.BsnsListParam;
import aicluster.pms.api.bsns.dto.BsnsTaskDto;
import aicluster.pms.common.dto.BsnsBasicDto;
import aicluster.pms.common.dto.BsnsListDto;
import aicluster.pms.common.dto.BsnsNmDto;
import aicluster.pms.common.entity.UsptBsns;

@Repository
public interface UsptBsnsDao {
	/** 등록 */
	void insert(UsptBsns info);
	/** 수정 */
	int update(UsptBsns info);
	/** 상세조회 */
	BsnsBasicDto select(String bsnsCd);
	/** 공고 사용 건수 조회 */
	Integer selectBsnsPblancCount(String bsnsCd);
	/** 목록 총 건수 조회 */
	Long selectListCount(BsnsListParam param);
	/** 목록 조회 */
	List<BsnsListDto> selectList(BsnsListParam param);
	/** 과제설정 */
	int updateTaskType(BsnsTaskDto taskInfo);
	/** 사용안함 설정 */
	int updateUnable(UsptBsns info);
	/** 사업연도 목록 조회 */
	List<String> selectBsnsYearList();
	/** 사업명 목록 조회 */
	List<BsnsNmDto> selectBsnsNmList(String bsnsYear);

	/** 사업선택 목록 총 건수 조회 */
	Long selectListCountForPblanc(BsnsListParam param);
	/** 사업선택 목록 조회 */
	List<BsnsListDto> selectListForPblanc(BsnsListParam param);
}
