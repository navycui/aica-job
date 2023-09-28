package aicluster.mvn.api.resource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.resource.dto.AlrsrcStatusParam;
import aicluster.mvn.common.dao.UsptResrceAsgnEntrpsDao;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbDao;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoDao;
import aicluster.mvn.common.dto.AlrsrcDstbStatusDto;
import aicluster.mvn.common.dto.AlrsrcFninfStatusDto;
import aicluster.mvn.common.dto.AlrsrcStatusListItemDto;
import aicluster.mvn.common.dto.AlrsrcStatusSmmryDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class AlrsrcStatusService {

	@Autowired
	UsptResrceAsgnEntrpsDao cmpnyDao;

	@Autowired
	UsptResrceInvntryInfoDao fninfDao;

	@Autowired
	UsptResrceAsgnDstbDao dstbDao;

	public AlrsrcStatusSmmryDto getSummary(AlrsrcStatusParam param)
	{
		SecurityUtils.checkWorkerIsInsider();

		long useCmpnyCnt = cmpnyDao.selectStatusList_count(param);
		List<AlrsrcFninfStatusDto> fninfSttusList = fninfDao.selectStatusList(param);

		return new AlrsrcStatusSmmryDto(useCmpnyCnt, fninfSttusList);
	}

	public CorePagination<AlrsrcStatusListItemDto> getList(AlrsrcStatusParam param, CorePaginationParam pageParam)
	{
		SecurityUtils.checkWorkerIsInsider();

		long totalItems = cmpnyDao.selectStatusList_count( param );
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);

		List<AlrsrcStatusListItemDto> list = cmpnyDao.selectStatusList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );
		for (AlrsrcStatusListItemDto item : list) {
			List<AlrsrcDstbStatusDto> alrsrcDstbSttusList = dstbDao.selectStatusList( item.getAlrsrcId() );
			item.setAlrsrcDstbSttusList(alrsrcDstbSttusList);
		}

		return new CorePagination<>(info, list);
	}

}
