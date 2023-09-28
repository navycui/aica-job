package aicluster.pms.api.evl.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.evl.dto.FrontEvlPresnatnParam;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dao.UsptEvlTrgetPresentnHistDao;
import aicluster.pms.common.dto.FrontEvlPresnatnListDto;
import aicluster.pms.common.entity.UsptEvlTrget;
import aicluster.pms.common.entity.UsptEvlTrgetPresentnHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

//Slf4j
@Service
public class FrontEvlPresnatnService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;
	@Autowired
	private UsptEvlTrgetPresentnHistDao usptEvlTrgetPresentnHistDao;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/

	/**
	 * 발표자료 제출 대상 목록조회
	 *
	 */
	public CorePagination<FrontEvlPresnatnListDto> getPresnatnTargetList(FrontEvlPresnatnParam frontEvlTargetListParam, Long page) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		if(page == null) {
			page = 1L;
		}
		if(frontEvlTargetListParam.getItemsPerPage() == null) {
			frontEvlTargetListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		//로그인한 회원
		if(string.isBlank(frontEvlTargetListParam.getMemberId())) {
			frontEvlTargetListParam.setMemberId(worker.getMemberId());
		}
		long totalItems = usptEvlTrgetDao.selectFrontPresnatnTargetListCount(frontEvlTargetListParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, frontEvlTargetListParam.getItemsPerPage(), totalItems);
		frontEvlTargetListParam.setBeginRowNum(info.getBeginRowNum());
		List<FrontEvlPresnatnListDto> list = usptEvlTrgetDao.selectFrontPresnatnTargetList(frontEvlTargetListParam);

		//출력할 자료 생성 후 리턴
		CorePagination<FrontEvlPresnatnListDto> pagination = new CorePagination<>(info, list);

		return pagination;
	}

	/**
	 * 보완사유 조회 팝업
	 *
	 */
	public UsptEvlTrgetPresentnHist getSplemntResnDetail(FrontEvlPresnatnParam frontEvlTargetListParamt) {
		SecurityUtils.checkWorkerIsMember();

		if (string.isBlank(frontEvlTargetListParamt.getEvlTrgetId()) ) {
			throw new InvalidationException("잘못된 조회 조건 입니다.");
		}
		return usptEvlTrgetPresentnHistDao.selectReason(frontEvlTargetListParamt.getEvlTrgetId());
	}

	/**
	 * 발표자료 대상 상세조회
	 *
	 */
	public FrontEvlPresnatnListDto getPresnatnTargetDetailList(FrontEvlPresnatnParam frontEvlTargetListParamt) {
		SecurityUtils.checkWorkerIsMember();

		String evlTrgetId = frontEvlTargetListParamt.getEvlTrgetId();			/** 평가대상ID */

		FrontEvlPresnatnListDto  frontEvlPresnatnListDto = usptEvlTrgetDao.selectFrontPresnatnTargetDetailList(evlTrgetId);
		if(frontEvlPresnatnListDto == null) {
			throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "발표자료 제출 대상"));
		}
		if (string.isNotBlank(frontEvlPresnatnListDto.getAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(frontEvlPresnatnListDto.getAttachmentGroupId());
			frontEvlPresnatnListDto.setAttachFileList(list);
		}
		return frontEvlPresnatnListDto;
	}

	/**
	 * 발표자료 제출
	 *  evlTrgetId   , attachmentGroupId
	 */
	public void modifyPresnatn(FrontEvlPresnatnListDto frontEvlPresnatnListDto, List<MultipartFile> fileList) {
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();
		/** 평가대상ID */
		String evlTrgetId = frontEvlPresnatnListDto.getEvlTrgetId();
		/*첨부파일 그룹ID*/
		String attachmentGroupId =frontEvlPresnatnListDto.getAttachmentGroupId();
		/*발표자료처리구분코드 - PRPR03*/
		String presnatnProcessDivCd = Code.presnatnProcessDiv.제출;

		/** 첨부파일*/
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(attachmentGroupId)) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				attachmentGroupId = fileGroupInfo.getAttachmentGroupId();
			}
		}

		//제출
		UsptEvlTrget usptEvlTrget  = new UsptEvlTrget();
		usptEvlTrget.setEvlTrgetId(evlTrgetId);
		usptEvlTrget.setPresnatnProcessDivCd(presnatnProcessDivCd);
		usptEvlTrget.setPresentnDt(now);
		usptEvlTrget.setAttachmentGroupId(attachmentGroupId);
		usptEvlTrget.setUpdatedDt(now);
		usptEvlTrget.setUpdaterId(strMemberId);
		int updateCnt = usptEvlTrgetDao.updatePresnatn(usptEvlTrget);

		//평가대상제출이력
		if(updateCnt>0) {
			UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist = new UsptEvlTrgetPresentnHist();
			String  presentnHistIdNew = CoreUtils.string.getNewId(Code.prefix.평가대상제출이력);

			usptEvlTrgetPresentnHist.setPresentnHistId(presentnHistIdNew);					/** 제출이력ID */
			usptEvlTrgetPresentnHist.setEvlTrgetId(evlTrgetId);									/** 평가대상ID */
			usptEvlTrgetPresentnHist.setPresnatnProcessDivCd(presnatnProcessDivCd);		/** 발표자료처리구분코드(G:PRESNATN_PROCESS_DIV) */
			usptEvlTrgetPresentnHist.setResnCn("제출 처리 되었습니다.");						/** 사유내용 */
			usptEvlTrgetPresentnHist.setProcessDt(now);											/** 처리일시 */
			usptEvlTrgetPresentnHist.setCreatedDt(now);
			usptEvlTrgetPresentnHist.setCreatorId(strMemberId);
			usptEvlTrgetPresentnHistDao.insert(usptEvlTrgetPresentnHist);
		}

		/** 첨부파일 삭제 */
		List<CmmtAtchmnfl> attachFileDeleteList = frontEvlPresnatnListDto.getAttachFileDeleteList();
		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}
	}

	/**
	 * 발표자료 파일 단건 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	public void getOneFileDown(HttpServletResponse response, String attachmentId) {
		SecurityUtils.checkWorkerIsMember();
		if (string.isNotBlank(attachmentId)) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		}
	}
}
