package aicluster.member.api.member.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.member.dto.EnterpriseParam;
import aicluster.member.common.dao.CmmtEntrprsInfoDao;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtEntrprsInfo;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;

@Service
public class EnterpriseService {

	@Autowired
	private CmmtEntrprsInfoDao cmmtEntDao;

	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;

	public CmmtEntrprsInfo add(EnterpriseParam enterParam) {
		Date now = new Date();

		// 로그인 여부를 검증하고 회원구분이 개인사업자/법인사업자/대학인지 검증한다.
		BnMember worker = SecurityUtils.checkLogin();
		if( !CodeExt.memberTypeExt.isBzmn(worker.getMemberType())) {
			throw new InvalidationException("기업정보 등록이 허용되지 않았습니다.\n(관리자에게 문의하세요.)");
		}

		// 회원의 기업정보가 존재하는지 검사한다.
		CmmtEntrprsInfo cmmtEnt = cmmtEntDao.select(worker.getMemberId());
		if(cmmtEnt != null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "기업정보" ));
		}

		// enterpriseParam 입력 VO의 memberId 세팅
		enterParam.setMemberId(worker.getMemberId());

		InvalidationsException Errs = new InvalidationsException();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(enterParam.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "회원정보"));
		}

		if(string.isBlank(enterParam.getCmpnyTypeCd())) {
			Errs.add("cmpnyTypeCd", String.format(validateMessageExt.입력없음, "기업유형코드"));
		}
		if(string.isBlank(enterParam.getIndustRealmCd())) {
			Errs.add("industRealmCd", String.format(validateMessageExt.입력없음, "사업분야코드"));
		}
		if(string.isBlank(enterParam.getFondDay())) {
			Errs.add("fondDay", String.format(validateMessageExt.입력없음, "설립일"));
		} else {
			Date fondDay = string.toDate(enterParam.getFondDay());
			if (fondDay == null) {
				Errs.add("fondDay", String.format(validateMessageExt.유효오류, "설립일"));
			}
			else {
				String Day = date.format(fondDay, "yyyyMMdd");
				enterParam.setFondDay(Day);
			}
		}
		if(enterParam.getEmplyCnt() == null) {
			Errs.add("emplyCnt", String.format(validateMessageExt.입력없음, "종업원수"));
		}
		if(enterParam.getResdngNmpr() == null) {
			Errs.add("resdngNmpr", String.format(validateMessageExt.입력없음, "상주인원수"));
		}
		if(enterParam.getEmpmnPrearngeNmpr() == null) {
			Errs.add("empmnPrearngeNmpr", String.format(validateMessageExt.입력없음, "채용예정인력수"));
		}

		// 입력값 검증 결과 메시지 처리
		if (Errs.size() > 0) {
			throw Errs;
		}

		cmmtEnt = CmmtEntrprsInfo.builder()
				.memberId(worker.getMemberId())
				.cmpnyTypeCd(enterParam.getCmpnyTypeCd())
				.industRealmCd(enterParam.getIndustRealmCd())
				.fondDay(enterParam.getFondDay())
				.emplyCnt(enterParam.getEmplyCnt())
				.resdngNmpr(enterParam.getResdngNmpr())
				.empmnPrearngeNmpr(enterParam.getEmpmnPrearngeNmpr())
				.induty(enterParam.getInduty())
				.mainInduty(enterParam.getMainInduty())
				.mainTchnlgyProduct(enterParam.getMainTchnlgyProduct())
				.zip(enterParam.getZip())
				.adres(enterParam.getAdres())
				.encFxnum(enterParam.getEncFxnum())
				.encReprsntTelno(enterParam.getEncReprsntTelno())
				.encCeoTelno(enterParam.getEncCeoTelno())
				.encCeoEmail(enterParam.getEncCeoEmail())
				.newFntnPlanCd(enterParam.getNewFntnPlanCd())
				.fondPlanCd(enterParam.getFondPlanCd())
				.prvyySalamt(enterParam.getPrvyySalamt())
				.creatorId(worker.getMemberId())
				.createdDt(now)
				.updaterId(worker.getMemberId())
				.updatedDt(now)
				.build();

		cmmtEntDao.insert(cmmtEnt);

		CmmtEntrprsInfo selectEnt = cmmtEntDao.select(worker.getMemberId());
		return selectEnt;
	}

	public CmmtEntrprsInfo get() {

		// 로그인 여부를 검증하고 회원구분이 개인사업자/법인사업자/대학인지 검증한다.
		BnMember worker = SecurityUtils.checkLogin();
		if( !CodeExt.memberTypeExt.isBzmn(worker.getMemberType()) ) {
			throw new InvalidationException("기업정보 등록이 허용되지 않았습니다.\n(관리자에게 문의하세요.)");
		}


		CmmtEntrprsInfo cmmtEnt = cmmtEntDao.select(worker.getMemberId());
//		if(cmmtEnt == null) {
//			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "기업정보"));
//		}

		return cmmtEnt;
	}

	public CmmtEntrprsInfo modify(EnterpriseParam enterParam) {

		Date now = new Date();
		// 로그인 여부를 검증하고 회원구분이 개인사업자/법인사업자/대학인지 검증한다.
		BnMember worker = SecurityUtils.checkLogin();
		if( !CodeExt.memberTypeExt.isBzmn(worker.getMemberType()) ) {
			throw new InvalidationException("기업정보 등록이 허용되지 않았습니다.\n(관리자에게 문의하세요.)");
		}

		// 회원의 기업정보가 존재하는지 검사한다.
		CmmtEntrprsInfo cmmtEnt = cmmtEntDao.select(worker.getMemberId());
		if(cmmtEnt == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "기업정보"));
		}

		enterParam.setMemberId(worker.getMemberId());

		if(!string.equals(worker.getMemberId(), cmmtEnt.getMemberId())) {
			throw new InvalidationException(String.format(validateMessageExt.수정권한없음, "기업정보 소유자"));
		}

		InvalidationsException Errs = new InvalidationsException();
		if(string.isBlank(enterParam.getIndustRealmCd())) {
			Errs.add("industryField", String.format(validateMessageExt.입력없음, "사업분야"));
		}
		if(string.isBlank(enterParam.getCmpnyTypeCd())) {
			Errs.add("companyType", String.format(validateMessageExt.입력없음, "기업구분"));
		}
		if(string.isBlank(enterParam.getFondDay())) {
			Errs.add("fondDay", String.format(validateMessageExt.입력없음, "설립일"));
		} else {
			Date fondDay = string.toDate(enterParam.getFondDay());
			if (fondDay == null) {
				Errs.add("fondDay", String.format(validateMessageExt.유효오류, "설립일"));
			}
			else {
				String Day = date.format(fondDay, "yyyyMMdd");
				enterParam.setFondDay(Day);
			}
		}
		if(enterParam.getEmplyCnt() == null) {
			Errs.add("emplyCnt", String.format(validateMessageExt.입력없음, "종업원수"));
		}
		if(enterParam.getResdngNmpr() == null) {
			Errs.add("resdngNmpr", String.format(validateMessageExt.입력없음, "상주인원수"));
		}
		if(enterParam.getEmpmnPrearngeNmpr() == null) {
			Errs.add("empmnPrearngeNmpr", String.format(validateMessageExt.입력없음, "채용예정인력수"));
		}

		// 입력값 검증 결과 메시지 처리
		if (Errs.size() > 0) {
			throw Errs;
		}

		cmmtEnt.setCmpnyTypeCd(enterParam.getCmpnyTypeCd());
		cmmtEnt.setIndustRealmCd(enterParam.getIndustRealmCd());
		cmmtEnt.setFondDay(enterParam.getFondDay());
		cmmtEnt.setEmplyCnt(enterParam.getEmplyCnt());
		cmmtEnt.setResdngNmpr(enterParam.getResdngNmpr());
		cmmtEnt.setEmpmnPrearngeNmpr(enterParam.getEmpmnPrearngeNmpr());
		cmmtEnt.setInduty(enterParam.getInduty());
		cmmtEnt.setMainInduty(enterParam.getMainInduty());
		cmmtEnt.setMainTchnlgyProduct(enterParam.getMainTchnlgyProduct());
		cmmtEnt.setZip(enterParam.getZip());
		cmmtEnt.setAdres(enterParam.getAdres());
		cmmtEnt.setEncFxnum(enterParam.getEncFxnum());
		cmmtEnt.setEncReprsntTelno(enterParam.getEncReprsntTelno());
		cmmtEnt.setEncCeoTelno(enterParam.getEncCeoTelno());
		cmmtEnt.setEncCeoEmail(enterParam.getEncCeoEmail());
		cmmtEnt.setNewFntnPlanCd(enterParam.getNewFntnPlanCd());
		cmmtEnt.setFondPlanCd(enterParam.getFondPlanCd());
		cmmtEnt.setPrvyySalamt(enterParam.getPrvyySalamt());
		cmmtEnt.setUpdaterId(worker.getMemberId());
		cmmtEnt.setUpdatedDt(now);

		cmmtEntDao.update(cmmtEnt);

		CmmtEntrprsInfo selectEnt = cmmtEntDao.select(enterParam.getMemberId());
		return selectEnt;
	}

	public void remove() {

		// 로그인 여부를 검증하고 회원구분이 개인사업자/법인사업자/대학인지 검증한다.
		BnMember worker = SecurityUtils.checkLogin();
		if( !CodeExt.memberTypeExt.isBzmn(worker.getMemberType()) ) {
			throw new InvalidationException("기업정보 등록이 허용되지 않았습니다.\n(관리자에게 문의하세요.)");
		}

		CmmtEntrprsInfo cmmtEnt = cmmtEntDao.select(worker.getMemberId());
		if(cmmtEnt == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "기업정보"));
		}

		cmmtEntDao.delete(worker.getMemberId());

	}

	public CmmtEntrprsInfo getMemberEnt(String memberId) {
		// 로그인 사용자가 내부사용자인지 검증한다.
		SecurityUtils.checkWorkerIsInsider();

		// memberId로 기업정보가 존재하는지 검사한다.
		CmmtEntrprsInfo cmmtEnt = cmmtEntDao.select(memberId);
//		if(cmmtEnt == null) {
//			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "기업정보"));
//		}

		return cmmtEnt;
	}





}
