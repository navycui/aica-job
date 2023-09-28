package aicluster.common.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.api.board.dto.GetArticlesParam;
import aicluster.common.common.dto.BoardArticleListItem;
import aicluster.common.common.entity.CmmtBbs;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.common.support.TestDaoSupport;

//@Slf4j
public class CmmtBbscttDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtBbsDao boardDao;

	@Autowired
	private CmmtBbscttDao articleDao;

	private CmmtBbs board;
	private CmmtBbsctt article1;
	private CmmtBbsctt article2;
	private CmmtBbsctt article3;

	@BeforeEach
	public void init() {
		Date now = new Date();

		board = CmmtBbs.builder()
				.boardId("testboard-")
				.systemId("portal")
				.boardNm("test")
				.articleCnt(0L)
				.enabled(true)
				.webEditor(false)
				.noticeAvailable(true)
				.commentable(true)
				.category(false)
				.categoryCodeGroup(null)
				.attachable(true)
				.attachmentSize(100*1000000L)
				.attachmentExt("GIF/PDF")
				.useSharedUrl(true)
				.useThumbnail(true)
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

		article1 = CmmtBbsctt.builder()
				.articleId("test-article1")
				.boardId(board.getBoardId())
				.title("테스트 입니다.")
				.article("테스트 내용입니다.")
				.notice(false)
				.attachmentGroupId(null)
				.imageGroupId(null)
				.categoryCd(null)
				.posting(true)
				.webEditor(false)
				.sharedUrl("http://www.patrick.com")
				.pcThumbnailFileId(null)
				.readCnt(0L)
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

		article2 = CmmtBbsctt.builder()
				.articleId("test-article2")
				.boardId(board.getBoardId())
				.title("두번째 제목 입니다.(2)")
				.article("두번째 테스트 내용입니다.(2)")
				.notice(false)
				.attachmentGroupId(null)
				.imageGroupId(null)
				.categoryCd(null)
				.posting(true)
				.webEditor(false)
				.sharedUrl("http://www.patrick.com")
				.pcThumbnailFileId(null)
				.readCnt(0L)
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

		article3 = CmmtBbsctt.builder()
				.articleId("test-article3")
				.boardId(board.getBoardId())
				.title("세번째 제목 입니다.(3)")
				.article("세번째 내용입니다.(3)")
				.notice(true)
				.attachmentGroupId(null)
				.imageGroupId(null)
				.categoryCd(null)
				.posting(true)
				.webEditor(false)
				.sharedUrl("http://www.patrick.com")
				.pcThumbnailFileId(null)
				.readCnt(0L)
				.createdDt(now)
				.creatorId("SYSTEM")
				.updatedDt(now)
				.updaterId("SYSTEM")
				.build();

	}

	@Test
	public void test() {
		boardDao.insert(board);

		// void insert(CmmtBoardArticle boardArticle);
		articleDao.insert(article1);
		articleDao.insert(article2);
		articleDao.insert(article3);

		// CmmtBoardArticle select(String articleId);
		CmmtBbsctt a1 = articleDao.select(article1.getArticleId());
		Assert.assertNotNull(a1);
		Assert.assertEquals(article1.getTitle(), a1.getTitle());

		// long selectList_count();
		GetArticlesParam param = new GetArticlesParam();
		param.setBoardId(board.getBoardId());
		param.setPosting(true);
		param.setArticleSrchCd("ALLPLUS");
		param.setArticleSrchWord("테");
		long totalItems = articleDao.selectList_count( param );
		Assert.assertTrue(totalItems == 2L);

		// boolean existsBoardArticles(String boardId);
		boolean exists = articleDao.existsBoardArticles(board.getBoardId());
		Assert.assertTrue(exists);

		// List<CmmtBoardArticle> selectList()
		List<BoardArticleListItem> list = articleDao.selectList( param, 1L, 1L, totalItems);
		Assert.assertTrue(list.size() == 1L);

		// List<CmmtBoardArticle> selectList_notice()
		list = articleDao.selectList_notice(board.getBoardId(), null, null);
		Assert.assertTrue(list.size() == 1L);

		// void incReadCnt(String articleId);
		articleDao.incReadCnt(article1.getArticleId());
		a1 = articleDao.select(article1.getArticleId());
		Assert.assertTrue(a1.getReadCnt() == 1L);

		// List<CmmtBoardArticle> select_recent()
		list = articleDao.selectList_recent(board.getBoardId(), null, false, 2L);
		Assert.assertTrue(list.size() == 2L);

		// List<CmmtBoardArticle> select_recentNotice()
		list = articleDao.selectList_recent(board.getBoardId(), null, true, 5L);
		Assert.assertTrue(list.size() == 3L);

		// void update(CmmtBoardArticle boardArticle);
		article1.setNotice(true);
		articleDao.update(article1);
		list = articleDao.selectList_notice(board.getBoardId(), null, null);
		Assert.assertTrue(list.size() == 2L);

		// void delete(String articleId);
		articleDao.delete(article1.getArticleId());
		a1 = articleDao.select(article1.getArticleId());
		Assert.assertNull(a1);;
	}
}
