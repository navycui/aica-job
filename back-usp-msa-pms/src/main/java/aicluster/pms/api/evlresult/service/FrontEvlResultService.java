package aicluster.pms.api.evlresult.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.evlresult.dto.FrontEvlResultDto;
import aicluster.pms.api.evlresult.dto.FrontEvlResultListParam;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqst;
import aicluster.pms.common.dao.UsptEvlObjcProcessHistDao;
import aicluster.pms.common.dao.UsptEvlSlctnObjcReqstDao;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dao.UsptLastSlctnObjcProcessHistDao;
import aicluster.pms.common.dao.UsptLastSlctnObjcReqstDao;
import aicluster.pms.common.dto.FrontEvlResultListDto;
import aicluster.pms.common.entity.UsptEvlObjcProcessHist;
import aicluster.pms.common.entity.UsptEvlSlctnObjcReqst;
import aicluster.pms.common.entity.UsptLastSlctnObjcProcessHist;
import aicluster.pms.common.entity.UsptLastSlctnObjcReqst;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class FrontEvlResultService {

	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;
	@Autowired
	private UsptEvlSlctnObjcReqstDao usptEvlSlctnObjcReqstDao;
	@Autowired
	private UsptEvlObjcProcessHistDao usptEvlObjcProcessHistDao;
	@Autowired
	private UsptLastSlctnObjcReqstDao usptLastSlctnObjcReqstDao;
	@Autowired
	private UsptLastSlctnObjcProcessHistDao usptLastSlctnObjcProcessHistDao;

	public static final long ITEMS_PER_PAGE = 10L;

	/**
	 * 평가결과 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<FrontEvlResultListDto> getList(FrontEvlResultListParam param, Long page){
		BnMember worker = SecurityUtils.checkLogin();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		param.setMemberId(worker.getMemberId());
		param.setSlctnBgnde(date.format(string.toDate(param.getSlctnBgnde()), "yyyyMMdd"));
		param.setSlctnEndde(date.format(string.toDate(param.getSlctnEndde()), "yyyyMMdd"));
		long totalItems = usptEvlTrgetDao.selectFrontEvlResultListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<FrontEvlResultListDto> list = usptEvlTrgetDao.selectFrontEvlResultList(param);
		CorePagination<FrontEvlResultListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 평가의견 확인
	 * @param evlTrgetId
	 * @return
	 */
	public FrontEvlResultDto get(String evlTrgetId) {
		BnMember worker = SecurityUtils.checkLogin();
		FrontEvlResultListParam param = new FrontEvlResultListParam();
		param.setMemberId(worker.getMemberId());
		param.setItemsPerPage(ITEMS_PER_PAGE);
		param.setBeginRowNum(1L);
		param.setEvlTrgetId(evlTrgetId);
		List<FrontEvlResultListDto> list = usptEvlTrgetDao.selectFrontEvlResultList(param);

		if(list == null || list.size() == 0) {
			throw new InvalidationException("해당되는 평가결과 정보가 없습니다.");
		}
		FrontEvlResultListDto listDto = list.get(0);
		FrontEvlResultDto dto = new FrontEvlResultDto();
		BeanUtils.copyProperties(listDto, dto);
		if(!CoreUtils.string.equals("최종선정", listDto.getEvlStepNm())) {
			dto.setEvlOpinionList(usptEvlTrgetDao.selectFrontEvlOpinionList(evlTrgetId, worker.getMemberId()));
		}
		if(listDto.getDspthElapseDe() > 10) {
			dto.setObjcReqstposblAt("N");
		} else {
			dto.setObjcReqstposblAt("Y");
		}
		return dto;
	}

	/**
	 * 결과이의신청 등록
	 * @param evlTrgetId
	 * @param slctnObjcReqst
	 * @param fileList
	 */
	public void add(String evlTrgetId, SlctnObjcReqst slctnObjcReqst, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkLogin();
		FrontEvlResultListParam param = new FrontEvlResultListParam();
		param.setMemberId(worker.getMemberId());
		param.setItemsPerPage(ITEMS_PER_PAGE);
		param.setBeginRowNum(1L);
		param.setEvlTrgetId(evlTrgetId);
		List<FrontEvlResultListDto> list = usptEvlTrgetDao.selectFrontEvlResultList(param);

		if(list == null || list.size() == 0) {
			throw new InvalidationException("해당되는 평가결과 정보가 없습니다.");
		}
		FrontEvlResultListDto listDto = list.get(0);


		if(slctnObjcReqst == null || CoreUtils.string.isBlank(slctnObjcReqst.getObjcReqstCn())) {
			throw new InvalidationException("결과이의신청 내용을 입력해주세요.");
		}

		Date now = new Date();
//		String[] exts = {"pdf"};
		if(!CoreUtils.string.equals("최종선정", listDto.getEvlStepNm())) {
			Long evltrgetCnt = usptEvlSlctnObjcReqstDao.selectCountByTrgetId(evlTrgetId);
			if(evltrgetCnt != 0L) {
				throw new InvalidationException("결과이의신청 정보가 존재합니다.\n이의신청은 한번만 등록할 수 있습니다.");
			}

			UsptEvlSlctnObjcReqst evlSlctnObjcReqst = new UsptEvlSlctnObjcReqst();
			evlSlctnObjcReqst.setEvlTrgetId(evlTrgetId);
			evlSlctnObjcReqst.setCreatedDt(now);
			evlSlctnObjcReqst.setCreatorId(worker.getMemberId());
			evlSlctnObjcReqst.setUpdatedDt(now);
			evlSlctnObjcReqst.setUpdaterId(worker.getMemberId());
			evlSlctnObjcReqst.setObjcApplcntId(worker.getMemberId());
			evlSlctnObjcReqst.setObjcReqstCn(slctnObjcReqst.getObjcReqstCn());
			evlSlctnObjcReqst.setObjcReqstDt(now);
			evlSlctnObjcReqst.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.신청);
			evlSlctnObjcReqst.setEvlSlctnObjcReqstId(CoreUtils.string.getNewId(Code.prefix.평가선정이의신청));

			if(fileList != null && fileList.size() > 0) {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				evlSlctnObjcReqst.setApplcntAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
			usptEvlSlctnObjcReqstDao.insert(evlSlctnObjcReqst);

			UsptEvlObjcProcessHist hist = new UsptEvlObjcProcessHist();
			hist.setEvlObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.평가이의처리이력));
			hist.setEvlSlctnObjcReqstId(evlSlctnObjcReqst.getEvlSlctnObjcReqstId());
			hist.setResnCn(evlSlctnObjcReqst.getObjcReqstCn());
			hist.setSlctnObjcProcessSttusCd(evlSlctnObjcReqst.getLastSlctnObjcProcessSttusCd());
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			usptEvlObjcProcessHistDao.insert(hist);
		} else {
			Long trgetCnt = usptLastSlctnObjcReqstDao.selectCountByTrgetId(evlTrgetId);
			if(trgetCnt != 0L) {
				throw new InvalidationException("결과이의신청 정보가 존재합니다.\n이의신청은 한번만 등록할 수 있습니다.");
			}

			UsptLastSlctnObjcReqst lastSlctnObjcReqst = new UsptLastSlctnObjcReqst();
			lastSlctnObjcReqst.setLastSlctnTrgetId(evlTrgetId);
			lastSlctnObjcReqst.setCreatedDt(now);
			lastSlctnObjcReqst.setCreatorId(worker.getMemberId());
			lastSlctnObjcReqst.setUpdatedDt(now);
			lastSlctnObjcReqst.setUpdaterId(worker.getMemberId());
			lastSlctnObjcReqst.setObjcApplcntId(worker.getMemberId());
			lastSlctnObjcReqst.setObjcReqstCn(slctnObjcReqst.getObjcReqstCn());
			lastSlctnObjcReqst.setObjcReqstDt(now);
			lastSlctnObjcReqst.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.신청);
			lastSlctnObjcReqst.setLastSlctnObjcReqstId(CoreUtils.string.getNewId(Code.prefix.최종선정이의신청));

			if(fileList != null && fileList.size() > 0) {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				lastSlctnObjcReqst.setApplcntAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
			usptLastSlctnObjcReqstDao.insert(lastSlctnObjcReqst);

			UsptLastSlctnObjcProcessHist hist = new UsptLastSlctnObjcProcessHist();
			hist.setLastSlctnObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.최종선정이의처리이력));
			hist.setLastSlctnObjcReqstId(lastSlctnObjcReqst.getLastSlctnObjcReqstId());
			hist.setResnCn(lastSlctnObjcReqst.getObjcReqstCn());
			hist.setSlctnObjcProcessSttusCd(lastSlctnObjcReqst.getLastSlctnObjcProcessSttusCd());
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			usptLastSlctnObjcProcessHistDao.insert(hist);
		}
	}
}
