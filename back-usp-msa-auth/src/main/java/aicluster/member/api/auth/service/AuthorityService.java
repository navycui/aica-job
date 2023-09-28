package aicluster.member.api.auth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.auth.dto.AuthorityRequestDto;
import aicluster.member.common.dao.CmmtAuthorDao;
import aicluster.member.common.dao.CmmtAuthorRoleDao;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.common.entity.CmmtAuthorRole;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class AuthorityService {

	@Autowired
	private CmmtAuthorDao cmmtAuthorityDao;
	@Autowired
	private CmmtAuthorRoleDao cmmtAuthorityRoleDao;
	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;

	/**
	 * 사용자 로그인 여부 검증
	 *
	 * @param worker	사용자정보
	 */



	public JsonList<CmmtAuthor> getList() {
		SecurityUtils.checkWorkerIsInsider();
		List<CmmtAuthor> cmmtAuthorityList = cmmtAuthorityDao.selectAll();
//		// 조회된 cmmtAuthorityList의 role_id를 ','로 분리하여 string array로 만든다.
//		for (int i = 0; i < cmmtAuthorityList.size(); i++) {
//			cmmtAuthorityList.get(i).getRoles();
//		}
		return new JsonList<>(cmmtAuthorityList);
	}

	public CmmtAuthor add(AuthorityRequestDto param) {

		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();
		
		if (param == null) {
			throw new InvalidationException("입력할 정보가 없습니다.");
		}

		if(string.isBlank(param.getAuthorityId())) {
			inputValidateErrs.add("authorityId",String.format(validateMessageExt.입력없음, "권한 ID"));
		}
		if(string.isBlank(param.getAuthorityNm())) {
			inputValidateErrs.add("authorityNm",String.format(validateMessageExt.입력없음, "권한이름"));
		}

		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}
		String authorityId = param.getAuthorityId();
		String authorityNm = param.getAuthorityNm();
		String[] roleId = param.getRoleId();

		//대문자 변환
		authorityId = string.upperCase(authorityId);
		authorityNm = string.upperCase(authorityNm);
		//whitespace 삭제
		authorityId = string.removeWhitespace(authorityId);
		authorityNm = string.removeWhitespace(authorityNm);

		//CMMT_AUTHORITY 테이블에서 authorityId로 조회
		CmmtAuthor cmmtAuthority = cmmtAuthorityDao.select(authorityId);
		if(cmmtAuthority != null) {
			throw new InvalidationException("권한 ID가 이미 사용중입니다.");
		}
		//CMMT_AUTHORITY 테이블에서 authorityNm으로 조회
		cmmtAuthority = cmmtAuthorityDao.selectByName(authorityNm);
		if(cmmtAuthority != null) {
			throw new InvalidationException("권한이름이 이미 사용중입니다.");
		}

		//authorityId와 authorityNm으로 CMMT_AUTHORITY 테이블에 insert
		cmmtAuthority = CmmtAuthor.builder()
					.authorityId(authorityId)
					.authorityNm(authorityNm)
					.createdDt(now)
					.creatorId(worker.getMemberId())
					.updatedDt(now)
					.updaterId(worker.getMemberId())
					.build();
		cmmtAuthorityDao.insert(cmmtAuthority);

		//authorityId와 roles를 이용하여, List<CmmtAuthorityRole>을 생성하여
		//CMMT_AUTHORITY_ROLE 테이블에 insert한다.
		if(roleId != null) {
			List<CmmtAuthorRole> list = new ArrayList<>();
			for (int i = 0; i < roleId.length; i++) {
				CmmtAuthorRole cmmtAuthorityRole;
				cmmtAuthorityRole =CmmtAuthorRole.builder()
						.authorityId(authorityId)
						.roleId(roleId[i])
						.build();
				list.add(cmmtAuthorityRole);

			}
			cmmtAuthorityRoleDao.insertList(list);
		}

		CmmtAuthor authority = cmmtAuthorityDao.select(authorityId);
		return authority;
	}

	public CmmtAuthor get(String authorityId) {
		SecurityUtils.checkWorkerIsInsider();
		if(string.isBlank(authorityId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "권한 ID"));
		}

		//대문자 변환
		authorityId = string.upperCase(authorityId);
		//whitespace 삭제
		authorityId = string.removeWhitespace(authorityId);

		CmmtAuthor cmmtAuthority = cmmtAuthorityDao.select(authorityId);
		if(cmmtAuthority == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}
		return cmmtAuthority;
	}

	public CmmtAuthor modify(AuthorityRequestDto param) {
		String authorityId = param.getAuthorityId();
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		if(string.isBlank(authorityId)) {
			inputValidateErrs.add("authorityId",String.format(validateMessageExt.입력없음, "권한 ID"));
		}
		if(string.isBlank(param.getAuthorityNm())) {
			inputValidateErrs.add("authorityNm",String.format(validateMessageExt.입력없음, "권한이름"));
		}
		String authorityNm = param.getAuthorityNm();
		String[] roleId = param.getRoleId();
		//대문자 변환
		authorityId = string.upperCase(authorityId);
		authorityNm = string.upperCase(authorityNm);
		//whitespace 삭제
		authorityId = string.removeWhitespace(authorityId);
		authorityNm = string.removeWhitespace(authorityNm);

		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		CmmtAuthor cmmtAuthority = cmmtAuthorityDao.select(authorityId);
		if(cmmtAuthority == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}

		cmmtAuthority.setUpdatedDt(now);
		cmmtAuthority.setUpdaterId(worker.getMemberId());
		cmmtAuthority.setAuthorityNm(authorityNm);
		cmmtAuthorityDao.update(cmmtAuthority);

		cmmtAuthorityRoleDao.deleteAuthorityId(authorityId);

		if(roleId != null) {
			List<CmmtAuthorRole> list = new ArrayList<>();
			for (int i = 0; i < roleId.length; i++) {
				CmmtAuthorRole cmmtAuthorityRole;
				cmmtAuthorityRole =CmmtAuthorRole.builder()
						.authorityId(authorityId)
						.roleId(roleId[i])
						.creatorId(cmmtAuthority.getCreatorId())
						.createdDt(cmmtAuthority.getCreatedDt())
						.build();
				list.add(cmmtAuthorityRole);

			}
			cmmtAuthorityRoleDao.insertList(list);
		}

		CmmtAuthor authority = cmmtAuthorityDao.select(authorityId);

		return authority;
	}

	public void remove(String authorityId) {
		SecurityUtils.checkWorkerIsInsider();

		//대문자 변환
		authorityId = string.upperCase(authorityId);
		//whitespace 삭제
		authorityId = string.removeWhitespace(authorityId);

		CmmtAuthor cmmtAuthority = cmmtAuthorityDao.select(authorityId);
		if(cmmtAuthority == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}

		long cnt = cmmtMemberDao.selectCountByAuthorityId(authorityId);
		if(cnt > 0) {
			throw new InvalidationException("권한을 사용 중인 회원이 존재하여 삭제할 수 없습니다.");
		}

		cmmtAuthorityRoleDao.deleteAuthorityId(authorityId);

		cmmtAuthorityDao.delete(authorityId);
	}

}
