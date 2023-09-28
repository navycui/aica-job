package aicluster.tsp.api.front.usereqst.resrce.controller;

import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.common.param.CommonReturn;
import aicluster.tsp.api.front.usereqst.resrce.param.UseReqstResrceParam;
import aicluster.tsp.api.front.usereqst.resrce.service.UseReqstResrceService;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.entity.TsptResrceUseReqst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/front/use-reqst/resrce")
@RequiredArgsConstructor
@Api(tags = "Front > 실증자원신청")

public class UseReqstResrceController {

    @Autowired
    private UseReqstResrceService service;

    @Autowired
    private CommonService commonService;


    @ApiOperation(value = "신청자 정보", notes = "신청자 정보 조회")
    @GetMapping("")
    public ApplcntDto getApplcnt() {
        return commonService.getApplcnt();
    }

    @ApiOperation(value = "실증자원 신청 정보 입력 ", notes = "실증자원 신청 정보 입력")
    @PostMapping("")
    public TsptResrceUseReqst putUseReqstResrce(@RequestBody UseReqstResrceParam param){
        return service.putUseReqstResrce(param);
    }

    @ApiOperation(value = "파일 한개 업로드 - New Group", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>attachment_group을 새로 만들고 cmmt_attachment 테이블에 데이터를 저장")
    @PostMapping("/upload-new-group")
    public CommonReturn<String> uploadFile_toNewGroup(@RequestPart(value = "attachment", required = false) MultipartFile attachment) {
        return new CommonReturn<>(service.uploadFile_toNewGroup(attachment));
    }
}
