package aicluster.tsp.api.admin.eqpmn.mngm.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.admin.eqpmn.dscnt.param.DscntApplyParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmDetailParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.TsptEqpmnDscntDao;
import aicluster.tsp.common.dao.TsptEqpmnExtndDao;
import aicluster.tsp.common.dao.TsptEqpmnMngmHistDao;
import aicluster.tsp.common.dao.TsptEqpmnMngmMgtDao;
import aicluster.tsp.common.entity.TsptEqpmn;
import aicluster.tsp.common.entity.TsptEqpmnProcessHist;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Slf4j
@Service
public class MngmDetailService {

	@Autowired
	private TsptEqpmnMngmMgtDao tsptEqpmnMngmMgtDao;
	@Autowired
	private TsptEqpmnMngmHistDao tsptEqpmnMngmHistDao;
	@Autowired
	private TsptEqpmnExtndDao tsptEqpmnExtndDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private TsptEqpmnDscntDao tsptEqpmnDscntDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EnvConfig config;

	Date now = new Date();

	// 장비 상세 정보 조회
	public MngmDetailParam getDetail(String eqpmnId) {
		MngmDetailParam mngmDetailParam = tsptEqpmnMngmMgtDao.selectEqpmnDetail(eqpmnId);
		mngmDetailParam.setEqpmnClParam(tsptEqpmnMngmMgtDao.selectEqpmnCl());
		mngmDetailParam.setDetailDscntParam(tsptEqpmnExtndDao.selectEqpmnExtndDetailDscnt(eqpmnId));
//		mngmDetailParam.setSavedFilePath(commonService.imageFilePath(mngmDetailParam.getImageId()));
		return mngmDetailParam;
	}

	// 장비 상세 정보 수정
	public MngmDetailParam modifyDetail(MngmDetailParam param){
		// 로그인 사용자 정보 추출
		BnMember worker = TspUtils.getMember();

		if( CoreUtils.string.isBlank(param.getAssetsNo()) ) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "자산번호"));
		}
		if( CoreUtils.string.isBlank(param.getEqpmnNmKorean()) ) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "장비명(국문)"));
		}
		if( CoreUtils.string.isBlank(param.getEqpmnClId()) ) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "장비분류ID"));
		}
		if( CoreUtils.string.isBlank(param.getImageId()) ) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "이미지파일ID"));
		}
		// 입력 값 셋팅
		TsptEqpmn tsptEqpmn = new TsptEqpmn();
		// param 값 -> tsptEqpmn  복사
		CoreUtils.property.copyProperties(tsptEqpmn, param);
		tsptEqpmn.setUpdtDt(now);
		if (param.getDetailDscntParam() != null) {
			tsptEqpmnMngmMgtDao.deleteEqpmnDetailDscnt(param.getEqpmnId());
			for (int i = 0; i < param.getDetailDscntParam().size(); i++) {
				DscntApplyParam dscntApplyParam = new DscntApplyParam();
				dscntApplyParam.setDscntId(param.getDetailDscntParam().get(i).getDscntId());
				dscntApplyParam.setEqpmnId(param.getEqpmnId());
				dscntApplyParam.setCreatrId(worker.getMemberId());
				dscntApplyParam.setUpdusrId(worker.getMemberId());
				tsptEqpmnDscntDao.postDscntApplyList(dscntApplyParam);
			}
		}
		tsptEqpmnMngmMgtDao.updateEqpmnDetail(tsptEqpmn);

		// 처리이력 히스토리 기록
		TsptEqpmnProcessHist tsptEqpmnProcessHist = TsptEqpmnProcessHist.builder()
				.histId(CoreUtils.string.getNewId("eqpmnHist-"))
				.eqpmnId(param.getEqpmnId())
				.mberId(worker.getMemberId())
				.opetrId(worker.getLoginId())
				.processKnd("SAVE")
				.processResn(String.format(TspCode.histMessage.처리이력, "저장"))
				.build();
		tsptEqpmnMngmHistDao.insertEqpmnHist(tsptEqpmnProcessHist);

		MngmDetailParam mngmDetailParam = getDetail(param.getEqpmnId());
		return mngmDetailParam;
	}

	// 이미지 업로드
	public String imageUpload(String eqpmnId, MultipartFile image) {
		if( image != null) {
			String attachmentId = tsptEqpmnMngmMgtDao.selectEqpmnImageId(eqpmnId);
			if (CoreUtils.string.isBlank(attachmentId)) {
				CmmtAtchmnfl attachment = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), image, null, 0);
				return updateImage(eqpmnId, attachment.getAttachmentId());
			} else {
				CmmtAtchmnfl imageInfo = attachmentService.getFileInfo(attachmentId);
				attachmentService.removeFiles_group(config.getDir().getUpload(), imageInfo.getAttachmentGroupId());

				CmmtAtchmnfl attachment = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), image, null, 0);
				return updateImage(eqpmnId, attachment.getAttachmentId());
			}
		} else {
			throw new InvalidationException("이미지 업로드 실패 ( Image is NULL )");
		}
	}

	// 이미지 업로드
	private String updateImage(String eqpmnId, String attachmentId) {
		// 로그인 사용자 정보 추출
		BnMember worker = TspUtils.getMember();

		// 기존 데이터 조회
		MngmDetailParam param = getDetail(eqpmnId);

		TsptEqpmn tsptEqpmn = new TsptEqpmn();
		// param 값 -> tsptEqpmn  복사
		CoreUtils.property.copyProperties(tsptEqpmn, param);
		// 셋팅
		tsptEqpmn.setUpdtDt(now);
		tsptEqpmn.setEqpmnId(eqpmnId);
		tsptEqpmn.setImageId(attachmentId);
		tsptEqpmn.setUpdusrId(worker.getMemberId());

		tsptEqpmnMngmMgtDao.updateEqpmnDetail(tsptEqpmn);
		return attachmentId;
	}

}

