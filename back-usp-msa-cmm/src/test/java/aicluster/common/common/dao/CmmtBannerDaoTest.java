package aicluster.common.common.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.common.entity.CmmtBanner;
import aicluster.common.support.TestDaoSupport;

public class CmmtBannerDaoTest extends TestDaoSupport{
	@Autowired
	private CmmtBannerDao bannerDao;

	private CmmtBanner banner;
	@BeforeEach
	public void init() {
		Date now = new Date();

		banner = CmmtBanner.builder()
				.bannerId("banner-test")
				.bannerNm("내용")
				.systemId("시스템ID")
				.bannerType("배너구분")
				.beginDt(now)
				.endDt(now)
				.targetUrl("URL")
				.newWindow(true)
				.animation(true)
				.textHtml("텍스트HTML")
				.sortOrder(1L)
				.enabled(true)
				.pcImageFileId("파일아이디")
				.build();
	}

	@Test
	public void test() {
		bannerDao.insert(banner);

//		List<CmmtBanner> list = bannerDao.selectList(banner.getSystemId());
//		Assert.assertNotNull(list);

		CmmtBanner test = bannerDao.select(banner.getBannerId());
		Assert.assertNotNull(test);

		banner.setBannerNm("두번째 내용");
		bannerDao.update(banner);
		test = bannerDao.select(banner.getBannerId());
		Assert.assertNotNull(test);
		Assert.assertEquals(test.getBannerNm(), banner.getBannerNm());

		bannerDao.delete(banner.getBannerId());
		test = bannerDao.select(banner.getBannerId());
		Assert.assertNull(test);



	}

}
