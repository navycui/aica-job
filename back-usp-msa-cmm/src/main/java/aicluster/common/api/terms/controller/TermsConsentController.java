package aicluster.common.api.terms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.terms.dto.TermsConsentParam;
import aicluster.common.api.terms.service.TermsConsentService;
import aicluster.framework.common.dto.SessionKeyDto;

@RestController
@RequestMapping("api/terms-consent")
public class TermsConsentController {

	@Autowired
	TermsConsentService service;

	@PostMapping("/session")
	public SessionKeyDto saveSession(@RequestBody List<TermsConsentParam> param) {
		return service.saveSession(param);
	}
}
