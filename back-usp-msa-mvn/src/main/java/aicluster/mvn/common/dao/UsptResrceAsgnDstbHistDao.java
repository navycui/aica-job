package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.dto.AlrsrcDstbHistDto;
import aicluster.mvn.common.dto.AlrsrcFirstDstbDto;
import aicluster.mvn.common.entity.UsptResrceAsgnDstbHist;

@Repository
public interface UsptResrceAsgnDstbHistDao {
	void insertList(List<UsptResrceAsgnDstbHist> list);
	List<AlrsrcDstbHistDto> selectList(String alrsrcId);
	List<AlrsrcFirstDstbDto> selectList_firstDstb(String alrsrcId);
}
