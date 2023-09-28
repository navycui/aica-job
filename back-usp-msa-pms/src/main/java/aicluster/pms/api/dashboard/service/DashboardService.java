package aicluster.pms.api.dashboard.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.DashboardDao;
import aicluster.pms.common.dto.DashboardDto;

@Service
public class DashboardService {

	@Autowired
	private DashboardDao dashboardDao;

	/**
	 * 대시보드 조회
	 * @return
	 */
	public DashboardDto get(){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		DashboardDto resultDto = new DashboardDto();
		DashboardDto tempDto = dashboardDao.selectBsnsApply(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectSlctnWait(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectObjcReqst(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectReprt(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectPfmc(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectEduWait(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectEvl(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectBsnsWtpln(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectBsnsCnvnSign(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectBsnsCnvnChg(worker.getMemberId());
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		tempDto = dashboardDao.selectExpert();
		if(tempDto != null)
			BeanUtils.copyProperties(tempDto, resultDto, getNullPropertyNames(tempDto));

		return resultDto;
	}


	protected String[] getNullPropertyNames (Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}

		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

}
