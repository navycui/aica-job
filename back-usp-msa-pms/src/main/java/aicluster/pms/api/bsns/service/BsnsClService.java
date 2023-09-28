package aicluster.pms.api.bsns.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.UsptBsnsClDao;
import aicluster.pms.common.entity.UsptBsnsCl;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;

@Service
public class BsnsClService {

	@Autowired
	private UsptBsnsClDao bsnsClDao;

	/**
	 * 산업분류 목록 조회
	 * @param parentBsnsClId
	 * @param enabled
	 * @return
	 */
	public JsonList<UsptBsnsCl> getList(String parentBsnsClId, String enabled) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(bsnsClDao.selectList(parentBsnsClId, enabled));
	}

	/**
	 * 산업분류 저장(추가/수정)
	 * @param bcList
	 */
	public void add(List<UsptBsnsCl> bcList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(bcList == null || bcList.size() == 0) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업분류 정보"));
		}

		Date now = new Date();
		for(UsptBsnsCl bcInfo : bcList) {
			bcInfo.setCreatedDt(now);
			bcInfo.setCreatorId(worker.getMemberId());
			bcInfo.setUpdatedDt(now);
			bcInfo.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.isEmpty(bcInfo.getBsnsClNm())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업분류명"));
			}

			if(CoreUtils.string.equals(Code.flag.등록, bcInfo.getFlag())) {
				bcInfo.setBsnsClId(CoreUtils.string.getNewId("bsnscl-"));
				if(CoreUtils.string.isEmpty(bcInfo.getParentBsnsClId())) {
					bcInfo.setParentBsnsClId(null);
				}
				bsnsClDao.insert(bcInfo);
			} else if(CoreUtils.string.equals(Code.flag.수정, bcInfo.getFlag())) {
				if(CoreUtils.string.isEmpty(bcInfo.getBsnsClId())) {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업분류 ID"));
				}
				if(bcInfo.getEnabled() == false) {
					if(bsnsClDao.selectUseCnt(bcInfo.getBsnsClId()) > 0) {
						throw new InvalidationException("사업에서 사용중인 사업분류["+ bcInfo.getBsnsClNm() +"]는 사용안함으로 저장할 수 없습니다.");
					}
				}
				if(bsnsClDao.update(bcInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}


	/**
	 * 산업분류 삭제
	 * @param bcList
	 */
	public void remove(List<UsptBsnsCl> infoList) {
		SecurityUtils.checkWorkerIsInsider();
		if(infoList == null || infoList.size() == 0) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업분류 정보"));
		}

		for(UsptBsnsCl bsnsCl : infoList) {
			if(CoreUtils.string.isEmpty(bsnsCl.getBsnsClId())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업분류 ID"));
			}

			if(bsnsClDao.selectUseCnt(bsnsCl.getBsnsClId()) != 0) {
				throw new InvalidationException("사용중인 산업분류 정보입니다.\n확인 후 삭제해주세요!");
			}

			bsnsClDao.deleteAllForChild(bsnsCl.getBsnsClId());
			if(bsnsClDao.delete(bsnsCl.getBsnsClId()) != 1) {
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			}
		}
	}
}
