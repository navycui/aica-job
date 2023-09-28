package aicluster.common.api.search.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.search.dto.ArkParam;
import aicluster.common.api.search.dto.PopwordParam;
import aicluster.common.api.search.dto.RecommendParam;
import aicluster.common.api.search.dto.SearchParam;
import aicluster.common.api.search.dto.SearchPopwordDto;
import aicluster.common.api.search.dto.SearchRecommendResultDto;
import aicluster.common.api.search.dto.SearchResultDto;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.common.config.SearchConfig;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchService {

	@Autowired
	private SearchConfig config;

	private void checkSearchSystem() {
		/*
		 * Unirest 재설정
		 */
		Unirest.config().reset();
		Unirest.config().connectTimeout(config.getConnectionTimeout());
		Unirest.config().socketTimeout(config.getSocketTimeout());

		/*
		 * 통합검색 서버 통신 체크
		 */
		HttpResponse<String> res = Unirest.get(config.getSearchUrl()).asString();
		if (res.getStatus() != 200) {
			log.info("-----------------");
			log.info("URL:" + config.getSearchUrl());
			log.info("status:" + res.getStatus());
			log.info("-----------------");
			throw new InvalidationException("통합검색 서버와 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
		}
		else {
			log.debug("-----------------");
			log.debug("URL:" + config.getSearchUrl());
			log.debug("status:" + res.getStatus());
			log.debug("body:\n" + res.getBody());
			log.debug("-----------------");
		}
	}

	private SearchResultDto getResponseBody(String url, Map<String, Object> queryParams)
	{
	 	// HTTP 호출
    	HttpResponse<String> res = Unirest.get(url)
    			.queryString(queryParams)
    			.asString();

    	// HTTP 오류 처리
    	if (res.getStatus() != 200) {
    		String errs = res.getBody();
    		log.error(errs);
    		throw new InvalidationException("통합검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
    	}

    	String body = res.getBody();
    	if(string.isBlank(body)) {
    		throw new InvalidationException("통합검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
    	}
    	SearchResultDto jsonBody = json.toObject(body, SearchResultDto.class);
    	log.debug(jsonBody.toString());

    	if (jsonBody.getErrorCode() != 0 || string.equals(jsonBody.getStatus(), "fail")) {
    		log.error(String.format("[%d] => %s", jsonBody.getErrorCode(), jsonBody.getErrorMsg()));
    		throw new InvalidationException("통합검색 결과 오류가 발생되었습니다.\\n관리자에게 문의하세요.");
    	}

    	return jsonBody;
	}
	
	private SearchPopwordDto getResponsePopwordBody(String url, Map<String, Object> queryParams)
	{
	 	// HTTP 호출
    	HttpResponse<String> res = Unirest.get(url)
    			.queryString(queryParams)
    			.asString();

    	// HTTP 오류 처리
    	if (res.getStatus() != 200) {
    		String errs = res.getBody();
    		log.error(errs);
    		throw new InvalidationException("통합검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
    	}

    	String body = res.getBody();
    	if(string.isBlank(body)) {
    		throw new InvalidationException("통합검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
    	}
    	SearchPopwordDto jsonBody = json.toObject(body, SearchPopwordDto.class);
    	log.debug(jsonBody.toString());

    	if (jsonBody.getErrorCode() != 0 || string.equals(jsonBody.getStatus(), "fail")) {
    		log.error(String.format("[%d] => %s", jsonBody.getErrorCode(), jsonBody.getErrorMsg()));
    		throw new InvalidationException("통합검색 결과 오류가 발생되었습니다.\\n관리자에게 문의하세요.");
    	}

    	return jsonBody;
	}
	
	private SearchRecommendResultDto getResponseRecommendBody(String url, Map<String, Object> queryParams)
	{
	 	// HTTP 호출
    	HttpResponse<String> res = Unirest.get(url)
    			.queryString(queryParams)
    			.asString();

    	// HTTP 오류 처리
    	if (res.getStatus() != 200) {
    		String errs = res.getBody();
    		log.error(errs);
    		throw new InvalidationException("통합검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
    	}

    	String body = res.getBody();
    	if(string.isBlank(body)) {
    		throw new InvalidationException("통합검색 API 통신이 원할하지 않습니다.\\n관리자에게 문의하세요.");
    	}
    	SearchRecommendResultDto jsonBody = json.toObject(body, SearchRecommendResultDto.class);
    	log.debug(jsonBody.toString());

    	if (jsonBody.getErrorCode() != 0 || string.equals(jsonBody.getStatus(), "fail")) {
    		log.error(String.format("[%d] => %s", jsonBody.getErrorCode(), jsonBody.getErrorMsg()));
    		throw new InvalidationException("통합검색 결과 오류가 발생되었습니다.\\n관리자에게 문의하세요.");
    	}

    	return jsonBody;
	}
	
	public SearchResultDto search(SearchParam param)
	{
		checkSearchSystem();

		// 입력 필수 검증
		if (string.isBlank(param.getSearchFlag())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색구분"));
		}
		if (string.equals("search", param.getSearchFlag()) && string.isBlank(param.getQuery())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색어"));
		}

		// HTTP 통신 Parameter
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("searchFlag", param.getSearchFlag());
		queryParams.put("query", param.getQuery());
		if (string.isNotBlank(param.getSystem())) {
			queryParams.put("system", param.getSystem());
		}
		if (string.isNotBlank(param.getCollection())) {
			queryParams.put("collection", param.getCollection());
		}
		if (string.isNotBlank(param.getRequery())) {
			queryParams.put("requery", param.getRequery());
		}
		if (param.getStartCount() != null) {
			queryParams.put("startCount", param.getStartCount());
		}
		if (param.getListCount() != null) {
			queryParams.put("listCount", param.getListCount());
		}

		log.info("HTTP Parameter : ["+json.toString(queryParams)+"]");

    	return getResponseBody(config.getSearchApiUrl(), queryParams);
	}

	public SearchResultDto recommandList(SearchParam param)
	{
		checkSearchSystem();

		// 입력 필수 검증
		if (string.isBlank(param.getSearchFlag())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색구분"));
		}
		if (string.isBlank(param.getQuery())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색어"));
		}

		// HTTP 통신 Parameter
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("searchFlag", param.getSearchFlag());
		queryParams.put("query", param.getQuery());
		if (string.isNotBlank(param.getCollection())) {
			queryParams.put("collection", param.getCollection());
		}
		if (param.getStartCount() != null) {
			queryParams.put("startCount", param.getStartCount());
		}
		if (param.getListCount() != null) {
			queryParams.put("listCount", param.getListCount());
		}

		log.info("HTTP Parameter : ["+json.toString(queryParams)+"]");

    	return getResponseBody(config.getRecommandListApiUrl(), queryParams);
	}

	public SearchResultDto recommandDetail(SearchParam param)
	{
		checkSearchSystem();

		// 입력 필수 검증
		if (string.isBlank(param.getSearchFlag())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색구분"));
		}

		// HTTP 통신 Parameter
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("searchFlag", param.getSearchFlag());
		if (string.isNotBlank(param.getCollection())) {
			queryParams.put("collection", param.getCollection());
		}
		if (string.isNotBlank(param.getExQuery())) {
			queryParams.put("exQuery", param.getExQuery());
		}

		log.info("HTTP Parameter : ["+json.toString(queryParams)+"]");

    	return getResponseBody(config.getRecommandDtlApiUrl(), queryParams);
	}

	/**
	 * 자동완성
	 * @param param
	 * @return
	 */
	public SearchResultDto ark(ArkParam param) {

		// 입력 필수 검증
		if (string.isBlank(param.getQuery())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색어"));
		}
		
		// HTTP 통신 Parameter
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("target", param.getTarget());
		queryParams.put("query", param.getQuery());
		queryParams.put("convert", param.getConvert());
		queryParams.put("charset", param.getCharset());
		queryParams.put("datatype", param.getDatatype());

		log.info("ARK HTTP Parameter : ["+json.toString(queryParams)+"]");

    	return getResponseBody(config.getArkApiUrl(), queryParams);
	}

	/**
	 * 인기검색어
	 * @param param
	 * @return
	 */
	public SearchPopwordDto popword(PopwordParam param) {
		// 입력 필수 검증
		if (string.isBlank(param.getRange())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "조회 범위"));
		}
				
		// HTTP 통신 Parameter
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("target", param.getTarget());
		queryParams.put("collection", param.getCollection());
		queryParams.put("range", param.getRange());
		queryParams.put("datatype", param.getDatatype());

		log.info("POPWORD HTTP Parameter : ["+json.toString(queryParams)+"]");

    	return getResponsePopwordBody(config.getPopwordApiUrl(), queryParams);
	}
	
	/**
	 * 추천 검색어
	 * @param param
	 * @return
	 */
	public SearchRecommendResultDto recommend(RecommendParam param) {
		// 입력 필수 검증
		if (string.isBlank(param.getQuery())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "검색어"));
		}
				
		String target = string.isBlank(param.getTarget()) ? "recommend" : param.getTarget();
		String label = string.isBlank(param.getLabel()) ? "_ALL_" : param.getLabel();
		String datatype = string.isBlank(param.getDatatype()) ? "json" : param.getDatatype();
		
		// HTTP 통신 Parameter
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("target", target);
		queryParams.put("label", label);
		queryParams.put("datatype", datatype);
		queryParams.put("query", param.getQuery());

		log.info("POPWORD HTTP Parameter : ["+json.toString(queryParams)+"]");

    	return getResponseRecommendBody(config.getRecommendApiUrl(), queryParams);
	}
	
}
