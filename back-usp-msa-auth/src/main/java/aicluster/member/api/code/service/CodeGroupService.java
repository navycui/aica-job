package aicluster.member.api.code.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.code.dto.CodeGroupDto;
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
public class CodeGroupService {

	@Autowired
	private CmmtCodeGroupDao cmmtCodeGroupDao;

	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	public JsonList<CmmtCodeGroup> getList(String codeGroup, String codeGroupNm) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		List<CmmtCodeGroup> list = cmmtCodeGroupDao.selectList(codeGroup, codeGroupNm);

		return new JsonList<>(list);
	}

	public CmmtCodeGroup addGroup(CodeGroupDto dto) {

		Date now = new Date();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/*
		 * 입력검사
		 */
		if (string.isBlank(dto.getCodeGroup())) {
			inputValidateErrs.add("codeGroup", String.format(validateMessageExt.입력없음, "코드그룹"));
		}

		if (string.isBlank(dto.getCodeGroupNm())) {
			inputValidateErrs.add("codeGroupNm", String.format(validateMessageExt.입력없음, "코드그룹 이름"));
		}

		if (dto.getEnabled() == null) {
			inputValidateErrs.add("enabled", String.format(validateMessageExt.입력없음, "사용여부"));
		}

		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 코드가 이미 사용 중인지 검사
		CmmtCodeGroup cmmtCodeGroup = cmmtCodeGroupDao.select(dto.getCodeGroup());
		if (cmmtCodeGroup != null) {
			throw new InvalidationException("코드그룹이 이미 사용 중 입니다.");
		}

		/*
		 * 입력
		 */
		cmmtCodeGroup = CmmtCodeGroup.builder()
				.codeGroup(dto.getCodeGroup())
				.codeGroupNm(dto.getCodeGroupNm())
				.remark(dto.getRemark())
				.enabled(dto.getEnabled())
				.createdDt(now)
				.creatorId(worker.getMemberId())
				.updatedDt(now)
				.updaterId(worker.getMemberId())
				.build();
		cmmtCodeGroupDao.insert(cmmtCodeGroup);

		// 입력결과 출력
		cmmtCodeGroup = cmmtCodeGroupDao.select(dto.getCodeGroup());
		return cmmtCodeGroup;
	}

	public CmmtCodeGroup modifyGroup(CodeGroupDto dto) {

		Date now = new Date();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/*
		 * 입력검사
		 */
		if (string.isBlank(dto.getCodeGroup())) {
			inputValidateErrs.add("codeGroup", String.format(validateMessageExt.입력없음, "코드그룹"));
		}

		if (string.isBlank(dto.getCodeGroupNm())) {
			inputValidateErrs.add("codeGroupNm", String.format(validateMessageExt.입력없음, "코드그룹 이름"));
		}

		if (dto.getEnabled() == null) {
			inputValidateErrs.add("enabled", String.format(validateMessageExt.입력없음, "사용여부"));
		}

		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 코드그룹이 존재하는 지 검사
		CmmtCodeGroup cmmtCodeGroup = cmmtCodeGroupDao.select(dto.getCodeGroup());
		if (cmmtCodeGroup == null) {
			throw new InvalidationException("코드그룹이 없습니다.");
		}

		/*
		 * 수정은 DB에서 조회한 값으로 수정해야 한다.
		 */
		cmmtCodeGroup.setCodeGroupNm(dto.getCodeGroupNm());
		cmmtCodeGroup.setEnabled(dto.getEnabled());
		cmmtCodeGroup.setRemark(dto.getRemark());
		cmmtCodeGroup.setUpdatedDt(now);
		cmmtCodeGroup.setUpdaterId(worker.getMemberId());

		cmmtCodeGroupDao.update(cmmtCodeGroup);

		// 수정결과 출력
		cmmtCodeGroup = cmmtCodeGroupDao.select(dto.getCodeGroup());
		return cmmtCodeGroup;
	}

	public void removeGroup(String codeGroup) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(codeGroup) ) {
			throw new InvalidationException("코드그룹을 입력하세요.");
		}

		CmmtCodeGroup cmmtCodeGroup = cmmtCodeGroupDao.select(codeGroup);
		if (cmmtCodeGroup == null) {
			throw new InvalidationException("코드그룹이 없습니다.");
		}

		List<CmmtCode> codeList = cmmtCodeDao.selectList(codeGroup);
		if (codeList.size() > 0) {
			throw new InvalidationException(String.format(validateMessageExt.삭제불가, "코드가 존재합니다."));
		}

		cmmtCodeGroupDao.delete(codeGroup);
	}





}
