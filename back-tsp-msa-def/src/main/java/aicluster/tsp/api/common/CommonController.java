package aicluster.tsp.api.common;

import aicluster.tsp.api.common.param.*;
import aicluster.tsp.common.dto.ApplcntDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = "공통")
public class CommonController {
    @Autowired
    private CommonService service;

    @ApiOperation(value = "Code 조회", notes = "코드 리스트를 조회합니다.")
    @GetMapping("/code")
    public CommonCodeParam getCodeList(@RequestParam(value = "codeGroup") List<String> list) {
        return service.getcodelist(list);
    }

    @ApiOperation(value = "첨부 파일 한개 다운로드 - contentType", notes = "attachmentId 로 파일을 contentType 에 맞게 해서 다운로드 한다.")
    @GetMapping("/file-dwld-contentType/{attachmentId}")
    public void downloadFile_contentType(HttpServletResponse response, @PathVariable String attachmentId) {
        service.downloadFile_contentType(response, attachmentId);
    }

    @ApiOperation(value = "첨부 파일 그룹 다운로드", notes = "attachmentGroupId 로 해서 해당 파일들을 전부 다운로드 한다.")
    @GetMapping("/file-dwld-groupfiles/{attachmentGroupId}")
    public void fileDownload_groupfiles(HttpServletResponse response, @PathVariable String attachmentGroupId) {
        service.fileDownload_groupfiles(response, attachmentGroupId);
    }

    @ApiOperation(value = "파일 한개 업로드 - Group(추가업로드)", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>attachment_group_id로 cmmt_attachment 테이블에 데이터를 저장")
    @PostMapping("/upload-group/{attachmentGroupId}")
    public void uploadFile_toGroup(@RequestPart(value = "attachment", required = false) MultipartFile attachment, @PathVariable String attachmentGroupId) {
        service.uploadFile_toGroup(attachment, attachmentGroupId);
    }

    @ApiOperation(value = "파일 여러개 업로드 - Group(추가업로드)", notes = "파일 여러 업로드 샘플 코드 입니다.<br/>attachment_group_id로 cmmt_attachment 테이블에 데이터를 저장")
    @PostMapping("/uploads-group/{attachmentGroupId}")
    public void uploadFiles_toGroup(@RequestPart(value = "attachment", required = false) List<MultipartFile> attachments, @PathVariable String attachmentGroupId) {
        service.uploadFiles_toGroup(attachments, attachmentGroupId);
    }

    @ApiOperation(value = "파일 한개 업로드 - New Group", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>attachment_group을 새로 만들고 cmmt_attachment 테이블에 데이터를 저장")
    @PostMapping("/upload-new-group")
    public CommonReturn<String> uploadFile_toNewGroup(@RequestPart(value = "attachment", required = false) MultipartFile attachment) {
        return new CommonReturn<>(service.uploadFile_toNewGroup(attachment));
    }

    @ApiOperation(value = "파일 여러개 업로드 - New Group", notes = "파일 여러개 업로드 샘플 코드 입니다.<br/>attachment_group을 새로 만들고 cmmt_attachment 테이블에 데이터를 저장")
    @PostMapping("/uploads-new-group")
    public CommonReturn<String> uploadFiles_toNewGroup(@RequestPart(value = "attachment", required = false) List<MultipartFile> attachment) {
        return new CommonReturn<>(service.uploadFiles_toNewGroup(attachment));
    }

    @ApiOperation(value = "장비 분류 조회", notes = "장비 분류 조회")
    @GetMapping("/cl-list")
    public CommonReturnList getCodeList() {
        return service.eqpmnClList();
    }


    @ApiOperation(value = "신청자 정보", notes = "신청자 정보 조회")
    @GetMapping("/applcnt")
    public ApplcntDto getApplcnt() {
        return service.getApplcnt();
    }

    @ApiOperation(value = "신청자 정보 수정", notes = "신청자 정보 조회")
    @PutMapping("/applcnt")
    public void putApplcnt(@RequestBody CommonApplcntParam param) {
        service.putApplcnt(param);
    }

    @ApiOperation(value = "신청자 가입?", notes = "신청자 정보 조회")
    @PostMapping("/applcnt")
    public void postApplcnt(@RequestBody CommonApplcntParam param) {
        service.postApplcnt(param);
    }

    @ApiOperation(value = "휴일 조회", notes = "휴일 조회")
    @GetMapping("/ymd")
    public CommonReturnList<String> getYmd() {
        return new CommonReturnList<>(service.getYmd());
    }

    @ApiOperation(value = "첨부 파일 한개 정보 가져오기", notes = "attachmentId 로 파일의 정보를 가져온다.")
    @GetMapping("/atchmnfl/{atchmnflId}")
    public CommonAttachmentParam getAttachment(@PathVariable String atchmnflId) {
        return service.getAttachment(atchmnflId);
    }

    @ApiOperation(value = "첨부 파일 한개 정보 가져오기(groupId)", notes = "attachmentGroupId 로 파일1개의 정보를 가져온다.")
    @GetMapping("/atchmnflInfo/{atchmnflGroupId}")
    public CommonAttachmentParam getAttachmentInfoGroup(@PathVariable String atchmnflGroupId) {
        return service.getAttachmentInfoGroup(atchmnflGroupId);
    }

    @ApiOperation(value = "첨부 파일 여러개 정보 가져오기", notes = "attachmentGroupId 로 파일의 정보를 가져온다.")
    @GetMapping("/atchmnfl-group/{atchmnflGroupId}")
    public List<CommonAttachmentParam> getAttachmentGroup(@PathVariable String atchmnflGroupId) {
        return service.getAttachmentGroup(atchmnflGroupId);
    }

    @ApiOperation(value = "장비 사용상태 변경 ", notes = "신청자 정보 조회")
    @PutMapping("/eqpmn-use-sttus")
    public void putEqpmnUseSttus(@RequestBody CommonEqpmnUseSttusParam param) {
        service.putEqpmnUseSttus(param);
    }

    @ApiOperation(value = "첨부파일 삭제(DB, 파일)", notes = "첨부파일 삭제(DB, 파일)")
    @DeleteMapping("/atchmnfl/{atchmnflId}")
    public void removeFile(@PathVariable String atchmnflId) {
        service.removeFile(atchmnflId);
    }

}