package aicluster.member.api.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.util.NiceIdUtils;
import aicluster.framework.common.util.dto.NiceIdConfig;
import aicluster.framework.common.util.dto.NiceIdEncDataParam;
import aicluster.member.api.module.dto.NiceIdEncData;

@RestController
@RequestMapping("/api/contact-module/mobile-cert")
public class MobileCertController {

	@Autowired
	private NiceIdConfig niceIdConfig;

	@Autowired
	private NiceIdUtils niceIdUtils;

	/**
	 * 휴대폰 본인인증 모듈 초기화
	 * @param param
	 * @return
	 */
	@PostMapping("/init")
	public NiceIdEncData init(@RequestBody NiceIdEncDataParam param) {
		String encData = niceIdUtils.getEncData(niceIdConfig, param);
		return new NiceIdEncData(encData);
	}
}
