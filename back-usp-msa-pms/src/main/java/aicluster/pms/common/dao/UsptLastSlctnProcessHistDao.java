package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.LastSlctnProcessHistDto;
import aicluster.pms.common.entity.UsptLastSlctnProcessHist;

@Repository
public interface UsptLastSlctnProcessHistDao {
	void insert(UsptLastSlctnProcessHist info);
	List<LastSlctnProcessHistDto> selectList(String evlLastSlctnId);
}
