package aicluster.pms.api.bsnsapply.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.bsnsapply.dto.BsnsApplyBhExmntDto;
import aicluster.pms.api.bsnsapply.dto.BsnsApplyBhExmntParam;
import aicluster.pms.api.bsnsapply.dto.BsnsApplyListParam;
import aicluster.pms.api.bsnsapply.dto.SamenssRateParam;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntEntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntHistDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyAtchmnflDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyAttachDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyBhExmntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyChklstDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyDplctAtchmnflDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskPartcptsDao;
import aicluster.pms.common.dao.UsptBsnsPblancDao;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dto.ApplyAttachDto;
import aicluster.pms.common.dto.BsnsApplyDto;
import aicluster.pms.common.dto.BsnsApplyListDto;
import aicluster.pms.common.dto.BsnsBasicDto;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsPblanc;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntHist;
import aicluster.pms.common.entity.UsptBsnsPblancApplyAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsPblancApplyBhExmnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplyDplctAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BsnsApplyService {
	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private CmmtMemberDao cmmtMemberDao;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private UsptBsnsDao usptBsnsDao;
	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;
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
	private UsptBsnsPblancApplcntHistDao usptBsnsPblancApplcntHistDao;
	@Autowired
	private UsptBsnsPblancApplyBhExmntDao usptBsnsPblancApplyBhExmntDao;
	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;
	@Autowired
	private UsptBsnsPblancApplyAtchmnflDao usptBsnsPblancApplyAtchmnflDao;
	@Autowired
	private UsptBsnsPblancApplyDplctAtchmnflDao usptBsnsPblancApplyDplctAtchmnflDao;

	/**
	 * 사업신청 목록 조회
	 * @param bsnsApplyListParam
	 * @param page
	 * @param ordtmRcrit
	 * @return
	 */
	public CorePagination<BsnsApplyListDto> getList(BsnsApplyListParam bsnsApplyListParam, Long page, Boolean ordtmRcrit){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(bsnsApplyListParam.getItemsPerPage() == null) {
			bsnsApplyListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		bsnsApplyListParam.setOrdtmRcrit(ordtmRcrit);
		bsnsApplyListParam.setInsiderId(worker.getMemberId());
		if(!CoreUtils.string.equalsAnyIgnoreCase(bsnsApplyListParam.getSortOrder(), "desc", "asc")) {
			bsnsApplyListParam.setSortOrder("desc");
		}

		long totalItems = usptBsnsPblancApplcntDao.selectListCount(bsnsApplyListParam);
		CorePaginationInfo info = new CorePaginationInfo(page, bsnsApplyListParam.getItemsPerPage(), totalItems);
		bsnsApplyListParam.setBeginRowNum(info.getBeginRowNum());
		List<BsnsApplyListDto> list = usptBsnsPblancApplcntDao.selectList(bsnsApplyListParam);
		CorePagination<BsnsApplyListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 사업신청 목록 엑셀 저장
	 * @param bsnsApplyListParam
	 * @param ordtmRcrit
	 * @return
	 */
	public List<BsnsApplyListDto> getListExcelDwld(BsnsApplyListParam bsnsApplyListParam, Boolean ordtmRcrit){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		bsnsApplyListParam.setOrdtmRcrit(ordtmRcrit);
		bsnsApplyListParam.setInsiderId(worker.getMemberId());
		if(!CoreUtils.string.equalsAnyIgnoreCase(bsnsApplyListParam.getSortOrder(), "desc", "asc")) {
			bsnsApplyListParam.setSortOrder("desc");
		}
		long totalItems = usptBsnsPblancApplcntDao.selectListCount(bsnsApplyListParam);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		bsnsApplyListParam.setBeginRowNum(info.getBeginRowNum());
		bsnsApplyListParam.setItemsPerPage(totalItems);
		return usptBsnsPblancApplcntDao.selectList(bsnsApplyListParam);
	}

	/**
	 * 사업신청 일괄 접수완료 처리
	 * @param applyIdList
	 */
	public void modifyReceipt(List<UsptBsnsPblancApplcnt> applyIdList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(applyIdList.size() == 0) {
			throw new InvalidationException("완료처리할 접수ID가 없습니다.");
		}

		Date now = new Date();
		List<UsptBsnsPblancApplcntHist> histList = new ArrayList<UsptBsnsPblancApplcntHist>();

		for(UsptBsnsPblancApplcnt applyInfo : applyIdList) {
			String chargerAuthorCd = usptBsnsPblancApplcntDao.selectChargerAuth(applyInfo.getApplyId(), worker.getMemberId());
			if(!CoreUtils.string.equals(Code.사업담당자_수정권한, chargerAuthorCd)) {
				throw new InvalidationException("권한이 없습니다.");
			}

			UsptBsnsPblancApplcnt applcntInfo = usptBsnsPblancApplcntDao.select(applyInfo.getApplyId());
			if(CoreUtils.string.equals(applcntInfo.getRceptSttusCd(), Code.rceptSttus.보완요청) ) {
				throw new InvalidationException("보완요청 상태일 경우 접수완료 처리를 할 수 없습니다.");
			}
			applcntInfo.setRceptSttusCd(Code.rceptSttus.접수완료);
			applcntInfo.setRceptSttusDt(now);
			applcntInfo.setUpdatedDt(now);
			applcntInfo.setUpdaterId(worker.getMemberId());

			if(usptBsnsPblancApplcntDao.updateRceptSttusCompt(applcntInfo) != 1) {
				throw new InvalidationException("접수완료 처리 중 오류가 발생했습니다.");
			}

			UsptBsnsPblancApplcntHist histInfo = new UsptBsnsPblancApplcntHist();
			histInfo.setApplyId(applyInfo.getApplyId());
			histInfo.setHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));
			histInfo.setCreatedDt(now);
			histInfo.setCreatorId(worker.getMemberId());
			histInfo.setOpetrId(worker.getMemberId());
			histInfo.setProcessCn(Code.histMessage.접수완료);
			histInfo.setRceptSttusCd(Code.rceptSttus.접수완료);
			histList.add(histInfo);
		}
		usptBsnsPblancApplcntHistDao.insertList(histList);
	}


	/**
	 * 사업신청 접수관리 상세조회
	 * @param request
	 * @param applyId
	 * @return
	 */
	public BsnsApplyDto get(HttpServletRequest request, String applyId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}
		String chargerAuthorCd = usptBsnsPblancApplcntDao.selectChargerAuth(applyId, worker.getMemberId());
		if(CoreUtils.string.isBlank(chargerAuthorCd)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		BsnsApplyDto dto = new BsnsApplyDto();
		UsptBsnsPblanc pblancInfo = usptBsnsPblancDao.select(info.getPblancId(), worker.getMemberId());
		BsnsBasicDto bsnsInfo = usptBsnsDao.select(pblancInfo.getBsnsCd());

		dto.setApplyId(info.getApplyId());
		dto.setReceiptNo(info.getReceiptNo());
		dto.setRceptSttus(info.getRceptSttus());
		dto.setRceptSttusCd(info.getRceptSttusCd());
		dto.setMemberType(info.getMemberType());
		dto.setBsnsNm(bsnsInfo.getBsnsNm());
		dto.setBsnsYear(bsnsInfo.getBsnsYear());
		dto.setBsnsTypeCd(bsnsInfo.getBsnsTypeCd());
		dto.setBsnsTypeNm(bsnsInfo.getBsnsTypeNm());
		dto.setPblancId(info.getPblancId());
		dto.setPblancNm(pblancInfo.getPblancNm());
		dto.setPblancNo(pblancInfo.getPblancNo());

		Date bsnsBgndt = string.toDate(pblancInfo.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(pblancInfo.getBsnsEndde());
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			dto.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				dto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				dto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			dto.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}

		/* 신청자 정보 */
		CmmtMember cmmtMember = cmmtMemberDao.select(info.getMemberId());
		dto.setCmmtMember(cmmtMember);
		if(!CoreUtils.string.equals(cmmtMember.getMemberType(), Code.memberType.개인사용자)) {
			UsptBsnsPblancApplcntEnt entInfo = usptBsnsPblancApplcntEntDao.select(applyId);
			if(entInfo != null) {
				entInfo.setFxnum(entInfo.getDecFxnum());
				entInfo.setCeoEmail(entInfo.getDecCeoEmail());
				entInfo.setCeoTelno(entInfo.getDecCeoTelno());
				entInfo.setReprsntTelno(entInfo.getDecReprsntTelno());
				dto.setApplcntEnt(entInfo);
			}
		}

		/* 과제정보 */
		UsptBsnsPblancApplyTask taskInfo = usptBsnsPblancApplyTaskDao.select(applyId);
		taskInfo.setFxnum(taskInfo.getDecFxnum());
		taskInfo.setTelno(taskInfo.getDecTelno());
		taskInfo.setBrthdy(taskInfo.getDecBrthdy());
		taskInfo.setMbtlnum(taskInfo.getDecMbtlnum());
		taskInfo.setEmail(taskInfo.getDecEmail());
		dto.setTaskInfo(taskInfo);

		/* 과제참여자 정보 */
		List<UsptBsnsPblancApplyTaskPartcpts> taskPartcptsList = usptBsnsPblancApplyTaskPartcptsDao.selectList(applyId);
		if(taskPartcptsList != null) {
			for(UsptBsnsPblancApplyTaskPartcpts partcptsInfo : taskPartcptsList) {
				partcptsInfo.setBrthdy(partcptsInfo.getDecBrthdy());
				partcptsInfo.setMbtlnum(partcptsInfo.getDecMbtlnum());
			}
		}
		dto.setPartcptslist(taskPartcptsList);

		/* 필수확인사항 목록 */
		dto.setChkList(usptBsnsPblancApplyChklstDao.selectList(applyId));
		/* 첨부파일 목록 */
		dto.setAtchmnflList(usptBsnsPblancApplyAttachDao.selectList(applyId, pblancInfo.getBsnsCd()));

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 신청자 정보
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("사업신청 접수관리 상세조회 - 신청자정보")
				.trgterId(cmmtMember.getMemberId())
				.email(cmmtMember.getEmail())
				.birthday(cmmtMember.getBirthday())
				.mobileNo(cmmtMember.getMobileNo())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		// 과제책임자 이력 생성
		LogIndvdlInfSrch taskLogParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("사업신청 접수관리 상세조회 - 과제책임자정보 : " + dto.getTaskInfo().getRspnberNm())
				.trgterId(cmmtMember.getMemberId())
				.email(dto.getTaskInfo().getEmail())
				.birthday(dto.getTaskInfo().getBrthdy())
				.mobileNo(dto.getTaskInfo().getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(taskLogParam);

		// 기업정보 이력 생성
		if(dto.getApplcntEnt() != null) {
			LogIndvdlInfSrch taskPartcptsLogParam = LogIndvdlInfSrch.builder()
					.memberId(worker.getMemberId())
					.memberIp(CoreUtils.webutils.getRemoteIp(request))
					.workTypeNm("조회")
					.workCn("사업신청 접수관리 상세조회 - 기업정보 : ")
					.trgterId(cmmtMember.getMemberId())
					.email(dto.getApplcntEnt().getCeoEmail())
					.birthday("")
					.mobileNo(dto.getApplcntEnt().getDecReprsntTelno())
					.build();
			indvdlInfSrchUtils.insert(taskPartcptsLogParam);
		}

		// 참여인력 이력 생성
		if(dto.getPartcptslist() != null) {
			for(UsptBsnsPblancApplyTaskPartcpts tpInfo : dto.getPartcptslist()) {
				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(CoreUtils.webutils.getRemoteIp(request))
						.workTypeNm("조회")
						.workCn("사업신청 접수관리 상세조회 - 참여인력정보 : " + tpInfo.getPartcptsNm())
						.trgterId(cmmtMember.getMemberId())
						.email("")
						.birthday(tpInfo.getBrthdy())
						.mobileNo(tpInfo.getMbtlnum())
						.build();
				indvdlInfSrchUtils.insert(tpLogParam);
			}
		}

		return dto;
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param applyId
	 * @param downloadType : ("ALL":전체 / "":단건)
	 * @param attachmentId
	 */
	public void getFileDownload(HttpServletResponse response, String applyId, String attachmentId, String downloadType) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}
		UsptBsnsPblanc pblancInfo = usptBsnsPblancDao.select(info.getPblancId(), worker.getMemberId());
		if(pblancInfo == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		List<ApplyAttachDto> fileList = usptBsnsPblancApplyAttachDao.selectList(applyId, pblancInfo.getBsnsCd());

		if(CoreUtils.string.equals(downloadType, "ALL")) {
			String strAtchmnfl = new String();
			for(int idx=0; idx<fileList.size(); idx++) {
				ApplyAttachDto fileDto = fileList.get(idx);
				if(CoreUtils.string.isNotEmpty(fileDto.getAttachmentId())) {
					if(CoreUtils.string.isBlank(strAtchmnfl)) {
						strAtchmnfl = fileDto.getAttachmentId();
					} else {
						strAtchmnfl += "," + fileDto.getAttachmentId();
					}
				}
			}
			if(CoreUtils.string.isBlank(strAtchmnfl)) {
				throw new InvalidationException("첨부파일이 존재하지 않습니다.");
			}
			attachmentService.downloadFiles(response, config.getDir().getUpload(), strAtchmnfl.split(","), "신청접수_첨부파일");
		} else {
			Optional<ApplyAttachDto> opt = fileList.stream().filter(x->CoreUtils.string.equals(attachmentId, x.getAttachmentId())).findFirst();
			if(opt.isPresent()) {
				attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
			} else {
				throw new InvalidationException("요청한 첨부파일이 존재하지 않습니다.");
			}
		}
	}


	/**
	 * 사업신청 상태 변경
	 * @param applyId
	 * @param rceptSttusCd
	 * @param processCn
	 */
	public void modifyRceptSttus(String applyId, String rceptSttusCd, String processCn) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}

		String chargerAuthorCd = usptBsnsPblancApplcntDao.selectChargerAuth(applyId, worker.getMemberId());
		if(CoreUtils.string.isBlank(chargerAuthorCd)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		info.setRceptSttusCd(rceptSttusCd);
		info.setRceptSttusDt(now);
		info.setCreatedDt(now);
		info.setCreatorId(worker.getMemberId());


		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.접수완료)
				&& CoreUtils.string.equals(info.getRceptSttusCd(), Code.rceptSttus.보완요청) ) {
			throw new InvalidationException("보완요청 상태일 경우 접수완료 처리를 할 수 없습니다.");
		}

		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.반려)) {
			info.setRejectReasonCn(processCn);
		}
		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.보완요청)) {
			info.setMakeupReqCn(processCn);
		}
		/*
		 * 접수완료 취소일 경우
		 */
		if(CoreUtils.string.equals(rceptSttusCd, Code.rceptSttus.신청)) {
			Integer evlTrgetCnt = usptEvlTrgetDao.selectApplyIdCount(applyId);
			if(evlTrgetCnt != 0) {
				throw new InvalidationException("평가대상자는 접수완료 취소 처리를 할 수 없습니다.");
			}
		}
		if(usptBsnsPblancApplcntDao.update(info) != 1) {
			throw new InvalidationException("신청접수 상태 수정 중 오류가 발생했습니다.");
		}

		UsptBsnsPblancApplcntHist histInfo = new UsptBsnsPblancApplcntHist();
		histInfo.setApplyId(applyId);
		histInfo.setHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));
		histInfo.setCreatedDt(now);
		histInfo.setCreatorId(worker.getMemberId());
		histInfo.setOpetrId(worker.getMemberId());
		histInfo.setProcessCn(processCn);
		histInfo.setRceptSttusCd(rceptSttusCd);
		usptBsnsPblancApplcntHistDao.insert(histInfo);
	}


	/**
	 * 사전검토 조회
	 * @param applyId
	 * @return
	 */
	public BsnsApplyBhExmntDto getBhExmnt(String applyId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}
		BsnsApplyBhExmntDto dto = new BsnsApplyBhExmntDto();
		UsptBsnsPblanc pblancInfo = usptBsnsPblancDao.select(info.getPblancId(), worker.getMemberId());
		if(pblancInfo == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		dto.setMakeupOpinionCn(info.getMakeupOpinionCn());
		dto.setBhExmntList(usptBsnsPblancApplyBhExmntDao.selectList(info.getApplyId(), pblancInfo.getBsnsCd()));
		dto.setAtchmnflList(usptBsnsPblancApplyAttachDao.selectList(applyId, pblancInfo.getBsnsCd()));
		return dto;
	}


	/**
	 * 문서 동일비율 조회 및 저장
	 * @param applyId
	 * @param attachmentId
	 */
	public void modifySamenssRate(String applyId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}

		try {
			/*
			 * 검색엔진
			- 운영환경 : 172.22.5.122 서비스포트 : 7700
			- 개발환경 : 172.22.2.25   서비스포트 : 7700
			- listCount : 총검색건수
			- selPer	: 유사비율(기본 50%)
			 */
			//변경 후 운영
//			String documentsUri = "http://172.22.5.122:7700/api/recommandSearch.do?searchFlag=iSimDoc&collection=ISIMDOC&exQuery=" + attachmentId+"&selPer=50";

			String url = "http://172.22.5.122:7700/api/recommandSearch.do";
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("searchFlag", "iSimDoc");
			queryParams.put("collection", "ISIMDOC");
			queryParams.put("exQuery",attachmentId);
//			queryParamsDplct.put("selPer","50");	//유사비율

		 	// HTTP 호출
	    	HttpResponse<String> res = Unirest.get(url)
	    			.queryString(queryParams)
	    			.asString();

	    	// HTTP 오류 처리
	    	if (res.getStatus() != 200) {
	    		String errs = res.getBody();
	    		log.error(errs);
	    		throw new InvalidationException("유사문서검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
	    	}

			JSONObject jObj = new JSONObject(res.getBody());
			String errorCode = jObj.getString("errorCode");

			if(CoreUtils.string.equals(errorCode, "0")) {
				JSONObject resultJObj = jObj.getJSONObject("result");
				JSONObject isimdocJObj = resultJObj.getJSONObject("ISIMDOC");
				String cnt = isimdocJObj.getString("cnt");


				UsptBsnsPblancApplyAtchmnfl bpaaInfo = new UsptBsnsPblancApplyAtchmnfl();
				bpaaInfo.setApplyId(applyId);
				bpaaInfo.setAtchmnflId(attachmentId);
				bpaaInfo.setUpdtDt(new Date());
				bpaaInfo.setUpdusrId(worker.getMemberId());
				bpaaInfo.setSamenssRate(Float.valueOf(cnt));

				if(usptBsnsPblancApplyAtchmnflDao.update(bpaaInfo) != 1) {
					throw new InvalidationException("문서 동일비율 확인 중 오류가 발생했습니다.");
				}

				/********************** 유사파일 목록 List  등록 ******************************/
				 if( Integer.parseInt(cnt) >0) {
//					documentsUri = "http://172.22.5.122:7700/api/recommandSearch.do?searchFlag=iSimDoc&collection=ISIMDOC&listCount="+ Integer.parseInt(0)+"&exQuery=" + attachmentId+"&selPer=50";

					String urlDplct = "http://172.22.5.122:7700/api/recommandSearch.do";
					Map<String, Object> queryParamsDplct = new HashMap<String, Object>();
					queryParamsDplct.put("searchFlag", "iSimDoc");
					queryParamsDplct.put("collection", "ISIMDOC");
					queryParamsDplct.put("listCount", Integer.parseInt(cnt));
					queryParamsDplct.put("exQuery",attachmentId);
//					queryParamsDplct.put("selPer","50");	//유사비율

				 	// HTTP 호출
			    	HttpResponse<String> resDplct = Unirest.get(urlDplct)
			    			.queryString(queryParamsDplct)
			    			.asString();

			    	// HTTP 오류 처리
			    	if (resDplct.getStatus() != 200) {
			    		String errs = resDplct.getBody();
			    		log.error(errs);
			    		throw new InvalidationException("유사문서검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
			    	}

					JSONObject jObjDplct = new JSONObject(resDplct.getBody());
					JSONObject resultJObjDplct = jObjDplct.getJSONObject("result");
					JSONObject isimdocJObjDplct = resultJObjDplct.getJSONObject("ISIMDOC");

					UsptBsnsPblancApplyDplctAtchmnfl deleteParam = new UsptBsnsPblancApplyDplctAtchmnfl();
				    // 값을 VO에 넣어준다.
					deleteParam.setApplyId(applyId);
					deleteParam.setAtchmnflId(attachmentId);
					//삭제
					usptBsnsPblancApplyDplctAtchmnflDao.delete(deleteParam);

					JSONArray jsonArr = isimdocJObjDplct.getJSONArray("collResult");

					log.info("##### jsonArr : " + jsonArr.toString());

					UsptBsnsPblancApplyAtchmnfl selectSetuIdParam = null;
					UsptBsnsPblancApplyDplctAtchmnfl returnParam = null;
					String atchmnflSetupId = "";
					for(Object arr : jsonArr) {
						JSONObject obj = (JSONObject) arr; // JSONArray 데이터를 하나씩 가져와 JSONObject로 변환해준다.

						//첨부파일설정ID 조회
						selectSetuIdParam = new UsptBsnsPblancApplyAtchmnfl();
						selectSetuIdParam.setApplyId(applyId);
						selectSetuIdParam.setAtchmnflId(attachmentId);
						atchmnflSetupId = usptBsnsPblancApplyAtchmnflDao.selectAtchmnflSetupId(selectSetuIdParam);
						//문서목록등록
						returnParam = new UsptBsnsPblancApplyDplctAtchmnfl();
					    // 값을 VO에 넣어준다.
						returnParam.setDplctDocAtchmnflId(CoreUtils.string.getNewId(Code.prefix.문서중복));/** 문서중복ID 생성 */
						returnParam.setApplyId(applyId);
						returnParam.setAtchmnflSetupId(atchmnflSetupId);			/*첨부파일설정ID*/
						returnParam.setAtchmnflId(attachmentId);						/*비교문서ID*/
						returnParam.setDocId((String) obj.get("DOCID"));				/*중복첨부파일ID*/
						returnParam.setFileNm((String) obj.get("FILE_NAME"));		/*파일명*/
						returnParam.setSamenssRate( Float.valueOf(((String) obj.get("RANK")).replaceAll("%", "")) );				/*문서비율*/
						returnParam.setCreatedDt(new Date());
						returnParam.setCreatorId(worker.getMemberId());
						if(usptBsnsPblancApplyDplctAtchmnflDao.insert(returnParam) != 1) {
							throw new InvalidationException("동일비율 문서 등록 중 오류가 발생했습니다.");
						}
					}
				 }

			} else {
				throw new InvalidationException("문서 동일비율 확인 중 오류가 발생했습니다.");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new InvalidationException("문서 동일비율 확인 중 오류가 발생했습니다.");
		}
	}


	/**
	 * 사업신청 목록 조회
	 * @param bsnsApplyListParam
	 * @param page
	 * @param ordtmRcrit
	 * @return
	 */
	public CorePagination<UsptBsnsPblancApplyDplctAtchmnfl> getSamenssRateList(SamenssRateParam samenssRateParam, Long page){
		SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(samenssRateParam.getItemsPerPage() == null) {
			samenssRateParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		long totalItems = usptBsnsPblancApplyDplctAtchmnflDao.selectSamenssRateListCnt(samenssRateParam);
		CorePaginationInfo info = new CorePaginationInfo(page, samenssRateParam.getItemsPerPage(), totalItems);
		samenssRateParam.setBeginRowNum(info.getBeginRowNum());
		List<UsptBsnsPblancApplyDplctAtchmnfl> list = usptBsnsPblancApplyDplctAtchmnflDao.selectSamenssRateList(samenssRateParam);
		CorePagination<UsptBsnsPblancApplyDplctAtchmnfl> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 사전검토 수정
	 * @param applyId
	 * @param bsnsApplyBhExmntParam
	 */
	public void modifyBhExmnt(String applyId, BsnsApplyBhExmntParam bsnsApplyBhExmntParam) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}
		String chargerAuthorCd = usptBsnsPblancApplcntDao.selectChargerAuth(applyId, worker.getMemberId());
		if(CoreUtils.string.isBlank(chargerAuthorCd)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		info.setMakeupOpinionCn(bsnsApplyBhExmntParam.getMakeupOpinionCn());
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());

		if(usptBsnsPblancApplcntDao.update(info) != 1) {
			throw new InvalidationException("사전검토 저장 중 오류가 발생했습니다.");
		}

		List<UsptBsnsPblancApplyBhExmnt> bhExmntList = bsnsApplyBhExmntParam.getBhExmntList();
		if(bhExmntList.size() != 0) {
			for(UsptBsnsPblancApplyBhExmnt regInfo : bhExmntList) {
				regInfo.setApplyId(applyId);
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				usptBsnsPblancApplyBhExmntDao.save(regInfo);
			}
		}
	}


	/**
	 * 이력 목록 조회
	 * @param applyId
	 * @return
	 */
	public JsonList<UsptBsnsPblancApplcntHist> getHist(String applyId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblancApplcnt info = usptBsnsPblancApplcntDao.select(applyId);
		if(info == null) {
			throw new InvalidationException("해당되는 신청접수 내용이 없습니다.");
		}
		String chargerAuthorCd = usptBsnsPblancApplcntDao.selectChargerAuth(applyId, worker.getMemberId());
		if(CoreUtils.string.isBlank(chargerAuthorCd)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		return new JsonList<>(usptBsnsPblancApplcntHistDao.selectList(applyId));
	}

}
