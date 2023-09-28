package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.HistDto;
import aicluster.pms.common.dto.RejectDto;
import aicluster.pms.common.entity.UsptEvlObjcProcessHist;

@Repository
public interface UsptEvlObjcProcessHistDao {
	void insert(UsptEvlObjcProcessHist info);
	List<HistDto> selectList(String evlSlctnObjcReqstId);
	RejectDto selectRejectReason(String evlSlctnObjcReqstId);
}
