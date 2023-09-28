package aicluster.pms.api.pblanc.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.pms.api.pblanc.dto.FrontBsnsPblancListParam;
import aicluster.pms.api.pblanc.dto.FrontMainRecomendParam;
import aicluster.pms.common.dao.UsptBsnsPblancDao;
import aicluster.pms.common.dao.UsptBsnsPblancDetailDao;
import aicluster.pms.common.dto.FrontBsnsPblancDto;
import aicluster.pms.common.dto.FrontBsnsPblancListDto;
import aicluster.pms.common.dto.FrontMainListDto;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class FrontBsnsPblancService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;
	@Autowired
	private UsptBsnsPblancDetailDao usptBsnsPblancDetailDao;

	/**
	 * 모집공고 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<FrontBsnsPblancListDto> getList(FrontBsnsPblancListParam param, Long page){
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setPblancId(null);
		if(param.getRecomendCl() != null && param.getRecomendCl().size() > 0) {
			List<String> temp = param.getRecomendCl().stream().filter(x->CoreUtils.string.isNotEmpty(x)).collect(Collectors.toList());
			if(temp.size() == 0) {
				param.setRecomendCl(null);
			} else {
				param.setRecomendCl(temp);
			}
		} else {
			param.setRecomendCl(null);
		}

		if(param.getPblancSttus() == null || param.getPblancSttus().size() == 0) {
			param.setPblancSttus(null);
		}
		if(param.getApplyMberType() == null || param.getApplyMberType().size() == 0) {
			param.setApplyMberType(null);
		}

		long totalItems = usptBsnsPblancDao.selectFrontListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<FrontBsnsPblancListDto> list = usptBsnsPblancDao.selectFrontList(param);
		CorePagination<FrontBsnsPblancListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 마감임박 공고 목록 조회
	 * @param frontBsnsPblancListParam
	 * @return
	 */
	public JsonList<FrontBsnsPblancListDto> getCloseingList(FrontBsnsPblancListParam frontBsnsPblancListParam){
		frontBsnsPblancListParam.setPblancId(null);
		frontBsnsPblancListParam.setIsCloseing("Y");
		if(frontBsnsPblancListParam.getRecomendCl() != null && frontBsnsPblancListParam.getRecomendCl().size() > 0) {
			List<String> temp = frontBsnsPblancListParam.getRecomendCl().stream().filter(x->CoreUtils.string.isNotEmpty(x)).collect(Collectors.toList());
			if(temp.size() == 0) {
				frontBsnsPblancListParam.setRecomendCl(null);
			} else {
				frontBsnsPblancListParam.setRecomendCl(temp);
			}
		} else {
			frontBsnsPblancListParam.setRecomendCl(null);
		}

		if(frontBsnsPblancListParam.getPblancSttus() == null || frontBsnsPblancListParam.getPblancSttus().size() == 0) {
			frontBsnsPblancListParam.setPblancSttus(null);
		}
		if(frontBsnsPblancListParam.getApplyMberType() == null || frontBsnsPblancListParam.getApplyMberType().size() == 0) {
			frontBsnsPblancListParam.setApplyMberType(null);
		}
		return new JsonList<>(usptBsnsPblancDao.selectFrontList(frontBsnsPblancListParam));
	}


	/**
	 * 모집공고 상세조회
	 * @param pblancId
	 * @param frontBsnsPblancListParam
	 * @return
	 */
	public FrontBsnsPblancDto get(String pblancId, FrontBsnsPblancListParam frontBsnsPblancListParam) {
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		info.setDetailList(usptBsnsPblancDetailDao.selectList(pblancId));
		info.setFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));

		frontBsnsPblancListParam.setPblancId(pblancId);
		frontBsnsPblancListParam.setOrdtmRcrit(info.getOrdtmRcrit());
		if(frontBsnsPblancListParam.getRecomendCl() != null && frontBsnsPblancListParam.getRecomendCl().size() > 0) {
			List<String> temp = frontBsnsPblancListParam.getRecomendCl().stream().filter(x->CoreUtils.string.isNotEmpty(x)).collect(Collectors.toList());
			if(temp.size() == 0) {
				frontBsnsPblancListParam.setRecomendCl(null);
			} else {
				frontBsnsPblancListParam.setRecomendCl(temp);
			}
		} else {
			frontBsnsPblancListParam.setRecomendCl(null);
		}

		if(frontBsnsPblancListParam.getPblancSttus() == null || frontBsnsPblancListParam.getPblancSttus().size() == 0) {
			frontBsnsPblancListParam.setPblancSttus(null);
		}
		if(frontBsnsPblancListParam.getApplyMberType() == null || frontBsnsPblancListParam.getApplyMberType().size() == 0) {
			frontBsnsPblancListParam.setApplyMberType(null);
		}
		List<FrontBsnsPblancListDto> list = usptBsnsPblancDao.selectFrontList(frontBsnsPblancListParam);
		if(list != null && list.size() > 0) {
			FrontBsnsPblancListDto fbpInfo = list.get(0);
			info.setPrePblancId(fbpInfo.getPrePblancId());
			info.setPrePblancNm(fbpInfo.getPrePblancNm());
			info.setNextPblancId(fbpInfo.getNextPblancId());
			info.setNextPblancNm(fbpInfo.getNextPblancNm());
		} else {
			info.setPrePblancNm("이전 공고가 없습니다.");
			info.setNextPblancNm("다음 공고가 없습니다.");
		}

		//조회수
		usptBsnsPblancDao.updateRdcnt(pblancId);

		return info;
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param pblancId
	 * @param attachmentId
	 */
	public void getFileDownload(HttpServletResponse response, String pblancId, String attachmentId) {
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}


	/**
	 * 썸네일 이미지 다운로드
	 * @param response
	 * @param pblancId
	 */
	public void downloadThumbnail(HttpServletResponse response, String pblancId) {
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		if(CoreUtils.string.isEmpty(info.getThumbnailFileId())) {
			throw new InvalidationException("해당되는 썸네일 이미지 파일이 없습니다.");
		}
		attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), info.getThumbnailFileId());
	}


	/**
	 * 메인화면 인기공고 목록조회
	 * @return
	 */
	public JsonList<FrontMainListDto> getFrontMainPopularList() {
		return new JsonList<>(usptBsnsPblancDao.selectFrontMainPopularList());
	}


	/**
	 * 메인화면 나에게 맞는 사업 추천 목록 조회 *
	 * @param param
	 * @return
	 */
	public JsonList<FrontMainListDto> getFrontMainRecomendList(FrontMainRecomendParam param) {
		if(param.getRecomendCl() == null || param.getRecomendCl().size() == 0) {
			throw new InvalidationException("관심사업을 선택해주세요!");
		}
		if(CoreUtils.string.isEmpty(param.getFntnRecomendClCd())) {
			throw new InvalidationException("창업단계를 선택해주세요!");
		}

		return new JsonList<>(usptBsnsPblancDao.selectFrontMainRecomendList(param));
	}

}
