package bnet.library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MimeTypeUtils {
	private Map<String, String> mimeTypeMap = new HashMap<>();
	private Tika tika = new Tika();

	public MimeTypeUtils() {
		init();
	}

	private void init() {
		// hwp
		mimeTypeMap.put("hwp", "application/x-tika-msoffice");

		// docx
		mimeTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

		// doc
		mimeTypeMap.put("doc", "application/msword");

		// pptx
		mimeTypeMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

		// ppt
		mimeTypeMap.put("ppt", "application/vnd.ms-powerpoint");

		// xlsx
		mimeTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		// xls
		mimeTypeMap.put("xls", "application/vnd.ms-excel");

		// pdf
		mimeTypeMap.put("pdf", "application/pdf");

		// jpg
		mimeTypeMap.put("jpg", "image/jpeg");

		// jpeg
		mimeTypeMap.put("jpeg", "image/jpeg");

		// png
		mimeTypeMap.put("png", "image/png");

		// gif
		mimeTypeMap.put("gif", "image/gif");
	}

	public String getMimeType(File file, String filename) {
		String mimeType = null;
		try (FileInputStream stream = new FileInputStream(file)) {
			mimeType = tika.detect(stream, filename);
		} catch (IOException e) {
			log.error("MimeTypeUtils.getMimeType error");
		}
		return mimeType;
	}

	public boolean checkMimeType(File file, String filename) {
		String mimeType1 = getMimeType(file, filename);
		if (string.isBlank(mimeType1)) {
			return false;
		}
		String ext = CoreUtils.filename.getExtension(filename);
		if (string.isBlank(ext)) {
			return false;
		}
		ext = string.lowerCase(ext);
		String mimeType2 = this.mimeTypeMap.get(ext);
		if (string.isBlank(mimeType2)) {
			return false;
		}
		return string.equalsIgnoreCase(mimeType1, mimeType2);
	}

//	public static void main(String[] args) {
//		MimeTypeUtils utils = new MimeTypeUtils();
//		String filename = "aaa.PDF";
//		File file = new File("/Users/patrick/dev/temp/aaa.pdf");
//		String mimeType = utils.getMimeType(file, filename);
//		System.out.println(mimeType);
//		System.out.println(utils.checkMimeType(file, filename));
//	}
}
