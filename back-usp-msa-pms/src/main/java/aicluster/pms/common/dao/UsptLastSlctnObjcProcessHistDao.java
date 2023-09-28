package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.HistDto;
import aicluster.pms.common.dto.RejectDto;
import aicluster.pms.common.entity.UsptLastSlctnObjcProcessHist;

@Repository
public interface UsptLastSlctnObjcProcessHistDao {
	void insert(UsptLastSlctnObjcProcessHist info);
	List<HistDto> selectList(String lastSlctnObjcReqstId);
	RejectDto selectRejectReason(String lastSlctnObjcReqstId);
}
