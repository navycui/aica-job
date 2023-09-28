package aicluster.pms.api.pblanc.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.UsptSlctnPblancDao;
import aicluster.pms.common.dao.UsptSlctnPblancDetailDao;
import aicluster.pms.common.dto.FrontSlctnPblancDto;
import aicluster.pms.common.dto.FrontSlctnPblancListDto;
import bnet.library.exception.InvalidationException;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class FrontSlctnPblancService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptSlctnPblancDao usptSlctnPblancDao;
	@Autowired
	private UsptSlctnPblancDetailDao usptSlctnPblancDetailDao;

	/**
	 * 선정결과 공고 목록 조회
	 * @param slctnPblancNm
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<FrontSlctnPblancListDto> getList(String slctnPblancNm, Long page, Long itemsPerPage){
		BnMember worker = SecurityUtils.checkLogin();
		if(page == null) {
			page = 1L;
		}
		if(itemsPerPage == null) {
			itemsPerPage = ITEMS_PER_PAGE;
		}
		long totalItems = usptSlctnPblancDao.selectFrontListCount(slctnPblancNm, worker.getMemberId());
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		List<FrontSlctnPblancListDto> list = usptSlctnPblancDao.selectFrontList(slctnPblancNm, worker.getMemberId(), info.getBeginRowNum(), itemsPerPage, null);
		CorePagination<FrontSlctnPblancListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 선정결과 공고 상세조회
	 * @param slctnPblancId
	 * @param slctnPblancNm
	 * @return
	 */
	public FrontSlctnPblancDto get(String slctnPblancId, String slctnPblancNm) {
		BnMember worker = SecurityUtils.checkLogin();
		FrontSlctnPblancDto info = usptSlctnPblancDao.selectFront(slctnPblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 선정결과공고가 없습니다.");
		}
		info.setDetailList(usptSlctnPblancDetailDao.selectList(slctnPblancId));
		info.setFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
		info.setSlctnList(usptSlctnPblancDao.selectFrontSlctnList(slctnPblancId));

		List<FrontSlctnPblancListDto> list = usptSlctnPblancDao.selectFrontList(slctnPblancNm, worker.getMemberId(), null, null, slctnPblancId);
		if(list != null && list.size() > 0) {
			FrontSlctnPblancListDto fspInfo = list.get(0);
			info.setPreSlctnPblancId(fspInfo.getPreSlctnPblancId());
			info.setPreSlctnPblancNm(fspInfo.getPreSlctnPblancNm());
			info.setNextSlctnPblancId(fspInfo.getNextSlctnPblancId());
			info.setNextSlctnPblancNm(fspInfo.getNextSlctnPblancNm());
		} else {
			info.setPreSlctnPblancNm("이전 공고가 없습니다.");
			info.setNextSlctnPblancNm("다음 공고가 없습니다.");
		}
		return info;
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param slctnPblancId
	 * @param attachmentId
	 */
	public void getFileDownload(HttpServletResponse response, String slctnPblancId, String attachmentId) {
		FrontSlctnPblancDto info = usptSlctnPblancDao.selectFront(slctnPblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 선정결과공고가 없습니다.");
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}
}
