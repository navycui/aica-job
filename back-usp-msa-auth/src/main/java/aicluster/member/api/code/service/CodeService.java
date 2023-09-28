package aicluster.member.api.code.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtCodeGroupDao;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtCodeGroup;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class CodeService {

	@Autowired
	private CmmtCodeDao cmmtCodeDao;
	@Autowired
	private CmmtCodeGroupDao cmmtCodeGroupdao;



	public JsonList<CmmtCode> getList(String codeGroup) {

		SecurityUtils.checkWorkerIsInsider();

		//입력값 codeGroup을 대문자로 변환
		codeGroup = string.upperCase(codeGroup);

		if (string.isBlank(codeGroup)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "코드그룹"));
		}

		List<CmmtCode> list = cmmtCodeDao.selectList(codeGroup);

		return new JsonList<>(list);
	}

	public JsonList<CmmtCode> add(CmmtCode requestCode) {

		Date now = new Date();
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		/*
		 * 입력검사
		 */
		if (string.isBlank(requestCode.getCodeGroup())) {
			inputValidateErrs.add("codeGroup",String.format(validateMessageExt.입력없음, "코드그룹"));
		}
		if (string.isBlank(requestCode.getCode())) {
			inputValidateErrs.add("code",String.format(validateMessageExt.입력없음, "코드"));
		}
		if (string.isBlank(requestCode.getCodeNm())) {
			inputValidateErrs.add("codeNm",String.format(validateMessageExt.입력없음, "코드명"));
		}
		if (requestCode.getEnabled() == null) {
			inputValidateErrs.add("enabled",String.format(validateMessageExt.입력없음, "사용여부"));
		}
		if (requestCode.getSortOrder() == null) {
			inputValidateErrs.add("sortOrder",String.format(validateMessageExt.입력없음, "정렬순서"));
		}
		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 대문자 변환
		String codeGroup = string.upperCase(requestCode.getCodeGroup());
		String code = string.upperCase(requestCode.getCode());

		String codeNm = requestCode.getCodeNm();
		String remark = requestCode.getRemark();
		String codeType = requestCode.getCodeType();
		Boolean enabled = requestCode.getEnabled();
		Long sortOrder = requestCode.getSortOrder();
		// 코드그룹이 정상인지 확인
		CmmtCodeGroup cmmtCodeGroup = cmmtCodeGroupdao.select(codeGroup);
		if (cmmtCodeGroup == null) {
			throw new InvalidationException("등록되어 있지 않은 코드그룹입니다.");
		}

		// 코드가 이미 등록되어 있는 지 검사
		CmmtCode cmmtCode = cmmtCodeDao.select(codeGroup, code);
		if (cmmtCode != null) {
			throw new InvalidationException(String.format(validateMessageExt.중복오류, "코드", code));
		}

		// 입력
		cmmtCode = CmmtCode.builder()
				.codeGroup(codeGroup)
				.code(code)
				.codeNm(codeNm)
				.remark(remark)
				.codeType(codeType)
				.enabled(enabled)
				.sortOrder(sortOrder)
				// 생성로그 입력
				.createdDt(now)
				.creatorId(worker.getMemberId())
				.updatedDt(now)
				.updaterId(worker.getMemberId())
				.build();
		cmmtCodeDao.insert(cmmtCode);

		// 입력결과 출력
		List<CmmtCode> list = cmmtCodeDao.selectList( codeGroup );

		return new JsonList<>(list);
	}

	public JsonList<CmmtCode> modify(CmmtCode requestCode) {

		Date now = new Date();
		BnMember worker = 	SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		/*
		 * 입력검사
		 */
		if (string.isBlank(requestCode.getCodeGroup())) {
			inputValidateErrs.add("codeGroup",String.format(validateMessageExt.입력없음, "코드그룹"));
		}
		if (string.isBlank(requestCode.getCode())) {
			inputValidateErrs.add("code",String.format(validateMessageExt.입력없음, "코드"));
		}
		if (string.isBlank(requestCode.getCodeNm())) {
			inputValidateErrs.add("codeNm",String.format(validateMessageExt.입력없음, "코드명"));
		}
		if (requestCode.getEnabled() == null) {
			inputValidateErrs.add("enabled",String.format(validateMessageExt.입력없음, "사용여부"));
		}
		if (requestCode.getSortOrder() == null) {
			inputValidateErrs.add("sortOrder",String.format(validateMessageExt.입력없음, "정렬순서"));
		}
		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 대문자 변환
		String codeGroup = string.upperCase(requestCode.getCodeGroup());
		String code = string.upperCase(requestCode.getCode());

		String codeNm = requestCode.getCodeNm();
		String remark = requestCode.getRemark();
		String codeType = requestCode.getCodeType();
		Boolean enabled = requestCode.getEnabled();
		Long sortOrder = requestCode.getSortOrder();

		// 코드가 등록되어 있는 지 검사
		CmmtCode cmmtCode = cmmtCodeDao.select(codeGroup, code);
		if(cmmtCode == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "코드정보"));
		}

		// 수정할 때는 DB에서 읽은 값을 수정해야 한다.
		//cmmtCode.setCodeGroup(codeGroup); 코드그룹은 Key이므로 수정하지 않음.
		//cmmtCode.setCode(codeType); 코드는 Key이므로 수정하지 않음.
		cmmtCode.setCodeNm(codeNm);
		cmmtCode.setCodeType(codeType);
		cmmtCode.setEnabled(enabled);
		cmmtCode.setRemark(remark);
		cmmtCode.setSortOrder(sortOrder);
		cmmtCode.setUpdatedDt(now);
		cmmtCode.setUpdaterId(worker.getMemberId());

		cmmtCodeDao.update(cmmtCode);

		// 수정결과 출력
		List<CmmtCode> list = cmmtCodeDao.selectList( codeGroup );
		return new JsonList<>(list);
	}

	public void remove(String codeGroup, String code) {
		SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		/*
		 * 입력검사
		 */
		if (string.isBlank(codeGroup)) {
			inputValidateErrs.add("codeGroup",String.format(validateMessageExt.입력없음, "코드그룹"));
		}
		if (string.isBlank(code)) {
			inputValidateErrs.add("code",String.format(validateMessageExt.입력없음, "코드"));
		}

		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 대문자 변환
		codeGroup = string.upperCase(codeGroup);
		code = string.upperCase(code);

		// 코드가 존재하는 지 검사
		CmmtCode cmmtCode = cmmtCodeDao.select(codeGroup, code);
		if(cmmtCode == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "코드정보"));
		}

		// 삭제
		cmmtCodeDao.delete(codeGroup, code);
	}

	public JsonList<CmmtCode> getEnabledList(String codeGroup) {
		return getEnabledList(codeGroup, null);
	}

	public JsonList<CmmtCode> getEnabledList(String codeGroup, String codeType) {
		List<CmmtCode> list = cmmtCodeDao.selectList_enabled(codeGroup, codeType);

		return new JsonList<>(list);
	}

}
