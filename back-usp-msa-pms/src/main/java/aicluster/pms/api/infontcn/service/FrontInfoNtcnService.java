package aicluster.pms.api.infontcn.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.UsptBsnsInfoNtcnDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dto.FrontBsnsPblancListDto;
import aicluster.pms.common.dto.FrontInfoNtcnEduListDto;
import aicluster.pms.common.dto.InfoNtcnDto;
import aicluster.pms.common.dto.MyBsnsNtcnListDto;
import aicluster.pms.common.entity.UsptBsnsInfoNtcn;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;

@Service
public class FrontInfoNtcnService {

	@Autowired
	private UsptBsnsInfoNtcnDao usptBsnsInfoNtcnDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;

	/**
	 * 목록 조회
	 * @return
	 */
	public JsonList<InfoNtcnDto> getList() {
		BnMember worker = SecurityUtils.checkLogin();
		return new JsonList<>(usptBsnsInfoNtcnDao.selectList(worker.getMemberId()));
	}

	/**
	 * 저장
	 * @param infoNtcnList
	 */
	public void add(List<UsptBsnsInfoNtcn> infoNtcnList) {
		if(infoNtcnList == null || infoNtcnList.size() == 0 ) {
			throw new InvalidationException("입력된 정보가 없습니다.");
		}

		Date now = new Date();
		BnMember worker = SecurityUtils.checkLogin();

		usptBsnsInfoNtcnDao.deleteAll(worker.getMemberId());
		for(UsptBsnsInfoNtcn regInfo : infoNtcnList) {
			regInfo.setMemberId(worker.getMemberId());
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			usptBsnsInfoNtcnDao.insert(regInfo);
		}
	}

	/**
	 * 삭제
	 */
	public void remove() {
		BnMember worker = SecurityUtils.checkLogin();
		usptBsnsInfoNtcnDao.deleteAll(worker.getMemberId());
	}

	/**
	 * 사업정보 알림 조회
	 * @param recomendClTypeCd
	 * @return
	 */
	public JsonList<FrontBsnsPblancListDto> getPblancList() {
		BnMember worker = SecurityUtils.checkLogin();
		return new JsonList<>(usptBsnsInfoNtcnDao.selectBsnsPblancList(worker.getMemberId()));
	}


	/**
	 * 교육정보 알림 조회
	 * @return
	 */
	public JsonList<FrontInfoNtcnEduListDto> getEduList() {
		BnMember worker = SecurityUtils.checkLogin();
		return new JsonList<>(usptBsnsInfoNtcnDao.selectEduList(worker.getMemberId()));
	}


	/**
	 * 나의 사업 알림 목록 조회
	 * @return
	 */
	public List<MyBsnsNtcnListDto> getMyBsnsNtcnList(){
		BnMember worker = SecurityUtils.checkLogin();
		return usptBsnsPblancApplcntDao.seletMyBsnsNtcnList(worker.getMemberId());
	}
}
