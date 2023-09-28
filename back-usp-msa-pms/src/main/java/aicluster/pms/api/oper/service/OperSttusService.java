package aicluster.pms.api.oper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.TsptEqpmnResultDao;
import aicluster.pms.common.dto.BsnsPortListDto;
import aicluster.pms.common.dto.EntrprsSttusListDto;
import aicluster.pms.common.dto.EqpmnResultDto;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;

@Service
public class OperSttusService {

	@Autowired
	private CmmtMemberDao cmmtMemberDao;
	@Autowired
	private TsptEqpmnResultDao tsptEqpmnResultDao;

	/**
	 * 기업현황 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	public List<EntrprsSttusListDto> getEntrprsSttusList(String bsnsYear) {
		SecurityUtils.checkWorkerIsInsider();
		if(CoreUtils.string.isEmpty(bsnsYear)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업연도"));
		}
		return cmmtMemberDao.selectEntrprsSttusList(bsnsYear);
	}


	/**
	 * 사업지원 현황 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	public List<BsnsPortListDto> getBsnsPortSttusList(String bsnsYear) {
		SecurityUtils.checkWorkerIsInsider();
		if(CoreUtils.string.isEmpty(bsnsYear)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업연도"));
		}
		return cmmtMemberDao.selectBsnsPortSttusList(bsnsYear);
	}


	/**
	 * 실증장비사용현황 목록 조회
	 * @param bgnde
	 * @param endde
	 * @param selectStdr
	 * @return
	 */
	public List<EqpmnResultDto> getEqpmnList(String bgnde, String endde, String  selectStdr) {
		SecurityUtils.checkWorkerIsInsider();

		if (!date.isValidDate(bgnde, "yyyyMMdd")) {
			throw new InvalidationException(String.format(Code.validateMessage.일자형식오류, "조회기간(시작일)"));
		}
		if (!date.isValidDate(endde, "yyyyMMdd")) {
			throw new InvalidationException(String.format(Code.validateMessage.일자형식오류, "조회기간(종료일)"));
		}
		if (date.compareDay(string.toDate(bgnde), string.toDate(endde)) > 0) {
			throw new InvalidationException("조회기간(시작일)은 조회기간(종료일) 보다 클수 없습니다");
		}
		if(CoreUtils.string.isBlank(selectStdr)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "조회기준"));
		}

		List<EqpmnResultDto> list = null;
		if ("DAY".equals(selectStdr)) {
			list = tsptEqpmnResultDao.selectStatsList(bgnde, endde);
		} else if ("MONTH".equals(selectStdr)) {
			bgnde = bgnde.substring(0, 6);
			endde = endde.substring(0, 6);
			list = tsptEqpmnResultDao.selectStatsYmList(bgnde, endde);
		}
		return list;
	}
}
