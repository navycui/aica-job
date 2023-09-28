package aicluster.pms.api.stdnt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsRecipientResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.stdnt.dto.StdntCareerParam;
import aicluster.pms.api.stdnt.dto.StdntCareerReqParam;
import aicluster.pms.common.dao.UsptAcdmcrDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptCrqfcDao;
import aicluster.pms.common.dao.UsptEtcCareerDao;
import aicluster.pms.common.dao.UsptJobCareerDao;
import aicluster.pms.common.dao.UsptMsvcDao;
import aicluster.pms.common.dao.UsptProgrmDao;
import aicluster.pms.common.dao.UsptWnpzDao;
import aicluster.pms.common.dto.StdntCareerDto;
import aicluster.pms.common.dto.StdntCareerListDto;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StdntCareerService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private UsptAcdmcrDao usptAcdmcrDao;
	@Autowired
	private UsptMsvcDao usptMsvcDao;
	@Autowired
	private UsptCrqfcDao usptCrqfcDao;
	@Autowired
	private UsptJobCareerDao usptJobCareerDao;
	@Autowired
	private UsptEtcCareerDao usptEtcCareerDao;
	@Autowired
	private UsptWnpzDao usptWnpzDao;
	@Autowired
	private UsptProgrmDao usptProgrmDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<StdntCareerListDto> getList(StdntCareerParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsPblancApplcntDao.selectStdntCareerListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<StdntCareerListDto> list = usptBsnsPblancApplcntDao.selectStdntCareerList(param);
		CorePagination<StdntCareerListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	public List<StdntCareerListDto> getListExcelDwld(StdntCareerParam param){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsPblancApplcntDao.selectStdntCareerListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptBsnsPblancApplcntDao.selectStdntCareerList(param);
	}


	/**
	 * 등록요청
	 * @param param
	 * @return
	 */
	public String addRegistReq(StdntCareerReqParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		StdntCareerParam listParam = new StdntCareerParam();
		listParam.setInsiderId(worker.getMemberId());
		listParam.setApplyIdList(param.getApplyIdList());
		List<StdntCareerListDto> list = this.getListExcelDwld(listParam);
		if(!list.isEmpty()) {
			if(!CoreUtils.string.equals(list.get(0).getChargerAuthorCd(), Code.사업담당자_수정권한)) {
				throw new InvalidationException("처리 권한이 없습니다.");
			}
		} else {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		if(CoreUtils.string.isBlank(param.getSndngMth())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송방법"));
		}
		if(CoreUtils.string.isBlank(param.getSndngCn())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송내용"));
		}

		/* 이메일 / SMS 전송 Start -------------------------------------------------------------------*/
		String emailCn = CoreUtils.file.readFileString("/form/email/email-common.html");
		SmsSendParam smsParam = new SmsSendParam();
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle("AICA 안내문");

		StringBuffer sbMsg = new StringBuffer();
		for(StdntCareerListDto info : list) {
			esParam.setContentHtml(true);
			if(param.getSndngMth().contains(Code.sndngMth.이메일)) {
				if(CoreUtils.string.isNotEmpty(info.getEmail())){
					Map<String, String> templateParam = new HashMap<>();
					templateParam.put("memNm", info.getMemberNm());
					templateParam.put("cnVal", param.getSndngCn());
					esParam.addRecipient(info.getEmail(), info.getMemberNm(), templateParam);
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.\n");
					log.debug("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.");
				}
			}

			if(param.getSndngMth().contains(Code.sndngMth.SMS)){
				if(CoreUtils.string.isNotEmpty(info.getMobileNo())){
					smsParam.setSmsCn("AICA 안내문\n " + param.getSndngCn());
					smsParam.addRecipient(info.getMobileNo());
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.\n");
					log.debug("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.");
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
		/* 이메일 / SMS 전송 End -------------------------------------------------------------------*/

		for(StdntCareerListDto info : list) {
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
	 * 교육생경력 상세조회
	 * @param request
	 * @param applyId
	 * @return
	 */
	public StdntCareerDto get(HttpServletRequest request, String applyId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		List<String> applyIdList = new ArrayList<String>();
		applyIdList.add(applyId);

		StdntCareerParam listParam = new StdntCareerParam();
		listParam.setInsiderId(worker.getMemberId());
		listParam.setApplyIdList(applyIdList);
		List<StdntCareerListDto> list = this.getListExcelDwld(listParam);

		if(list.isEmpty()) {
			throw new InvalidationException("요청하신 교육생 정보가 없습니다.");
		}

		StdntCareerListDto info = list.get(0);
		StdntCareerDto dto = new StdntCareerDto();
		BeanUtils.copyProperties(info, dto);
		dto.setEmail(info.getEmail());
		dto.setMobileNo(info.getMobileNo());

		dto.setAcdmcrList(usptAcdmcrDao.selectList(info.getMemberId()));
		dto.setMsvcInfo(usptMsvcDao.select(info.getMemberId()));
		dto.setCrqfcList(usptCrqfcDao.selectList(info.getMemberId()));
		dto.setJobCareerList(usptJobCareerDao.selectList(info.getMemberId()));
		dto.setEtcCareerList(usptEtcCareerDao.selectList(info.getMemberId()));
		dto.setWnpzList(usptWnpzDao.selectList(info.getMemberId()));
		dto.setProgrmList(usptProgrmDao.selectList(info.getMemberId()));

		// 이력 생성
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("교육생이력 상세조회")
				.trgterId(info.getMemberId())
				.email(dto.getEmail())
				.birthday("")
				.mobileNo(dto.getMobileNo())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return dto;
	}
}
