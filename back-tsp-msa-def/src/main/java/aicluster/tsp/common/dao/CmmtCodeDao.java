package aicluster.tsp.common.dao;

import aicluster.tsp.api.common.param.CommonApplcntParam;
import aicluster.tsp.api.common.param.CommonAttachmentParam;
import aicluster.tsp.api.common.param.CommonEqpmnUseSttusParam;
import aicluster.tsp.api.common.param.CommonReturnMap;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.EqpmnCodeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmmtCodeDao {

	List<EqpmnCodeDto> selectCodeNameList(String codeGroup);

	List<List<Object>> selectWorkingRntfee(String eqpmnId, String startDt, String endDt);

//	String imageFilePath(String attachmentId);

	List<CommonReturnMap> selectEqpmnClNm();

	ApplcntDto selectApplcnt(String mberId);

	void insertApplcnt(CommonApplcntParam param);

	void updateApplcnt(CommonApplcntParam param);

	List<String> getYmd();

	List<CommonAttachmentParam> selectAttachmentGroup(String atchmnflGroupId);

	CommonAttachmentParam selectAttachment(String atchmnflId);
	Long selectAttachmentInfoGroupCount(String atchmnflGroupId);
	CommonAttachmentParam selectAttachmentInfoGroup(String atchmnflGroupId);

	void updateEqpmnUseSttus(CommonEqpmnUseSttusParam param);
}
