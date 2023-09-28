package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.dto.AlrsrcDstbStatusDto;
import aicluster.mvn.common.dto.AlrsrcDstbUserDto;
import aicluster.mvn.common.entity.UsptResrceAsgnDstb;

@Repository
public interface UsptResrceAsgnDstbDao {
	void insertList(List<UsptResrceAsgnDstb> list);
	void updateList(List<UsptResrceAsgnDstb> list);
	void delete(String alrsrcId);
	List<UsptResrceAsgnDstb> selectList(String alrsrcId);
	List<AlrsrcDstbStatusDto> selectStatusList(String alrsrcId);
	List<AlrsrcDstbUserDto> selectUserList(String alrsrcId);
}
