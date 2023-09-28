package aicluster.framework.common.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.dao.CmmtAtchmnflDao;
import aicluster.framework.common.dao.CmmtAtchmnflGroupDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.exception;
import bnet.library.util.CoreUtils.file;
import bnet.library.util.CoreUtils.filename;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.CoreUtils.zip;
import bnet.library.util.dto.ZipFileInfo;
import bnet.library.util.MimeTypeUtils;

@Service("componentAttachmentService")
public class AttachmentService {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String SINGLE_FILE_GROUP_ID = "single-file-group-id";
	public static final int MAX_FILE_NM_LEN = 500;

	@Autowired
	private MimeTypeUtils mimeTypeUtils;

    @Autowired
    private CmmtAtchmnflDao attachmentDao;

    @Autowired
    private CmmtAtchmnflGroupDao attachmentGroupDao;

    /**
     * 첨부파일 저장.
     * 이력서, 증명사진 등과 같이 그룹에 묶여 있지 않은 단일 파일을 업로드할 때 사용.
     *
     * @param uploadRootDir		upload한 파일을 저장할 root directory
     * @param multipartFile		업로드한 파일
     * @param allowedFileExts	저장이 허용된 확장자 array. null이면 검사하지 않음.
     * @param maxFileSize		저장 가능한 최대 파일 크기. 0이면 검사하지 않음.
     * @return	저장된 첨부파일 정보
     */
	public CmmtAtchmnfl uploadFile_noGroup(String uploadRootDir, MultipartFile multipartFile, String[] allowedFileExts, long maxFileSize) {
		boolean fileExists = multipartFile != null && multipartFile.getSize() > 0;
        if (!fileExists) {
            throw new InvalidationException("파일을 입력하세요.");
        }
        if (string.isEmpty(uploadRootDir)) {
            throw new InvalidationException("파일을 저장할 디렉토리 정보입력하세요.");
        }

        Date now = new Date();
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 생성할 수 없습니다.");
        }

        // 파일 검사
        long fileSize = 0;
        String filename = multipartFile.getOriginalFilename();
        if (filename.length() > MAX_FILE_NM_LEN) {
            throw new InvalidationException("파일명은 " + MAX_FILE_NM_LEN + "자를 넘을 수 없습니다.");
        }
        String ext = CoreUtils.filename.getExtension(filename);
        if (allowedFileExts != null && allowedFileExts.length > 0) {
            if (string.isEmpty(ext) || !string.containsIgnoreCase(allowedFileExts, ext)) {
                throw new InvalidationException("지원하지 않는 파일 확장자가 포함되어 있습니다.(" + ext + ")");
            }
        }
        fileSize = multipartFile.getSize();

        if (maxFileSize > 0 && fileSize > maxFileSize) {
            throw new InvalidationException("저장가능한 파일크기(" + string.formatFileSize(maxFileSize) + ")를 초과하였습니다.");
        }

        // 첨부파일 그룹 생성
        CmmtAtchmnflGroup attachmentGroup = attachmentGroupDao.select(SINGLE_FILE_GROUP_ID);
        if (attachmentGroup == null) {
            attachmentGroup = new CmmtAtchmnflGroup();
            attachmentGroup.setAttachmentGroupId(SINGLE_FILE_GROUP_ID);
            attachmentGroup.setCreatedDt(now);
            attachmentGroup.setCreatorId(user.getMemberId());
            attachmentGroup.setUpdatedDt(now);
            attachmentGroup.setUpdaterId(user.getMemberId());
            attachmentGroupDao.insert(attachmentGroup);
        }

        List<CmmtAtchmnfl> attachments = new ArrayList<>();
        CmmtAtchmnfl attachment = null;

        filename = multipartFile.getOriginalFilename();
        String attachmentId = string.getNewId("att-");
        String savedFilePath = dir + "/" + attachmentId;
        try {
        	File nfile = new File(uploadRootDir + savedFilePath);
        	multipartFile.transferTo(nfile);
        	// Mimetype 확인
            boolean mimeType = mimeTypeUtils.checkMimeType(nfile, filename);
            if (!mimeType) {
            	throw new InvalidationException(String.format("첨부파일(%s)의 내용이 파일형식과 일치하지 않습니다.", filename));
            }
        } catch (IllegalStateException | IOException e) {
            this.logger.error("Fail to save file: " + uploadRootDir + savedFilePath);
            throw new InvalidationException("시스템 문제로 파일을 저장할 수 없습니다.");
        }

        attachment = new CmmtAtchmnfl();
        attachment.setAttachmentGroupId( attachmentGroup.getAttachmentGroupId() );
        attachment.setAttachmentId(attachmentId);
        attachment.setContentType(multipartFile.getContentType());
        attachment.setCreatedDt(now);
        attachment.setCreatorId(user.getMemberId());
        attachment.setSavedFilePath(savedFilePath);
        attachment.setDownloadCnt(0L);
        attachment.setFileDeleted(false);
        attachment.setFileNm(filename);
        attachment.setFileSize(multipartFile.getSize());

        attachments.add(attachment);

        attachmentDao.insertList(attachments);
        return attachment;
	}

	/**
	 * 사용자가 업로드한 파일이 아닌, 서버에서 생성한 파일을 저장할 때 사용.
	 *
	 * @param uploadRootDir	upload한 파일을 저장할 root directory
	 * @param localFile		저장할 파일
	 * @param filename		저장할 파일명
	 * @return 첨부파일 정보
	 */
	public CmmtAtchmnfl uploadLocalFile_noGroup(String uploadRootDir, File localFile, String filename) {
		boolean fileExists = localFile != null && localFile.exists();
        if (!fileExists) {
            throw new InvalidationException("저장할 파일이 없습니다.");
        }
        if (string.isEmpty(uploadRootDir)) {
            throw new InvalidationException("파일을 저장할 디렉토리 정보가 입력되지 않았습니다.");
        }

        Date now = new Date();
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

        // 파일 검사
        long fileSize = 0;
        if (filename.length() > MAX_FILE_NM_LEN) {
            throw new InvalidationException("파일명은 " + MAX_FILE_NM_LEN + "자를 넘을 수 없습니다.");
        }
        fileSize = localFile.length();

        // 첨부파일 그룹 생성
        CmmtAtchmnflGroup attachmentGroup = attachmentGroupDao.select(SINGLE_FILE_GROUP_ID);
        if (attachmentGroup == null) {
            attachmentGroup = new CmmtAtchmnflGroup();
            attachmentGroup.setAttachmentGroupId(SINGLE_FILE_GROUP_ID);
            attachmentGroup.setCreatedDt(now);
            attachmentGroup.setCreatorId(user.getMemberId());
            attachmentGroup.setUpdatedDt(now);
            attachmentGroup.setUpdaterId(user.getMemberId());
            attachmentGroupDao.insert(attachmentGroup);
        }

        List<CmmtAtchmnfl> attachments = new ArrayList<>();
        CmmtAtchmnfl attachment = null;

        String attachmentId = string.getNewId("att-");
        String savedFilePath = dir + "/" + attachmentId;
        try {
        	file.copyFile(localFile, new File(uploadRootDir + savedFilePath));
        } catch (IOException e) {
            this.logger.error(exception.getStackTraceString(e));
            throw new InvalidationException("시스템 문제로 파일을 저장할 수 없습니다.");
        }

        attachment = new CmmtAtchmnfl();
        attachment.setAttachmentGroupId( attachmentGroup.getAttachmentGroupId() );
        attachment.setAttachmentId(attachmentId);
        attachment.setContentType("UNKNOWN");
        attachment.setCreatedDt(now);
        attachment.setCreatorId(user.getMemberId());
        attachment.setSavedFilePath(savedFilePath);
        attachment.setDownloadCnt(0L);
        attachment.setFileDeleted(false);
        attachment.setFileNm(filename);
        attachment.setFileSize(fileSize);

        attachments.add(attachment);

        attachmentDao.insertList(attachments);
        return attachment;
	}

	/**
	 * 첨부파일 정보를 읽는다.
	 *
	 * @param attachmentId	첨부파일 ID
	 * @return	첨부파일 정보
	 */
	public CmmtAtchmnfl getFileInfo(String attachmentId) {
		CmmtAtchmnfl att = attachmentDao.select(attachmentId);
		if (att == null) {
			return null;
		}
		return att;
	}

	public File getFile(String uploadRootDir, String attachmentId) {
		CmmtAtchmnfl att = attachmentDao.select(attachmentId);
		if (att == null) {
			return null;
		}
		File file = new File(uploadRootDir + att.getSavedFilePath());
		if (file.exists()) {
			attachmentDao.increaseDownloadCnt(attachmentId);
			return file;
		}
		return null;
	}

	/**
	 * 게시판의 첨부파일과 같이 하나의 파일 그룹에 묶일 파일들을 최초 등록할 때 사용.
	 *
	 * @param uploadRootDir		upload한 파일들을 저장할 root directory
	 * @param multipartFiles	업로드한 파일들
	 * @param allowedFileExts	저장이 허용된 확장자 array. null 이면 검사하지 않음.
	 * @param maxFileSize		저장 가능한 최대 파일 크기 합. 0 이면 검사히지 않음.
	 * @return	첨부파일그룹 정보
	 */
	public CmmtAtchmnflGroup uploadFiles_toNewGroup(String uploadRootDir, Collection<MultipartFile> multipartFiles, String[] allowedFileExts, long maxFileSize) {
        boolean fileExists = multipartFiles != null && multipartFiles.size() > 0;
        if (!fileExists) {
            throw new InvalidationException("저장할 파일이 없습니다.");
        }
        if (string.isEmpty(uploadRootDir)) {
            throw new InvalidationException("파일을 저장할 디렉토리 정보가 입력되지 않았습니다.");
        }

        Date now = new Date();
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

        // 파일 검사
        long fileSize = 0;
        for (MultipartFile file: multipartFiles) {
            String filename = file.getOriginalFilename();
            if (filename.length() > MAX_FILE_NM_LEN) {
                throw new InvalidationException("파일명은 " + MAX_FILE_NM_LEN + "자를 넘을 수 없습니다.");
            }
            if (allowedFileExts != null && allowedFileExts.length > 0) {
                String ext = CoreUtils.filename.getExtension(filename);
                if (string.isEmpty(ext) || !string.containsIgnoreCase(allowedFileExts, ext)) {
                    throw new InvalidationException("지원하지 않는 파일 확장자가 포함되어 있습니다.(" + ext + ")");
                }
            }
            if (fileSize > Long.MAX_VALUE - file.getSize()) {
            	throw new InvalidationException("파일크기가 최대 크기를 넘어섰습니다.");
            }
            fileSize += file.getSize();
        }

        if (maxFileSize > 0 && fileSize > maxFileSize) {
        	throw new InvalidationException("저장가능한 파일크기(" + string.formatFileSize(maxFileSize) + ")를 초과하였습니다.");
        }

        // 첨부파일 그룹 생성
        String groupId = string.getNewId("attg-");
        CmmtAtchmnflGroup attachmentGroup = new CmmtAtchmnflGroup();
        attachmentGroup.setAttachmentGroupId(groupId);
        attachmentGroup.setCreatedDt(now);
        attachmentGroup.setCreatorId(user.getMemberId());

        attachmentGroupDao.insert(attachmentGroup);

        List<CmmtAtchmnfl> attachments = new ArrayList<>();
        CmmtAtchmnfl attachment = null;

        for (MultipartFile file: multipartFiles) {
            String filename = file.getOriginalFilename();
            String attachmentId = string.getNewId("att-");
            String savedFilePath = dir + "/" + attachmentId;
            try {
            	File nfile = new File(uploadRootDir + savedFilePath);
                file.transferTo(nfile);
                // Mimetype 확인
                boolean mimeType = mimeTypeUtils.checkMimeType(nfile, filename);
                if (!mimeType) {
                	throw new InvalidationException(String.format("첨부파일(%s)의 내용이 파일형식과 일치하지 않습니다.", filename));
                }
            } catch (IllegalStateException | IOException e) {
                this.logger.error("Fail to save file: " + uploadRootDir + savedFilePath);
                throw new InvalidationException("시스템 문제로 파일을 저장할 수 없습니다.");
            }

            attachment = new CmmtAtchmnfl();
            attachment.setAttachmentGroupId(groupId);
            attachment.setAttachmentId(attachmentId);
            attachment.setContentType(file.getContentType());
            attachment.setCreatedDt(now);
            attachment.setCreatorId(user.getMemberId());
            attachment.setSavedFilePath(savedFilePath);
            attachment.setDownloadCnt(0L);
            attachment.setFileDeleted(false);
            attachment.setFileNm(filename);
            attachment.setFileSize(file.getSize());

            attachments.add(attachment);
        }
        attachmentDao.insertList(attachments);
        return attachmentGroup;
	}

	/**
	 * 게시판의 첨부파일과 같이 하나의 파일 그룹에 묶일 파일 하나를 최초 등록할 때 사용.
	 *
	 * @param uploadRootDir		upload한 파일들을 저장할 root directory
	 * @param file				업로드한 파일
	 * @param allowedFileExts	저장이 허용된 확장자 array. null 이면 검사하지 않음.
	 * @param maxFileSize		저장 가능한 최대 파일 크기 합. 0 이면 검사히지 않음.
	 * @return	첨부파일 정보
	 */
	public CmmtAtchmnfl uploadFile_toNewGroup(String uploadRootDir, MultipartFile file, String[] allowedFileExts, Integer maxFileSize) {
        boolean fileExists = file != null && file.getSize() > 0;
        if (!fileExists) {
            throw new InvalidationException("저장할 파일이 없습니다.");
        }
        if (string.isEmpty(uploadRootDir)) {
            throw new InvalidationException("파일을 저장할 디렉토리 정보가 입력되지 않았습니다.");
        }

        Date now = new Date();
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                CoreUtils.file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

        // 파일 검사
        long fileSize = 0;
        String filename = file.getOriginalFilename();
        if (filename.length() > MAX_FILE_NM_LEN) {
            throw new InvalidationException("파일명은 " + MAX_FILE_NM_LEN + "자를 넘을 수 없습니다.");
        }
        String ext = CoreUtils.filename.getExtension(filename);
        if (allowedFileExts != null && allowedFileExts.length > 0) {
            if (string.isEmpty(ext) || !string.containsIgnoreCase(allowedFileExts, ext)) {
                throw new InvalidationException("지원하지 않는 파일 확장자가 포함되어 있습니다.(" + ext + ")");
            }
        }
        fileSize = file.getSize();

        if (maxFileSize > 0 && fileSize > maxFileSize) {
        	throw new InvalidationException("저장가능한 파일크기(" + string.formatFileSize(maxFileSize) + ")를 초과하였습니다.");
        }

        // 첨부파일 그룹 생성
        String groupId = string.getNewId("fgrp_");
        CmmtAtchmnflGroup attachmentGroup = new CmmtAtchmnflGroup();
        attachmentGroup.setAttachmentGroupId(groupId);
        attachmentGroup.setCreatedDt(now);
        attachmentGroup.setCreatorId(user.getMemberId());

        attachmentGroupDao.insert(attachmentGroup);

        // 첨부파일 생성
        List<CmmtAtchmnfl> attachments = new ArrayList<>();
        CmmtAtchmnfl attachment = null;

        String attachmentId = string.getNewId("att-");
        String savedFilePath = dir + "/" + attachmentId;
        try {
        	File nfile = new File(uploadRootDir + savedFilePath);
            file.transferTo(nfile);
			// Mimetype 확인
            boolean mimeType = mimeTypeUtils.checkMimeType(nfile, filename);
            if (!mimeType) {
            	throw new InvalidationException(String.format("첨부파일(%s)의 내용이 파일형식과 일치하지 않습니다.", filename));
            }
        } catch (IllegalStateException | IOException e) {
            this.logger.error("Fail to save file: " + uploadRootDir + savedFilePath);
            throw new InvalidationException("시스템 문제로 파일을 저장할 수 없습니다.");
        }

        attachment = new CmmtAtchmnfl();
        attachment.setAttachmentGroupId(groupId);
        attachment.setAttachmentId(attachmentId);
        attachment.setContentType(file.getContentType());
        attachment.setCreatedDt(now);
        attachment.setCreatorId(user.getMemberId());
        attachment.setSavedFilePath(savedFilePath);
        attachment.setDownloadCnt(0L);
        attachment.setFileDeleted(false);
        attachment.setFileNm(filename);
        attachment.setFileSize(file.getSize());

        attachments.add(attachment);
        attachmentDao.insertList(attachments);

        return attachment;
	}

	/**
	 * 첨부파일그룹에 새로 업로드된 파일 추가하기
	 *
	 * @param uploadRootDir			upload한 파일들을 저장할 root directory
	 * @param attachmentGroupId		업로드할 파일을 추가할 파일그룹ID
	 * @param multipartFiles		업로드한 파일들
	 * @param allowedFileExts		저장이 허용된 확장자 array. null 이면 검사하지 않음.
	 * @param maxFileSize			허용된 최대 파일 크기 합. 0 이면 검사하지 않음.
	 * @return	저장된 파일정보 목록
	 */
	public List<CmmtAtchmnfl> uploadFiles_toGroup(String uploadRootDir, String attachmentGroupId, Collection<MultipartFile> multipartFiles, String[] allowedFileExts, long maxFileSize) {
        boolean fileExists = multipartFiles != null && multipartFiles.size() > 0;
        if (!fileExists) {
            throw new InvalidationException("저장할 파일이 없습니다.");
        }
        if (string.isEmpty(uploadRootDir)) {
            throw new InvalidationException("파일을 저장할 디렉토리 정보가 입력되지 않았습니다.");
        }

        Date now = new Date();
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

        // 파일 검사
        long fileSize = 0;
        List<CmmtAtchmnfl> atts = attachmentDao.selectList(attachmentGroupId);
        for (CmmtAtchmnfl att : atts) {
        	fileSize += att.getFileSize();
        }

        for (MultipartFile file: multipartFiles) {
            String filename = file.getOriginalFilename();
            if (filename.length() > MAX_FILE_NM_LEN) {
                throw new InvalidationException("파일명은 " + MAX_FILE_NM_LEN + "자를 넘을 수 없습니다.");
            }
            String ext = CoreUtils.filename.getExtension(filename);
            if (allowedFileExts != null && allowedFileExts.length > 0) {
                if (string.isEmpty(ext) || !string.containsIgnoreCase(allowedFileExts, ext)) {
                    throw new InvalidationException("지원하지 않는 파일 확장자가 포함되어 있습니다.(" + ext + ")");
                }
            }
            if (fileSize > Long.MAX_VALUE - file.getSize()) {
            	throw new InvalidationException("파일크기가 최대 크기를 넘어섰습니다.");
            }
            fileSize += file.getSize();
        }

        if (maxFileSize > 0 && fileSize > maxFileSize) {
        	throw new InvalidationException("저장가능한 파일크기(" + string.formatFileSize(maxFileSize) + ")를 초과하였습니다.");
        }

        List<CmmtAtchmnfl> attachments = new ArrayList<>();
        CmmtAtchmnfl attachment = null;

        for (MultipartFile file: multipartFiles) {
            String filename = file.getOriginalFilename();
            String attachmentId = string.getNewId("att-");
            String savedFilePath = dir + "/" + attachmentId;
            try {
            	File nfile = new File(uploadRootDir + savedFilePath);
                file.transferTo(nfile);
    			// Mimetype 확인
                boolean mimeType = mimeTypeUtils.checkMimeType(nfile, filename);
                if (!mimeType) {
                	throw new InvalidationException(String.format("첨부파일(%s)의 내용이 파일형식과 일치하지 않습니다.", filename));
                }
            } catch (IllegalStateException | IOException e) {
                this.logger.error("Fail to save file: " + uploadRootDir + savedFilePath);
                throw new InvalidationException("시스템 문제로 파일을 저장할 수 없습니다.");
            }

            attachment = new CmmtAtchmnfl();
            attachment.setAttachmentGroupId(attachmentGroupId);
            attachment.setAttachmentId(attachmentId);
            attachment.setContentType(file.getContentType());
            attachment.setCreatedDt(now);
            attachment.setCreatorId(user.getMemberId());
            attachment.setSavedFilePath(savedFilePath);
            attachment.setDownloadCnt(0L);
            attachment.setFileDeleted(false);
            attachment.setFileNm(filename);
            attachment.setFileSize(file.getSize());

            attachments.add(attachment);
        }
        attachmentDao.insertList(attachments);
        return attachments;
	}

	/**
	 * 첨부파일그룹에 파일 하나를 새로 추가하기
	 *
	 * @param uploadRootDir			업로드된 파일을 저장할 root directory
	 * @param attachmentGroupId		첨부파일그룹 ID
	 * @param file					업로드된 파일
	 * @param allowedFileExts		저장이 허용된 파일 확장자 array. null 이면 검사하지 않음.
	 * @param maxFileSize			그룹에 허용된 파일 크기 합. 0 이면 검사하지 않음.
	 * @return 저장된 첨부파일 정보
	 */
	public CmmtAtchmnfl uploadFile_toGroup(String uploadRootDir, String attachmentGroupId, MultipartFile file, String[] allowedFileExts, Integer maxFileSize) {
        boolean fileExists = file != null && file.getSize() > 0;
        if (!fileExists) {
            throw new InvalidationException("저장할 파일이 없습니다.");
        }
        if (string.isEmpty(uploadRootDir)) {
            throw new InvalidationException("파일을 저장할 디렉토리 정보가 입력되지 않았습니다.");
        }

        Date now = new Date();
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                CoreUtils.file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

        // 파일 검사
        long fileSize = 0;
        List<CmmtAtchmnfl> atts = attachmentDao.selectList(attachmentGroupId);
        for (CmmtAtchmnfl att : atts) {
        	fileSize += att.getFileSize();
        }

        String filename = file.getOriginalFilename();
        if (filename.length() > MAX_FILE_NM_LEN) {
            throw new InvalidationException("파일명은 " + MAX_FILE_NM_LEN + "자를 넘을 수 없습니다.");
        }

        if (allowedFileExts != null && allowedFileExts.length > 0) {
            String ext = CoreUtils.filename.getExtension(filename);
            if (string.isEmpty(ext) || !string.containsIgnoreCase(allowedFileExts, ext)) {
                throw new InvalidationException("지원하지 않는 파일 확장자가 포함되어 있습니다.(" + ext + ")");
            }
        }
        fileSize += file.getSize();

        if (maxFileSize > 0 && fileSize > maxFileSize) {
        	throw new InvalidationException("저장가능한 파일크기(" + string.formatFileSize(maxFileSize) + ")를 초과하였습니다.");
        }

        List<CmmtAtchmnfl> attachments = new ArrayList<>();
        CmmtAtchmnfl attachment = null;

        String attachmentId = string.getNewId("att-");
        String savedFilePath = dir + "/" + attachmentId;
        try {
        	File nfile = new File(uploadRootDir + savedFilePath);
            file.transferTo(nfile);
			// Mimetype 확인
            boolean mimeType = mimeTypeUtils.checkMimeType(nfile, filename);
            if (!mimeType) {
            	throw new InvalidationException(String.format("첨부파일(%s)의 내용이 파일형식과 일치하지 않습니다.", filename));
            }
        } catch (IllegalStateException | IOException e) {
            this.logger.error("Fail to save file: " + uploadRootDir + savedFilePath);
            throw new InvalidationException("시스템 문제로 파일을 저장할 수 없습니다.");
        }

        attachment = new CmmtAtchmnfl();
        attachment.setAttachmentGroupId(attachmentGroupId);
        attachment.setAttachmentId(attachmentId);
        attachment.setContentType(file.getContentType());
        attachment.setCreatedDt(now);
        attachment.setCreatorId(user.getMemberId());
        attachment.setSavedFilePath(savedFilePath);
        attachment.setDownloadCnt(0L);
        attachment.setFileDeleted(false);
        attachment.setFileNm(filename);
        attachment.setFileSize(file.getSize());

        attachments.add(attachment);

        attachmentDao.insertList(attachments);
        return attachment;
	}

	/**
	 * 파일그룹에 속한 모든 파일정보 구하기
	 *
	 * @param attachmentGroupId		첨부파일그룹 ID
	 * @return						첨부파일 정보 목록
	 */
	public List<CmmtAtchmnfl> getFileInfos_group(String attachmentGroupId) {
		return attachmentDao.selectList(attachmentGroupId);
	}

	/**
	 * 특정 파일 다운로드 하기
	 *
	 * @param response			HttpServletResponse
	 * @param uploadRootDir		업로드된 파일이 저장된 root directory
	 * @param attachmentId		첨부파일 ID
	 */
	public void downloadFile(HttpServletResponse response, String uploadRootDir, String attachmentId) {
		CmmtAtchmnfl attachment = attachmentDao.select(attachmentId);
		if (attachment == null || attachment.getFileDeleted()) {
			throw new InvalidationException("파일이 없습니다.");
		}

		attachmentDao.increaseDownloadCnt(attachmentId);
		File file = new File(uploadRootDir + attachment.getSavedFilePath());
		webutils.downloadFile(response, file, attachment.getFileNm());
	}

	/**
	 * 특정 파일을 contentType으로 다운로드 하기. 주로 image를 출력할 때 사용.
	 *
	 * @param response			HttpServletResponse
	 * @param uploadRootDir		업로드된 파일이 저장된 root directory
	 * @param attachmentId		첨부파일 ID
	 */
	public void downloadFile_contentType(HttpServletResponse response, String uploadRootDir, String attachmentId) {
		CmmtAtchmnfl attachment = attachmentDao.select(attachmentId);
		if (attachment == null || attachment.getFileDeleted()) {
			throw new InvalidationException("파일이 없습니다.");
		}

		attachmentDao.increaseDownloadCnt(attachmentId);
		File file = new File(uploadRootDir + attachment.getSavedFilePath());
		webutils.downloadFile(response, file, attachment.getFileNm(), attachment.getContentType());
	}

	/**
	 * 첨부파일그룹에 속한 파일이 존재하는지 검사
	 * @param attachmentGroupId	첨부파일 그ID
	 * @return					true: 파일이 존재, false: 파일 없음.
	 */
	public boolean existsGroupFiles(String attachmentGroupId ) {
		return attachmentDao.existsGroupFiles(attachmentGroupId);
	}

	/**
	 * 파일 삭제(데이터 + 물리적-파일)
	 * (파일 삭제 후 파일그룹에 해당하는 파일이 없는 경우 파일그룹도 삭제한다.)
	 *
	 * @param uploadRootDir		업로드된 파일이 저장된 root directory
	 * @param attachmentId		첨부파일 ID
	 * @return					true: 파일이 존재, false: 파일이 없음.
	 */
	public boolean removeFile(String uploadRootDir, String attachmentId) {
		CmmtAtchmnfl attachment = attachmentDao.select(attachmentId);
		if (attachment == null) {
			throw new InvalidationException("파일이 없습니다.");
		}
		attachmentDao.delete(attachmentId);

		boolean exists = true;
		if (!string.equals(SINGLE_FILE_GROUP_ID, attachment.getAttachmentGroupId())) {
			exists = attachmentDao.existsGroupFiles(attachment.getAttachmentGroupId());
			if (!exists) {
				attachmentGroupDao.delete(attachment.getAttachmentGroupId());
			}
		}

		File file = new File(uploadRootDir + attachment.getSavedFilePath());
		file.delete();

		return exists;
	}

	/**
	 * 파일 삭제(데이터 + 물리적-파일)
	 * (파일삭제 후 파일그룹에 해당하는 파일이 없어도 파일그룹은 삭제하지 않는다.)
	 *
	 * @param uploadRootDir		업로드된 파일이 저장된 root directory
	 * @param attachmentId		첨부파일 ID
	 */
	public void removeFile_keepEmptyGroup(String uploadRootDir, String attachmentId) {
		CmmtAtchmnfl attachment = attachmentDao.select(attachmentId);
		if (attachment == null) {
			throw new InvalidationException("파일이 없습니다.");
		}
		attachmentDao.delete(attachmentId);

		File file = new File(uploadRootDir + attachment.getSavedFilePath());
		file.delete();
	}

	/**
	 * 파일그룹에 속한 모든 파일 삭제하기(데이터 + 물리적-파일)
	 *
	 * @param uploadRootDir			업로드된 파일이 저장된 root directory
	 * @param attachmentGroupId		첨부파일그룹 ID
	 */
	public void removeFiles_group(String uploadRootDir, String attachmentGroupId) {
		if (string.equals(SINGLE_FILE_GROUP_ID, attachmentGroupId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}
		CmmtAtchmnflGroup group = attachmentGroupDao.select(attachmentGroupId);
		if (group == null) {
			return;
		}

		List<CmmtAtchmnfl> files = attachmentDao.selectList(attachmentGroupId);
		for (CmmtAtchmnfl file : files) {
			attachmentDao.delete(file.getAttachmentId());
			File f = new File(uploadRootDir + file.getSavedFilePath());
			f.delete();
		}
		attachmentGroupDao.delete(attachmentGroupId);
	}

	/**
	 * 첨부파일그룹에 속한 모든 파일의 물리적 파일만 삭제하고, 데이터는 삭제됨으로 수정
	 *
	 * @param uploadRootDir			업로드된 파일이 저장된 root directory
	 * @param attachmentGroupId		첨부파일그룹 ID
	 */
	public void removePhysicalFilesOnly_group(String uploadRootDir, String attachmentGroupId) {
		CmmtAtchmnflGroup group = attachmentGroupDao.select(attachmentGroupId);
		if (group == null) {
			throw new InvalidationException("파일이 없습니다.");
		}
		List<CmmtAtchmnfl> files = attachmentDao.selectList(attachmentGroupId);
		for (CmmtAtchmnfl file : files) {
			if (file.getFileDeleted()) {
				continue;
			}
			File f = new File(uploadRootDir + file.getSavedFilePath());
			if (f.exists()) {
				f.delete();
			}
		}

		attachmentDao.updateRemoved_group(attachmentGroupId);
	}

	/**
	 * 특정 파일의 물리적 파일만 삭제하고, 데이터는 삭제됨으로 수정
	 *
	 * @param uploadRootDir		업로드된 파일이 저장된 root directory
	 * @param attachmentId		첨부파일그룹 ID
	 */
	public void removePhysicalFileOnly(String uploadRootDir, String attachmentId) {
		CmmtAtchmnfl att = attachmentDao.select(attachmentId);
		if (att == null) {
			throw new InvalidationException("파일이 없습니다.");
		}
		if (att.getFileDeleted()) {
			return;
		}
		File f = new File(uploadRootDir + att.getSavedFilePath());
		if (f.exists()) {
			f.delete();
		}
		att.setFileDeleted(true);
		attachmentDao.updateRemoved(attachmentId);
	}

	/**
	 * 첨부파일그룹에 속한 모든 파일의 크기 합
	 *
	 * @param attachmentGroupId		첨부파일그룹 ID
	 * @return						파일 크기 합
	 */
	public long getFileSize_group(String attachmentGroupId) {
		return attachmentDao.selectGroupFileSize(attachmentGroupId);
	}

	/**
	 * 첨부파일그룹에 속한 모든 파일 목록 구하기
	 *
	 * @param uploadRootDir			업로드된 파일이 저장된 root directory
	 * @param attachmentGroupId		첨부파일그룹 ID
	 * @return						첨부파일 목록
	 */
	public List<File> getFiles_group(String uploadRootDir, String attachmentGroupId) {
		List<CmmtAtchmnfl> atts = attachmentDao.selectList(attachmentGroupId);
		List<File> files = new ArrayList<File>();
		for (CmmtAtchmnfl att : atts) {
			File f = new File(uploadRootDir + att.getSavedFilePath());
			if (f.exists()) {
				files.add(f);
			}
		}
		return files;
	}

	/**
	 * 특정 파일그룹의 모든 파일을 새로운 파일그룹으로 복사하기
	 *
	 * @param uploadRootDir			업로드된 파일이 저장된 root directory
	 * @param attachmentGroupId		복사할 첨부파일그룹 ID
	 * @param creatorId				작업자 user_id
	 * @return						복사 결과로 생성된 첨부파일그룹 정보
	 */
	public CmmtAtchmnflGroup copyGroupFiles_toNewGroup(String uploadRootDir, String attachmentGroupId, String creatorId) {
		String groupId = string.getNewId("attg-");
		Date now = new Date();
		CmmtAtchmnflGroup ag = new CmmtAtchmnflGroup();
		ag.setAttachmentGroupId(groupId);
		ag.setCreatorId(creatorId);
		ag.setCreatedDt(now);

		attachmentGroupDao.insert(ag);

		List<CmmtAtchmnfl> list = attachmentDao.selectList(attachmentGroupId);
		if (list.size() == 0) {
			return ag;
		}

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

		List<CmmtAtchmnfl> atts = new ArrayList<CmmtAtchmnfl>();

		for (CmmtAtchmnfl at : list) {
			if (at.getFileDeleted()) {
				continue;
			}

			CmmtAtchmnfl nat = new CmmtAtchmnfl();
			property.copyProperties(nat, at);

			String attachmentId = string.getNewId("att-");
			String savedFilePath = dir + "/" + attachmentId;

			nat.setAttachmentGroupId(groupId);
			nat.setAttachmentId(attachmentId);
			nat.setSavedFilePath(savedFilePath);
			nat.setDownloadCnt(0L);
			nat.setFileDeleted(false);
			nat.setCreatedDt(now);
			nat.setCreatorId(creatorId);

			File f1 = new File(uploadRootDir + at.getSavedFilePath());
			if (!f1.exists()) {
				continue;
			}

			File f2 = new File(uploadRootDir + savedFilePath);
			try {
				file.copyFile(f1, f2);
			} catch (IOException e) {
				logger.error(exception.getStackTraceString(e));
				throw new InvalidationException("파일을 복사하는 중에 오류가 발생하였습니다.");
			}

			atts.add(nat);
		}

		attachmentDao.insertList(atts);
		return ag;
	}

	/**
	 * 그룹에 속하지 않은 파일 하나를 복사해서 새로 등록
	 *
	 * @param uploadRootDir		업로드된 파일이 저장된 root directory
	 * @param attachmentId		복사할 첨부파일 ID
	 * @param creatorId			작업자의 user_id
	 * @return					복사 결과 생성된 첨부파일 정보
	 */
	public CmmtAtchmnfl copyFile_noGroup(String uploadRootDir, String attachmentId, String creatorId) {
		Date now = new Date();
		String groupId = SINGLE_FILE_GROUP_ID;
		CmmtAtchmnflGroup ag = attachmentGroupDao.select(SINGLE_FILE_GROUP_ID);
		if (ag == null) {
			ag = new CmmtAtchmnflGroup();
			ag.setAttachmentGroupId(SINGLE_FILE_GROUP_ID);
			ag.setCreatorId(creatorId);
			ag.setCreatedDt(now);
			attachmentGroupDao.insert(ag);
		}
		CmmtAtchmnfl at = attachmentDao.select(attachmentId);
		if (at == null || at.getFileDeleted()) {
			return null;
		}

        String dir = date.format(now, "/yyyy/MM");
        String fullDir = uploadRootDir + dir;
        try {
            File fdir = new File(fullDir);
            if (!fdir.exists()) {
                file.forceMkdir(new File(fullDir));
            }
        } catch (IOException e) {
            this.logger.error("Fail to create directory '" + fullDir + "'");
            throw new InvalidationException("파일을 저장할 디렉토리를 만들 수 없습니다.");
        }

        CmmtAtchmnfl nat = new CmmtAtchmnfl();
        property.copyProperties(nat, at);

		String newAttachmentId = string.getNewId("att-");
		String savedFilePath = dir + "/" + newAttachmentId;

		nat.setAttachmentGroupId(groupId);
		nat.setAttachmentId(newAttachmentId);
		nat.setSavedFilePath(savedFilePath);
		nat.setDownloadCnt(0L);
		nat.setFileDeleted(false);
		nat.setCreatedDt(now);
		nat.setCreatorId(creatorId);

		File f1 = new File(uploadRootDir + at.getSavedFilePath());
		if (!f1.exists()) {
			return null;
		}

		File f2 = new File(uploadRootDir + savedFilePath);
		try {
			file.copyFile(f1, f2);
		} catch (IOException e) {
			logger.error(exception.getStackTraceString(e));
			throw new InvalidationException("파일을 복사하는 중에 오류가 발생하였습니다.");
		}

		List<CmmtAtchmnfl> list = new ArrayList<CmmtAtchmnfl>();
		list.add(nat);
		attachmentDao.insertList(list);
		return nat;
	}

	/**
	 * 파일그룹에 속한 파일들을 zip 파일로 압축하여 다운로드한다.
	 * @param response HttpServletResponse
	 * @param uploadRootDir	파일 업로드 디렉토리
	 * @param attachmentGroupId 파일그룹ID
	 * @param zipFilename ZIP파일명(확장자는 쓰지 말것)
	 */
	public void downloadGroupFiles(HttpServletResponse response, String uploadRootDir, String attachmentGroupId, String zipFilename) {
		/*
		 * 삭제되지 않은 파일만 조회
		 */
		List<CmmtAtchmnfl> allList = attachmentDao.selectList_notDeleted(attachmentGroupId);
		String[] attachmentIds = new String[allList.size()];
		int i = 0;
		for (CmmtAtchmnfl att : allList) {
			attachmentIds[i++] = att.getAttachmentId();
		}
		downloadFiles(response, uploadRootDir, attachmentIds, zipFilename);
	}

	/**
	 * 여러 개의 파일들을 ZIP파일로 압축하여 다운로드한다.
	 *
	 * @param response HttpServletResponse
	 * @param uploadRootDir 파일 업로드 디렉토리
	 * @param attachmentIds 파일ID Array
	 * @param zipFilename ZIP파일명(확장자는 쓰지 말것)
	 */
	public void downloadFiles(HttpServletResponse response, String uploadRootDir, String[] attachmentIds, String zipFilename) {
		if (attachmentIds == null || attachmentIds.length == 0) {
			throw new InvalidationException("다운로드할 파일을 입력하세요.");
		}

		List<ZipFileInfo> zfis = new ArrayList<ZipFileInfo>();
		List<String> filenames = new ArrayList<String>();

		int cnt = 0;
		for (String attachmentId : attachmentIds) {
			CmmtAtchmnfl att = attachmentDao.select(attachmentId);
			if (att == null) {
				throw new InvalidationException("삭제된 파일이 포함되어 있습니다.");
			}
			if (att.getFileDeleted()) {
				throw new InvalidationException("삭제된 파일이 포함되어 있습니다.(" + att.getFileNm() + ")");
			}

			File file = getFile(uploadRootDir, att.getAttachmentId());
			if (file == null) {
				throw new InvalidationException("파일이 삭제된 것 같습니다. 시스템 담당자에게 문의하십시오.");
			}
			if (!file.exists()) {
				throw new InvalidationException("파일(" + att.getFileNm() + ")이 삭제된 것 같습니다. 시스템 담당자에게 문의하십시오.");
			}

			cnt++;
			String basename = filename.getBaseName(att.getFileNm());
			String ext = filename.getExtension(att.getFileNm());
			int i = 0;
			while( filenames.contains(basename + "." + ext)) {
				i++;
				basename += "(" + i + ")";
			}
			filenames.add(basename + "." + ext);
			ZipFileInfo zfi = new ZipFileInfo("", basename + "." + ext, file);
			zfis.add(zfi);
		}

		if (cnt == 0) {
			throw new InvalidationException("다운로드할 파일이 없습니다.");
		}

		downloadFiles(response, uploadRootDir, zfis, zipFilename);
	}


	private void downloadFiles(HttpServletResponse response, String uploadRootDir, List<ZipFileInfo> zfis, String zipfilename) {
		if (zfis.size() == 0) {
			throw new InvalidationException("다운로드할 파일이 없습니다.");
		}

		// 파일이 1개 일때.
		if (zfis.size() == 1) {
			webutils.downloadFile(response, zfis.get(0).getFile(), zfis.get(0).getFileName());
			return;
		}

		File dir = new File(uploadRootDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File zipfile = new File(uploadRootDir + "/" + string.getNewId("zip_") + ".zip");
		try {
			zip.zipfiles(zfis, zipfile);
		} catch (IOException e) {
			throw new InvalidationException("파일을 압축하는 중에 오류가 발생하였습니다.");
		}

		webutils.downloadFile(response, zipfile, zipfilename + ".zip");
		zipfile.delete();
	}

}
