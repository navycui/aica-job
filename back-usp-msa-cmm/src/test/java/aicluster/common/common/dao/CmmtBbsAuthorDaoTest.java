package aicluster.common.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.common.entity.CmmtBbs;
import aicluster.common.common.entity.CmmtBbsAuthor;
import aicluster.common.common.util.CodeExt;
import aicluster.common.support.TestDaoSupport;

//@Slf4j
public class CmmtBbsAuthorDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtBbsDao boardDao;
	@Autowired
	private CmmtBbsAuthorDao baDao;

	private CmmtBbs board;

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
	}

	@Test
	public void test() {

		// void insert(CmmtBoard board);
		boardDao.insert(board);

		// insertList()
		List<CmmtBbsAuthor> list = new ArrayList<>();
		CmmtBbsAuthor ba = CmmtBbsAuthor.builder()
				.boardId(board.getBoardId())
				.authorityId("권한ID1")
				.boardAuthority(CodeExt.boardAuthority.관리자)
				.build();
		list.add(ba);
		ba = CmmtBbsAuthor.builder()
				.boardId(board.getBoardId())
				.authorityId("권한ID2")
				.boardAuthority(CodeExt.boardAuthority.관리자)
				.build();
		list.add(ba);
		baDao.insertList(list);

		// selectList()
		list = baDao.selectList(board.getBoardId());
		Assert.assertTrue(list.size() == 2);

		// select()
		ba = baDao.select(board.getBoardId(), "권한ID2");
		Assert.assertNotNull(ba);
		Assert.assertEquals(ba.getAuthorityId(), "권한ID2");

		// save()
		ba.setAuthorityId("권한ID3");
		baDao.save(ba);
		CmmtBbsAuthor ba2 = baDao.select(ba.getBoardId(), ba.getAuthorityId());
		Assert.assertNotNull(ba2);
		Assert.assertEquals(ba.getAuthorityId(), ba2.getAuthorityId());

		// delete_board()
		baDao.delete_board(board.getBoardId());
		list = baDao.selectList(board.getBoardId());
		Assert.assertTrue(list.size() == 0);
	}
}
