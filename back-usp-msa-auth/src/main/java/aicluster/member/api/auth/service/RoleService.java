package aicluster.member.api.auth.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.common.dao.CmmtAuthorDao;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtMenuRoleDao;
import aicluster.member.common.dao.CmmtProgrmDao;
import aicluster.member.common.dao.CmmtRoleDao;
import aicluster.member.common.entity.CmmtAuthorRole;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtProgrmRole;
import aicluster.member.common.entity.CmmtRole;
import aicluster.member.common.util.CodeExt.prefix;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class RoleService {
	@Autowired
	private CmmtRoleDao roleDao;
	@Autowired
	private CmmtAuthorDao authorityDao;
	@Autowired
	private CmmtProgrmDao programDao;
	@Autowired
	private CmmtMenuRoleDao cmmtMenuRoleDao;
	@Autowired
	private CmmtCodeDao codeDao;

	public JsonList<CmmtRole> getList()
	{
		SecurityUtils.checkWorkerIsInsider();

		// '미로그인' 역할 확인 후 생성
		CmmtRole cmmtRole = roleDao.select("ROLE_ANONYMOUS");
		if (cmmtRole == null) {
			cmmtRole = CmmtRole.builder().roleId("ROLE_ANONYMOUS").roleNm("미로그인").build();
			roleDao.insert(cmmtRole);
		}

		// 역할 목록 조회
		List<CmmtRole> list = roleDao.selectList();

		return new JsonList<>(list);
	}

	public CmmtRole insert(String roleId, String roleNm)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(roleId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "역할ID"));  // %s를 입력하세요.
		}
		if (string.isBlank(roleNm)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "역할명"));  // %s를 입력하세요.
		}

		// Whitespace 삭제
		roleId = string.removeWhitespace(roleId);
		roleNm = string.removeWhitespace(roleNm);

		// 대문자 변환
		roleId = string.upperCase(roleId);
		roleNm = string.upperCase(roleNm);

		// 역할ID prefix 검증
		if (!string.startsWith(roleId, prefix.ROLE)) {
			throw new InvalidationException(String.format(validateMessageExt.접두어오류, "역할ID", prefix.ROLE, roleId));  // %s는 '%s'로 시작되어야 합니다.
		}

		// 역할ID 중복 검증
		CmmtRole chkCmmtRole = roleDao.select(roleId);
		if (chkCmmtRole != null) {
			throw new InvalidationException(String.format(validateMessageExt.중복오류, "역할ID", roleId));  // 사용 중인 %s 입니다.[%s]
		}

		// 역할명 중복 검증
		chkCmmtRole = null;
		chkCmmtRole = roleDao.selectByName(roleNm);
		if (chkCmmtRole != null) {
			throw new InvalidationException(String.format(validateMessageExt.중복오류, "역할명", roleNm));  // 사용 중인 %s 입니다.[%s]
		}

		// Role Entity 세팅
		CmmtRole cmmtRole = CmmtRole.builder().roleId(roleId).roleNm(roleNm).build();

		// 테이블 공통 컬럼 세팅
		cmmtRole.setCreatedDt(now);
		cmmtRole.setCreatorId(worker.getMemberId());
		cmmtRole.setUpdatedDt(now);
		cmmtRole.setUpdaterId(worker.getMemberId());

		// 역할 정보 Insert
		roleDao.insert(cmmtRole);

		// 입력된 역할 정보 조회
		return roleDao.select(cmmtRole.getRoleId());
	}

	public CmmtRole select(String roleId)
	{
		SecurityUtils.checkWorkerIsInsider();

//		// 입력값 검증
//		if (string.isBlank(roleId)) {
//			throw new InvalidationException(String.format(validateMessageExt.입력없음, "역할ID"));  // %s를 입력하세요.
//		}

		// 역할정보 조회
		CmmtRole cmmtRole = roleDao.select(roleId);
		if (cmmtRole == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "역할 정보"));  // %s이(가) 없습니다.
		}

		return cmmtRole;
	}

	public CmmtRole modify(String roleId, String roleNm)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
//		if (string.isBlank(roleId)) {
//			throw new InvalidationException(String.format(validateMessageExt.입력없음, "역할ID"));  // %s를 입력하세요.
//		}
		if (string.isBlank(roleNm)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "역할명"));  // %s를 입력하세요.
		}

		// Whitespace 삭제
		roleId = string.removeWhitespace(roleId);
		roleNm = string.removeWhitespace(roleNm);

		// 대문자 변환
		roleId = string.upperCase(roleId);
		roleNm = string.upperCase(roleNm);

		// 역할 정보 유무 검증
		CmmtRole chkCmmtRole = roleDao.select(roleId);
		if (chkCmmtRole == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "역할 정보"));  // %s이(가) 없습니다.
		}

		// 역할명 중복 검증
		chkCmmtRole = null;
		chkCmmtRole = roleDao.selectByName(roleNm, roleId);
		if (chkCmmtRole != null) {
			throw new InvalidationException(String.format(validateMessageExt.중복오류, "역할명", roleNm));  // 사용 중인 %s 입니다.[%s]
		}

		// Role Entity 세팅
		CmmtRole cmmtRole = CmmtRole.builder().roleId(roleId).roleNm(roleNm).build();

		// 테이블 공통 컬럼 세팅
		cmmtRole.setUpdatedDt(now);
		cmmtRole.setUpdaterId(worker.getMemberId());

		// 역할 정보 Update
		roleDao.update(cmmtRole);

		// 변경된 역할 정보 조회
		return roleDao.select(cmmtRole.getRoleId());
	}

	public void delete(String roleId)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(roleId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "역할ID"));  // %s를 입력하세요.
		}

		// 역할 정보 유무 검증
		CmmtRole chkCmmtRole = roleDao.select(roleId);
		if (chkCmmtRole == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "역할 정보"));  // %s이(가) 없습니다.
		}

		// 역할-권한 매핑 검증
		List<CmmtAuthorRole> authorityRoleList = authorityDao.selectByRoleId(roleId);
		if (authorityRoleList.size() > 0) {
			throw new InvalidationException(String.format(validateMessageExt.삭제불가, "역할을 사용하는 권한 존재"));  // 데이터를 삭제할 수 없습니다.[사유:%s]
		}

		// 역할-프로그램 매핑 검증
		List<CmmtProgrmRole> programRoleList = programDao.selectByRoleId(roleId);
		if (programRoleList.size() > 0) {
			throw new InvalidationException(String.format(validateMessageExt.삭제불가, "역할에 매핑된 프로그램 존재"));  // 데이터를 삭제할 수 없습니다.[사유:%s]
		}

		// 시스템ID 공통코드 조회 후 메뉴-역할 매핑 삭제
		List<CmmtCode> codeList = codeDao.selectList("SYSTEM_ID");
		for (CmmtCode code : codeList) {
			// cmmt_menu_role 삭제
			cmmtMenuRoleDao.deleteByRoleId(code.getCode(), roleId);
		}

		// 역할 정보 Delete
		roleDao.delete(roleId);
	}
}
