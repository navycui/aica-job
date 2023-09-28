package aicluster.member.api.insider.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.insider.dto.NotificationRecptnParam;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtSysChargerDao;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtSysCharger;
import aicluster.member.common.util.CodeExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.validation;
import bnet.library.util.dto.JsonList;

@Service
public class NotificationRecptnService {

	@Autowired
	private CmmtSysChargerDao dao;

	@Autowired
	private CmmtCodeDao codeDao;

	/**
	 * 오류알림 수신담당자 목록 조회
	 *
	 * @return 오류알림 수신담당자 목록
	 */
	public JsonList<CmmtSysCharger> getList()
	{
		SecurityUtils.checkWorkerIsInsider();

		return new JsonList<>(dao.selectList());
	}

	/**
	 *	오류알림 수신담당자 목록 입력
	 *
	 * @param param 오류알림 수신담당자 입력정보 목록
	 * @return 오류알림 수신담당자 목록
	 */
	public JsonList<CmmtSysCharger> insertList(NotificationRecptnParam param)
	{
		SecurityUtils.checkWorkerIsInsider();

		if (param == null) {
			throw new InvalidationException("입력 값이 없습니다.");
		}

		if (param.getList().isEmpty()) {
			throw new InvalidationException(String.format(CodeExt.validateMessageExt.입력없음, "오류알림 수신담당자 목록"));
		}

		List<CmmtSysCharger> insertList = new ArrayList<>();
		InvalidationsException errs = new InvalidationsException();

		// API시스템ID 공통코드 목록 조회
		List<CmmtCode> codeList = codeDao.selectList("API_SYSTEM_ID");

		int i = 0;
		for (CmmtSysCharger insertItem : param.getList()) {
			// API시스템ID 입력 검증
			if (string.isBlank(insertItem.getApiSysId())) {
				errs.add("apiSysId", String.format(CodeExt.validateMessageExt.선택없음, (i+1) + "행의 API 시스템"));
			}
			else {
				// API시스템ID 공통코드 검증
				boolean isCode = codeList.stream().filter(obj->obj.getCode().equals(insertItem.getApiSysId())).findFirst().isPresent();
				if (BooleanUtils.isNotTrue(isCode)) {
					errs.add("apiSysId", String.format(CodeExt.validateMessageExt.유효오류, (i+1) + "행의 API 시스템"));
				}
			}

			// 담당자 이메일 검증
			if (string.isBlank(insertItem.getChargerEmail())) {
				errs.add("chargerEmail", String.format(CodeExt.validateMessageExt.입력없음, (i+1) + "행의 담당자 이메일"));
			}
			else {
				// 이메일 형식 검증
				if (!validation.isEmail(insertItem.getChargerEmail())) {
					errs.add("chargerEmail", (i+1) + "행의 담당자 이메일이 이메일 형식과 맞지 않습니다. 다시 확인하세요.");
				}
			}

			// 담당자명 검증
			if (string.isBlank(insertItem.getChargerNm())) {
				errs.add("chargerNm", String.format(CodeExt.validateMessageExt.입력없음, (i+1) + "행의 담당자명"));
			}

			if (errs.size() == 0) {
				insertList.add(insertItem);
			}
		}

		// 목록 입력값 오류 처리
		if (errs.size() > 0) {
			throw errs;
		}

		// 기존 데이터 모두 삭제
		dao.deleteList();

		// 목록 데이터 입력
		dao.insertList(insertList);

		// 전체 목록 조회
		return new JsonList<>(dao.selectList());
	}

}
