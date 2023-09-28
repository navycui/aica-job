package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.FrontBsnsPblancListDto;
import aicluster.pms.common.dto.FrontInfoNtcnEduListDto;
import aicluster.pms.common.dto.InfoNtcnDto;
import aicluster.pms.common.entity.UsptBsnsInfoNtcn;

@Repository
public interface UsptBsnsInfoNtcnDao {
	void insert(UsptBsnsInfoNtcn info);
	int deleteAll(String memberId);
	List<InfoNtcnDto> selectList(String memberId);
	List<FrontBsnsPblancListDto> selectBsnsPblancList(String memberId);
	List<FrontInfoNtcnEduListDto> selectEduList(String memberId);
}
