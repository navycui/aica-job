package aicluster.member.api.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.member.api.module.dto.PkiResultParam;
import aicluster.member.api.module.service.PkiCertService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/contact-module/pki-cert")
public class PkiCertController {

	@Autowired
	private PkiCertService service;

	@PostMapping("/save-signed")
	public SessionKeyDto saveSigned(@RequestBody PkiResultParam param)
	{
		log.debug(param.toString());
		return service.saveSigned(param);
	}

}
