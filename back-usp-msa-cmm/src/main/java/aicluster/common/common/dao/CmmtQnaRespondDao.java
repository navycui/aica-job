package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtQnaRespond;

@Repository
public interface CmmtQnaRespondDao {

	CmmtQnaRespond select(
			@Param("qnaId") String qnaId,
			@Param("memberId") String memberId);


	List<CmmtQnaRespond> selectList(
			@Param("qnaId") String qnaId,
			@Param("memberNm") String memberNm,
			@Param("loginId") String loginId);

	void insert(CmmtQnaRespond qa);

	void insertList(@Param("list") List<CmmtQnaRespond> list);

	void delete(
			@Param("qnaId") String qnaId,
			@Param("memberId") String memberId);

	void deleteList(String qnaId);

}