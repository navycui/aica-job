package aicluster.tsp.api.sample.service;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.sample.dto.SampleParam;
import aicluster.tsp.common.sample.dao.SampleDao;
import aicluster.tsp.common.sample.entity.CmmtCode;
import aicluster.tsp.common.sample.entity.CmmtCodeThree;
import aicluster.tsp.common.sample.entity.CmmtCodeTwo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Slf4j
@Service
public class SampleService {

	@Autowired
	private SampleDao dao;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;

	public List<CmmtCode> getCodeList() {
		return dao.selectAll();
	}

	public void getParam(String id, Long page, Long itemsPerPage) { 
		log.debug("get 사용시 param 받는 테스트 ======================================");
		log.debug("\t- id				: ["+id+"]");
		log.debug("\t- page				: ["+page+"]");
		log.debug("\t- itemsPerPage		: ["+itemsPerPage+"]");

	}

	public void paramTest(SampleParam param) {
		log.debug("RequestBody 사용시 다른 엔티티를 가지고 있는 prarm 사용 테스트 ======================================");
		log.debug("\t- getCodeNm		: ["+param.getCmmtCodeTwo().getCodeNm()+"]");
		log.debug("\t- getRemark		: ["+param.getCmmtCodeTwo().getRemark()+"]");
		log.debug("\t- getCode			: ["+param.getCmmtCodeThree().getCode()+"]");
		log.debug("\t- getCodeGroup		: ["+param.getCmmtCodeThree().getCodeGroup()+"]");
		CmmtCodeTwo cmmtCodeTwo = new CmmtCodeTwo();
		cmmtCodeTwo.setCodeNm(param.getCmmtCodeTwo().getCodeNm());
		cmmtCodeTwo.setRemark(param.getCmmtCodeTwo().getRemark());
		// 예시 ) dao.insert(cmmtCodeTwo);

		CmmtCodeThree cmmtCodeThree = new CmmtCodeThree();
		cmmtCodeThree.setCode(param.getCmmtCodeThree().getCode());
		cmmtCodeThree.setCodeGroup(param.getCmmtCodeThree().getCodeGroup());
		// 예시 ) dao.insert(cmmtCodeTwo);

	}

	public List<CmmtCode> getListExcelDwld() {
		/*
		// 엑셀 유무를 아래와 같은 쿼리문으로 구분
		<if test="!isExcel">
				offset #{beginRowNum} -1
				limit #{itemsPerPage}
		</if>
		 */
		//stdrBsnsListParam.setIsExcel(true);
		return dao.selectAll();
	}
	/*
	 * =====================================================================================
	 * 파일 업로드
	 * =====================================================================================
	 * */
	public void uploadFile_noGroup(MultipartFile fileList) {
		// 파일 하나 업로드하는데 cmmt_attachment 테이블에만 데이터 넣어준다.
		if(fileList != null) {
			CmmtAtchmnfl fileGroupInfo = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), fileList, null, 0);

			String AttachmentId = fileGroupInfo.getAttachmentId();
		}
	}
	public void uploadFile_toGroup(MultipartFile fileList, String attachmentGroupId) {
		// 파일 하나 업로드하는데 attachmentGroupId 로 해서  cmmt_attachment 테이블에 데이터를 넣어준다.
		if(fileList != null) {
			CmmtAtchmnfl fileGroupInfo = attachmentService.uploadFile_toGroup(config.getDir().getUpload(), attachmentGroupId, fileList, null, 0);
			String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
		}
	}
	public void uploadFile_toNewGroup(MultipartFile fileList) {
		// 파일 하나 업로드하는데 cmmt_attachment_group 테이블과 cmmt_attachment 테이블에 데이터를 넣어준다.
		if(fileList != null ) {
			CmmtAtchmnfl fileGroupInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
			String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
		}
	}
	public void uploadFiles_toGroup(List<MultipartFile> fileList, String attachmentGroupId) {
		// 파일 여러개를 업로드하는데 attachmentGroupId 로 해서  cmmt_attachment 테이블에 데이터를 넣어준다.
		if(fileList != null && fileList.size() > 0) {
			List<CmmtAtchmnfl> fileGroupInfo = attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, fileList, null, 0);
		}
	}
	public void uploadFiles_toNewGroup(List<MultipartFile> fileList) {
		// 파일 여러개를 업로드하는데 cmmt_attachment_group 테이블과 cmmt_attachment 테이블에 데이터를 넣어준다.
		if(fileList != null && fileList.size() > 0) {
			CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
			String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
		}
	}

	/*
	 * =====================================================================================
	 * 파일 다운로드
	 * =====================================================================================
	 * */
	public void fileDownload(HttpServletResponse response, String attachmentId) {
		// 한개 파일 다운로드
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}
	public void fileDownloads(HttpServletResponse response, String[] attachmentIds) {
		// 여러파일 Zip으로 다운로드
		attachmentService.downloadFiles(response, config.getDir().getUpload(), attachmentIds, "filsZip");
	}
	public void downloadFile_contentType(HttpServletResponse response, String attachmentId) {
		// 한개 파일 다운로드
		attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), attachmentId);
	}
	public void fileDownload_groupfiles(HttpServletResponse response, String attachmentGroupId) {
		// 여러파일 Zip으로 다운로드
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), attachmentGroupId, "groupFilsZip");
	}

	/*
	 * =====================================================================================
	 * 파일 삭제
	 * =====================================================================================
	 * */
	public void removeFile(String attachmentId) {
		// 파일과 DB 둘다 삭제
		attachmentService.removeFile(config.getDir().getUpload(), attachmentId);
	}
	public void removeFiles_group(String attachmentGroupId) {
		// 파일과 DB 둘다 삭제
		attachmentService.removeFiles_group(config.getDir().getUpload(), attachmentGroupId);
	}
	public void removePhysicalFileOnly(String attachmentId) {
		// 파일만 삭제
		attachmentService.removePhysicalFileOnly(config.getDir().getUpload(), attachmentId);
	}
	public void removePhysicalFilesOnly_group(String attachmentGroupId) {
		// 파일만 삭제
		attachmentService.removePhysicalFilesOnly_group(config.getDir().getUpload(), attachmentGroupId);
	}

	/*
	 * =====================================================================================
	 * 실제 데이터 추가시 참조 샘플
	 * =====================================================================================
	 * */
	public void uploadFile_noGroup_sampleId(String sampleid, SampleParam param, MultipartFile _image1, MultipartFile _image2, List<MultipartFile> _image3) {
		CmmtAtchmnfl image1 = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), _image1, null, 0);
		CmmtAtchmnfl image2 = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), _image2, null, 0);
		CmmtAtchmnflGroup image3 = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), _image3, null, 0);

		log.debug("정보 추가 샘플 테스트 ======================================");
		log.debug("\t- sampleid			: ["+sampleid+"]");
		log.debug("\t- getCodeNm		: ["+param.getCmmtCodeTwo().getCodeNm()+"]");
		log.debug("\t- getRemark		: ["+param.getCmmtCodeTwo().getRemark()+"]");
		log.debug("\t- getCode			: ["+param.getCmmtCodeThree().getCode()+"]");
		log.debug("\t- getCodeGroup		: ["+param.getCmmtCodeThree().getCodeGroup()+"]");
		log.debug("\t- image1			: ["+image1.getAttachmentId()+"]");
		log.debug("\t- image2			: ["+image2.getAttachmentId()+"]");
		log.debug("\t- image3			: ["+image3.getAttachmentGroupId()+"]");
	}
}
