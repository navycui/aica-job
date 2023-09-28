package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.board.dto.GetArticlesParam;
import aicluster.common.common.dto.BoardArticleListItem;
import aicluster.common.common.dto.BoardArticlePrevNextItem;
import aicluster.common.common.entity.CmmtBbsctt;

@Repository
public interface CmmtBbscttDao {

	boolean existsBoardArticles(String boardId);

	CmmtBbsctt select(String articleId);

	long selectList_count(@Param("param") GetArticlesParam param);

	List<BoardArticleListItem> selectList(
			@Param("param") GetArticlesParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	List<BoardArticleListItem> selectList_notice(
			@Param("boardId") String boardId,
			@Param("categoryCd") String categoryCd,
			@Param("posting") Boolean posting);

	void insert(CmmtBbsctt boardArticle);

	void update(CmmtBbsctt boardArticle);

	void delete(String articleId);

	void incReadCnt(String articleId);

	List<BoardArticleListItem> selectList_recent(
			@Param("boardId") String boardId,
			@Param("categoryCd") String categoryCd,
			@Param("hasNotice") Boolean hasNotice,
			@Param("cnt") Long cnt);

	BoardArticlePrevNextItem selectPrevNext(
			@Param("noticeAvailable") boolean noticeAvailable,
			@Param("articleId") String articleId,
			@Param("param") GetArticlesParam param);

	long selectMinuteCount(@Param("boardId") String boardId, @Param("memberId") String memberId);
}
