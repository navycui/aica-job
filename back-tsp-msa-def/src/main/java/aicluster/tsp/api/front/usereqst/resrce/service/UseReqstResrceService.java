package aicluster.tsp.api.front.usereqst.resrce.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.front.usereqst.resrce.param.UseReqstResrceParam;
import aicluster.tsp.common.dao.CmmtCodeDao;
import aicluster.tsp.common.dao.TsptFrontResrceDao;
import aicluster.tsp.common.entity.TsptResrceUseReqst;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UseReqstResrceService {


    @Autowired
    TsptFrontResrceDao tsptFrontResrceDao;

    @Autowired
    private CmmtCodeDao cmmtCodeDao;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private EnvConfig config;

    public TsptResrceUseReqst putUseReqstResrce(UseReqstResrceParam param){
        BnMember worker = TspUtils.getMember();
//        ApplcntDto applcntDto = cmmtCodeDao.selectApplcnt(worker.getMemberId());
//        if(CoreUtils.string.isBlank(applcntDto.getCttpc()))
//            throw new InvalidationException("연락처 필수 입력 ( Data is NULL )");
        if(param.getCpuCo()<0 && param.getCpuCo()==null)
            throw new InvalidationException("CPU 갯수 입력 오류");
        else if(CoreUtils.string.isBlank(param.getUseprps()))
            throw new InvalidationException("활용목적 필수 입력 ( Data is NULL )");
        else if(param.getUseBeginDt() == null || param.getUseEndDt() == null)
            throw new InvalidationException("날짜를 선택해주세요 ( Data is NULL )");
        else {
            TsptResrceUseReqst tsptResrceUseReqst = TsptResrceUseReqst.builder()
                    .reqstId(CoreUtils.string.getNewId("reqst-"))
                    .reqstSttus("APPLY")
                    .applcntId(worker.getMemberId())
                    .gpuAt(param.isGpuAt())
                    .cpuCo(param.getCpuCo())
                    .dataStorgeAt(param.isDataStorgeAt())
                    .atchmnflGroupId(param.getAtchmnflGroupId())
                    .useprps(param.getUseprps())
                    .useBeginDt(param.getUseBeginDt())
                    .useEndDt(param.getUseEndDt())
                    .creatrId(worker.getMemberId())
                    .useSttus("WAITING")
                    .partcptnDiv(param.getPartcptnDiv())
                    .build();
            tsptFrontResrceDao.insertUseReqstResrce(tsptResrceUseReqst);
            param.setApplcntId(worker.getMemberId());
            tsptFrontResrceDao.upsertUserInfo(param);
            return tsptResrceUseReqst;
        }
    }

    public String uploadFile_toNewGroup(MultipartFile fileList) {
        // 파일 하나 업로드하는데 cmmt_attachment_group 테이블과 cmmt_attachment 테이블에 데이터를 넣어준다.
        if(fileList != null ) {
            CmmtAtchmnfl fileGroupInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
            String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
            return AttachmentGroupId;
        }else
            throw new InvalidationException("업로드 실패 ( Data is NULL )");
    }
}
