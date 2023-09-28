package aicluster.common.api.event.controller;

import java.util.List;

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

import aicluster.common.api.event.dto.EventGetListParam;
import aicluster.common.api.event.service.EventService;
import aicluster.common.common.dto.EventListItem;
import aicluster.common.common.entity.CmmtEvent;
import aicluster.common.common.entity.CmmtEventCn;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/events")
public class EventController {

	@Autowired
	private EventService eventService;

	/**
	 * 이벤트 목록 조회
	 *
	 * @param posting
	 * @param eventNm
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("")
	public CorePagination<EventListItem> getList(EventGetListParam param, CorePaginationParam pageParam) {
		return eventService.getList(param, pageParam);
	}

	/**
	 * 이벤트 등록
	 *
	 * @param event
	 * @param eventCnList
	 * @param pcThumbnailFile
	 * @param mobileThumbnailFile
	 * @param pcImageFile
	 * @param mobileImageFile
	 * @param attachment
	 * @return
	 */
	@PostMapping("")
	public CmmtEvent add(
			@RequestPart(value = "event", required = true) CmmtEvent event,
			@RequestPart(value = "eventCnList", required = false) List<CmmtEventCn> eventCnList,
			@RequestPart(value = "pcThumbnailFile", required = false) MultipartFile pcThumbnailFile,
			@RequestPart(value = "mobileThumbnailFile", required = false) MultipartFile mobileThumbnailFile,
			@RequestPart(value = "pcImageFile", required = false) MultipartFile pcImageFile,
			@RequestPart(value = "mobileImageFile", required = false) MultipartFile mobileImageFile,
			@RequestPart(value = "attachment", required = false) List<MultipartFile> attachment) {
		return eventService.addEvent(event, eventCnList, pcThumbnailFile, mobileThumbnailFile, pcImageFile, mobileImageFile, attachment);
	}

	/**
	 * 이벤트 상세 조회
	 * @param eventId
	 * @return
	 */
	@GetMapping("/{eventId}")
	public CmmtEvent get(@PathVariable String eventId) {
		return eventService.getEvent(eventId);
	}

	/**
	 * 이벤트 수정
	 *
	 * @param eventId
	 * @param event
	 * @param eventCnList
	 * @param pcThumbnailFile
	 * @param mobileThumbnailFile
	 * @param pcImageFile
	 * @param mobileImageFile
	 * @param attachment
	 * @return
	 */
	@PutMapping("/{eventId}")
	public CmmtEvent modify(
			@PathVariable String eventId,
			@RequestPart(value = "event", required = true) CmmtEvent event,
			@RequestPart(value = "eventCnList", required = false) List<CmmtEventCn> eventCnList,
			@RequestPart(value = "pcThumbnailFile", required = false) MultipartFile pcThumbnailFile,
			@RequestPart(value = "mobileThumbnailFile", required = false) MultipartFile mobileThumbnailFile,
			@RequestPart(value = "pcImageFile", required = false) MultipartFile pcImageFile,
			@RequestPart(value = "mobileImageFile", required = false) MultipartFile mobileImageFile,
			@RequestPart(value = "attachment", required = false) List<MultipartFile> attachment) {
		event.setEventId(eventId);
		return eventService.modifyEvent(event, eventCnList, pcThumbnailFile, mobileThumbnailFile, pcImageFile, mobileImageFile, attachment);
	}

	/**
	 * 이벤트 삭제
	 *
	 * @param eventId
	 */
	@DeleteMapping("/{eventId}")
	public void remove(@PathVariable String eventId) {
		eventService.removeEvent(eventId);
	}

	/**
	 * 이벤트 게시상태 변경
	 *
	 * @param eventId
	 * @param posting
	 * @return
	 */
	@PutMapping("/{eventId}/posting")
	public CmmtEvent modifyPosting(@PathVariable String eventId, Boolean posting) {
		return eventService.modifyStatus(eventId, posting);
	}
}
