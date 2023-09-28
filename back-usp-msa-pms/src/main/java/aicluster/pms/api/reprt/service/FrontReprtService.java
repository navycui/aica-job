package aicluster.pms.api.reprt.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.reprt.dto.FrontReprtListParam;
import aicluster.pms.api.reprt.dto.ReprtMakeupDto;
import aicluster.pms.common.dao.UsptReprtDao;
import aicluster.pms.common.dto.FrontReprtDto;
import aicluster.pms.common.dto.FrontReprtListDto;
import aicluster.pms.common.entity.UsptReprt;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class FrontReprtService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private UsptReprtDao usptReprtDao;
	@Autowired
	private AttachmentService attachmentService;


	/**
	 * 보고서 제출 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<FrontReprtListDto> getList(FrontReprtListParam param, Long page){
		BnMember worker = SecurityUtils.checkLogin();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setPresentnStartDate(date.format(string.toDate(param.getPresentnStartDate()), "yyyyMMdd"));
		param.setPresentnEndDate(date.format(string.toDate(param.getPresentnEndDate()), "yyyyMMdd"));
		param.setMemberId(worker.getMemberId());
		long totalItems = usptReprtDao.selectFrontListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<FrontReprtListDto> list = usptReprtDao.selectFrontList(param);
		CorePagination<FrontReprtListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 보고서 보완요청 조회
	 * @param reprtId
	 * @return
	 */
	public ReprtMakeupDto getMakeup(String reprtId) {
		SecurityUtils.checkLogin();
		UsptReprt reprtInfo = usptReprtDao.select(reprtId);
		if(reprtInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		ReprtMakeupDto dto = new ReprtMakeupDto();
		dto.setMakeupReqDate(date.format(reprtInfo.getMakeupReqDe(), "yyyy-MM-dd HH:mm"));
		dto.setMakeupReqCn(reprtInfo.getMakeupReqCn());
		if(!CoreUtils.string.isBlank(reprtInfo.getMakeupAttachmentGroupId())) {
			dto.setAttachFileList(attachmentService.getFileInfos_group(reprtInfo.getMakeupAttachmentGroupId()));
		}
		return dto;
	}

	/**
	 * 보고서 보완요청 첨부파일 다운로드
	 * @param response
	 * @param reprtId
	 * @param attachmentId
	 */
	public void getMakeupAtchmnfl(HttpServletResponse response, String reprtId, String attachmentId) {
		SecurityUtils.checkLogin();
		UsptReprt reprtInfo = usptReprtDao.select(reprtId);
		if(reprtInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "첨부파일ID"));
		}

		List<CmmtAtchmnfl> fileList = attachmentService.getFileInfos_group(reprtInfo.getMakeupAttachmentGroupId());
		Optional<CmmtAtchmnfl> opt = fileList.stream().filter(x-> CoreUtils.string.equals(x.getAttachmentId(), attachmentId)).findFirst();
		if(opt.isPresent()) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		} else {
			throw new InvalidationException("요청한 정보의 첨부파일이 존재하지않습니다.");
		}
	}


	/**
	 * 보고서 상세조회
	 * @param reprtId
	 * @return
	 */
	public FrontReprtDto get(String reprtId) {
		SecurityUtils.checkLogin();
		FrontReprtDto dto = usptReprtDao.selectFront(reprtId);
		if(dto == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(!CoreUtils.string.isBlank(dto.getAttachmentGroupId())) {
			dto.setAttachFileList(attachmentService.getFileInfos_group(dto.getAttachmentGroupId()));
		}
		return dto;
	}

	/**
	 * 보고서 첨부파일 다운로드
	 * @param response
	 * @param reprtId
	 * @param attachmentId
	 */
	public void getAtchmnfl(HttpServletResponse response, String reprtId, String attachmentId) {
		SecurityUtils.checkLogin();
		UsptReprt reprtInfo = usptReprtDao.select(reprtId);
		if(reprtInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "첨부파일ID"));
		}

		List<CmmtAtchmnfl> fileList = attachmentService.getFileInfos_group(reprtInfo.getAttachmentGroupId());
		Optional<CmmtAtchmnfl> opt = fileList.stream().filter(x-> CoreUtils.string.equals(x.getAttachmentId(), attachmentId)).findFirst();
		if(opt.isPresent()) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		} else {
			throw new InvalidationException("요청한 정보의 첨부파일이 존재하지않습니다.");
		}
	}

	/**
	 * 보고서 제출
	 * @param reprtId
	 * @param reprtSumryCn
	 * @param fileList
	 */
	public void modify(String reprtId, String reprtSumryCn, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptReprt reprtInfo = usptReprtDao.select(reprtId);
		if(reprtInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}

		if(fileList != null && fileList.size() > 0) {
			String[] exts = {"DOCX", "PPTX", "XLSX", "PDF", "PNG"};
			int size = 100 * (1024 * 1024);
			for(MultipartFile fileInfo : fileList) {
				if(CoreUtils.string.isBlank(reprtInfo.getAttachmentGroupId())) {
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
					reprtInfo.setAttachmentGroupId(caInfo.getAttachmentGroupId());
				} else {
					attachmentService.uploadFile_toGroup(config.getDir().getUpload(), reprtInfo.getAttachmentGroupId(), fileInfo, exts, size);
				}
			}
		}

		Date now = new Date();
		reprtInfo.setReprtSumryCn(reprtSumryCn);
		reprtInfo.setReprtSttusCd(Code.reprtSttus.제출);
		reprtInfo.setUpdatedDt(now);
		reprtInfo.setUpdaterId(worker.getMemberId());
		reprtInfo.setPresentnDt(now);
		if(usptReprtDao.update(reprtInfo) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "보고서 제출 처리"));
		}
	}
}
