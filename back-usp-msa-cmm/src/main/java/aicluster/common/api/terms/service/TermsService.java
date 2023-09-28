package aicluster.common.api.terms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.terms.dto.TermsParam;
import aicluster.common.common.dao.CmmtCodeDao;
import aicluster.common.common.dao.CmmtStplatDao;
import aicluster.common.common.dto.TermsListItem;
import aicluster.common.common.dto.TermsDetailDto;
import aicluster.common.common.entity.CmmtCode;
import aicluster.common.common.entity.CmmtStplat;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtStplatAgreDtls;
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class TermsService {

	@Autowired
	private CmmtStplatDao cmmtTermsDao;

	@Autowired
	private TermsUtils termsUtils;

	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	/**
	 * 약관유형에 해당하는 시행일 목록을 조회한다.
	 *
	 * @param termsType : 약관유형(G: TERMS_TYPE)
	 * @param srchCd : 조회구분(TOTAL: 전체(default), TODAY: 최근 시행일)
	 * @return 시행일 목록
	 */
	public JsonList<TermsListItem> getDayList(String termsType, String srchCd)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 약관유형 시행일 목록 조회
		List<TermsListItem> list = cmmtTermsDao.selectList_beginDay(termsType, srchCd);

		return new JsonList<>(list);
	}

	/**
	 * 약관유형에 대한 신규 시행일에 해당하는 필수/선택 약관정보 생성
	 *
	 * @param param : 필수/선택 약관정보
	 * @return 약관정보(필수/선택)
	 */
	public TermsDetailDto insert(TermsParam param)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		if(string.isBlank(param.getTermsType())) {
			inputValidateErrs.add("termsType",String.format(validateMessageExt.입력없음, "약관구분"));
		}
		else {
			CmmtCode cmmtCode = cmmtCodeDao.select(CodeExt.termsType.CODE_GROUP, param.getTermsType());
			if (cmmtCode == null) {
				inputValidateErrs.add("termsType", "약관구분이 올바르지 않습니다.");
			}
		}

		if(string.isBlank(param.getBeginDay())) {
			inputValidateErrs.add("beginDay",String.format(validateMessageExt.입력없음, "시작일"));
		}

		if(string.isBlank(param.getRequiredTermsCn())) {
			inputValidateErrs.add("requiredTermsCn",String.format(validateMessageExt.입력없음, "필수 약관내용"));
		}

		if(string.isBlank(param.getPossessTermCd())) {
			inputValidateErrs.add("possesTermCd",String.format(validateMessageExt.선택없음, "보유기간"));
		}
		else {
			CmmtCode cmmtCode = cmmtCodeDao.select(CodeExt.possessTerm.CODE_GROUP, param.getPossessTermCd());
			if (cmmtCode == null) {
				inputValidateErrs.add("possesTermCd", "보유기간이 올바르지 않습니다.");
			}
		}

		//입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		List<CmmtStplat> termsSet = cmmtTermsDao.selectSet(param.getTermsType(), param.getBeginDay());
		if(!termsSet.isEmpty()) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "동일한 시행일의 약관"));
		}

		// 필수약관 데이터 insert
		CmmtStplat requiredTerms = CmmtStplat.builder()
										.termsType(param.getTermsType())
										.beginDay(param.getBeginDay())
										.required(true)
										.termsCn(param.getRequiredTermsCn())
										.possessTermCd(param.getPossessTermCd())
										.createdDt(now)
										.creatorId(worker.getMemberId())
										.updatedDt(now)
										.updaterId(worker.getMemberId())
										.build();
		cmmtTermsDao.insert(requiredTerms);

		// 선택약관 데이터 insert
		if (string.isNotBlank(param.getOptionedTermsCn())) {
			CmmtStplat optionedTerms = CmmtStplat.builder()
											.termsType(param.getTermsType())
											.beginDay(param.getBeginDay())
											.required(false)
											.termsCn(param.getOptionedTermsCn())
											.possessTermCd(param.getPossessTermCd())
											.createdDt(now)
											.creatorId(worker.getMemberId())
											.updatedDt(now)
											.updaterId(worker.getMemberId())
											.build();
			cmmtTermsDao.insert(optionedTerms);
		}

		// 입력 데이터 재조회
		termsSet = cmmtTermsDao.selectSet(param.getTermsType(), param.getBeginDay());

		// 출력VO 생성
		TermsDetailDto detail = new TermsDetailDto();
		for (CmmtStplat terms:termsSet) {
			detail.setTermsType(terms.getTermsType());
			detail.setTermsTypeNm(terms.getTermsTypeNm());
			detail.setBeginDay(terms.getBeginDay());
			detail.setPossessTermCd(terms.getPossessTermCd());
			detail.setPossessTermNm(terms.getPossessTermNm());

			if (terms.isRequired()) {
				detail.setRequiredTermsCn(terms.getTermsCn());
			}
			else {
				detail.setOptionedTermsCn(terms.getTermsCn());
			}
		}

		return detail;
	}

	/**
	 * 약관유형과 시행일에 해당하는 필수/선택 약관정보를 조회한다.
	 *
	 * @param termsType : 약관유형(G: TERMS_TYPE)
	 * @param beginDay : 시작일(=시행일)
	 * @return 약관정보(필수/선택)
	 */
	public TermsDetailDto get(String termsType, String beginDay)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 시행일에 해당하는 필수,선택 약관정보 조회
		List<CmmtStplat> termsSet = cmmtTermsDao.selectSet(termsType, beginDay);
		if(termsSet.isEmpty()) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "시행일의 약관정보"));
		}

		// 출력VO 생성
		TermsDetailDto detail = new TermsDetailDto();
		for (CmmtStplat terms:termsSet) {
			detail.setTermsType(terms.getTermsType());
			detail.setTermsTypeNm(terms.getTermsTypeNm());
			detail.setBeginDay(terms.getBeginDay());
			detail.setPossessTermCd(terms.getPossessTermCd());
			detail.setPossessTermNm(terms.getPossessTermNm());

			if (terms.isRequired()) {
				detail.setRequiredTermsCn(terms.getTermsCn());
			}
			else {
				detail.setOptionedTermsCn(terms.getTermsCn());
			}
		}

		return detail;
	}

	/**
	 * 약관유형과 시행일에 해당하는 필수/선택 약관정보를 수정한다.
	 *
	 * @param param : 약관수정정보
	 * @return 약관정보(필수/선택)
	 */
	public TermsDetailDto modify(TermsParam param) {

		Date now = new Date();
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();

		// 시행일에 해당하는 필수, 선택 약관정보 조회
		List<CmmtStplat> termsSet = cmmtTermsDao.selectSet(param.getTermsType(), param.getBeginDay());
		if(termsSet.isEmpty()) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "시행일의 약관정보"));
		}

		if(string.isBlank(param.getRequiredTermsCn())) {
			throw new InvalidationsException("termsCn",String.format(validateMessageExt.입력없음, "필수 약관내용"));
		}

		if(string.isBlank(param.getPossessTermCd())) {
			throw new InvalidationsException(String.format(validateMessageExt.선택없음, "보유기간"));
		}
		else {
			CmmtCode cmmtCode = cmmtCodeDao.select(CodeExt.possessTerm.CODE_GROUP, param.getPossessTermCd());
			if (cmmtCode == null) {
				throw new InvalidationsException("보유기간이 올바르지 않습니다.");
			}
		}

		// 필수, 선택 약관 데이터 건별 update
		boolean isOptionedRun = false;
		for (CmmtStplat terms:termsSet) {
			// 필수,선택 공통사항 변경
			terms.setPossessTermCd(param.getPossessTermCd());
			terms.setUpdaterId(worker.getMemberId());
			terms.setUpdatedDt(now);

			// 필수,선택 약관내용 변경
			if (terms.isRequired()) {
				terms.setTermsCn(param.getRequiredTermsCn());
			}
			else {
				isOptionedRun = true;
				terms.setTermsCn(param.getOptionedTermsCn());
			}

			// 약관데이터 수정
			cmmtTermsDao.update(terms);
		}

		// 기존에 선택 약관정보가 없었다면 선택 약관데이터 insert
		if (!isOptionedRun && string.isNotBlank(param.getOptionedTermsCn())) {
			CmmtStplat optionedTerms = CmmtStplat.builder()
											.termsType(param.getTermsType())
											.beginDay(param.getBeginDay())
											.required(false)
											.termsCn(param.getOptionedTermsCn())
											.possessTermCd(param.getPossessTermCd())
											.createdDt(now)
											.creatorId(worker.getMemberId())
											.updatedDt(now)
											.updaterId(worker.getMemberId())
											.build();
			cmmtTermsDao.insert(optionedTerms);
		}

		// 수정 데이터 재조회
		termsSet = cmmtTermsDao.selectSet(param.getTermsType(), param.getBeginDay());

		// 출력VO 생성
		TermsDetailDto detail = new TermsDetailDto();
		for (CmmtStplat terms:termsSet) {
			detail.setTermsType(terms.getTermsType());
			detail.setTermsTypeNm(terms.getTermsTypeNm());
			detail.setBeginDay(terms.getBeginDay());
			detail.setPossessTermCd(terms.getPossessTermCd());
			detail.setPossessTermNm(terms.getPossessTermNm());

			if (terms.isRequired()) {
				detail.setRequiredTermsCn(terms.getTermsCn());
			}
			else {
				detail.setOptionedTermsCn(terms.getTermsCn());
			}
		}

		return detail;
	}

	/**
	 * 약관정보 삭제
	 *
	 * @param termsType : 약관유형(G: TERMS_TYPE)
	 * @param beginDay : 시작일(=시행일)
	 */
	public void remove(String termsType, String beginDay)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 시행일에 해당하는 필수, 선택 약관정보 조회
		List<CmmtStplat> termsList = cmmtTermsDao.selectSet(termsType, beginDay);
		if(termsList.isEmpty()) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "시행일의 약관정보"));
		}

		// 약관동의 정보가 존재하는지 검증(약관동의 정보가 있는 경우 삭제 금지)
		for (CmmtStplat terms : termsList) {
			List<CmmtStplatAgreDtls> consentList = termsUtils.selectList(terms.getTermsType(), terms.getBeginDay(), terms.isRequired());
			if ( !consentList.isEmpty() ) {
				StringBuilder reasonMessage = new StringBuilder(terms.getTermsTypeNm());
				reasonMessage.append(terms.isRequired() ? " 필수항목에 대한 " : " 선택항목에 대한 ");
				reasonMessage.append("약관동의정보가 존재합니다.");

				throw new InvalidationException(String.format(validateMessageExt.삭제불가, reasonMessage.toString()));
			}
		}

		// 시행일에 해당하는 필수, 선택 약관정보 delete
		cmmtTermsDao.delete(termsType, beginDay);
	}

	/**
	 * 현재일자 기준 시행일에 해당하는 약관정보 조회
	 *
	 * @param termsType : 약관유형(G: TERMS_TYPE)
	 * @return List<CmmtTerms> : 약관정보목록
	 */
	public JsonList<CmmtStplat> getTodayTerms(String termsType)
	{
		// 오늘 유효한 필수,선택 약관정보 조회
		List<CmmtStplat> list = cmmtTermsDao.select_today(termsType);
		// 삭제하기로 한 사항 - 개인정보 3자 제공 동의
		if(string.equals(termsType, "PRVC_THRD_AGRE_MBR")) {
			list.clear();
		}
		
		return new JsonList<>(list);
	}
}
