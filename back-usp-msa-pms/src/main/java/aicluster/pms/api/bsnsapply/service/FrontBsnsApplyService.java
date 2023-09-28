package aicluster.pms.api.bsnsapply.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.config.DocsConfig;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.bsns.dto.Chklst;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplicantDto;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyDto;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyInfoDto;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyListParam;
import aicluster.pms.api.bsnsapply.dto.FrontBsnsApplyParam;
import aicluster.pms.api.bsnsapply.dto.FrontReqReasonDto;
import aicluster.pms.api.bsnsapply.dto.PdfGatewayDto;
import aicluster.pms.api.common.dto.StreamDocsResponseDto;
import aicluster.pms.common.dao.CmmtEntDao;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptAtchmnflMapngDao;
import aicluster.pms.common.dao.UsptBsnsApplyRealmDao;
import aicluster.pms.common.dao.UsptBsnsAtchmnflSetupDao;
import aicluster.pms.common.dao.UsptBsnsChklstDao;
import aicluster.pms.common.dao.UsptBsnsDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntEntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyAttachDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyChklstDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskPartcptsDao;
import aicluster.pms.common.dao.UsptBsnsPblancDao;
import aicluster.pms.common.dto.ApplyMberTypeDto;
import aicluster.pms.common.dto.BsnsBasicDto;
import aicluster.pms.common.dto.FrontBsnsApplyListDto;
import aicluster.pms.common.dto.FrontBsnsPblancDto;
import aicluster.pms.common.entity.UsptAtchmnflMapng;
import aicluster.pms.common.entity.UsptBsnsAtchmnflSetup;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplyAttach;
import aicluster.pms.common.entity.UsptBsnsPblancApplyChklst;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import aicluster.pms.common.util.Code;
import aicluster.pms.config.PdfConfig;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FrontBsnsApplyService {
	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private TermsUtils termsUtils;
	@Autowired
	private EnvConfig config;
	@Autowired
	private PdfConfig pdfConfig;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private CmmtEntDao cmmtEntDao;
	@Autowired
	private CmmtMemberDao cmmtMemberDao;
	@Autowired
	private UsptBsnsDao usptBsnsDao;
	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;
	@Autowired
	private UsptBsnsChklstDao usptBsnsChklstDao;
	@Autowired
	private UsptBsnsApplyRealmDao usptBsnsApplyRealmDao;
	@Autowired
	private UsptBsnsAtchmnflSetupDao usptBsnsAtchmnflSetupDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	@Autowired
	private UsptBsnsPblancApplyTaskDao usptBsnsPblancApplyTaskDao;
	@Autowired
	private UsptBsnsPblancApplcntEntDao usptBsnsPblancApplcntEntDao;
	@Autowired
	private UsptBsnsPblancApplyChklstDao usptBsnsPblancApplyChklstDao;
	@Autowired
	private UsptBsnsPblancApplyAttachDao usptBsnsPblancApplyAttachDao;
	@Autowired
	private UsptBsnsPblancApplyTaskPartcptsDao usptBsnsPblancApplyTaskPartcptsDao;

	@Autowired
	private DocsConfig docsConfig;
	@Autowired
	private UsptAtchmnflMapngDao usptAtchmnflMapngDao;

	/**
	 * 필수확인사항 조회
	 * @param pblancId
	 * @return
	 */
	public JsonList<Chklst> getChklst(String pblancId) {
		BnMember worker = SecurityUtils.checkLogin();
		if(usptBsnsPblancApplcntDao.selectApplcntCount(pblancId, worker.getMemberId()) > 0 ){
			throw new InvalidationException("이미 신청한 사업공고입니다.");
		}
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.pblancSttus.접수중, info.getPblancSttusCd())){
			throw new InvalidationException("모집상태가 접수중인 공고만 지원가능합니다.");
		}

		ApplyMberTypeDto mberType = usptBsnsPblancDao.selectApplyMberType(pblancId);
		if(!mberType.getMberTypeCd().contains(worker.getMemberType())) {
			throw new InvalidationException("지원가능한 회원이 아닙니다.\n[지원가능 회원유형:" + mberType.getMberType()+"]");
		}
		return new JsonList<>(usptBsnsChklstDao.selectList(info.getBsnsCd()));
	}

	/**
	 * 신청자 정보 조회
	 * @param pblancId
	 * @return
	 */
	public FrontBsnsApplicantDto getApplicant(String pblancId) {
		BnMember worker = SecurityUtils.checkLogin();
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.pblancSttus.접수중, info.getPblancSttusCd())){
			throw new InvalidationException("모집상태가 접수중인 공고만 지원가능합니다.");
		}
		ApplyMberTypeDto mberType = usptBsnsPblancDao.selectApplyMberType(pblancId);
		if(!mberType.getMberTypeCd().contains(worker.getMemberType())) {
			throw new InvalidationException("지원가능한 회원이 아닙니다.\n[지원가능 회원유형:" + mberType.getMberType()+"]");
		}
		FrontBsnsApplicantDto dto = new FrontBsnsApplicantDto();
		dto.setCmmtMember(cmmtMemberDao.select(worker.getMemberId()));

		if(!CoreUtils.string.equals(worker.getMemberType(), Code.memberType.개인사용자)) {
			dto.setCmmtEnt(cmmtEntDao.select(worker.getMemberId()));
		}

		return dto;
	}

	/**
	 * 신청정보 조회
	 * @param pblancId
	 * @return
	 */
	public FrontBsnsApplyDto getApply(String pblancId) {
		BnMember worker = SecurityUtils.checkLogin();
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.pblancSttus.접수중, info.getPblancSttusCd())){
			throw new InvalidationException("모집상태가 접수중인 공고만 지원가능합니다.");
		}
		ApplyMberTypeDto mberType = usptBsnsPblancDao.selectApplyMberType(pblancId);
		if(!mberType.getMberTypeCd().contains(worker.getMemberType())) {
			throw new InvalidationException("지원가능한 회원이 아닙니다.\n[지원가능 회원유형:" + mberType.getMberType()+"]");
		}
		BsnsBasicDto bsnsInfo = usptBsnsDao.select(info.getBsnsCd());

		FrontBsnsApplyDto applyDto = new FrontBsnsApplyDto();
		applyDto.setBsnsTypeCd(bsnsInfo.getBsnsTypeCd());
		applyDto.setBsnsTypeNm(bsnsInfo.getBsnsTypeNm());
		applyDto.setTaskTypeCd(bsnsInfo.getTaskTypeCd());
		Date bsnsBgndt = string.toDate(info.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(info.getBsnsEndde());
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			applyDto.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				applyDto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				applyDto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			applyDto.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}

		applyDto.setApplyRealmList(usptBsnsApplyRealmDao.selectList(info.getBsnsCd()));
		applyDto.setAtchmnflList(usptBsnsAtchmnflSetupDao.selectList(info.getBsnsCd()));
		return applyDto;
	}

	/**
	 * 파일 다운로드
	 * @param response
	 * @param pblancId
	 * @param attachmentId
	 */
	public void getFileDownload(HttpServletResponse response, String pblancId, String attachmentId) {
		SecurityUtils.checkLogin();
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}

	/**
	 * 사업신청 저장
	 * @param rceptSttusCd
	 * @param pblancId
	 * @param frontBsnsApplyParam
	 * @param fileList
	 */
	public void add(String rceptSttusCd , String pblancId, FrontBsnsApplyParam frontBsnsApplyParam, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkLogin();
		if(usptBsnsPblancApplcntDao.selectApplcntCount(pblancId, worker.getMemberId()) > 0 ){
			throw new InvalidationException("이미 신청한 사업공고입니다.");
		}

		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.pblancSttus.접수중, info.getPblancSttusCd())){
			throw new InvalidationException("모집상태가 접수중인 공고만 지원가능합니다.");
		}
		ApplyMberTypeDto mberType = usptBsnsPblancDao.selectApplyMberType(pblancId);
		if(!mberType.getMberTypeCd().contains(worker.getMemberType())) {
			throw new InvalidationException("지원가능한 회원이 아닙니다.\n[지원가능 회원유형:" + mberType.getMberType()+"]");
		}

		List<Chklst> orgChkList = usptBsnsChklstDao.selectList(info.getBsnsCd());

		InvalidationsException ies = new InvalidationsException();
		if(orgChkList.size() != frontBsnsApplyParam.getChkList().size()) {
			ies.add("chkList", String.format(Code.validateMessage.입력없음, "필수확인사항"));
		}
		if(CoreUtils.string.isBlank(frontBsnsApplyParam.getSessionId())) {
			ies.add("sessionId", String.format(Code.validateMessage.입력없음, "약관동의 세션ID"));
		}
		if(frontBsnsApplyParam.getApplyTask() == null) {
			ies.add("applyTask", String.format(Code.validateMessage.입력없음, "과제정보"));
		}

		UsptBsnsPblancApplyTask taskInfo = frontBsnsApplyParam.getApplyTask();
		if(CoreUtils.string.isBlank(taskInfo.getTaskNmKo())) {
			ies.add("taskNmKo", String.format(Code.validateMessage.입력없음, "과제명/프로젝트명(국문)"));
		}
		if(CoreUtils.string.isBlank(taskInfo.getRspnberNm())) {
			ies.add("rspnberNm", String.format(Code.validateMessage.입력없음, "과제책임자 이름"));
		}
		if(CoreUtils.string.isBlank(taskInfo.getMbtlnum())) {
			ies.add("mbtlnum", String.format(Code.validateMessage.입력없음, "과제책임자 휴대폰번호"));
		}
		if(CoreUtils.string.isBlank(taskInfo.getEmail())) {
			ies.add("email", String.format(Code.validateMessage.입력없음, "과제책임자 이메일"));
		}
		if(!CoreUtils.string.isBlank(taskInfo.getEmail())) {
			if(!CoreUtils.validation.isEmail(taskInfo.getEmail())) {
				ies.add("email", String.format(Code.validateMessage.이메일오류, "과제책임자 이메일"));
			}
		}

		UsptBsnsPblancApplcntEnt applcntEnt = frontBsnsApplyParam.getApplcntEnt();
		if(applcntEnt != null) {
			if(!CoreUtils.string.isBlank(applcntEnt.getCeoEmail())) {
				if(!CoreUtils.validation.isEmail(applcntEnt.getCeoEmail())) {
					ies.add("ceoEmail", String.format(Code.validateMessage.이메일오류, "대표자 이메일"));
				}
			}
		}


		BsnsBasicDto bsnsInfo = usptBsnsDao.select(info.getBsnsCd());
		if(CoreUtils.string.equals(bsnsInfo.getBsnsTypeCd(), Code.bsnsType.교육사업) && CoreUtils.string.equals(worker.getMemberType(), Code.memberType.개인사용자) ) {
			if(CoreUtils.string.isBlank(taskInfo.getHopeDtyCd())) {
				ies.add("hopeDtyCd", String.format(Code.validateMessage.입력없음, "희망직무"));
			}
		}
		List<UsptBsnsPblancApplyAttach> uploadList = frontBsnsApplyParam.getUploadFileList();
		if(uploadList != null && fileList != null && uploadList.size() != fileList.size()) {
			ies.add("uploadFileList", "파일업로드 정보와 업로드파일 수가 동일하지 않습니다.");
		}

		if (ies.size() > 0) {
			throw ies;
		}


		Date now = new Date();
		/* 사업공고신청자 등록 */
		UsptBsnsPblancApplcnt regInfo = new UsptBsnsPblancApplcnt();
		regInfo.setApplyId(CoreUtils.string.getNewId(Code.prefix.공고신청자));
		regInfo.setPblancId(info.getPblancId());
		regInfo.setRceptOdr(info.getRceptOdr());
		regInfo.setMemberId(worker.getMemberId());
		regInfo.setRceptSttusCd(rceptSttusCd);
		regInfo.setRceptSttusDt(now);
		regInfo.setCreatedDt(now);
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setSlctn(false);
		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.신청)) {
			regInfo.setRceptDt(now);
		}
		usptBsnsPblancApplcntDao.insert(regInfo);

		/** 변환 대상 파일 리스트 */
		List<CmmtAtchmnfl> chgFileList = new ArrayList<CmmtAtchmnfl>();

		/* 첨부파일 저장 */
		List<UsptBsnsAtchmnflSetup> orgAtchmnflSetupList = usptBsnsAtchmnflSetupDao.selectList(info.getBsnsCd());
		if(fileList != null && fileList.size() > 0) {
			List<UsptBsnsPblancApplyAttach> applyAttachList = new ArrayList<UsptBsnsPblancApplyAttach>();
			List<UsptBsnsPblancApplyAttach> uploadFileList = frontBsnsApplyParam.getUploadFileList();
			for(UsptBsnsAtchmnflSetup orgAtchmnflSetupInfo  : orgAtchmnflSetupList) {
				Optional<UsptBsnsPblancApplyAttach> opt = null;
				if(uploadFileList.size() > 0) {
					opt = uploadFileList.stream().filter(x->x.getAtchmnflSetupId().equals(orgAtchmnflSetupInfo.getAtchmnflSetupId())).findFirst();
				}
				if(orgAtchmnflSetupInfo.getEssntl()) {
					if(opt == null || !opt.isPresent()) {
						throw new InvalidationException("필수 첨부파일이 등록되지 않았습니다.");
					}
				}

				if(opt != null && opt.isPresent()) {
					UsptBsnsPblancApplyAttach regAttachInfo = opt.get();
					if(fileList.size() < regAttachInfo.getFileOrder()) {
						throw new InvalidationException("업로드 첨부파일 정보가 정확하지 않습니다.");
					}
					CmmtAtchmnfl attachment = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), fileList.get(regAttachInfo.getFileOrder()), null, 0);
					UsptBsnsPblancApplyAttach temp = new UsptBsnsPblancApplyAttach();
					temp.setApplyId(regInfo.getApplyId());
					temp.setAtchmnflSetupId(regAttachInfo.getAtchmnflSetupId());
					temp.setAttachmentId(attachment.getAttachmentId());
					temp.setCreatorId(worker.getMemberId());
					temp.setCreatedDt(now);
					temp.setUpdaterId(worker.getMemberId());
					temp.setUpdatedDt(now);
					applyAttachList.add(temp);
					/** 변환 대상 파일 저장 */
					chgFileList.add(attachment);
				}
			}
			if(applyAttachList.size() > 0) {
				usptBsnsPblancApplyAttachDao.insertList(applyAttachList);
			}
		} else {
			if(orgAtchmnflSetupList != null) {
				Optional<UsptBsnsAtchmnflSetup> essntlFile = orgAtchmnflSetupList.stream().filter(x -> x.getEssntl() != null && x.getEssntl() == true).findFirst();
				if(essntlFile != null && essntlFile.isPresent()) {
					throw new InvalidationException("필수 첨부파일이 등록되지 않았습니다.");
				}
			}
		}

		/* 사업공고 신청 체크리스트 등록 */
		List<UsptBsnsPblancApplyChklst> chkList = frontBsnsApplyParam.getChkList();
		for(UsptBsnsPblancApplyChklst chklst : chkList) {
			chklst.setApplyId(regInfo.getApplyId());
			chklst.setCreatorId(worker.getMemberId());
			chklst.setCreatedDt(now);
			chklst.setUpdaterId(worker.getMemberId());
			chklst.setUpdatedDt(now);
		}
		if(chkList != null && chkList.size() > 0) {
			usptBsnsPblancApplyChklstDao.insertList(chkList);
		}

		/* 사업공고 신청 과제 등록 */
		taskInfo.setApplyId(regInfo.getApplyId());
		taskInfo.setEncEmail(CoreUtils.aes256.encrypt(taskInfo.getEmail(), regInfo.getApplyId()));
		taskInfo.setEncMbtlnum(CoreUtils.aes256.encrypt(taskInfo.getMbtlnum(), regInfo.getApplyId()));
		if(CoreUtils.string.isNotBlank(taskInfo.getBrthdy())) {
			taskInfo.setEncBrthdy(CoreUtils.aes256.encrypt(taskInfo.getBrthdy(), regInfo.getApplyId()));
		}
		if(CoreUtils.string.isNotBlank(taskInfo.getFxnum())) {
			taskInfo.setEncFxnum(CoreUtils.aes256.encrypt(taskInfo.getFxnum(), regInfo.getApplyId()));
		}
		if(CoreUtils.string.isNotBlank(taskInfo.getTelno())) {
			taskInfo.setEncTelno(CoreUtils.aes256.encrypt(taskInfo.getTelno(), regInfo.getApplyId()));
		}
		if(!CoreUtils.string.equals(bsnsInfo.getTaskTypeCd(), Code.taskType.자유지정과제)) {
			taskInfo.setTaskTypeCd(bsnsInfo.getTaskTypeCd());
		}
		if(CoreUtils.string.isBlank(taskInfo.getApplyRealmId())) {
			taskInfo.setApplyRealmId(null);
		}

		taskInfo.setSctecrno(CoreUtils.string.getNumberOnly(taskInfo.getSctecrno()));
		taskInfo.setCreatorId(worker.getMemberId());
		taskInfo.setCreatedDt(now);
		taskInfo.setUpdaterId(worker.getMemberId());
		taskInfo.setUpdatedDt(now);
		usptBsnsPblancApplyTaskDao.insert(taskInfo);

		/* 사업공고 신청자 기업정보 등록 */
		if(frontBsnsApplyParam.getApplcntEnt() != null) {
			UsptBsnsPblancApplcntEnt entInfo = frontBsnsApplyParam.getApplcntEnt();
			entInfo.setApplyId(regInfo.getApplyId());
			if(CoreUtils.string.isNotBlank(entInfo.getFxnum())) {
				entInfo.setEncFxnum(CoreUtils.aes256.encrypt(entInfo.getFxnum(), entInfo.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(entInfo.getReprsntTelno())) {
				entInfo.setEncReprsntTelno(CoreUtils.aes256.encrypt(entInfo.getReprsntTelno(), entInfo.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(entInfo.getCeoTelno())) {
				entInfo.setEncCeoTelno(CoreUtils.aes256.encrypt(entInfo.getCeoTelno(), entInfo.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(entInfo.getCeoEmail())) {
				entInfo.setEncCeoEmail(CoreUtils.aes256.encrypt(entInfo.getCeoEmail(), entInfo.getApplyId()));
			}
			Date fondDayDt = CoreUtils.string.toDate(entInfo.getFondDay());
			entInfo.setFondDay(date.format(fondDayDt, "yyyyMMdd"));
			entInfo.setCreatorId(worker.getMemberId());
			entInfo.setCreatedDt(now);
			entInfo.setUpdaterId(worker.getMemberId());
			entInfo.setUpdatedDt(now);
			usptBsnsPblancApplcntEntDao.insert(entInfo);
		}

		/* 사업공고 신청 과제참여자 등록 */
		List<UsptBsnsPblancApplyTaskPartcpts> partcptsList = frontBsnsApplyParam.getTaskPartcptsList();
		for(UsptBsnsPblancApplyTaskPartcpts partcpts : partcptsList) {
			partcpts.setApplyId(regInfo.getApplyId());
			partcpts.setPartcptsId(CoreUtils.string.getNewId(Code.prefix.과제참여자));
			if(CoreUtils.string.isNotBlank(partcpts.getMbtlnum())) {
				partcpts.setEncMbtlnum(CoreUtils.aes256.encrypt(partcpts.getMbtlnum(), partcpts.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(partcpts.getBrthdy())) {
				partcpts.setEncBrthdy(CoreUtils.aes256.encrypt(partcpts.getBrthdy(), partcpts.getApplyId()));
			}
			partcpts.setCreatorId(worker.getMemberId());
			partcpts.setCreatedDt(now);
			partcpts.setUpdaterId(worker.getMemberId());
			partcpts.setUpdatedDt(now);
		}
		if(partcptsList != null && partcptsList.size() > 0) {
			usptBsnsPblancApplyTaskPartcptsDao.insertList(partcptsList);
		}
		termsUtils.insertList(frontBsnsApplyParam.getSessionId(), worker.getMemberId());

		this.callPdfGatewayApi(chgFileList, info.getPblancNm());
	}



	/**
	 * 사업신청 목록 조회
	 * @param bsnsApplyListParam
	 * @param page
	 * @return
	 */
	public CorePagination<FrontBsnsApplyListDto> getList(FrontBsnsApplyListParam bsnsApplyListParam, Long page){
		BnMember worker = SecurityUtils.checkLogin();
		if(page == null) {
			page = 1L;
		}
		if(bsnsApplyListParam.getItemsPerPage() == null) {
			bsnsApplyListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		Date startDt = CoreUtils.string.toDate(bsnsApplyListParam.getRceptDtStart());
		bsnsApplyListParam.setRceptDtStart(date.format(startDt, "yyyyMMdd"));
		Date endDt = CoreUtils.string.toDate(bsnsApplyListParam.getRceptDtEnd());
		bsnsApplyListParam.setRceptDtEnd(date.format(endDt, "yyyyMMdd"));
		bsnsApplyListParam.setMemberId(worker.getMemberId());

		long totalItems = usptBsnsPblancApplcntDao.selectFrontListCount(bsnsApplyListParam);
		CorePaginationInfo info = new CorePaginationInfo(page, bsnsApplyListParam.getItemsPerPage(), totalItems);
		bsnsApplyListParam.setBeginRowNum(info.getBeginRowNum());
		List<FrontBsnsApplyListDto> list = usptBsnsPblancApplcntDao.selectFrontList(bsnsApplyListParam);
		CorePagination<FrontBsnsApplyListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 보완요청/반려 사유 확인
	 * @param applyId
	 * @return
	 */
	public FrontReqReasonDto getReason(String applyId) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 사업신청 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(info.getRceptSttusCd(), Code.rceptSttus.보완요청)
				&& !CoreUtils.string.equals(info.getRceptSttusCd(), Code.rceptSttus.반려)) {
			throw new InvalidationException("접수상태를 확인해주세요");
		}

		if(!CoreUtils.string.equals(worker.getMemberId(), info.getMemberId())) {
			throw new InvalidationException("요청 사용자의 사업신청내역이 아닙니다.");
		}

		FrontReqReasonDto dto = new FrontReqReasonDto();
		dto.setReqDt(date.format(info.getRceptSttusDt(), "yyyy-MM-dd HH:mm"));
		if(CoreUtils.string.equals(info.getRceptSttusCd(), Code.rceptSttus.보완요청)) {
			dto.setReason(info.getMakeupReqCn());
		} else if(CoreUtils.string.equals(info.getRceptSttusCd(), Code.rceptSttus.반려)) {
			dto.setReason(info.getRejectReasonCn());
		}
		return dto;
	}



	/**
	 * 사업신청 필수확인사항 조회
	 * @param applyId
	 * @return
	 */
	public JsonList<UsptBsnsPblancApplyChklst> getApplyChklst(String applyId) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 사업신청 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(worker.getMemberId(), info.getMemberId())) {
			throw new InvalidationException("요청 사용자의 사업신청내역이 아닙니다.");
		}
		return new JsonList<>(usptBsnsPblancApplyChklstDao.selectList(applyId));
	}


	/**
	 * 사업신청 신청자 정보 조회
	 * @param pblancId
	 * @return
	 */
	public FrontBsnsApplicantDto getApplyApplicant(String applyId) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 사업신청 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(worker.getMemberId(), info.getMemberId())) {
			throw new InvalidationException("요청 사용자의 사업신청내역이 아닙니다.");
		}

		FrontBsnsApplicantDto dto = new FrontBsnsApplicantDto();
		dto.setCmmtMember(cmmtMemberDao.select(worker.getMemberId()));
		if(!CoreUtils.string.equals(worker.getMemberType(), Code.memberType.개인사용자)) {
			UsptBsnsPblancApplcntEnt entInfo = usptBsnsPblancApplcntEntDao.select(applyId);
			if(entInfo != null) {
				entInfo.setFxnum(entInfo.getDecFxnum());
				entInfo.setCeoEmail(entInfo.getDecCeoEmail());
				entInfo.setCeoTelno(entInfo.getDecCeoTelno());
				entInfo.setReprsntTelno(entInfo.getDecReprsntTelno());
				dto.setApplcntEnt(entInfo);
			}
		}
		return dto;
	}

	/**
	 * 사업신청 신청정보 조회
	 * @param pblancId
	 * @return
	 */
	public FrontBsnsApplyInfoDto getApplyInfo(String applyId) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 사업신청 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(worker.getMemberId(), info.getMemberId())) {
			throw new InvalidationException("요청 사용자의 사업신청내역이 아닙니다.");
		}

		FrontBsnsPblancDto pblancInfo = usptBsnsPblancDao.selectFront(info.getPblancId());
		if(pblancInfo == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		BsnsBasicDto bsnsInfo = usptBsnsDao.select(pblancInfo.getBsnsCd());

		FrontBsnsApplyInfoDto applyInfoDto = new FrontBsnsApplyInfoDto();
		applyInfoDto.setBsnsTypeCd(bsnsInfo.getBsnsTypeCd());
		applyInfoDto.setBsnsTypeNm(bsnsInfo.getBsnsTypeNm());
		applyInfoDto.setPblancId(info.getPblancId());
		applyInfoDto.setTaskTypeCd(bsnsInfo.getTaskTypeCd());

		Date bsnsBgndt = string.toDate(pblancInfo.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(pblancInfo.getBsnsEndde());
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			applyInfoDto.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				applyInfoDto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				applyInfoDto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			applyInfoDto.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}

		applyInfoDto.setApplyRealmList(usptBsnsApplyRealmDao.selectList(pblancInfo.getBsnsCd()));
		applyInfoDto.setAtchmnflList(usptBsnsPblancApplyAttachDao.selectList(applyId, pblancInfo.getBsnsCd()));

		UsptBsnsPblancApplyTask taskInfo = usptBsnsPblancApplyTaskDao.select(applyId);
		taskInfo.setFxnum(taskInfo.getDecFxnum());
		taskInfo.setTelno(taskInfo.getDecTelno());
		taskInfo.setBrthdy(taskInfo.getDecBrthdy());
		taskInfo.setMbtlnum(taskInfo.getDecMbtlnum());
		taskInfo.setEmail(taskInfo.getDecEmail());
		applyInfoDto.setTaskInfo(taskInfo);

		List<UsptBsnsPblancApplyTaskPartcpts> taskPartcptsList = usptBsnsPblancApplyTaskPartcptsDao.selectList(applyId);
		if(taskPartcptsList != null) {
			for(UsptBsnsPblancApplyTaskPartcpts partcptsInfo : taskPartcptsList) {
				partcptsInfo.setBrthdy(partcptsInfo.getDecBrthdy());
				partcptsInfo.setMbtlnum(partcptsInfo.getDecMbtlnum());
			}
		}
		applyInfoDto.setPartcptslist(taskPartcptsList);
		return applyInfoDto;
	}


	/**
	 * 사업신청 정보 수정
	 * @param rceptSttusCd
	 * @param applyId
	 * @param frontBsnsApplyParam
	 * @param fileList
	 */
	public void modify(String rceptSttusCd, String applyId, FrontBsnsApplyParam frontBsnsApplyParam, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 사업신청 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(worker.getMemberId(), info.getMemberId())) {
			throw new InvalidationException("요청 사용자의 사업신청내역이 아닙니다.");
		}

		String saveMsg = "임시 저장";
		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.신청)) {
			saveMsg = "제출 저장";
		}

		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.임시저장) && !CoreUtils.string.equals(info.getRceptSttusCd(), Code.rceptSttus.임시저장)) {
			throw new InvalidationException("임시저장 상태의 사업신정 정보가 아닙니다.\n 임시저장 상태의 사업신청 정보만 임시저장 상태로 저장이 가능합니다.");
		}

		FrontBsnsPblancDto pblancInfo = usptBsnsPblancDao.selectFront(info.getPblancId());
		if(pblancInfo == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}

		InvalidationsException ies = new InvalidationsException();
		List<Chklst> orgChkList = usptBsnsChklstDao.selectList(pblancInfo.getBsnsCd());
		if(orgChkList != null && !orgChkList.isEmpty()) {
			if(frontBsnsApplyParam.getChkList() == null) {
				ies.add("chkList", String.format(Code.validateMessage.입력없음, "필수확인사항"));
			} else {
				if(orgChkList.size() != frontBsnsApplyParam.getChkList().size()) {
					ies.add("chkList", String.format(Code.validateMessage.입력없음, "필수확인사항"));
				}
			}
		}
		if(frontBsnsApplyParam.getApplyTask() == null) {
			ies.add("applyTask", String.format(Code.validateMessage.입력없음, "과제정보"));
		}

		UsptBsnsPblancApplyTask regTaskInfo = frontBsnsApplyParam.getApplyTask();
		if(CoreUtils.string.isBlank(regTaskInfo.getTaskNmKo())) {
			ies.add("taskNmKo", String.format(Code.validateMessage.입력없음, "과제명/프로젝트명(국문)"));
		}
		if(CoreUtils.string.isBlank(regTaskInfo.getRspnberNm())) {
			ies.add("rspnberNm", String.format(Code.validateMessage.입력없음, "과제책임자 이름"));
		}
		if(CoreUtils.string.isBlank(regTaskInfo.getMbtlnum())) {
			ies.add("mbtlnum", String.format(Code.validateMessage.입력없음, "과제책임자 휴대폰번호"));
		}
		if(CoreUtils.string.isBlank(regTaskInfo.getEmail())) {
			ies.add("email", String.format(Code.validateMessage.입력없음, "과제책임자 이메일"));
		}
		if(!CoreUtils.string.isBlank(regTaskInfo.getEmail())) {
			if(!CoreUtils.validation.isEmail(regTaskInfo.getEmail())) {
				ies.add("email", String.format(Code.validateMessage.이메일오류, "과제책임자 이메일"));
			}
		}

		UsptBsnsPblancApplcntEnt applcntEnt = frontBsnsApplyParam.getApplcntEnt();
		if(applcntEnt != null) {
			if(!CoreUtils.string.isBlank(applcntEnt.getCeoEmail())) {
				if(!CoreUtils.validation.isEmail(applcntEnt.getCeoEmail())) {
					ies.add("ceoEmail", String.format(Code.validateMessage.이메일오류, "대표자 이메일"));
				}
			}
		}


		BsnsBasicDto bsnsInfo = usptBsnsDao.select(pblancInfo.getBsnsCd());
		if(CoreUtils.string.equals(bsnsInfo.getBsnsTypeCd(), Code.bsnsType.교육사업) && CoreUtils.string.equals(worker.getMemberType(), Code.memberType.개인사용자) ) {
			if(CoreUtils.string.isBlank(regTaskInfo.getHopeDtyCd())) {
				ies.add("hopeDtyCd", String.format(Code.validateMessage.입력없음, "희망직무"));
			}
		}
		List<UsptBsnsPblancApplyAttach> uploadList = frontBsnsApplyParam.getUploadFileList();
		if(uploadList != null && fileList != null && uploadList.size() != fileList.size()) {
			ies.add("uploadFileList", "파일업로드 정보와 업로드파일 수가 동일하지 않습니다.");
		}
		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();

		/* 신청자 정보 */
		info.setRceptSttusCd(rceptSttusCd);
		info.setRceptSttusDt(now);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.신청)) {
			info.setRceptDt(now);
		}
		if(usptBsnsPblancApplcntDao.update(info) != 1) {
			throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(사업신청정보 수정 오류)");
		}

		/** 변환 대상 파일 리스트 */
		List<CmmtAtchmnfl> chgFileList = new ArrayList<CmmtAtchmnfl>();

		/* 첨부파일 저장 */
		if(fileList != null && fileList.size() > 0) {
			for(UsptBsnsPblancApplyAttach attachInfo : uploadList) {
				if(attachInfo.getFileOrder() > -1) {
					if(fileList.size() < attachInfo.getFileOrder()) {
						throw new InvalidationException("업로드 첨부파일 정보가 정확하지 않습니다.");
					}
					CmmtAtchmnfl attachment = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), fileList.get(attachInfo.getFileOrder()), null, 0);
					attachInfo.setApplyId(info.getApplyId());

					UsptBsnsPblancApplyAttach orgAttachInfo = usptBsnsPblancApplyAttachDao.select(applyId, attachInfo.getAtchmnflSetupId());
					if(orgAttachInfo == null) {
						attachInfo.setAttachmentId(attachment.getAttachmentId());
						attachInfo.setCreatorId(worker.getMemberId());
						attachInfo.setCreatedDt(now);
						attachInfo.setUpdaterId(worker.getMemberId());
						attachInfo.setUpdatedDt(now);
						usptBsnsPblancApplyAttachDao.insert(attachInfo);
					} else {
						orgAttachInfo.setAttachmentId(attachment.getAttachmentId());
						orgAttachInfo.setUpdaterId(worker.getMemberId());
						orgAttachInfo.setUpdatedDt(now);
						if(usptBsnsPblancApplyAttachDao.update(orgAttachInfo) != 1) {
							throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(첨부파일 수정 오류)");
						}
					}
					chgFileList.add(attachment);
				}
			}
		}

		/* 필수확인사항 */
		List<UsptBsnsPblancApplyChklst> checkList = frontBsnsApplyParam.getChkList();
		if(checkList.size() != 0) {
			for(UsptBsnsPblancApplyChklst chkInfo : checkList) {
				chkInfo.setApplyId(info.getApplyId());
				chkInfo.setUpdatedDt(now);
				chkInfo.setUpdaterId(worker.getMemberId());
				if(usptBsnsPblancApplyChklstDao.update(chkInfo) != 1) {
					throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(필수확인사항 수정 오류)");
				}
			}
		}

		/* 신청자 기업정보 */
		UsptBsnsPblancApplcntEnt regEntInfo = frontBsnsApplyParam.getApplcntEnt();
		if(regEntInfo != null) {
			if(CoreUtils.string.isNotBlank(regEntInfo.getFxnum())) {
				regEntInfo.setEncFxnum(CoreUtils.aes256.encrypt(regEntInfo.getFxnum(), regEntInfo.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(regEntInfo.getReprsntTelno())) {
				regEntInfo.setEncReprsntTelno(CoreUtils.aes256.encrypt(regEntInfo.getReprsntTelno(), regEntInfo.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(regEntInfo.getCeoTelno())) {
				regEntInfo.setEncCeoTelno(CoreUtils.aes256.encrypt(regEntInfo.getCeoTelno(), regEntInfo.getApplyId()));
			}
			if(CoreUtils.string.isNotBlank(regEntInfo.getCeoEmail())) {
				regEntInfo.setEncCeoEmail(CoreUtils.aes256.encrypt(regEntInfo.getCeoEmail(), regEntInfo.getApplyId()));
			}
			Date fondDayDt = CoreUtils.string.toDate(regEntInfo.getFondDay());
			regEntInfo.setApplyId(info.getApplyId());
			regEntInfo.setFondDay(date.format(fondDayDt, "yyyyMMdd"));
			regEntInfo.setUpdatedDt(now);
			regEntInfo.setUpdaterId(worker.getMemberId());
			if(usptBsnsPblancApplcntEntDao.update(regEntInfo) != 1) {
				throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(기업정보 수정 오류)");
			}
		}

		/* 신청 과제정보 */
		regTaskInfo.setApplyId(info.getApplyId());
		regTaskInfo.setEncEmail(CoreUtils.aes256.encrypt(regTaskInfo.getEmail(), regTaskInfo.getApplyId()));
		regTaskInfo.setEncMbtlnum(CoreUtils.aes256.encrypt(regTaskInfo.getMbtlnum(), regTaskInfo.getApplyId()));
		if(CoreUtils.string.isNotBlank(regTaskInfo.getBrthdy())) {
			regTaskInfo.setEncBrthdy(CoreUtils.aes256.encrypt(regTaskInfo.getBrthdy(), regTaskInfo.getApplyId()));
		}
		if(CoreUtils.string.isNotBlank(regTaskInfo.getFxnum())) {
			regTaskInfo.setEncFxnum(CoreUtils.aes256.encrypt(regTaskInfo.getFxnum(), regTaskInfo.getApplyId()));
		}
		if(CoreUtils.string.isNotBlank(regTaskInfo.getTelno())) {
			regTaskInfo.setEncTelno(CoreUtils.aes256.encrypt(regTaskInfo.getTelno(), regTaskInfo.getApplyId()));
		}
		if(!CoreUtils.string.equals(bsnsInfo.getTaskTypeCd(), Code.taskType.자유지정과제)) {
			regTaskInfo.setTaskTypeCd(bsnsInfo.getTaskTypeCd());
		}
		if(CoreUtils.string.isBlank(regTaskInfo.getApplyRealmId())) {
			regTaskInfo.setApplyRealmId(null);
		}
		regTaskInfo.setUpdatedDt(now);
		regTaskInfo.setUpdaterId(worker.getMemberId());
		if(usptBsnsPblancApplyTaskDao.update(regTaskInfo) != 1) {
			throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(과제정보 수정 오류)");
		}

		/* 과제 참여인력 */
		List<UsptBsnsPblancApplyTaskPartcpts> partcptsList = frontBsnsApplyParam.getTaskPartcptsList();
		if(partcptsList.size() != 0) {
			for(UsptBsnsPblancApplyTaskPartcpts regPartcptsInfo : partcptsList) {
				regPartcptsInfo.setApplyId(info.getApplyId());
				if(CoreUtils.string.isNotBlank(regPartcptsInfo.getMbtlnum())) {
					regPartcptsInfo.setEncMbtlnum(CoreUtils.aes256.encrypt(regPartcptsInfo.getMbtlnum(), regPartcptsInfo.getApplyId()));
				}
				if(CoreUtils.string.isNotBlank(regPartcptsInfo.getBrthdy())) {
					regPartcptsInfo.setEncBrthdy(CoreUtils.aes256.encrypt(regPartcptsInfo.getBrthdy(), regPartcptsInfo.getApplyId()));
				}
				regPartcptsInfo.setCreatedDt(now);
				regPartcptsInfo.setCreatorId(worker.getMemberId());
				regPartcptsInfo.setUpdatedDt(now);
				regPartcptsInfo.setUpdaterId(worker.getMemberId());

				if(CoreUtils.string.equals(regPartcptsInfo.getFlag(), Code.flag.등록)) {
					regPartcptsInfo.setPartcptsId(CoreUtils.string.getNewId(Code.prefix.과제참여자));
					usptBsnsPblancApplyTaskPartcptsDao.insert(regPartcptsInfo);
				} else if(CoreUtils.string.equals(regPartcptsInfo.getFlag(), Code.flag.수정)) {
					if(usptBsnsPblancApplyTaskPartcptsDao.update(regPartcptsInfo) != 1) {
						throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(과제 참여인력 수정 오류)");
					}
				} else if(CoreUtils.string.equals(regPartcptsInfo.getFlag(), Code.flag.삭제)) {
					if(usptBsnsPblancApplyTaskPartcptsDao.delete(regPartcptsInfo) != 1) {
						throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(과제 참여인력 수정 오류)");
					}
				} else {
					throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(과제 참여인력 FLAG 값 오류)");
				}
			}
		}

		this.callPdfGatewayApi(chgFileList, pblancInfo.getPblancNm());
	}

	/**
	 * 신청취소
	 * @param applyId
	 */
	public void modifyApplyCancel(String applyId) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 사업신청 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(worker.getMemberId(), info.getMemberId())) {
			throw new InvalidationException("요청 사용자의 사업신청내역이 아닙니다.");
		}

		Date now = new Date();
		info.setRceptSttusCd(Code.rceptSttus.신청취소);
		info.setRceptSttusDt(now);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());

		if(usptBsnsPblancApplcntDao.update(info) != 1) {
			throw new InvalidationException("신청취소 중 오류가 발생했습니다.");
		}
	}


	/**
	 * 전화번호 유효성 체크
	 * @param number
	 * @return
	 */
	public static boolean validPhoneNumber(String number) {
		Pattern pattern = Pattern.compile("\\d{11}");
		Matcher matcher = pattern.matcher(number);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * PDF 변환 파일 저장
	 * @param chgFileList
	 */
	public void changeFileSave(List<CmmtAtchmnfl> chgFileList, String pblancNm) {
		log.debug("##### server : " + pdfConfig.getServer());
		if(CoreUtils.string.equals("AICA", pdfConfig.getServer())) {
			BnMember worker = SecurityUtils.checkLogin();
			String currentTime = CoreUtils.date.getCurrentDate("yyyyMMddHHmmss");
			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.submit(() -> {
				if(chgFileList != null && !chgFileList.isEmpty()) {
					String saveFileNm = "";
					File orgFile = null;
					File outFile = null;
					for(CmmtAtchmnfl fileInfo : chgFileList) {
						try {
							orgFile = new File(config.getDir().getUpload() + fileInfo.getSavedFilePath());
							saveFileNm = pdfConfig.getPmsInputFolder() + File.separator + pblancNm + "_" + worker.getMemberNm() + "_" + fileInfo.getFileNm() + "_" + currentTime + "." + CoreUtils.filename.getExtension(fileInfo.getFileNm());
							outFile = new File(saveFileNm);
							log.debug("##### 대상파일 경로 : " + orgFile.getAbsolutePath());
							log.debug("##### 저장파일 경로 : " + outFile.getAbsolutePath());
							FileUtils.copyFile(orgFile, outFile);
						} catch (IOException e) {
							log.error("##### pdf 변환 파일 저장 오류 : " + e.getMessage());
						}
					}
				}
			});
		}
	}



	/**
	 * PDF-GATEWAY api 호출 (PDF 변환)
	 * @param chgFileList
	 * @param pblancNm
	 */
	public void callPdfGatewayApi(List<CmmtAtchmnfl> chgFileList, String pblancNm) {
		log.debug("##### server : " + pdfConfig.getServer());
		if(CoreUtils.string.equals("AICA", pdfConfig.getServer())) {
			BnMember worker = SecurityUtils.checkLogin();
			String currentTime = CoreUtils.date.getCurrentDate("yyyyMMddHHmmss");
			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.submit(() -> {
				if(chgFileList != null && !chgFileList.isEmpty()) {
					String saveFileNm = "";
					String outputFileNm = "";
					String apiUrl = pdfConfig.getUri() + "/pdf-gateway/api/task/build";
					String callbackUri = pdfConfig.getPmsDomain() + "/psm/api/front/bsns-apply/pdf-gateway/result";

					Date now = new Date();
					UsptAtchmnflMapng afmInfo = new UsptAtchmnflMapng();
					afmInfo.setCreatDt(now);
					afmInfo.setMberId(worker.getMemberId());

					ResponseEntity<String> response = null;
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<MultiValueMap<String, String>> requestEntity = null;
					MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

					File orgFile = null;
					File outFile = null;

					for(CmmtAtchmnfl fileInfo : chgFileList) {
						orgFile = new File(config.getDir().getUpload() + fileInfo.getSavedFilePath());
						if(orgFile != null) {
							try {
								saveFileNm = pblancNm + "_" + worker.getMemberNm() + "_" + fileInfo.getFileNm() + "_" + currentTime + "." + CoreUtils.filename.getExtension(fileInfo.getFileNm());
								outputFileNm = pblancNm + "_" + worker.getMemberNm() + "_" + fileInfo.getFileNm() + "_" + currentTime + ".pdf";
								outFile = new File(pdfConfig.getPmsInputFolder() + File.separator + saveFileNm);
								log.info("##### 대상파일 경로 : " + orgFile.getAbsolutePath());
								log.info("##### 저장파일 경로 : " + outFile.getAbsolutePath());
								FileUtils.copyFile(orgFile, outFile);

								body.add("inputUri", "file:/" + pdfConfig.getInputFolder() + "/" + saveFileNm);
								body.add("outputUri", "file:/" + pdfConfig.getOutputFolder() + "/" + outputFileNm);
								body.add("taskName", "BSNS_APPLY");
								body.add("callbackUri", callbackUri);

								log.info("##### pdf-gateway input 경로 : " + pdfConfig.getInputFolder() + "/" + saveFileNm);
								log.info("##### pdf-gateway output 경로 : " + pdfConfig.getOutputFolder() + "/" + outputFileNm);


								log.info("new RestTemplate().postForEntity Call");
								requestEntity = new HttpEntity<MultiValueMap<String, String>>(body, headers);
								response = new RestTemplate().postForEntity(apiUrl, requestEntity, String.class);
								log.info("response body: {}", response.getBody());

								PdfGatewayDto resultInfo = CoreUtils.json.toObject(response.getBody(), PdfGatewayDto.class);

								afmInfo.setAtchmnflId(fileInfo.getAttachmentId());
								afmInfo.setRegistId(resultInfo.getOid());
								afmInfo.setSttusNm(resultInfo.getStatus());
								usptAtchmnflMapngDao.insert(afmInfo);
							} catch (RestClientException | IOException e) {
								log.error("##### PDF-GATEWAY API CALL FAIL");
								log.error(e.getMessage());
							}
						}
					}
				}
			});
		}
	}


	/**
	 * @param response
	 * PDF-GATEWAY를 위한 파일 다운로드
	 * @param attachmentId
	 */
	public void getFileDownloadForPdfGateway(HttpServletResponse response, String attachmentId) {
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}


	/**
	 * PDF-GATEWAY 변환 결과 저장 및 PDF-STREAMDOCS에 저장
	 * @param resultInfo
	 */
	public void modifyPdfGatewayResult(PdfGatewayDto resultInfo) {
		log.info("##### PDF-GATEWAY CALL BACK Process");

		if(resultInfo == null || CoreUtils.string.isBlank(resultInfo.getOid())) {
			throw new InvalidationException("PDF 변환 결과 정보가 없습니다.");
		}
		UsptAtchmnflMapng afmInfo = usptAtchmnflMapngDao.select(resultInfo.getOid());
		if(afmInfo == null) {
			throw new InvalidationException("PDF 변환 요청 정보가 없습니다.");
		}
		afmInfo.setSttusNm(resultInfo.getStatus());
		afmInfo.setComptDt(new Date(resultInfo.getCompletedAt()));
		afmInfo.setUpdtDt(new Date());
		usptAtchmnflMapngDao.update(afmInfo);

/*추가 개발을 위해 임시 작성*/
//		try {
//			String documentsUri = docsConfig.getUrl() + "/v4/documents/external-resources";
//			log.info("##### streamdocs url : " + documentsUri);
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
//			body.add("externalResource", "file:/" + afmInfo.getFileStrePath());
//
//
//			log.info("##### PDF 문서 등록 API Call");
//			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
//			ResponseEntity<String> response = new RestTemplate().postForEntity(documentsUri, requestEntity, String.class);
//			log.info("##### PDF 문서 등록 API response body: {}", response.getBody());
//
//			StreamDocsResponseDto result = CoreUtils.json.toObject(response.getBody(), StreamDocsResponseDto.class);
//
//			afmInfo.setLinkUrl(result.getAlink());
//			afmInfo.setUpdtDt(new Date());
//			usptAtchmnflMapngDao.updateLinkUrl(afmInfo);
//
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
	}

}
