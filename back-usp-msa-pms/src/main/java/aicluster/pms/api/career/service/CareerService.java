package aicluster.pms.api.career.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.career.dto.CareerDto;
import aicluster.pms.common.dao.UsptAcdmcrDao;
import aicluster.pms.common.dao.UsptCrqfcDao;
import aicluster.pms.common.dao.UsptEtcCareerDao;
import aicluster.pms.common.dao.UsptJobCareerDao;
import aicluster.pms.common.dao.UsptMsvcDao;
import aicluster.pms.common.dao.UsptProgrmDao;
import aicluster.pms.common.dao.UsptWnpzDao;
import aicluster.pms.common.entity.UsptAcdmcr;
import aicluster.pms.common.entity.UsptCrqfc;
import aicluster.pms.common.entity.UsptEtcCareer;
import aicluster.pms.common.entity.UsptJobCareer;
import aicluster.pms.common.entity.UsptMsvc;
import aicluster.pms.common.entity.UsptProgrm;
import aicluster.pms.common.entity.UsptWnpz;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;

@Service
public class CareerService {

	@Autowired
	private UsptAcdmcrDao usptAcdmcrDao;
	@Autowired
	private UsptMsvcDao usptMsvcDao;
	@Autowired
	private UsptCrqfcDao usptCrqfcDao;
	@Autowired
	private UsptJobCareerDao usptJobCareerDao;
	@Autowired
	private UsptEtcCareerDao usptEtcCareerDao;
	@Autowired
	private UsptWnpzDao usptWnpzDao;
	@Autowired
	private UsptProgrmDao usptProgrmDao;

	/**
	 * 조회
	 * @return
	 */
	public CareerDto get() {
		BnMember worker = SecurityUtils.checkLogin();
		CareerDto dto = new CareerDto();
		dto.setAcdmcrList(usptAcdmcrDao.selectList(worker.getMemberId()));
		dto.setMsvcInfo(usptMsvcDao.select(worker.getMemberId()));
		dto.setCrqfcList(usptCrqfcDao.selectList(worker.getMemberId()));
		dto.setJobCareerList(usptJobCareerDao.selectList(worker.getMemberId()));
		dto.setEtcCareerList(usptEtcCareerDao.selectList(worker.getMemberId()));
		dto.setWnpzList(usptWnpzDao.selectList(worker.getMemberId()));
		dto.setProgrmList(usptProgrmDao.selectList(worker.getMemberId()));
		return dto;
	}


	/**
	 * 수정
	 * @param params
	 */
	public void modify(CareerDto params) {
		BnMember worker = SecurityUtils.checkLogin();
		Date now = new Date();

		/* 학력 */
		List<UsptAcdmcr> acdmcList = params.getAcdmcrList();
		if(acdmcList != null && acdmcList.size() != 0) {
			for(UsptAcdmcr regInfo : acdmcList) {
				regInfo.setMemberId(worker.getMemberId());
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setCreatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				regInfo.setUpdatedDt(now);

				Date bgndeDt = CoreUtils.string.toDate(regInfo.getBgnde());
				regInfo.setBgnde(date.format(bgndeDt, "yyyyMMdd"));
				Date enddeDt = CoreUtils.string.toDate(regInfo.getEndde());
				regInfo.setEndde(date.format(enddeDt, "yyyyMMdd"));

				if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
					regInfo.setAcdmcrId(CoreUtils.string.getNewId(Code.prefix.학력));
					usptAcdmcrDao.insert(regInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
					if(usptAcdmcrDao.update(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
					if(usptAcdmcrDao.delete(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "flag"));
				}
			}
		}

		/* 군복무 */
		UsptMsvc msvcInfo = params.getMsvcInfo();
		if(msvcInfo != null) {
			msvcInfo.setMemberId(worker.getMemberId());
			msvcInfo.setCreatorId(worker.getMemberId());
			msvcInfo.setCreatedDt(now);
			msvcInfo.setUpdaterId(worker.getMemberId());
			msvcInfo.setUpdatedDt(now);
			Date bgndeDt = CoreUtils.string.toDate(msvcInfo.getMsvcBgnde());
			msvcInfo.setMsvcBgnde(date.format(bgndeDt, "yyyyMMdd"));
			Date enddeDt = CoreUtils.string.toDate(msvcInfo.getMsvcEndde());
			msvcInfo.setMsvcEndde(date.format(enddeDt, "yyyyMMdd"));
			usptMsvcDao.save(msvcInfo);
		}

		/* 자격증 */
		List<UsptCrqfc> crqfcList = params.getCrqfcList();
		if(crqfcList != null && crqfcList.size() != 0) {
			for(UsptCrqfc regInfo : crqfcList) {
				regInfo.setMemberId(worker.getMemberId());
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setCreatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				Date acqdtDt = CoreUtils.string.toDate(regInfo.getAcqdt());
				regInfo.setAcqdt(date.format(acqdtDt, "yyyyMMdd"));

				if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
					regInfo.setCrqfcId(CoreUtils.string.getNewId(Code.prefix.자격증));
					usptCrqfcDao.insert(regInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
					if(usptCrqfcDao.update(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
					if(usptCrqfcDao.delete(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "flag"));
				}
			}
		}

		/* 직장경력 */
		List<UsptJobCareer> jobCareerList = params.getJobCareerList();
		if(jobCareerList != null && jobCareerList.size() != 0) {
			for(UsptJobCareer regInfo : jobCareerList) {
				regInfo.setMemberId(worker.getMemberId());
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setCreatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				regInfo.setUpdatedDt(now);

				Date bgndeDt = CoreUtils.string.toDate(regInfo.getBgnde());
				regInfo.setBgnde(date.format(bgndeDt, "yyyyMMdd"));
				Date enddeDt = CoreUtils.string.toDate(regInfo.getEndde());
				regInfo.setEndde(date.format(enddeDt, "yyyyMMdd"));

				if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
					regInfo.setJobCareerId(CoreUtils.string.getNewId(Code.prefix.직장경력));
					usptJobCareerDao.insert(regInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
					if(usptJobCareerDao.update(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
					if(usptJobCareerDao.delete(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "flag"));
				}
			}
		}

		/* 기타경력 */
		List<UsptEtcCareer> etcCareerList = params.getEtcCareerList();
		if(etcCareerList != null && etcCareerList.size() != 0) {
			for(UsptEtcCareer regInfo : etcCareerList) {
				regInfo.setMemberId(worker.getMemberId());
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setCreatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				regInfo.setUpdatedDt(now);

				Date bgndeDt = CoreUtils.string.toDate(regInfo.getBgnde());
				regInfo.setBgnde(date.format(bgndeDt, "yyyyMMdd"));
				Date enddeDt = CoreUtils.string.toDate(regInfo.getEndde());
				regInfo.setEndde(date.format(enddeDt, "yyyyMMdd"));

				if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
					regInfo.setEtcCareerId(CoreUtils.string.getNewId(Code.prefix.기타경력));
					usptEtcCareerDao.insert(regInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
					if(usptEtcCareerDao.update(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
					if(usptEtcCareerDao.delete(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "flag"));
				}
			}
		}

		/* 수상 */
		List<UsptWnpz> wnpzList = params.getWnpzList();
		if(wnpzList != null && wnpzList.size() != 0) {
			for(UsptWnpz regInfo : wnpzList) {
				regInfo.setMemberId(worker.getMemberId());
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setCreatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				Date acqdtDt = CoreUtils.string.toDate(regInfo.getAcqdt());
				regInfo.setAcqdt(date.format(acqdtDt, "yyyyMMdd"));

				if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
					regInfo.setWnpzId(CoreUtils.string.getNewId(Code.prefix.수상));
					usptWnpzDao.insert(regInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
					if(usptWnpzDao.update(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
					if(usptWnpzDao.delete(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "flag"));
				}
			}
		}

		/* 프로그램 */
		List<UsptProgrm> progrmList = params.getProgrmList();
		if(progrmList != null && progrmList.size() != 0) {
			for(UsptProgrm regInfo : progrmList) {
				regInfo.setMemberId(worker.getMemberId());
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setCreatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				regInfo.setUpdatedDt(now);

				if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
					regInfo.setProgrmId(CoreUtils.string.getNewId(Code.prefix.프로그램));
					usptProgrmDao.insert(regInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
					if(usptProgrmDao.update(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
					if(usptProgrmDao.delete(regInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "flag"));
				}
			}
		}
	}
}
