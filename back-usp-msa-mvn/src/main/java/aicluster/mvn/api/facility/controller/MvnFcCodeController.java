package aicluster.mvn.api.facility.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.common.dto.MvnFcCodeDto;
import aicluster.mvn.common.dto.MvnFcOfficeRoomDto;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/facilities/codes")
public class MvnFcCodeController {

    @Autowired
    private MvnFcService service;

    /**
     * 사무실에 해당하는 입주시설만을 대상으로 건물 동호수 코드성 목록 조회
     *
     * @return List<MvnFcOfficeRoomDto>
     */
    @GetMapping("/office-room")
    JsonList<MvnFcOfficeRoomDto> getCodeList()
    {
        return service.getBnoRoomCodeList();
    }

    /**
     * 사무실에 해당하는 입주시설만을 대상으로 입주시설 속성값 코드성 목록 조회
     *
     * @param codeType(CAPACITY:수용인원, AREA:시설면적)
     * @return List<MvnFcCodeDto>
     */
    @GetMapping("/office-prop")
    JsonList<MvnFcCodeDto> getPropCodeList(String codeType)
    {
    	return service.getPropCodeList(codeType);
    }
}
