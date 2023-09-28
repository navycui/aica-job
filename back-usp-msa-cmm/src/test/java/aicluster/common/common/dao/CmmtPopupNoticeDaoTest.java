package aicluster.common.common.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.api.popup.dto.PopupGetListParam;
import aicluster.common.common.dto.PopupListItem;
import aicluster.common.common.entity.CmmtPopupNotice;
import aicluster.common.common.util.CodeExt;
import aicluster.common.support.TestDaoSupport;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.pagination.CorePaginationInfo;

//@Slf4j
public class CmmtPopupNoticeDaoTest extends TestDaoSupport {
	@Autowired
	private CmmtPopupNoticeDao popupDao;

	private CmmtPopupNotice popup;

	@BeforeEach
	public void init() {
		Date now = new Date();

		popup = CmmtPopupNotice.builder()
				.popupId("popup-test")
				.popupType(CodeExt.popupType.메인화면)
				.title("제목")
				.article("내용")
				.beginDt(now)
				.endDt(date.addDays(now, 1))
				.popupWidth(800L)
				.popupHeight(800L)
				.imageFileId(null)
				.linkUrl("http://www.brainednet.com")
				.build();
	}

	@Test
	public void test() {
		// insert
		popupDao.insert(popup);

		// select
		CmmtPopupNotice p1 = popupDao.select(popup.getPopupId());
		Assert.assertNotNull(p1);

		// selectList_time
		Date now = new Date();
		List<CmmtPopupNotice> list = popupDao.selectList_time(CodeExt.popupType.메인화면, now);
		Assert.assertTrue(list.size() > 0);

		// selectList_count
		Date beginDt = new Date();
		Date endDt = date.addHours(beginDt, 1);
		PopupGetListParam param = PopupGetListParam.builder()
										.systemId(popup.getSystemId())
										.title(popup.getTitle())
										.posting(popup.getPosting())
										.beginDt(beginDt)
										.endDt(endDt)
										.build();
		long totalItems = popupDao.selectList_count(param);
		Assert.assertTrue(totalItems > 0);

		// selectList
		CorePaginationInfo info = new CorePaginationInfo(1L, 10L, totalItems);
		List<PopupListItem> list2 = popupDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);
		Assert.assertTrue(list2.size() > 0);

		// update
		popup.setTitle("두번 째 제목");
		popupDao.update(popup);
		p1 = popupDao.select(popup.getPopupId());
		Assert.assertNotNull(p1);
		Assert.assertEquals(p1.getTitle(), popup.getTitle());

		// delete
		popupDao.delete(popup.getPopupId());
		p1 = popupDao.select(popup.getPopupId());
		Assert.assertNull(p1);

	}
}
