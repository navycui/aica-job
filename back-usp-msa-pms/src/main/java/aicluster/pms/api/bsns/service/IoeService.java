package aicluster.pms.api.bsns.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.UsptWctIoeDao;
import aicluster.pms.common.dao.UsptWctTaxitmDao;
import aicluster.pms.common.dto.WctIoeDto;
import aicluster.pms.common.entity.UsptWctIoe;
import aicluster.pms.common.entity.UsptWctTaxitm;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IoeService {

	@Autowired
	private UsptWctIoeDao usptWctIoeDao;
	@Autowired
	private UsptWctTaxitmDao usptWctTaxitmDao;

	/**
	 * 목록 조회
	 * @return
	 */
	public JsonList<WctIoeDto> getList(){
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptWctIoeDao.selectList());
	}

	/**
	 * 수정
	 * @param ioeList
	 */
	public void modify(List<WctIoeDto> ioeList){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		Date now = new Date();
		for(WctIoeDto info : ioeList) {
			UsptWctIoe regInfo = new UsptWctIoe();
			BeanUtils.copyProperties(info, regInfo);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())){
				regInfo.setWctIoeId(CoreUtils.string.getNewId(Code.prefix.비목));
				usptWctIoeDao.insert(regInfo);
			} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())){
				if(usptWctIoeDao.update(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())){
				int useCnt = usptWctIoeDao.checkIsUse(regInfo);
				if(useCnt != 0) {
					throw new InvalidationException("사용중인 비목은 삭제할 수 없습니다.[" + regInfo.getWctIoeNm() + "]");
				}
				try {
					usptWctTaxitmDao.deleteAll(regInfo.getWctIoeId());
				} catch (Exception e) {
					log.debug(e.getMessage());
					if(e.getCause() instanceof SQLException) {
						if(e.getMessage().contains("foreign key")) {
							throw new InvalidationException("사용중인 비목은 삭제할 수 없습니다.[" + regInfo.getWctIoeNm() + "]");
						}
					}
					throw e;
				}
				if(usptWctIoeDao.delete(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}

			if(!CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())){
				if(info.getTaxitmList() != null) {
					for(UsptWctTaxitm taxitmInfo : info.getTaxitmList()) {
						taxitmInfo.setCreatedDt(now);
						taxitmInfo.setCreatorId(worker.getMemberId());
						taxitmInfo.setUpdatedDt(now);
						taxitmInfo.setUpdaterId(worker.getMemberId());
						taxitmInfo.setWctIoeId(regInfo.getWctIoeId());
						if(CoreUtils.string.equals(Code.flag.등록, taxitmInfo.getFlag())){
							taxitmInfo.setWctTaxitmId(CoreUtils.string.getNewId(Code.prefix.세목));
							usptWctTaxitmDao.insert(taxitmInfo);
						} else if(CoreUtils.string.equals(Code.flag.수정, taxitmInfo.getFlag())){
							if(usptWctTaxitmDao.update(taxitmInfo) != 1)
								throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
						} else if(CoreUtils.string.equals(Code.flag.삭제, taxitmInfo.getFlag())){
							int useCnt = usptWctTaxitmDao.checkIsUse(taxitmInfo);
							if(useCnt != 0) {
								throw new InvalidationException("사용중인 세목은 삭제할 수 없습니다.[" + taxitmInfo.getWctTaxitmNm() + "]");
							}
							if(usptWctTaxitmDao.delete(taxitmInfo) != 1)
								throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
						} else {
							throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
						}
					}
				}
			}
		}
	}

}
