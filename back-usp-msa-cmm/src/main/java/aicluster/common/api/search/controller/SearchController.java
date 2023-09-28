package aicluster.common.api.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.search.dto.ArkParam;
import aicluster.common.api.search.dto.PopwordParam;
import aicluster.common.api.search.dto.RecommendParam;
import aicluster.common.api.search.dto.SearchParam;
import aicluster.common.api.search.dto.SearchPopwordDto;
import aicluster.common.api.search.dto.SearchRecommendResultDto;
import aicluster.common.api.search.dto.SearchResultDto;
import aicluster.common.api.search.service.SearchService;

@RestController
@RequestMapping("api")
public class SearchController {
	Logger log = LoggerFactory.getLogger("SearchController");
	
	@Autowired
	private SearchService service;

	/**
	 * 통합검색 조회
	 * @param param
	 * @return
	 */
	@GetMapping("search")
	SearchResultDto search(SearchParam param) {
		return service.search(param);
	}

	/**
	 * 통합검색 유사문서 목록검색
	 * @param param
	 * @return
	 */
	@GetMapping("recommandSearchList")
	SearchResultDto recommandList(SearchParam param) {
		return service.recommandList(param);
	}

	/**
	 * 통합검색 유사문서 문서검색
	 * @param param
	 * @return
	 */
	@GetMapping("recommandSearch")
	SearchResultDto recommandDetail(SearchParam param) {
		return service.recommandDetail(param);
	}
	
	/**
	 * 자동완성
	 * @param param
	 * @return
	 */
	@GetMapping("ark")
	SearchResultDto ark(ArkParam param) {
		return service.ark(param);
	}
	
	/**
	 * 인기검색어
	 * @param param
	 * @return
	 */
	@GetMapping("popword")
	SearchPopwordDto popword(PopwordParam param) {
		return service.popword(param);
	}
	
	/**
	 * 추천검색어
	 * @param param
	 * @return
	 */
	@GetMapping("recommend")
	SearchRecommendResultDto recommend(RecommendParam param) {
		return service.recommend(param);
	}
}
