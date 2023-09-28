package aicluster.pms.api.bsns.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfDownUtils;
import aicluster.framework.common.util.dto.LogIndvdInfTrgtItem;
import aicluster.framework.common.util.dto.LogIndvdlInfDown;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsRecipientResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.bsns.dto.EntrpsSttusListParam;
import aicluster.pms.api.bsns.dto.EntrpsSttusSendParam;
import aicluster.pms.api.evl.dto.DspthResultDto;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyDao;
import aicluster.pms.common.dto.EntrpsSttusListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EntrpsSttusSerivce {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	@Autowired
	private UsptBsnsPblancApplyTaskDao usptBsnsPblancApplyTaskDao;
	@Autowired
	private UsptTaskPrtcmpnyDao UsptTaskPrtcmpnyDao;
	@Autowired
	private LogIndvdlInfDownUtils indvdlInfDownUtils;


	/**
	 * 업체현황 목록 조회
	 * @param entrpsSttusListParam
	 * @param page
	 * @return
	 */
	public CorePagination<EntrpsSttusListDto> getList(EntrpsSttusListParam entrpsSttusListParam, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(entrpsSttusListParam.getItemsPerPage() == null) {
			entrpsSttusListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		entrpsSttusListParam.setIsExcel(false);
		entrpsSttusListParam.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsPblancApplcntDao.selectEntrpsSttusListCount(entrpsSttusListParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, entrpsSttusListParam.getItemsPerPage(), totalItems);
		entrpsSttusListParam.setBeginRowNum(info.getBeginRowNum());
		List<EntrpsSttusListDto> list = usptBsnsPblancApplcntDao.selectEntrpsSttusList(entrpsSttusListParam);
		//출력할 자료 생성 후 리턴
		CorePagination<EntrpsSttusListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 업체현황 목록 엑셀 저장
	 * @param request
	 * @param entrpsSttusListParam
	 * @return
	 */
	public List<EntrpsSttusListDto> getListExcelDwld(HttpServletRequest request, EntrpsSttusListParam entrpsSttusListParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		entrpsSttusListParam.setIsExcel(true);
		entrpsSttusListParam.setInsiderId(worker.getMemberId());
		List<EntrpsSttusListDto> list = usptBsnsPblancApplcntDao.selectEntrpsSttusList(entrpsSttusListParam);

		if(CoreUtils.string.isBlank(entrpsSttusListParam.getExcelDwldWorkCn())) {
			throw new InvalidationException("엑셀다운 작업내용을 입력해주세요");
		}
		List<LogIndvdInfTrgtItem> logTrgt = new ArrayList<>();
		for (EntrpsSttusListDto excelDto : list) {
			LogIndvdInfTrgtItem trgtItem = new LogIndvdInfTrgtItem();
			trgtItem.setTrgterId(excelDto.getMemberId());
			trgtItem.setTrgterNm(excelDto.getEntrpsNm());
			trgtItem.setEmail(excelDto.getEmail());
			trgtItem.setBirthday("");
			trgtItem.setMobileNo(excelDto.getMbtlnum());
			logTrgt.add(trgtItem);
		}
		LogIndvdlInfDown logParam = LogIndvdlInfDown.builder()
				.memberId(worker.getMemberId())
				.memberIp(webutils.getRemoteIp(request))
				.workTypeNm("엑셀다운로드")
				.workCn(entrpsSttusListParam.getExcelDwldWorkCn())
				.menuId("menu-ADM010201")
				.menuNm("업체현황조회")
				.fileNm("업체현황.xlsx")
				.trgtIdList(logTrgt)
				.build();
		indvdlInfDownUtils.insertList(logParam);
		return list;
	}


	/**
	 * 메시지 전송
	 * @param param
	 * @return
	 */
	public DspthResultDto sendMsg(EntrpsSttusSendParam param) {
		SecurityUtils.checkWorkerIsInsider();
		StringBuffer sbMsg = new StringBuffer();

		DspthResultDto resultDto = new DspthResultDto();
		if((param.getApplyIdList() == null || param.getApplyIdList().size() == 0)
				&& (param.getTaskPartcptnEntrprsIdList() == null) || param.getTaskPartcptnEntrprsIdList().size() == 0) {
			throw new InvalidationException("발송대상이 설정되지 않았습니다.");
		}
		List<EntrpsSttusListDto> list = usptBsnsPblancApplcntDao.selectContactList(param);

		String emailCn = CoreUtils.file.readFileString("/form/email/email-common.html");
		SmsSendParam smsParam = new SmsSendParam();
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle("AICA 안내문");

		resultDto.setTotalCnt(list.size());

		for(EntrpsSttusListDto info : list) {
			esParam.setContentHtml(true);
			if(param.getSndngMth().contains(Code.sndngMth.이메일)) {
				log.debug("##### email : {}", info.getEmail());
				if(CoreUtils.string.isNotEmpty(info.getEmail())){
					Map<String, String> templateParam = new HashMap<>();
					templateParam.put("memNm", info.getRspnberNm());
					templateParam.put("cnVal", param.getSndngCn());
					esParam.addRecipient(info.getEmail(), info.getRspnberNm(), templateParam);
				} else {
					log.debug("[" + info.getRspnberNm() +"] 이메일 정보가 없습니다.");
					sbMsg.append("[" + info.getRspnberNm() +"] 이메일 정보가 없습니다.\n");
				}
			}

			if(param.getSndngMth().contains(Code.sndngMth.SMS)){
				log.debug("##### 전화번호 : {}", info.getMbtlnum());
				if(CoreUtils.string.isNotEmpty(info.getMbtlnum())){
					smsParam.setSmsCn("AICA 안내문\n " + param.getSndngCn());
					smsParam.addRecipient(info.getMbtlnum());
				} else {
					log.debug("[" + info.getRspnberNm() +"] 핸드폰번호 정보가 없습니다.");
					sbMsg.append("[" + info.getRspnberNm() +"] 핸드폰번호 정보가 없습니다.\n");
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

		Date now = new Date();
		UsptTaskPrtcmpny regTaskInfo = new UsptTaskPrtcmpny();
		regTaskInfo.setUpdatedDt(now);
		UsptBsnsPblancApplyTask regApplyInfo = new UsptBsnsPblancApplyTask();
		regApplyInfo.setUpdatedDt(now);
		regApplyInfo.setRecentSendDate(now);

		for(EntrpsSttusListDto info : list) {
			if(er != null) {
				resultDto.setEmailTotalCnt(er.getTotalCnt());
				resultDto.setEmailSuccessCnt(er.getSuccessCnt());
				resultDto.setEmailFailCnt(er.getFailCnt());
				List<EmailReceiverResult> erList = er.getReceiverList();
				Optional<EmailReceiverResult> opt = erList.stream().filter(x->x.getReceiveMailAddr().equals(info.getEmail())).findFirst();
				if(opt.isPresent()) {
					EmailReceiverResult result = opt.get();
					if(result.getIsSuccessful()) {
						if(CoreUtils.string.isBlank(info.getTaskPartcptnEntrprsId())) {
							regApplyInfo.setApplyId(info.getApplyId());
							usptBsnsPblancApplyTaskDao.updateRecentSendDate(regApplyInfo);
						} else {
							regTaskInfo.setTaskPartcptnEntrprsId(info.getTaskPartcptnEntrprsId());
							UsptTaskPrtcmpnyDao.updateRecentSendDate(regTaskInfo);
						}
					} else {
						sbMsg.append("[" + info.getRspnberNm() +"] 이메일 발송이 실패하였습니다.\n");
					}
				}
			}
			if(sr != null) {
				resultDto.setSmsTotalCnt(sr.getTotalCnt());
				resultDto.setSmsSuccessCnt(sr.getSuccessCnt());
				resultDto.setSmsFailCnt(sr.getFailCnt());
				List<SmsRecipientResult> srList = sr.getRecipientList();
				Optional<SmsRecipientResult> opt = srList.stream().filter(x->x.getRecipientNo().equals(info.getMbtlnum())).findFirst();
				if(opt.isPresent()) {
					SmsRecipientResult result = opt.get();
					if(result.getIsSuccessful()) {
						if(CoreUtils.string.isBlank(info.getTaskPartcptnEntrprsId())) {
							regApplyInfo.setApplyId(info.getApplyId());
							usptBsnsPblancApplyTaskDao.updateRecentSendDate(regApplyInfo);
						} else {
							regTaskInfo.setTaskPartcptnEntrprsId(info.getTaskPartcptnEntrprsId());
							UsptTaskPrtcmpnyDao.updateRecentSendDate(regTaskInfo);
						}
					} else {
						sbMsg.append("[" + info.getRspnberNm() +"] SMS 발송이 실패하였습니다.\n");
					}
				}
			}
		}
		resultDto.setResultMessage(sbMsg.toString());
		return resultDto;
	}
}
