package aicluster.batch.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptBsnsPblanc;

@Repository
public interface UsptBsnsPblancDao {

	/**
	 * 게시 처리 목록 조회
	 * @param today
	 * @return
	 */
	List<UsptBsnsPblanc> selectNtceAtList(String today);


	/**
	 * 게시여부 수정
	 * @param info
	 * @return
	 */
	int updateNtceAt(UsptBsnsPblanc info);


	/**
	 * 접수마감처리 대상 목록 조회
	 * @return
	 */
	List<UsptBsnsPblanc> selectRceptClosingList(
			@Param("today") String today
			, @Param("hour") String hour);


	/**
	 * 접수마감처리 대상 목록 조회
	 * 전일 마감시간이 설정이 안된 대상
	 * @param yesterday
	 * @return
	 */
	List<UsptBsnsPblanc> selectRceptClosingListOfYesterday(String yesterday);


	/**
	 * 접수마감처리
	 * @param info
	 * @return
	 */
	int updateRceptClosing(UsptBsnsPblanc info);
}
