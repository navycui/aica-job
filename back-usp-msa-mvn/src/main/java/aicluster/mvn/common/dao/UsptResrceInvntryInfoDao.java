package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.resource.dto.AlrsrcStatusParam;
import aicluster.mvn.common.dto.AlrsrcFninfRsrcDto;
import aicluster.mvn.common.dto.AlrsrcFninfStatusDto;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;

@Repository
public interface UsptResrceInvntryInfoDao {

	void insertList(List<UsptResrceInvntryInfo> list);

	UsptResrceInvntryInfo select(String rsrcId);

	void updateList(List<UsptResrceInvntryInfo> list);

	void updateInvtQy(
			@Param("rsrcId") String rsrcId,
			@Param("invtQy") int invtQy,
			@Param("totalQy") int totalQy,
			@Param("updaterId") String updaterId
			);

	void delete(String rsrcId);

	List<UsptResrceInvntryInfo> selectList();

	List<AlrsrcFninfRsrcDto> selectRsrcCodeList(String rsrcGroupCd);

	List<AlrsrcFninfStatusDto> selectStatusList(
			@Param("param") AlrsrcStatusParam param
			);
}
