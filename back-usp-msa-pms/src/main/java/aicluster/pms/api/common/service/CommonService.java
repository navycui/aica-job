package aicluster.pms.api.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.DocsConfig;
import aicluster.framework.config.EnvConfig;
import aicluster.pms.api.common.dto.StreamDocsResponseDto;
import aicluster.pms.common.dao.UsptBsnsDao;
import aicluster.pms.common.dao.UsptBsnsPblancAppnTaskDao;
import aicluster.pms.common.dto.BsnsNmDto;
import aicluster.pms.common.entity.UsptBsnsPblancAppnTask;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import kong.unirest.HttpResponse;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CommonService {

	@Autowired
	private EnvConfig config;
	@Autowired
	private DocsConfig docsConfig;
	@Autowired
	private UsptBsnsDao usptBsnsDao;
	@Autowired
	private UsptBsnsPblancAppnTaskDao usptBsnsPblancAppnTaskDao;
	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 사업연도 목록 조회
	 * @return
	 */
	public List<String> getBsnsYearList(){
		List<String> list = usptBsnsDao.selectBsnsYearList();
		if(list.isEmpty()) {
			list.add(CoreUtils.date.getCurrentDate("yyyy"));
		}
		return list;
	}

	/**
	 * 사업명 목록 조회
	 * @param bsnsYear
	 * @return
	 */
	public List<BsnsNmDto> getBsnsNmList(String bsnsYear){
		return usptBsnsDao.selectBsnsNmList(bsnsYear);
	}

	/**
	 * 공고 지원분야 목록 조회
	 * @return
	 */
	public List<UsptBsnsPblancAppnTask> getAppnTaskList(){
		return usptBsnsPblancAppnTaskDao.selectAppnTaskList();
	}


	/**
	 * pdf view
	 * @param fileInfo
	 */
	public StreamDocsResponseDto getPdfView(CmmtAtchmnfl fileInfo) {
		if(fileInfo == null || CoreUtils.string.isEmpty(fileInfo.getAttachmentId())) {
			throw new InvalidationException("요청한 파일 정보가 없습니다.");
		}

		fileInfo = attachmentService.getFileInfo(fileInfo.getAttachmentId());
		if(fileInfo == null) {
			throw new InvalidationException("요청한 파일 정보가 없습니다.");
		}

		if(fileInfo.getFileDeleted()) {
			throw new InvalidationException("삭제된 파일입니다.");
		}

		String ext = CoreUtils.filename.getExtension(fileInfo.getFileNm());
		if(!CoreUtils.string.equalsIgnoreCase("pdf", ext)) {
			throw new InvalidationException("PDF 파일이 아닙니다.");
		}

		String documentsUri = docsConfig.getUrl() + "/v4/documents";
		log.info("##### streamdocs url : " + documentsUri);

		FileInputStream is = null;
		try {
			File pdfFile = new File(config.getDir().getUpload() + fileInfo.getSavedFilePath());
			is = new FileInputStream(pdfFile);
			log.info("##### File path  : " + pdfFile.getAbsolutePath());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
			body.add("pdf", new MultipartInputStreamFileResource(is, fileInfo.getFileNm()));

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
			log.info("new RestTemplate().postForEntity Call");
			 ResponseEntity<String> response = new RestTemplate().postForEntity(documentsUri, requestEntity, String.class);
			log.info("RestTemplate response body: {}", response.getBody());
			StreamDocsResponseDto result = CoreUtils.json.toObject(response.getBody(), StreamDocsResponseDto.class);

			log.info("json result: {}", result);

			return result;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new InvalidationException("PDF Viewer 요청 중 오류가 발생했습니다.");
		} finally {
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}


	class MultipartInputStreamFileResource extends InputStreamResource {
		private final String filename;
		MultipartInputStreamFileResource(InputStream inputStream, String filename) {
			super(inputStream);
			this.filename = filename;
		}

		@Override
		public String getFilename() {
			return this.filename;
		}

		@Override
		public long contentLength() throws IOException {
			return -1; // we do not want to generally read the whole stream into memory ...
		}
	}
}
