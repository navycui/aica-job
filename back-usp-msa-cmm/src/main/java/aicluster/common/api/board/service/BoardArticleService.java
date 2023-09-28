package aicluster.common.api.board.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.board.dto.AddArticleParam;
import aicluster.common.api.board.dto.BoardArticleCnParam;
import aicluster.common.api.board.dto.BoardArticleUrlParam;
import aicluster.common.api.board.dto.GetArticlesParam;
import aicluster.common.api.board.dto.ModifyArticleParam;
import aicluster.common.common.dao.CmmtBbsAnswerDao;
import aicluster.common.common.dao.CmmtBbsDao;
import aicluster.common.common.dao.CmmtBbscttCnDao;
import aicluster.common.common.dao.CmmtBbscttDao;
import aicluster.common.common.dao.CmmtBbscttLinkDao;
import aicluster.common.common.dao.CmmtMberInfoDao;
import aicluster.common.common.dto.BoardArticleListItem;
import aicluster.common.common.dto.BoardArticlePrevNextItem;
import aicluster.common.common.dto.CreatrInfDto;
import aicluster.common.common.entity.CmmtBbs;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.common.common.entity.CmmtBbscttCn;
import aicluster.common.common.entity.CmmtBbscttLink;
import aicluster.common.common.entity.CmmtMberInfo;
import aicluster.common.common.util.BoardUtils;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.common.dao.FwCodeDao;
import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.entity.CodeDto;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class BoardArticleService {

	@Autowired
	private EnvConfig config;

	@Autowired
	private CmmtBbsDao cmmtBoardDao;

	@Autowired
	private CmmtBbscttCnDao cmmtBoardArticleCnDao;

	@Autowired
	private CmmtBbscttLinkDao cmmtBoardArticleUrlDao;

	@Autowired
	private FwCodeDao codeDao;

	@Autowired
	private CmmtBbscttDao cmmtBoardArticleDao;

	@Autowired
	private CmmtBbsAnswerDao cmmtBoardCmmntDao;

	@Autowired
	private FwUserDao userDao;

	@Autowired
	private CmmtMberInfoDao memberDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private BoardUtils boardUtils;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	/**
	 * 게시글 목록조회
	 * @param param
	 * @param pageParam
	 * @return
	 */
	public CorePagination<BoardArticleListItem> getList(GetArticlesParam param, CorePaginationParam pageParam) {
		boolean isAddNoti = true;
		Boolean isNotiPosting = null;

		CmmtBbs cmmtBoard = cmmtBoardDao.select(param.getBoardId());
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시판 권한검사
		BnMember worker = SecurityUtils.getCurrentMember();
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		// 내부사용자가 아닌 경우에는 게시된 게시글(posting = true)만 조회해야 한다.
		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			param.setPosting(true);
			isNotiPosting = true;

			// 사용자인 경우(사용자페이지 호출인 경우) 페이지 번호(page)가 첫 페이지(1)인 경우에만 출력한다.
			if (pageParam.getPage() > 1) {
				isAddNoti = false;
			}
		}

		// 카테고리를 사용할 수 없는 게시판인가?
		if (!cmmtBoard.getCategory()) {
			param.setCategoryCd(null);
		}

		// 사용자 게시물검색코드 대문자 변경
		if (string.isNotBlank(param.getArticleSrchCd())) {
			param.setArticleSrchCd(string.upperCase(param.getArticleSrchCd()));
		}

		// 총 건수
		long totalCnt = cmmtBoardArticleDao.selectList_count(param);

		// 조회 범위
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalCnt);

		// 목록 조회
		List<BoardArticleListItem> list = cmmtBoardArticleDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());

		// 고정고지 추가
		if (cmmtBoard.getNoticeAvailable() == true && isAddNoti == true) {
			List<BoardArticleListItem> noticeList = cmmtBoardArticleDao.selectList_notice(param.getBoardId(), param.getCategoryCd(), isNotiPosting);
			if (noticeList.size() > 0) {
				list.addAll(0, noticeList);
			}
		}
		CorePagination<BoardArticleListItem> cp = new CorePagination<>(info, list);
		return cp;
	}

	/**
	 * 게시글 추가
	 * @param param					게시글 등록 정보
	 * @param pcThumbnailFile		PC용 썸네일 이미지
	 * @param mobileThumbnailFile	모바일용 썸네일 이미지
	 * @param images				이미지 파일 목록
	 * @param attachments			첨부파일 목록
	 * @return
	 */
	public CmmtBbsctt add(
			AddArticleParam param,
			MultipartFile pcThumbnailFile,
			MultipartFile mobileThumbnailFile,
			List<MultipartFile> images,
			List<MultipartFile> attachments) {
		BnMember worker = SecurityUtils.getCurrentMember();

		if (worker == null) {
			throw new InvalidationException("로그인한 후 사용하세요.");
		}

		// 게시판 존재 확인
		CmmtBbs cmmtBoard = cmmtBoardDao.select(param.getBoardId());
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시판 권한검사
		if (!boardUtils.canWriteArticle(param.getBoardId(), worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		
		long count = cmmtBoardArticleDao.selectMinuteCount(param.getBoardId(), worker.getMemberId());
		if(count > 0) {
			throw new InvalidationException("1분이내 등록한 글이 존재하여 등록할 수 없습니다.");
		}

		/*
		 * 입력 검사
		 */

		InvalidationsException exs = new InvalidationsException();

		// 글 내용 검사
		if (cmmtBoard.getUseForm()) {
			List<BoardArticleCnParam> cnList = new ArrayList<>();
			if (param.getArticleCnList() != null) {
				for (BoardArticleCnParam bap : param.getArticleCnList()) {
					if (string.isNotBlank(bap.getHeader()) || string.isNotBlank(bap.getArticleCn())) {
						cnList.add(bap);
					}
				}
			}
			if (cnList.isEmpty()) {
				if (string.isNotBlank(param.getArticle())) {
					exs.add("article", "양식을 사용하여 내용글을 입력하세요.");
				}
				else {
					exs.add("article", "내용글을 입력하세요.");
				}
			}
			param.setArticle(null);
			param.setArticleCnList(cnList);
		} else {
			param.setArticleCnList(null);
			if (string.isBlank(param.getArticle())) {
				exs.add("article", "게시판 관리에서 양식 사용 여부가 '아니요'로 설정 되어 있습니다.\n양식 사용 여부를 '예'로 설정해 주세요.");
			}
		}

		// URL 목록 검사(URL 목록은 양식을 사용하는 경우에만)
		if (cmmtBoard.getUseForm()) {
			List<BoardArticleUrlParam> urlList = new ArrayList<>();

			if (param.getArticleUrlList() != null) {
				boolean blank = false;
				for (BoardArticleUrlParam bau : param.getArticleUrlList()) {
					String urlNm = string.trim( bau.getUrlNm() );
					String url = string.trim( bau.getUrl() );
					if (string.isBlank(urlNm) && string.isBlank(url)) {
						continue;
					}
					if (string.isBlank(urlNm) || string.isBlank(url)) {
						blank = true;
						break;
					}

					String upperUrl = string.upperCase(url);
					boolean http = string.startsWithAny(upperUrl, "HTTP://", "HTTPS://");
					if (!http) {
						url = "http://" + url;
					}

					bau.setUrl(url);
					bau.setUrlNm(urlNm);
					urlList.add(bau);
				}
				if (blank) {
					exs.add("articleUrl", "URL은 이름과 URL 모두를 입력해야 합니다.");
				}
			}
			param.setArticleUrlList(urlList);
		} else {
			param.setArticleUrlList(null);
		}

		// 카테고리 검사
		if (cmmtBoard.getCategory()) {
			if (string.isBlank(param.getCategoryCd())) {
				exs.add("categoryCd", "카테고리를 입력하세요.");
			} else {
				String codeGroup = cmmtBoard.getCategoryCodeGroup();
				CodeDto code = codeDao.select(codeGroup, param.getCategoryCd());
				if (code == null) {
					exs.add("categoryCd","카테고리 코드가 공통코드에 등록되어 있지 않습니다.");
				}
			}
		} else {
			param.setCategoryCd(null);
		}

		// 고정공지 여부
		if (param.getNotice() == null) {
			param.setNotice(false);
		}

		if (param.getNotice()) {
			if (!cmmtBoard.getNoticeAvailable()) {
				exs.add("notice", "고정공지를 사용할 수 없는 게시판입니다.");
			}
			else if (!boardUtils.canWriteNotice(param.getBoardId(), worker)){
				exs.add("notice", "고정공지는 게시판 관리자만 등록할 수 있습니다.");
			}
		}

		// 전시여부
		if (param.getPosting() == null) {
			param.setPosting(true);
		}

		// 내부사용자가 아니면, posting은 무조건 true
		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			param.setPosting(true);
		}

		// 제목
		if (string.isBlank(param.getTitle())) {
			exs.add("title", "제목을 입력하세요.");
		}

		// 웹에디터 사용여부
		if (param.getWebEditor() == null) {
			param.setWebEditor(false);
		}

		if (exs.size() > 0) {
			throw exs;
		}

		/*
		 * 파일 저장
		 */

		// thumbnail
		String pcThumbnailFileId = null;
		String mobileThumbnailFileId = null;
		String thumbnailAltCn = null;
		if (cmmtBoard.getUseThumbnail()) {
			if (pcThumbnailFile != null && pcThumbnailFile.getSize() > 0) {
				String[] exts = CodeExt.uploadExt.imageExt;
				CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcThumbnailFile, exts, 0);
				pcThumbnailFileId = att.getAttachmentId();

				// 썸네일 대체 내용 세
				thumbnailAltCn = param.getThumbnailAltCn();
			}
			if (mobileThumbnailFile != null && mobileThumbnailFile.getSize() > 0) {
				String[] exts = CodeExt.uploadExt.imageExt;
				CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileThumbnailFile, exts, 0);
				mobileThumbnailFileId = att.getAttachmentId();

				// 썸네일 대체 내용 세
				thumbnailAltCn = param.getThumbnailAltCn();
			}
		}

		// 첨부파일
		String attachmentGroupId = null;
		if (cmmtBoard.getAttachable()) {
			if (attachments != null && attachments.size() > 0) {
				String[] exts = cmmtBoard.getAttachmentExt().split("/");
				long size = cmmtBoard.getAttachmentSize() * (1024 * 1024);
				CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachments, exts, size);
				attachmentGroupId = attGroup.getAttachmentGroupId();
			}
		}

		// 이미지파일
		String imageGroupId = null;
		if (images != null && images.size() > 0) {
			String[] exts = CodeExt.uploadExt.imageExt;
			CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), images, exts, 0);
			imageGroupId = attGroup.getAttachmentGroupId();
		}

		/*
		 * 입력
		 */

		Date now = new Date();

		// 게시글 내용 세팅(목록 출력 시 게시글 출력을 위해 게시글 내용 세팅)
		StringBuilder articleCn = new StringBuilder();
		if (cmmtBoard.getUseForm()) {
			for (BoardArticleCnParam bap : param.getArticleCnList()) {
				if (articleCn.length() > 0) {
					// 개행으로 이어 붙이기
					articleCn.append(System.lineSeparator());
				}
				articleCn.append(bap.getHeader()).append(" ").append(bap.getArticleCn());
			}
		} else {
			articleCn.append(param.getArticle());
		}

		// 게시글 입력
		String articleId = string.getNewId("article-");
		CmmtBbsctt cmmtBoardArticle = CmmtBbsctt.builder()
				.articleId(articleId)
				.boardId(param.getBoardId())
				.title(param.getTitle())
				.article(articleCn.toString())
				.notice(param.getNotice())
				.attachmentGroupId(attachmentGroupId)
				.imageGroupId(imageGroupId)
				.categoryCd(param.getCategoryCd())
				.posting(param.getPosting())
				.webEditor(param.getWebEditor())
				.sharedUrl(param.getSharedUrl())
				.pcThumbnailFileId(pcThumbnailFileId)
				.mobileThumbnailFileId(mobileThumbnailFileId)
				.thumbnailAltCn(thumbnailAltCn)
				.readCnt(0L)
				.createdDt(now)
				.creatorId(worker.getMemberId())
				.updatedDt(now)
				.updaterId(worker.getMemberId())
				.build();
		cmmtBoardArticleDao.insert(cmmtBoardArticle);

		// 양식형 게시글 입력
		if (param.getArticleCnList() != null && param.getArticleCnList().size() > 0) {
			List<CmmtBbscttCn> articleCnList = new ArrayList<>();
			long idx = 0;
			for (BoardArticleCnParam cn : param.getArticleCnList()) {
				CmmtBbscttCn cmmtBoardArticleCn = CmmtBbscttCn.builder()
						.articleCnId(string.getNewId("articlecn-"))
						.articleId(articleId)
						.sortOrder(++idx)
						.header(cn.getHeader())
						.articleCn(cn.getArticleCn())
						.build();
				articleCnList.add(cmmtBoardArticleCn);
			}

			if (articleCnList.size() > 0) {
				cmmtBoardArticleCnDao.insertList(articleCnList);
			}
		}

		// URL 입력
		if (param.getArticleUrlList() != null && param.getArticleUrlList().size() > 0) {
			List<CmmtBbscttLink> articleUrlList = new ArrayList<>();
			long idx = 0;
			for (BoardArticleUrlParam up : param.getArticleUrlList()) {
				CmmtBbscttLink bau = CmmtBbscttLink.builder()
						.urlId(string.getNewId("bau-"))
						.articleId(articleId)
						.sortOrder(++idx)
						.urlNm(up.getUrlNm())
						.url(up.getUrl())
						.build();
				articleUrlList.add(bau);
			}

			if (articleUrlList.size() > 0) {
				cmmtBoardArticleUrlDao.insertList(articleUrlList);
			}
		}

		/*
		 * 게시판 글 건수 수정
		 */
		cmmtBoardDao.updateArticleCnt(param.getBoardId());
		cmmtBoard = cmmtBoardDao.select(param.getBoardId());

		/*
		 * 수정된 내용을 조회하여 출력
		 */
		cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoard.getUseForm()) {
			List<CmmtBbscttCn> articleCnList = cmmtBoardArticleCnDao.selectList(articleId);
			List<CmmtBbscttLink> articleUrlList = cmmtBoardArticleUrlDao.selectList(articleId);
			cmmtBoardArticle.setArticleCnList(articleCnList);
			cmmtBoardArticle.setArticleUrlList(articleUrlList);
		}
		cmmtBoardArticle.setCmmtBoard(cmmtBoard);

		return cmmtBoardArticle;
	}

	/**
	 * 게시글 상세 조회(zip파일 다운로드)
	 * @param boardId
	 * @param articleId
	 * @return
	 */
	public CmmtBbsctt get(String boardId, String articleId) {
		BnMember worker = SecurityUtils.getCurrentMember();

		// 게시판 조회
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}
		// 권한검사
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 게시글 조회
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}
		// 내부사용자가 아닌데, 전시여부가 false이면
		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			if (!cmmtBoardArticle.getPosting()) {
				throw new InvalidationException("게시글이 존재하지 않습니다.");
			}
		}
		if (!string.equals(cmmtBoardArticle.getBoardId(), boardId)) {
			throw new InvalidationException("해당 게시판에 게시글이 존재하지 않습니다.");
		}

		// 게시글 조회건수를 증가한다.
		cmmtBoardArticleDao.incReadCnt(articleId);
		cmmtBoardArticle.setReadCnt(cmmtBoardArticle.getReadCnt() + 1);

		// 게시글 내용 조회, URL 조회
		if (cmmtBoard.getUseForm()) {
			List<CmmtBbscttCn> articleCnList = cmmtBoardArticleCnDao.selectList(articleId);
			List<CmmtBbscttLink> articleUrlList = cmmtBoardArticleUrlDao.selectList(articleId);
			cmmtBoardArticle.setArticleCnList(articleCnList);
			cmmtBoardArticle.setArticleUrlList(articleUrlList);
		}

		// 게시판 정보를 추가한다.
		cmmtBoardArticle.setCmmtBoard(cmmtBoard);

		// 첨부파일 목록 추가
		if (cmmtBoard.getAttachable() && string.isNotBlank(cmmtBoardArticle.getAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(cmmtBoardArticle.getAttachmentGroupId());
			cmmtBoardArticle.setAttachmentList(list);
		}

		// 이미지파일 목록 추가
		if (string.isNotBlank(cmmtBoardArticle.getImageGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(cmmtBoardArticle.getImageGroupId());
			cmmtBoardArticle.setImageList(list);
		}
		return cmmtBoardArticle;
	}
	
	/**
	 * 게시글 상세 조회
	 * @param boardId
	 * @param articleId
	 * @return
	 */
	public CmmtBbsctt getDownloadZip(String boardId, String articleId) {
		BnMember worker = SecurityUtils.getCurrentMember();

		// 게시판 조회
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}
		// 권한검사
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 게시글 조회
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}
		// 내부사용자가 아닌데, 전시여부가 false이면
		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			if (!cmmtBoardArticle.getPosting()) {
				throw new InvalidationException("게시글이 존재하지 않습니다.");
			}
		}
		if (!string.equals(cmmtBoardArticle.getBoardId(), boardId)) {
			throw new InvalidationException("해당 게시판에 게시글이 존재하지 않습니다.");
		}

		// 게시글 내용 조회, URL 조회
		if (cmmtBoard.getUseForm()) {
			List<CmmtBbscttCn> articleCnList = cmmtBoardArticleCnDao.selectList(articleId);
			List<CmmtBbscttLink> articleUrlList = cmmtBoardArticleUrlDao.selectList(articleId);
			cmmtBoardArticle.setArticleCnList(articleCnList);
			cmmtBoardArticle.setArticleUrlList(articleUrlList);
		}

		// 게시판 정보를 추가한다.
		cmmtBoardArticle.setCmmtBoard(cmmtBoard);

		// 첨부파일 목록 추가
		if (cmmtBoard.getAttachable() && string.isNotBlank(cmmtBoardArticle.getAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(cmmtBoardArticle.getAttachmentGroupId());
			cmmtBoardArticle.setAttachmentList(list);
		}

		// 이미지파일 목록 추가
		if (string.isNotBlank(cmmtBoardArticle.getImageGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(cmmtBoardArticle.getImageGroupId());
			cmmtBoardArticle.setImageList(list);
		}
		return cmmtBoardArticle;
	}

	/**
	 * 게시글 삭제
	 *
	 * @param boardId
	 * @param articleId
	 */
	public void remove(String boardId, String articleId) {
		BnMember worker = SecurityUtils.getCurrentMember();

		if (worker == null) {
			throw new InvalidationException("로그인한 후 사용하세요.");
		}

		// 게시판 조회
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시글 조회
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}
		if (!string.equals(cmmtBoardArticle.getBoardId(), boardId)) {
			throw new InvalidationException("해당 게시판에 게시글이 존재하지 않습니다.");
		}

		// 권한검사
		if (!boardUtils.canRemoveArticle(boardId, cmmtBoardArticle, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 게시글 댓글 삭제
		cmmtBoardCmmntDao.deleteList(articleId);

		// 게시글 내용 삭제
		cmmtBoardArticleCnDao.deleteList(articleId);

		// 게시글 URL 삭제
		cmmtBoardArticleUrlDao.deleteList(articleId);

		// 게시글 삭제
		cmmtBoardArticleDao.delete(articleId);

		/*
		 * 게시판 글 건수 수정
		 */
		cmmtBoardDao.updateArticleCnt(boardId);

		/*
		 * 첨부파일 삭제
		 */
		if (string.isNotBlank(cmmtBoardArticle.getAttachmentGroupId())) {
			attachmentService.removeFiles_group(config.getDir().getUpload(), cmmtBoardArticle.getAttachmentGroupId());
		}
		if (string.isNotBlank(cmmtBoardArticle.getImageGroupId())) {
			attachmentService.removeFiles_group(config.getDir().getUpload(), cmmtBoardArticle.getImageGroupId());
		}
		if (string.isNotBlank(cmmtBoardArticle.getPcThumbnailFileId())) {
			attachmentService.removeFile(config.getDir().getUpload(), cmmtBoardArticle.getPcThumbnailFileId());
		}
		if (string.isNotBlank(cmmtBoardArticle.getMobileThumbnailFileId())) {
			attachmentService.removeFile(config.getDir().getUpload(), cmmtBoardArticle.getMobileThumbnailFileId());
		}
	}

	/**
	 * 최근 게시글 목록조회
	 *
	 * @param boardId
	 * @param categoryCd
	 * @param hasNotice
	 * @param cnt
	 * @return
	 */
	public JsonList<BoardArticleListItem> getRecentList(String boardId, String categoryCd, Boolean hasNotice, Long cnt) {
		BnMember worker = SecurityUtils.getCurrentMember();

		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 권한검사
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}


		if (!cmmtBoard.getCategory()) {
			categoryCd = null;
		}
		if (!cmmtBoard.getNoticeAvailable()) {
			hasNotice = false;
		}
		if (hasNotice == null) {
			hasNotice = false;
		}
		if (cnt == null) {
			cnt = 5L;
		}

		List<BoardArticleListItem> list = cmmtBoardArticleDao.selectList_recent(boardId, categoryCd, hasNotice, cnt);

		return new JsonList<>(list);
	}

	/**
	 * 게시글 수정
	 *
	 * @param param					게시글 등록 정보
	 * @param pcThumbnailFile		PC용 썸네일 이미지
	 * @param mobileThumbnailFile	모바일용 썸네일 이미지
	 * @param images				이미지 파일 목록
	 * @param attachments			첨부파일 목록
	 * @return
	 */
	public CmmtBbsctt modify(
			ModifyArticleParam param,
			MultipartFile pcThumbnailFile,
			MultipartFile mobileThumbnailFile,
			List<MultipartFile> images,
			List<MultipartFile> attachments) {

		/*
		 * ThumbnailFile이 입력되었다면, 기존 파일은 삭제하여야 한다.
		 * attachments는 입력된 파일을 추가로 등록하는 개념. 기존 파일 삭제는 별도의 기능으로 각각 삭제하여야 한다.
		 */

		BnMember worker = SecurityUtils.getCurrentMember();

		if (worker == null) {
			throw new InvalidationException("로그인한 후 사용하세요.");
		}

		// 게시판 존재 확인
		CmmtBbs cmmtBoard = cmmtBoardDao.select(param.getBoardId());
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시글 조회
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(param.getArticleId());
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		// 권한검사
		if (!boardUtils.canModifyArticle(param.getBoardId(), cmmtBoardArticle, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		/*
		 * 입력검사
		 */

		InvalidationsException exs = new InvalidationsException();

		// 글 내용 검사
		if (cmmtBoard.getUseForm()) {
			List<BoardArticleCnParam> cnList = new ArrayList<>();
			if (param.getArticleCnList() != null) {
				for (BoardArticleCnParam bap : param.getArticleCnList()) {
					if (string.isNotBlank(bap.getHeader()) || string.isNotBlank(bap.getArticleCn())) {
						cnList.add(bap);
					}
				}
			}
			if (cnList.isEmpty()) {
				if (string.isNotBlank(param.getArticle())) {
					exs.add("article", "양식을 사용하여 내용글을 입력하세요.");
				}
				else {
					exs.add("article", "내용글을 입력하세요.");
				}
			}
			param.setArticle(null);
			param.setArticleCnList(cnList);
		} else {
			param.setArticleCnList(null);
			if (string.isBlank(param.getArticle())) {
				exs.add("article", "내용글을 입력하세요.");
			}
		}

		// URL 목록 검사
		if (cmmtBoard.getUseForm()) {
			List<BoardArticleUrlParam> urlList = new ArrayList<>();

			if (param.getArticleUrlList() != null) {
				boolean blank = false;
				for (BoardArticleUrlParam bau : param.getArticleUrlList()) {
					String urlNm = string.trim( bau.getUrlNm() );
					String url = string.trim( bau.getUrl() );
					if (string.isBlank(urlNm) && string.isBlank(url)) {
						continue;
					}
					if (string.isBlank(urlNm) || string.isBlank(url)) {
						blank = true;
						break;
					}

					String upperUrl = string.upperCase(url);
					boolean http = string.startsWithAny(upperUrl, "HTTP://", "HTTPS://");
					if (!http) {
						url = "http://" + url;
					}

					bau.setUrl(url);
					bau.setUrlNm(urlNm);
					urlList.add(bau);
				}
				if (blank) {
					exs.add("articleUrl", "URL은 이름과 URL 모두를 입력해야 합니다.");
				}
			}
			param.setArticleUrlList(urlList);
		} else {
			param.setArticleUrlList(null);
		}

		// 카테고리 검사
		if (cmmtBoard.getCategory()) {
			if (string.isBlank(param.getCategoryCd())) {
				exs.add("categoryCd", "카테고리를 입력하세요.");
			} else {
				String codeGroup = cmmtBoard.getCategoryCodeGroup();
				CodeDto code = codeDao.select(codeGroup, param.getCategoryCd());
				if (code == null) {
					exs.add("categoryCd","카테고리 코드가 공통코드에 등록되어 있지 않습니다.");
				}
			}
		} else {
			param.setCategoryCd(null);
		}

		// 고정공지 여부
		if (param.getNotice() == null) {
			param.setNotice(false);
		}

		if (param.getNotice()) {
			if (!cmmtBoard.getNoticeAvailable()) {
				exs.add("notice", "고정공지를 사용할 수 없는 게시판입니다.");
			}
			else if (!boardUtils.canWriteNotice(param.getBoardId(), worker)){
				exs.add("notice", "고정공지는 게시판 관리자만 등록할 수 있습니다.");
			}
		}

//		// 전시여부
//		if (param.getPosting() == null) {
//			param.setPosting(true);
//		}
//
//		// 내부사용자가 아니면, posting은 무조건 true
//		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
//			param.setPosting(true);
//		}

		// 제목
		if (string.isBlank(param.getTitle())) {
			exs.add("title", "제목을 입력하세요.");
		}

		// 웹에디터 사용여부
		if (param.getWebEditor() == null) {
			param.setWebEditor(false);
		}

		if (exs.size() > 0) {
			throw exs;
		}

		/*
		 * 파일 저장
		 */

		// thumbnail
		String pcThumbnailFileId = null;
		String mobileThumbnailFileId = null;
		String thumbnailAltCn = null;
		if (cmmtBoard.getUseThumbnail()) {
			if (pcThumbnailFile != null && pcThumbnailFile.getSize() > 0) {
				String[] exts = CodeExt.uploadExt.imageExt;
				CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcThumbnailFile, exts, 0);
				pcThumbnailFileId = att.getAttachmentId();

				// 썸네일 대체 내용 세팅
				thumbnailAltCn = param.getThumbnailAltCn();
			}
			if (mobileThumbnailFile != null && mobileThumbnailFile.getSize() > 0) {
				String[] exts = CodeExt.uploadExt.imageExt;
				CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileThumbnailFile, exts, 0);
				mobileThumbnailFileId = att.getAttachmentId();

				// 썸네일 대체 내용 세팅
				thumbnailAltCn = param.getThumbnailAltCn();
			}
		}

		// 첨부파일
		String attachmentGroupId = cmmtBoardArticle.getAttachmentGroupId();
		if (cmmtBoard.getAttachable()) {
			if (attachments != null && attachments.size() > 0) {
				String[] exts = cmmtBoard.getAttachmentExt().split("/");
				long size = cmmtBoard.getAttachmentSize() * (1024 * 1024);
				if (string.isBlank(attachmentGroupId)) {
					CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachments, exts, size);
					attachmentGroupId = attGroup.getAttachmentGroupId();
				} else {
					attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, attachments, exts, size);
				}
			}
		}

		// 이미지 파일
		String imageGroupId = cmmtBoardArticle.getImageGroupId();
		if (images != null && images.size() > 0) {
			String[] exts = CodeExt.uploadExt.imageExt;
			if (string.isBlank(imageGroupId)) {
				CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), images, exts, 0);
				imageGroupId = attGroup.getAttachmentGroupId();
			} else {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), imageGroupId, images, exts, 0);
			}
		}

		/*
		 * 수정
		 */

		Date now = new Date();

		// 게시글 내용 세팅(목록 출력 시 게시글 출력을 위해 게시글 내용 세팅)
		StringBuilder articleCn = new StringBuilder();
		if (cmmtBoard.getUseForm()) {
			for (BoardArticleCnParam bap : param.getArticleCnList()) {
				if (articleCn.length() > 0) {
					// 개행으로 이어 붙이기
					articleCn.append(System.lineSeparator());
				}
				articleCn.append(bap.getHeader()).append(" ").append(bap.getArticleCn());
			}
		}
		else {
			articleCn.append(param.getArticle());
		}

		// 게시글 수정
		boolean pcThumbnailChanged = string.isNotBlank(cmmtBoardArticle.getPcThumbnailFileId()) && string.isNotBlank(pcThumbnailFileId);
		String orgPcThumbnailFileId = cmmtBoardArticle.getPcThumbnailFileId();

		boolean mobileThumbnailChanged = string.isNotBlank(cmmtBoardArticle.getMobileThumbnailFileId()) && string.isNotBlank(mobileThumbnailFileId);
		String orgMobileThumbnailFileId = cmmtBoardArticle.getMobileThumbnailFileId();

		cmmtBoardArticle.setTitle(param.getTitle());
		cmmtBoardArticle.setArticle(articleCn.toString());
		cmmtBoardArticle.setNotice(param.getNotice());
		cmmtBoardArticle.setAttachmentGroupId(attachmentGroupId);
		cmmtBoardArticle.setImageGroupId(imageGroupId);
		cmmtBoardArticle.setCategoryCd(param.getCategoryCd());
//		cmmtBoardArticle.setPosting(param.getPosting());
		cmmtBoardArticle.setWebEditor(param.getWebEditor());
		cmmtBoardArticle.setSharedUrl(param.getSharedUrl());
		if (string.isNotBlank(pcThumbnailFileId)) {
			cmmtBoardArticle.setPcThumbnailFileId(pcThumbnailFileId);
		}
		if (string.isNotBlank(mobileThumbnailFileId)) {
			cmmtBoardArticle.setMobileThumbnailFileId(mobileThumbnailFileId);
		}
		cmmtBoardArticle.setThumbnailAltCn(thumbnailAltCn);
		cmmtBoardArticle.setUpdatedDt(now);
		cmmtBoardArticle.setUpdaterId(worker.getMemberId());

		cmmtBoardArticleDao.update(cmmtBoardArticle);

		// 기존 양식형 게시글 삭제
		cmmtBoardArticleCnDao.deleteList(param.getArticleId());

		// 양식형 게시글 입력
		if (param.getArticleCnList() != null && param.getArticleCnList().size() > 0) {
			// 새글 입력
			List<CmmtBbscttCn> articleCnList = new ArrayList<>();
			long idx = 0;
			for (BoardArticleCnParam cn : param.getArticleCnList()) {
				CmmtBbscttCn cmmtBoardArticleCn = CmmtBbscttCn.builder()
						.articleCnId(string.getNewId("articlecn-"))
						.articleId(param.getArticleId())
						.sortOrder(++idx)
						.header(cn.getHeader())
						.articleCn(cn.getArticleCn())
						.build();
				articleCnList.add(cmmtBoardArticleCn);
			}

			if (articleCnList.size() > 0) {
				cmmtBoardArticleCnDao.insertList(articleCnList);
			}
		}

		// 기존 URL 삭제
		cmmtBoardArticleUrlDao.deleteList(param.getArticleId());

		// URL 입력
		if (param.getArticleUrlList() != null && param.getArticleUrlList().size() > 0) {
			List<CmmtBbscttLink> articleUrlList = new ArrayList<>();
			long idx = 0;
			for (BoardArticleUrlParam up : param.getArticleUrlList()) {
				CmmtBbscttLink bau = CmmtBbscttLink.builder()
						.urlId(string.getNewId("bau-"))
						.articleId(param.getArticleId())
						.sortOrder(++idx)
						.urlNm(up.getUrlNm())
						.url(up.getUrl())
						.build();
				articleUrlList.add(bau);
			}

			if (articleUrlList.size() > 0) {
				cmmtBoardArticleUrlDao.insertList(articleUrlList);
			}
		}


		/*
		 * 기존 Thumbnail 파일 삭제
		 * (주의) 파일 삭제는 DB 오류를 대비하여, DB 작업이 모두 끝난 다음에 하도록 한다.
		 */
		if (pcThumbnailChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgPcThumbnailFileId);
		}
		if (mobileThumbnailChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgMobileThumbnailFileId);
		}

		/*
		 * 수정된 내용을 조회하여 출력
		 */
		cmmtBoardArticle = cmmtBoardArticleDao.select(param.getArticleId());
		if (cmmtBoard.getUseForm()) {
			List<CmmtBbscttCn> articleCnList = cmmtBoardArticleCnDao.selectList(param.getArticleId());
			List<CmmtBbscttLink> articleUrlList = cmmtBoardArticleUrlDao.selectList(param.getArticleId());
			cmmtBoardArticle.setArticleCnList(articleCnList);
			cmmtBoardArticle.setArticleUrlList(articleUrlList);
		}
		cmmtBoardArticle.setCmmtBoard(cmmtBoard);

		return cmmtBoardArticle;
	}

	/**
	 * 게시글 기본정보 조회
	 *
	 * @param boardId
	 * @param articleId
	 * @return
	 */
	public CmmtBbsctt select(String boardId, String articleId)
	{
		// 게시판 조회
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시글 조회
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}
		if (!string.equals(cmmtBoardArticle.getBoardId(), boardId)) {
			throw new InvalidationException("해당 게시판에 게시글이 존재하지 않습니다.");
		}

		return cmmtBoardArticle;
	}

	/**
	 * 게시글 목록조회의 검색조건을 기준으로 게시글에 대한 이전글/다음글 정보를 조회힌다.
	 *
	 * @param boardId : 게시판ID
	 * @param articleId : 게시글ID
	 * @param param : 검색조건
	 * @return BoardArticlePrevNextItem
	 */
	public BoardArticlePrevNextItem getPreviousNext(String boardId, String articleId, GetArticlesParam param)
	{
		CmmtBbs cmmtBoard = cmmtBoardDao.select(param.getBoardId());
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시판 권한검사
		BnMember worker = SecurityUtils.getCurrentMember();
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}
		// 내부사용자가 아닌 경우에는 posting = true로 만 해야 한다.
		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			param.setPosting(true);
		}
		// 카테고리를 사용할 수 없는 게시판인가?
		if (!cmmtBoard.getCategory()) {
			param.setCategoryCd(null);
		}

		// 사용자 게시물검색코드 대문자 변경
		if (string.isNotBlank(param.getArticleSrchCd())) {
			param.setArticleSrchCd(string.upperCase(param.getArticleSrchCd()));
		}

		// 공지글 게시 여부
		boolean noticeAvailable = false;
		if (BooleanUtils.isTrue(cmmtBoard.getNoticeAvailable())) {
			noticeAvailable = true;
		}

		return cmmtBoardArticleDao.selectPrevNext(noticeAvailable, articleId, param);
	}

	/**
	 * 게시글의 작성자 정보를 조회한다.
	 * (관리자페이지에서만 사용되는 API이므로 내부사용자 로그인 검증을 수행한다.)
	 *
	 * @param boardId	게시판ID
	 * @param articleId	게시글ID
	 * @return CreatrInfDto	작성자정보
	 */
	public CreatrInfDto getBbscttWriterInfo(String boardId, String articleId)
	{
		//1.내부사용자만 조회할 수 있다.
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//2.게시판 정보를 조회한다.
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		//3.게시판 권한 검사한다.
		if (!boardUtils.canReadArticle(cmmtBoard, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		//4.게시글 정보를 조회한다.
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}
		if (!string.equals(cmmtBoardArticle.getBoardId(), boardId)) {
			throw new InvalidationException("해당 게시판에 게시글이 존재하지 않습니다.");
		}

		//5.게시글 작성자 정보를 조회한다.
		UserDto userDto = userDao.select(cmmtBoardArticle.getCreatorId());
		if (userDto == null) {
			throw new InvalidationException("해당 게시글의 작성자 정보가 존재하지 않습니다.");
		}

		CreatrInfDto writerInfDto = new CreatrInfDto();
		property.copyProperties(writerInfDto, userDto);

		//5-1.회원유형명 세팅
		CodeDto code = codeDao.select("MEMBER_TYPE", writerInfDto.getMemberType());
		writerInfDto.setMemberTypeNm(code.getCodeNm());

		//5-2.기업회원인 경우 담당자명 세팅
		if (CodeExt.memberTypeExt.isBzmn(userDto.getMemberType())) {
			CmmtMberInfo member = memberDao.select(writerInfDto.getMemberId());
			writerInfDto.setChargerNm(member.getChargerNm());
		}

		//6.개인정보조회 log를 생성한다.
		StringBuilder workCnForm = new StringBuilder();
		workCnForm.append("[%s] 게시판의 [%s] 게시글 작성자 정보 조회").append(System.lineSeparator()).append(" - (게시글ID : [%s])");
		if (!string.equals(worker.getMemberId(), writerInfDto.getMemberId())) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
					.memberId(worker.getMemberId())
					.memberIp(webutils.getRemoteIp(request))
					.workTypeNm("조회")
					.workCn(String.format(workCnForm.toString(), cmmtBoard.getBoardNm(), cmmtBoardArticle.getTitle(), cmmtBoardArticle.getArticleId()))
					.trgterId(writerInfDto.getMemberId())
					.email(writerInfDto.getEmail())
					.birthday(null)
					.mobileNo(writerInfDto.getMobileNo())
					.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return writerInfDto;
	}

	/**
	 * 게시글 게시상태 변경
	 *
	 * @param boardId	게시판ID
	 * @param articleId	게시글ID
	 * @param posting	게시여부
	 * @return CmmtBoardArticle	게시글
	 */
	public CmmtBbsctt modifyPosting(String boardId, String articleId, Boolean posting)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();

		if (worker == null) {
			throw new InvalidationException("로그인한 후 사용하세요.");
		}

		// 게시판 존재 확인
		CmmtBbs cmmtBoard = cmmtBoardDao.select(boardId);
		if (cmmtBoard == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시글 조회
		CmmtBbsctt cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoardArticle == null) {
			throw new InvalidationException("게시글이 존재하지 않습니다.");
		}

		// 권한검사
		if (!boardUtils.canPostingArticle(boardId, cmmtBoardArticle, worker)) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 전시여부
		if (posting == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "전시여부"));
		}

		// 게시글 수정
		cmmtBoardArticle.setPosting(posting);
		if (posting == false) {
			// 전시안함으로 설정하는 경우 공지글(고정여부)를 내린다.
			cmmtBoardArticle.setNotice(false);
		}
		cmmtBoardArticle.setUpdatedDt(now);
		cmmtBoardArticle.setUpdaterId(worker.getMemberId());

		cmmtBoardArticleDao.update(cmmtBoardArticle);

		/*
		 * 수정된 내용을 조회하여 출력
		 */
		cmmtBoardArticle = cmmtBoardArticleDao.select(articleId);
		if (cmmtBoard.getUseForm()) {
			List<CmmtBbscttCn> articleCnList = cmmtBoardArticleCnDao.selectList(articleId);
			List<CmmtBbscttLink> articleUrlList = cmmtBoardArticleUrlDao.selectList(articleId);
			cmmtBoardArticle.setArticleCnList(articleCnList);
			cmmtBoardArticle.setArticleUrlList(articleUrlList);
		}
		cmmtBoardArticle.setCmmtBoard(cmmtBoard);

		return cmmtBoardArticle;
	}

}

