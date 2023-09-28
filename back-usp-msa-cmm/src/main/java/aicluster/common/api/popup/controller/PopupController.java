package aicluster.common.api.popup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.popup.dto.PopupGetListParam;
import aicluster.common.api.popup.service.PopupService;
import aicluster.common.common.dto.PopupListItem;
import aicluster.common.common.entity.CmmtPopupNotice;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.RequiredArgsConstructor;

//@Slf4j
@RestController
@RequestMapping("api/popups")
@RequiredArgsConstructor
public class PopupController {

	@Autowired
	private PopupService popupService;

	/**
	 * 팝업공지 목록 조회
	 *
	 * @param param: 검색조건(systemId, posting, title, beginDt, endDt)
	 * @param pageParam
	 * @return
	 */
	@GetMapping("")
	public CorePagination<PopupListItem> getList(PopupGetListParam param, CorePaginationParam pageParam) {
		return popupService.getlist(param, pageParam);
	}

	/**
	 * 팝업공지 등록
	 *
	 * @param popup
	 * @param image
	 * @return
	 */
	@PostMapping("")
	public CmmtPopupNotice add(
				@RequestPart(value = "popup", required = true) CmmtPopupNotice popup,
				@RequestPart(value = "image", required = false) MultipartFile image) {
		CmmtPopupNotice cmmtPopup = popupService.addPopup(popup, image);
		return cmmtPopup;
	}

	/**
	 * 팝업공지 조회
	 *
	 * @param popupId
	 * @return
	 */
	@GetMapping("/{popupId}")
	public CmmtPopupNotice get(@PathVariable String popupId) {
		CmmtPopupNotice cmmtPopup = popupService.getPopup(popupId);
		return cmmtPopup;
	}

	/**
	 * 팝업공지 수정
	 *
	 * @param popupId
	 * @param popup
	 * @param image
	 * @return
	 */
	@PutMapping("/{popupId}")
	public CmmtPopupNotice modify(
				@PathVariable String popupId,
				@RequestPart(value = "popup", required = true) CmmtPopupNotice popup,
				@RequestPart(value = "image", required = false) MultipartFile image) {
		popup.setPopupId(popupId);
		CmmtPopupNotice cmmtPopup = popupService.modifyPopup(popup, image);
		return cmmtPopup;
	}

	/**
	 * 팝업공지 삭제
	 *
	 * @param popupId
	 */
	@DeleteMapping("/{popupId}")
	public void remove(@PathVariable String popupId) {
		popupService.removePopup(popupId);
	}

	/**
	 * 지금 유효한 팝업공지 ID 목록 조회
	 *
	 * @param systemId
	 * @return
	 */
	@GetMapping("/now/{systemId}")
	public JsonList<CmmtPopupNotice> getTodayList(@PathVariable String systemId) {
		return popupService.getTodayList(systemId);
	}

	/**
	 * 팝업공지 게시상태변경
	 *
	 * @param popupId
	 * @param posting
	 * @return
	 */
	@PutMapping("/{popupId}/posting")
	public CmmtPopupNotice modifyPosting(@PathVariable String popupId, Boolean posting) {
		CmmtPopupNotice cmmtPopup = popupService.modifyStatus(popupId, posting);
		return cmmtPopup;
	}

}
