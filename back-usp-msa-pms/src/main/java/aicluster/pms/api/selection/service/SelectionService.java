package aicluster.pms.api.selection.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsRecipientResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.selection.dto.LastSelectionDspthParam;
import aicluster.pms.api.selection.dto.LastSelectionParam;
import aicluster.pms.api.selection.dto.SelectionDto;
import aicluster.pms.api.selection.dto.SelectionListParam;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsSlctnDao;
import aicluster.pms.common.dao.UsptEvlLastSlctnDao;
import aicluster.pms.common.dao.UsptLastSlctnProcessHistDao;
import aicluster.pms.common.dao.UsptLastSlctnTrgetDao;
import aicluster.pms.common.dto.DspthTrgetListDto;
import aicluster.pms.common.dto.LastSlctnProcessHistDto;
import aicluster.pms.common.dto.SelectionListDto;
import aicluster.pms.common.dto.SlctnDetailDto;
import aicluster.pms.common.dto.SlctnTrgetListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsSlctn;
import aicluster.pms.common.entity.UsptEvlLastSlctn;
import aicluster.pms.common.entity.UsptLastSlctnProcessHist;
import aicluster.pms.common.entity.UsptLastSlctnTrget;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class SelectionService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptEvlLastSlctnDao usptEvlLastSlctnDao;
	@Autowired
	private UsptLastSlctnProcessHistDao usptLastSlctnProcessHistDao;
	@Autowired
	private UsptLastSlctnTrgetDao usptLastSlctnTrgetDao;
	@Autowired
	private UsptBsnsSlctnDao usptBsnsSlctnDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	/**
	 * 선정 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<SelectionListDto> getList(SelectionListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptEvlLastSlctnDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<SelectionListDto> list = usptEvlLastSlctnDao.selectList(param);
		CorePagination<SelectionListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 선정 목록 엑셀 저장을 위한 조회
	 * @param param
	 * @return
	 */
	public List<SelectionListDto> getListExcelDwld(SelectionListParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptEvlLastSlctnDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptEvlLastSlctnDao.selectList(param);
	}

	/**
	 * 선정 상세조회
	 * @param evlLastSlctnId
	 * @return
	 */
	public SelectionDto get(String evlLastSlctnId){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnDetailDto slctnInfo = usptEvlLastSlctnDao.selectSlctnDetail(evlLastSlctnId, worker.getMemberId());
		if(slctnInfo == null) {
			throw new InvalidationException("요청하신 선정정보가 없습니다.");
		}
		SelectionDto dto = new SelectionDto();
		dto.setSlctnInfo(slctnInfo);
		dto.setSlctnTrgetList(usptEvlLastSlctnDao.selectSlctnTrgetList(evlLastSlctnId, worker.getMemberId()));
		return dto;
	}

	/**
	 * 최종선정 처리
	 * @param evlLastSlctnId
	 * @param paramList
	 * @return
	 */
	public List<SlctnTrgetListDto> modifyLastSelection(String evlLastSlctnId, List<LastSelectionParam> paramList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnDetailDto slctnInfo = usptEvlLastSlctnDao.selectSlctnDetail(evlLastSlctnId, worker.getMemberId());
		if(slctnInfo == null) {
			throw new InvalidationException("요청하신 선정정보가 없습니다.");
		}
		if(slctnInfo.getLastSlctn() != null && slctnInfo.getLastSlctn()) {
			throw new InvalidationException("이미 최종선정된 정보입니다.\n수정할 수 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, slctnInfo.getChargerAuthorCd())) {
			throw new InvalidationException("요청하신 권한이 없습니다.");
		}

		List<SlctnTrgetListDto> trgetList = usptEvlLastSlctnDao.selectSlctnTrgetList(evlLastSlctnId, worker.getMemberId());
		if(trgetList.size() != paramList.size()) {
			throw new InvalidationException("선정대상 전체의 최종선정 정보를 요청해주세요!");
		}

		Date now = new Date();
		for(LastSelectionParam param : paramList) {
			if(CoreUtils.string.isBlank(param.getApplyId())){
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "신청ID"));
			}
			if(CoreUtils.string.isBlank(param.getSlctnAt())){
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "선정여부"));
			}

			// 자원할당사업이 아니고 사업비심의조정일 경우 필수값 체크
			if(!CoreUtils.string.equals(Code.bsnsType.자원할당사업, slctnInfo.getBsnsTypeCd()) && slctnInfo.getWctMdatDlbrt() != null && slctnInfo.getWctMdatDlbrt()) {
				if(param.getTotalWct() == null){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "총사업비"));
				}
				if(param.getSportBudget() == null){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "지원예산"));
				}
				if(param.getAlotmCash() == null){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "부담금현금"));
				}
				if(param.getAlotmActhng() == null){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "부담금현물"));
				}
			}

			UsptLastSlctnTrget trgetInfo = new  UsptLastSlctnTrget();
			trgetInfo.setApplyId(param.getApplyId());
			trgetInfo.setEvlLastSlctnId(evlLastSlctnId);
			if(CoreUtils.string.equalsIgnoreCase("Y", param.getSlctnAt())) {
				trgetInfo.setSlctn(true);
			} else {
				trgetInfo.setSlctn(false);
			}
			trgetInfo.setCreatedDt(now);
			trgetInfo.setCreatorId(worker.getMemberId());
			trgetInfo.setLastSlctnTrgetId(CoreUtils.string.getNewId(Code.prefix.최종선정대상));
			usptLastSlctnTrgetDao.insert(trgetInfo);

			if(CoreUtils.string.equalsIgnoreCase("Y", param.getSlctnAt())) {
				UsptBsnsSlctn bsInfo = new UsptBsnsSlctn();
				bsInfo.setLastSlctnTrgetId(trgetInfo.getLastSlctnTrgetId());
				bsInfo.setTotalWct(param.getTotalWct());
				bsInfo.setSportBudget(param.getSportBudget());
				bsInfo.setAlotmCash(param.getAlotmCash());
				bsInfo.setAlotmActhng(param.getAlotmActhng());
				bsInfo.setCreatedDt(now);
				bsInfo.setCreatorId(worker.getMemberId());
				bsInfo.setBsnsSlctnId(CoreUtils.string.getNewId(Code.prefix.사업선정대상));
				usptBsnsSlctnDao.insert(bsInfo);

				/* 신청자정보에 선정여부 갱신 */
				UsptBsnsPblancApplcnt applcntInfo = usptBsnsPblancApplcntDao.select(trgetInfo.getApplyId());
				applcntInfo.setUpdatedDt(now);
				applcntInfo.setUpdaterId(worker.getMemberId());
				applcntInfo.setSlctn(true);
				applcntInfo.setSlctnDt(now);
				if(usptBsnsPblancApplcntDao.update(applcntInfo) != 1) {
					throw new InvalidationException("최종선정 처리 중 오류가발생했습니다.");
				}
			}
		}

		UsptEvlLastSlctn evlLastSlctnInfo = usptEvlLastSlctnDao.select(evlLastSlctnId);
		evlLastSlctnInfo.setLastSlctnProcessDivCd(Code.lastSlctnProcessDiv.최종선정);
		evlLastSlctnInfo.setUpdatedDt(now);
		evlLastSlctnInfo.setUpdaterId(worker.getMemberId());
		evlLastSlctnInfo.setLastSlctn(true);
		evlLastSlctnInfo.setLastSlctnDt(now);
		if(evlLastSlctnInfo.getSlctnDspth() == null) {
			evlLastSlctnInfo.setSlctnDspth(false);
		}
		if(evlLastSlctnInfo.getPtrmsnDspth() == null) {
			evlLastSlctnInfo.setPtrmsnDspth(false);
		}
		if(usptEvlLastSlctnDao.update(evlLastSlctnInfo) != 1) {
			throw new InvalidationException("최종선정 처리 중 오류가발생했습니다.");
		}

		UsptLastSlctnProcessHist hist = new UsptLastSlctnProcessHist();
		hist.setCreatedDt(now);
		hist.setCreatorId(worker.getMemberId());
		hist.setEvlLastSlctnId(evlLastSlctnId);
		hist.setLastSlctnProcessDivCd(evlLastSlctnInfo.getLastSlctnProcessDivCd());
		hist.setResnCn("최종선정 처리되었습니다.");
		hist.setLastSlctnProcessHistId(CoreUtils.string.getNewId(Code.prefix.최종선정처리이력));
		usptLastSlctnProcessHistDao.insert(hist);

		return usptEvlLastSlctnDao.selectSlctnTrgetList(evlLastSlctnId, worker.getMemberId());
	}


	/**
	 * 통보
	 * @param evlLastSlctnId
	 * @param param
	 * @return
	 */
	public String modifyDspth(String evlLastSlctnId, LastSelectionDspthParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnDetailDto slctnInfo = usptEvlLastSlctnDao.selectSlctnDetail(evlLastSlctnId, worker.getMemberId());
		if(slctnInfo == null) {
			throw new InvalidationException("요청하신 선정정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, slctnInfo.getChargerAuthorCd())) {
			throw new InvalidationException("요청하신 권한이 없습니다.");
		}
		if(slctnInfo.getLastSlctn() == null || !slctnInfo.getLastSlctn()) {
			throw new InvalidationException("최종선정 처리 후 통보가 가능합니다. ");
		}

		if(CoreUtils.string.isBlank(param.getSndngTrget())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송대상"));
		}
		if(CoreUtils.string.isBlank(param.getSndngMth())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송방법"));
		}
		if(CoreUtils.string.isBlank(param.getSndngCn())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송내용"));
		}

		Date now = new Date();
		Boolean slctn = param.getSndngTrget().equalsIgnoreCase("slctn") ? true : false;

		UsptEvlLastSlctn evlLastSlctninfo = usptEvlLastSlctnDao.select(evlLastSlctnId);
		evlLastSlctninfo.setUpdatedDt(now);
		evlLastSlctninfo.setUpdaterId(worker.getMemberId());
		evlLastSlctninfo.setLastSlctnProcessDivCd(Code.lastSlctnProcessDiv.통보);
		if(slctn) {
			evlLastSlctninfo.setSlctnDspth(true);
			evlLastSlctninfo.setSlctnDspthDt(now);
			evlLastSlctninfo.setSlctnSndngCn(param.getSndngCn());
			evlLastSlctninfo.setSlctnSndngMth(param.getSndngMth());
		} else {
			evlLastSlctninfo.setPtrmsnDspth(true);
			evlLastSlctninfo.setPtrmsnDspthDt(now);
			evlLastSlctninfo.setPtrmsnSndngCn(param.getSndngCn());
			evlLastSlctninfo.setPtrmsnSndngMth(param.getSndngMth());
		}
		if(usptEvlLastSlctnDao.update(evlLastSlctninfo) != 1) {
			throw new InvalidationException("발송 중 오류가 발생했습니다.");
		}

		UsptLastSlctnProcessHist hist = new UsptLastSlctnProcessHist();
		hist.setCreatedDt(now);
		hist.setCreatorId(worker.getMemberId());
		hist.setEvlLastSlctnId(evlLastSlctnId);
		hist.setLastSlctnProcessDivCd(evlLastSlctninfo.getLastSlctnProcessDivCd());
		hist.setResnCn(param.getSndngCn());
		hist.setLastSlctnProcessHistId(CoreUtils.string.getNewId(Code.prefix.최종선정처리이력));
		usptLastSlctnProcessHistDao.insert(hist);

		List<DspthTrgetListDto> list = usptEvlLastSlctnDao.selectDspthTrgetList(evlLastSlctnId, slctn, worker.getMemberId());

		StringBuffer sbMsg = new StringBuffer();
		String emailCn = CoreUtils.file.readFileString("/form/email/email-common.html");
		SmsSendParam smsParam = new SmsSendParam();
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle("AICA 안내문");

		for(DspthTrgetListDto info : list) {
			esParam.setContentHtml(true);
			if(param.getSndngMth().contains(Code.sndngMth.이메일)) {
				if(CoreUtils.string.isNotEmpty(info.getEmail())){
					Map<String, String> templateParam = new HashMap<>();
					templateParam.put("memNm", info.getMemberNm());
					templateParam.put("cnVal", param.getSndngCn());
					 esParam.addRecipient(info.getEmail(), info.getMemberNm(), templateParam);
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.\n");
				}
			}

			if(param.getSndngMth().contains(Code.sndngMth.SMS)){
				if(CoreUtils.string.isNotEmpty(info.getMobileNo())){
					smsParam.setSmsCn("AICA 안내문\n " + param.getSndngCn());
					smsParam.addRecipient(info.getMobileNo());
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.\n");
				}
			}
		}

		EmailResult er = null;
		SmsResult sr = null;
		if(param.getSndngMth().contains(Code.sndngMth.이메일) && esParam.getRecipientList() != null && esParam.getRecipientList().size() > 0 ) {
			er = emailUtils.send(esParam);
		}
		if(param.getSndngMth().contains(Code.sndngMth.SMS) && smsParam.getRecipientList() != null && smsParam.getRecipientList().size() > 0 ){
			sr = smsUtils.send(smsParam);
		}

		UsptTaskPrtcmpny regTaskInfo = new UsptTaskPrtcmpny();
		regTaskInfo.setUpdatedDt(now);
		UsptBsnsPblancApplyTask regApplyInfo = new UsptBsnsPblancApplyTask();
		regApplyInfo.setUpdatedDt(now);
		for(DspthTrgetListDto info : list) {
			if(er != null) {
				List<EmailReceiverResult> erList = er.getReceiverList();
				Optional<EmailReceiverResult> opt = erList.stream().filter(x->x.getReceiveMailAddr().equals(info.getEmail())).findFirst();
				if(opt.isPresent()) {
					EmailReceiverResult result = opt.get();
					if(result.getIsSuccessful()) {
					} else {
						sbMsg.append("[" + info.getMemberNm() +"] 이메일 발송이 실패하였습니다.\n");
					}
				}
			}
			if(sr != null) {
				List<SmsRecipientResult> srList = sr.getRecipientList();
				Optional<SmsRecipientResult> opt = srList.stream().filter(x->x.getRecipientNo().equals(info.getMobileNo())).findFirst();
				if(opt.isPresent()) {
					SmsRecipientResult result = opt.get();
					if(result.getIsSuccessful()) {
					} else {
						sbMsg.append("[" + info.getMemberNm() +"] SMS 발송이 실패하였습니다.\n");
					}
				}
			}
		}

		return sbMsg.toString();
	}


	/**
	 * 선정 처리이력 목록 조회
	 * @param evlLastSlctnId
	 * @return
	 */
	public JsonList<LastSlctnProcessHistDto> getHist(String evlLastSlctnId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnDetailDto slctnInfo = usptEvlLastSlctnDao.selectSlctnDetail(evlLastSlctnId, worker.getMemberId());
		if(slctnInfo == null) {
			throw new InvalidationException("요청하신 선정정보가 없습니다.");
		}
		return new JsonList<>(usptLastSlctnProcessHistDao.selectList(evlLastSlctnId));
	}
}
