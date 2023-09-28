package aicluster.member.api.member.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfDownUtils;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdInfTrgtItem;
import aicluster.framework.common.util.dto.LogIndvdlInfDown;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.member.dto.BlackListDto;
import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.api.member.dto.MemberParam;
import aicluster.member.common.dao.CmmtAuthorDao;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.dao.CmmtMberInfoHistDao;
import aicluster.member.common.dto.MemberDto;
import aicluster.member.common.dto.MemberExcelDto;
import aicluster.member.common.dto.MemberStatsDto;
import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import aicluster.member.common.util.MemberHistUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class MemberService {

	public static final long ITEMS_PER_PAGE = 20;

	@Autowired(required = false)
	private CmmtMberInfoDao cmmtMemberDao;
	@Autowired
	private CmmtAuthorDao cmmtAuthorityDao;
	@Autowired
	private CmmtCodeDao cmmtCodeDao;
	@Autowired
	private CmmtMberInfoHistDao cmmtMemberHistDao;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MemberHistUtils memberHistUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private LogIndvdlInfDownUtils indvdlInfDownUtils;

	/**
	 * 회원 목록 조회
	 *
	 * @param param
	 * @return
	 */
	public CorePagination<MemberDto> getList(MemberGetListParam param, CorePaginationParam pageParam) {

		SecurityUtils.checkWorkerIsInsider();

		if (string.isNotBlank(param.getBeginDay()) && string.isNotBlank(param.getEndDay())) {
			if (date.getDiffDays(string.toDate(param.getBeginDay()), string.toDate(param.getEndDay())) < 0) {
				throw new InvalidationException(String.format(validateMessageExt.일시_작은비교오류, "종료일", "시작일", "날짜"));
			}
		}

		//전체 건수
		long totalItems = cmmtMemberDao.selectCount( param );
		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		//페이지의 목록 조회
		List<MemberDto> list = cmmtMemberDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage());
		//출력할 자료 생성 후 리턴
		CorePagination<MemberDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 회원정보 상세조회
	 *
	 * @param memberId
	 * @return
	 */
	public CmmtMberInfo get(String memberId) {
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();
		if(string.isBlank(memberId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "회원ID"));
		}
		//id 조회
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if(cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "사용자 정보"));
		}
		// log 정보생성
		if (!string.equals(worker.getMemberId(), memberId)) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
										.memberId(worker.getMemberId())
										.memberIp(webutils.getRemoteIp(request))
										.workTypeNm("조회")
										.workCn("회원관리 상세페이지 조회 업무")
										.trgterId(memberId)
										.email(cmmtMember.getEmail())
										.birthday(cmmtMember.getBirthday())
										.mobileNo(cmmtMember.getMobileNo())
										.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return cmmtMember;
	}

	/**
	 * 회원정보 수정
	 *
	 * @param param
	 * @return
	 */
	public CmmtMberInfo modify(MemberParam param) {
		Date now = new Date();
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();

		if(string.isBlank(param.getMemberId())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "회원 ID"));
		}

		//대문자 변환
		String authorityId = string.upperCase(param.getAuthorityId());
		authorityId = string.removeWhitespace(authorityId);
		String memberSt = string.upperCase(param.getMemberSt());
		memberSt = string.removeWhitespace(memberSt);

		String memberId = param.getMemberId();

		//id 조회
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if(cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "사용자 정보"));
		}
		//authorityId,memberSt 존재 여부 검사
		CmmtAuthor cmmtAuthority = cmmtAuthorityDao.select(authorityId);
		if(cmmtAuthority == null) {
			throw new InvalidationException("권한이 올바르지 않습니다.");
		}
		CmmtCode cmmtCode = cmmtCodeDao.select("MEMBER_ST",memberSt);
		if(cmmtCode == null) {
			throw new InvalidationException("회원상태가 올바르지 않습니다.");
		}

		//cmmtMember를 cmmtMemberNew로 복사
		CmmtMberInfo cmmtMemberNew = new CmmtMberInfo();
		CoreUtils.property.copyProperties(cmmtMemberNew, cmmtMember);

		//cmmtMemberNew에 입력받은 정보 세팅
		cmmtMemberNew.setAuthorityId(authorityId);
		cmmtMemberNew.setMemberSt(memberSt);
//		cmmtMemberNew.setBlackListBeginDay(param.getBlackListBeginDay());
//		cmmtMemberNew.setBlackListEndDay(param.getBlackListEndDay());
//		cmmtMemberNew.setBlackListReason(param.getBlackListReason());
		cmmtMemberNew.setInstr(param.getInstr());
		cmmtMemberNew.setUpdaterId(worker.getMemberId());
		cmmtMemberNew.setUpdatedDt(now);

		//CMMT_MEMBER update
		cmmtMemberDao.update(cmmtMemberNew);
		cmmtMemberNew = cmmtMemberDao.select(memberId);

		//변경이력 등록
		memberHistUtils.addChangeHist(worker.getMemberId(), cmmtMember, cmmtMemberNew);

		// log 정보생성
		if (!string.equals(worker.getMemberId(), memberId)) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
					.memberId(worker.getMemberId())
					.memberIp(webutils.getRemoteIp(request))
					.workTypeNm("변경")
					.workCn("회원관리 정보 변경 업무")
					.trgterId(memberId)
					.email(cmmtMemberNew.getEmail())
					.birthday(cmmtMemberNew.getBirthday())
					.mobileNo(cmmtMemberNew.getMobileNo())
					.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return cmmtMemberNew;

	}

	/**
	 * 로그인 사용자 회원탈퇴
	 */
	public void secession()
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkLogin();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if(cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "사용자 정보"));
		}

		if (string.equals(cmmtMember.getMemberSt(), CodeExt.memberSt.탈퇴)) {
			throw new InvalidationException("이미 탈퇴 계정입니다.");
		}
		cmmtMember.setMemberSt(CodeExt.memberSt.탈퇴);
		cmmtMember.setMemberStDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());
		cmmtMember.setUpdatedDt(now);

		cmmtMemberDao.update(cmmtMember);

		// 회원변경이력 등록
		memberHistUtils.addSecessionHist(worker.getMemberId());
	}

	/**
	 * 불량회원 설정
	 *
	 * @param blackListDto
	 * @return
	 */
	public CmmtMberInfo black(BlackListDto blackListDto) {
		Date now = new Date();
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		if(string.isBlank(blackListDto.getMemberId())) {
			inputValidateErrs.add("memberId",String.format(validateMessageExt.입력없음, "회원 ID"));
		}

		if(blackListDto.getRegisterReasons() == null) {
			inputValidateErrs.add("registerResonsList",String.format(validateMessageExt.체크없음, "등록사유"));
		}

		if(string.isBlank(blackListDto.getLimitBeginDt())) {
			inputValidateErrs.add("limitBeginDt","이용제한 시작일을 설정해주세요.");
		} else {
			Date beginDt = string.toDate(blackListDto.getLimitBeginDt());
			if (beginDt == null) {
				inputValidateErrs.add("limitBeginDt", "시작일이 유효하지 않습니다.");
			}
		}

		if(string.isBlank(blackListDto.getLimitEndDt())) {
			inputValidateErrs.add("limitEndDt","이용제한 종료일을 설정해주세요.");
		} else {
			Date beginDt = string.toDate(blackListDto.getLimitEndDt());
			if (beginDt == null) {
				inputValidateErrs.add("limitEndDt", "시작일이 유효하지 않습니다.");
			}
		}

		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}
		String memberId = blackListDto.getMemberId();
		String[] registerReasonsList = blackListDto.getRegisterReasons();
		String limitBeginDt = string.getNumberOnly(blackListDto.getLimitBeginDt());
		String limitEndDt = string.getNumberOnly(blackListDto.getLimitEndDt());
		String detailReason = blackListDto.getDetailReason();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if(cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "사용자 정보"));
		}

		if(cmmtMember.getMemberSt().equals(CodeExt.memberSt.정상) == false ) {
			throw new InvalidationException("회원상태가 '정상'인 회원만 불량회원 등록이 가능합니다.");
		}

		boolean codeValidate = true;

		StringBuilder message = new StringBuilder();

		for (String reasonCode : registerReasonsList) {
			CmmtCode cmmtCode = cmmtCodeDao.select("BLACK_LIST_RI", reasonCode);
			if(cmmtCode == null) {
				codeValidate = false;
			}else {
				message.append(cmmtCode.getCodeNm()).append("\n");
			}
		}

		if(codeValidate == false) {
			throw new InvalidationException(String.format(validateMessageExt.포함불가, "등록사유 코드"));
		}

		if(CoreUtils.date.isValidDate(limitBeginDt, "yyyymmdd") == false) {
			throw new InvalidationException(String.format(validateMessageExt.유효오류, "시작일자"));
		}

		if(CoreUtils.date.isValidDate(limitEndDt, "yyyymmdd") == false) {
			throw new InvalidationException(String.format(validateMessageExt.유효오류, "종료일자"));
		}

		Date beginDt = CoreUtils.date.parseDateSilently(limitBeginDt, "yyyymmdd");
		Date endDt = CoreUtils.date.parseDateSilently(limitEndDt, "yyyymmdd");

		if(CoreUtils.date.compareDay(beginDt, endDt) >= 0 ) {
			throw new InvalidationException(String.format(validateMessageExt.크거나같은비교오류	, "시작일", "종료일"));
		}

		CmmtMberInfo cmmtMemberNew = new CmmtMberInfo();
		CoreUtils.property.copyProperties(cmmtMemberNew, cmmtMember);

		cmmtMemberNew.setMemberSt(CodeExt.memberSt.불량회원);
		cmmtMemberNew.setMemberStDt(now);
		cmmtMemberNew.setBlackListBeginDay(limitBeginDt);
		cmmtMemberNew.setBlackListEndDay(limitEndDt);
		cmmtMemberNew.setBlackListReason(detailReason);
		cmmtMemberNew.setUpdaterId(worker.getMemberId());
		cmmtMemberNew.setUpdatedDt(now);

		cmmtMemberDao.update(cmmtMemberNew);
		cmmtMemberNew = cmmtMemberDao.select(cmmtMember.getMemberId());

		// 변경이력 등록
		memberHistUtils.addChangeHist(worker.getMemberId(), cmmtMember, cmmtMemberNew);

		return cmmtMemberNew;
	}

	/**
	 * 불량회원 해제
	 *
	 * @param memberId
	 * @return
	 */
	public CmmtMberInfo unblack(String memberId) {
		Date now = new Date();
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();

		if(string.isBlank(memberId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "회원 ID"));
		}

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if(cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "사용자 정보"));
		}

		if(cmmtMember.getMemberSt().equals(CodeExt.memberSt.불량회원) == false) {
			throw new InvalidationException("회원상태가 '불량회원'인 회원만 불량회원 해제가 가능합니다.");
		}

		CmmtMberInfo cmmtMemberNew = new CmmtMberInfo();
		CoreUtils.property.copyProperties(cmmtMemberNew, cmmtMember);

		cmmtMemberNew.setMemberSt(CodeExt.memberSt.정상);
		cmmtMemberNew.setMemberStDt(new Date());
		cmmtMemberNew.setBlackListBeginDay(null);
		cmmtMemberNew.setBlackListEndDay(null);
		cmmtMemberNew.setBlackListReason(null);
		cmmtMemberNew.setUpdaterId(worker.getMemberId());
		cmmtMemberNew.setUpdatedDt(now);

		cmmtMemberDao.update(cmmtMemberNew);
		cmmtMemberNew = cmmtMemberDao.select(cmmtMemberNew.getMemberId());

		// 이력 등록
		memberHistUtils.addChangeHist(worker.getMemberId(), cmmtMember, cmmtMemberNew);

		return cmmtMemberNew;
	}

	/**
	 * 회원 이력 목록 조회
	 *
	 * @param memberId
	 * @return
	 */
	public JsonList<CmmtMberInfoHist> getHistList(String memberId) {
		SecurityUtils.checkWorkerIsInsider();

		//memberId로 cmmt_member를 조회한다.
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if(cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}

		//cmmt_member_hist을 조회한다.

		List<CmmtMberInfoHist> list = cmmtMemberHistDao.selectListByMemberId(memberId);

		return new JsonList<>(list);
	}

	/**
	 * 회원 상태 목록 조회
	 *
	 * @param searchType
	 * @param memberType
	 * @param beginDay
	 * @param endDay
	 * @return
	 */
	public MemberStatsDto getStatsList(String searchType, String memberType, String beginDay, String endDay)
	{
		// 내부사용자 확인
		SecurityUtils.checkWorkerIsInsider();

		// 필수값 체크
		if (string.isBlank(searchType)) {
			throw new InvalidationException(String.format(CodeExt.validateMessageExt.입력없음, "검색구분"));
		}
		if (!date.isValidDate(beginDay, "yyyyMMdd")) {
			throw new InvalidationException(String.format(CodeExt.validateMessageExt.날짜없음, "조회기간(시작일)"));
		}
		if (!date.isValidDate(endDay, "yyyyMMdd")) {
			throw new InvalidationException(String.format(CodeExt.validateMessageExt.날짜없음, "조회기간(종료일)"));
		}

		// 기간 검증
		if (date.compareDay(string.toDate(beginDay), string.toDate(endDay)) > -1) {
			throw new InvalidationException(String.format(CodeExt.validateMessageExt.크거나같은비교오류, "조회기간(시작일)", "조회기간(종료일)"));
		}

		MemberStatsDto memberStats = cmmtMemberDao.selectCurrStats();

		if ("DAY".equals(searchType)) {
			memberStats.setStatsList(cmmtMemberDao.selectDayStatsList(memberType, beginDay, endDay));
		}
		else if ("MONTH".equals(searchType)) {
			memberStats.setStatsList(cmmtMemberDao.selectMonthStatsList(memberType, beginDay, endDay));
		}

		return memberStats;
	}

	/**
	 * 회원 비밀번호 초기화
	 *
	 * @param memberId
	 */
	public void passwdInit(String memberId) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 회원정보 조회
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if (cmmtMember == null) {
			throw new InvalidationException(String.format(CodeExt.validateMessageExt.조회결과없음, "회원정보"));
		}

		// 이메일 주소 검사
		if (string.isBlank(cmmtMember.getEmail())) {
			throw new InvalidationException("이메일이 없어 비밀번호를 초기화할 수 없습니다.");
		}

		// 임시 비밀번호 생성
		String tmpPasswd = CoreUtils.password.getRandomPassword();
		String encTmpPasswd = CoreUtils.password.encode(tmpPasswd);

		// 이메일 발송
		String emailCn = CoreUtils.file.readFileString("/form/email/email-tmp-passwd.html");
		if (string.isBlank(emailCn)) {
			throw new InvalidationException("이메일 본문파일을 찾을 수 없습니다.");
		}
		EmailSendParam eparam = new EmailSendParam();
		eparam.setContentHtml(true);
		eparam.setEmailCn(emailCn);
		eparam.setTitle("[인공지능산업융합사업단] 비밀번호 초기화 안내");
		Map<String, String> templateParam = new HashMap<>();
		templateParam.put("tmpPasswd", tmpPasswd);
		eparam.addRecipient(cmmtMember.getEmail(), cmmtMember.getMemberNm(), templateParam);

		EmailResult er = emailUtils.send(eparam);
		if (er.getFailCnt() > 0) {
			throw new InvalidationException("이메일 발송에 실패하여 비밀번호 초기화를 중단하였습니다.");
		}

		// 임시 비밀번호 Update
		cmmtMember.setEncPasswd(encTmpPasswd);
		cmmtMember.setPasswdDt(now);
		cmmtMember.setPasswdInit(true);
		cmmtMemberDao.update(cmmtMember);

		// 비밀번호 초기화 이력 생성
		memberHistUtils.addPasswdInitHist(worker.getMemberId(), memberId);

	}

	/**
	 * 회원목록 엑셀다운로드 데이터 조회
	 *
	 * @param param 검색조건
	 * @return
	 */
	public List<MemberExcelDto> getExcelData(String workCn, String filename)
	{
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		MemberGetListParam param = new MemberGetListParam();
		if(string.isEmpty(workCn)) {
			workCn = "회원 목록을 엑셀다운로드합니다.";
		}
		
		//전체 건수
		long totalItems = cmmtMemberDao.selectCount( param );

		//목록 조회
		List<MemberDto> list = cmmtMemberDao.selectList(param, 1L, totalItems);

		//엑셀 다운로드 데이터 생성 및 개인정보 다운로드 이력 기록
		List<MemberExcelDto> excelList = new ArrayList<>();
		List<LogIndvdInfTrgtItem> logTrgt = new ArrayList<>();
		for (MemberDto dto : list) {
			MemberExcelDto excelDto = new MemberExcelDto();
			property.copyProperties(excelDto, dto);
			excelList.add(excelDto);

			//다운로드 대상자 정보 생성
			LogIndvdInfTrgtItem trgtItem = new LogIndvdInfTrgtItem();
			trgtItem.setTrgterId(excelDto.getMemberId());
			trgtItem.setTrgterNm(excelDto.getMemberNm());
			trgtItem.setEmail(excelDto.getEmail());
			trgtItem.setBirthday(excelDto.getBirthday());
			trgtItem.setMobileNo(excelDto.getMobileNo());
			logTrgt.add(trgtItem);
		}

		//개인정보 다운로드 이력 데이터 생성
		LogIndvdlInfDown logParam = LogIndvdlInfDown.builder()
										.memberId(worker.getMemberId())
										.memberIp(webutils.getRemoteIp(request))
										.workTypeNm("엑셀다운로드")
										.workCn(workCn)
										.menuId("menu-ADM100600")
										.menuNm("회원관리")
										.fileNm(filename)
										.trgtIdList(logTrgt)
										.build();


		//개인정보 다운로드 이력 insert
		indvdlInfDownUtils.insertList(logParam);

		return excelList;
	}


}

