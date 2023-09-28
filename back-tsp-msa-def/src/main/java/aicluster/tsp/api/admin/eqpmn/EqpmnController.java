package aicluster.tsp.api.admin.eqpmn;

import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.common.param.CommonReturn;
import aicluster.tsp.common.dto.EqpmnAllListDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/admin/eqpmns")
@RequiredArgsConstructor
@Api(tags = "장비관리")
public class EqpmnController {
    @Autowired
    private EqpmnService service;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EnvConfig config;

//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK", response = EqpmnAllListDto.class)
//    })
    @ApiOperation(value = "장비 목록 조회", notes = "장비 목록 리스트를 조회합니다.")
    @GetMapping("")
    public CorePagination<EqpmnAllListDto> getList(EqpmnAllListParam eqpmnAllListParam, CorePaginationParam cpParam){
        return service.getlist(eqpmnAllListParam, cpParam);
    }

    @ApiOperation(value = "장비 목록 엑셀 다운로드", notes = "장비 목록 리스트중 조회된 리스트를 엑셀로 다운로드합니다.")
    @GetMapping("/excel-dwld")
    public ModelAndView getListExcelDwld(EqpmnAllListParam eqpmnAllListParam){
    	return service.getListExcelDwld(eqpmnAllListParam);
    }

    @GetMapping("/{imageFileId}/image")
    public void downloadImage(HttpServletResponse response, @PathVariable("imageFileId") String imageFileId) {
        attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), imageFileId);
    }
//
    // 장비 추가
    @PostMapping("")
    @ApiOperation(value = "장비 추가", notes = "신규 장비 추가")
    public EqpmnParam insert(@RequestBody(required = false) EqpmnParam param){
        return service.insert(param);
    }
//
    @ApiOperation(value = "장비 이미지 업로드", notes = "장비 이미지 업로드")
    @PostMapping("/image-upload")
    public CommonReturn<String> imageUpload(@RequestPart(value = "image", required = false) MultipartFile image) {
        return new CommonReturn<>(service.imageUpload(image));
    }
//
//    // 장비 삭제
//    @DeleteMapping("/{id}")
//    public void add(@PathVariable("id") String id) {
//        service.delete(id);
//    }
}
