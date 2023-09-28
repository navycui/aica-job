package aicluster.member.api.insider.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.insider.dto.ChangePasswdParam;
import aicluster.member.common.dao.CmmtEmpInfoDao;
import aicluster.member.common.dao.CmmtPasswordHistDao;
import aicluster.member.common.entity.CmmtEmpInfo;
import aicluster.member.common.entity.CmmtPasswordHist;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;

@Service
public class InsiderSelfService {

	@Autowired
	private CmmtEmpInfoDao cmmtInsiderDao;

	@Autowired
	private CmmtPasswordHistDao cmmtPasswdHistDao;

	public void changePasswd(ChangePasswdParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		CmmtEmpInfo cmmtInsider = cmmtInsiderDao.select(worker.getMemberId());
		if (cmmtInsider == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "사용자 정보"));
		}
		/*
		 * 입력검사
		 */
		InvalidationsException ies = new InvalidationsException();

		// 현재 비밀번호 검사
		if (string.isBlank(param.getOldPasswd())) {
			ies.add("oldPasswd", String.format(validateMessageExt.입력없음, "현재 비밀번호"));
		}

		// 새 비밀번호 1
		if (string.isBlank(param.getNewPasswd1())) {
			ies.add("newPasswd1", String.format(validateMessageExt.입력없음, "새 비밀번호"));
		}

		// 새 비밀번호 2
		if (string.isBlank(param.getNewPasswd2())) {
			ies.add("newPasswd2", String.format(validateMessageExt.입력없음, "새 비밀번호 확인"));
		}

		if (ies.size() > 0) {
			throw ies;
		}

		/*
		 * 비밀번호 검사
		 */
		if (!CoreUtils.password.compare(param.getOldPasswd(), cmmtInsider.getEncPasswd())) {
			throw new InvalidationException("현재 비밀번호가 일치하지 않습니다.");
		}

		// 동일한가?
		if (!string.equals(param.getNewPasswd1(), param.getNewPasswd2())) {
			throw new InvalidationException("(새 비밀번호)와 (새 비밀번호 확인)이 다릅니다. 동일하게 입력하세요.");
		}

		// 새 비밀번호 적합성 검사
		List<CmmtPasswordHist> passwdHistList = cmmtPasswdHistDao.selectList_recent(cmmtInsider.getMemberId(), 3);
		CodeExt.passwd.checkValidation(param.getNewPasswd1(), null, passwdHistList);

		/*
		 * 수정
		 */
		Date now = new Date();
		String encPasswd = CoreUtils.password.encode(param.getNewPasswd1());
		cmmtInsider.setEncPasswd(encPasswd);
		cmmtInsider.setPasswdDt(now);
		cmmtInsider.setPasswdInit(false);
		cmmtInsider.setUpdatedDt(now);
		cmmtInsider.setUpdaterId(worker.getMemberId());

		cmmtInsiderDao.update(cmmtInsider);

		/*
		 * 비밀번호 이력 추가
		 */
		CmmtPasswordHist pwdHist = CmmtPasswordHist.builder()
				.memberId(cmmtInsider.getMemberId())
				.histDt(now)
				.encPasswd(encPasswd)
				.build();

		cmmtPasswdHistDao.insert(pwdHist);
	}

}
