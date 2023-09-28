import {HorizontalInterval, LoadingProgress, SubContents} from "shared/components/LayoutComponents";
import {Stack, Table, TableBody, TableContainer, TableRow} from "@mui/material";
import {TableTextCell} from "shared/components/TableComponents";
import {dayFormat, phoneNumberFormat, toTimeFormat} from "shared/utils/stringUtils";
import {AnalsReqstProcess, StatusState, UseEquipAnalysisDetail} from "~/service/Model";
import {CustomButton, CustomLoadingButton} from "shared/components/ButtonComponents";
import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {AnalysisService} from "~/service/AnalysisMgt/AnalysisService";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";

export const AnalysisApplyInfo = () => {
  const {id} = useParams()
  const navigate = useNavigate()
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", 'MEMBER_TYPE', 'ANALS_UNT_DIV', 'PARTCPTN_DIV'])
  const {addModal} = useGlobalModalStore()
  const information = AnalysisService.getEquipAnalsDetail(id!.toString())
  const [state, setState] = useState<UseEquipAnalysisDetail>()
  const [putState, setPutState] = useState<AnalsReqstProcess>({reqstId:id!.toString(), useSttus:'', rsndqf:''})

  useEffect(() => {
    if (!information.isLoading && !information.isFetching) {
      if (!!information.data) {
        setState({...information.data})
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  if (information.isLoading || !state) return <LoadingProgress/>

  return <Stack spacing={"40px"}>
    <SubContents title={"신청정보"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"신청상태"} label={toCommtCodeName(commtCode, 'EQPMN_REQST_ST', state.useSttus)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"신청일"} label={dayFormat(state.creatDt)}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"신청자정보"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"구분"} label={toCommtCodeName(commtCode, 'MEMBER_TYPE', state.mberDiv)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"사업자명/이름"} label={state.entrprsNm}
                thWidth={"13%"}/>

              <TableTextCell
                division title={"직위"} label={state.ofcps}
                thWidth={"13%"} tdWidth={"21%"}/>

            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"연락처"} label={`${!state.cttpc ? '' : phoneNumberFormat(state.cttpc)}`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"이메일"} label={state.email}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell title={"AI 집적단지 사업참여 여부"} label={toCommtCodeName(commtCode, 'PARTCPTN_DIV', state.partcptnDiv)} thWidth={"13%"}/>

            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"신청타입"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"신청타입"} label={`${toCommtCodeName(commtCode, 'ANALS_UNT_DIV', state.analsUntDiv)}`}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"이용일시"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"이용일시"} label={`${dayFormat(state.useBeginDt)} ~ ${dayFormat(state.useEndDt)}`}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"신청사유"} maxHeight={'100%'}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"신청사유"} label={state.reqstResn}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%"}}>
      <CustomButton
        label={"목록"} type={"largeList"} color={"outlined"}
        onClick={() => {
          navigate(-1);
        }}/>

      <Stack flexDirection={"row"}>
        {/*분석포털 담당자가 실증 페이지를 접속 안함*/}
        {/*{*/}
        {/*    (state.useSttus === StatusState.APPLY) && <>*/}
        {/*        <CustomLoadingButton*/}
        {/*            label={"반려"}*/}
        {/*            type={"largeList"}*/}
        {/*            onClick={async () => {*/}
        {/*              await AnalysisService.putEquipAnalsProcess({...putState, rsndqf:'반려사유', useSttus:StatusState.REJECT})*/}
        {/*              setState({...state, useSttus: StatusState.REJECT})*/}
        {/*              addModal({open: true, isDist: true, content: '반려되었습니다.'})*/}
        {/*            }}*/}
        {/*        />*/}
        {/*    <HorizontalInterval size={'10px'}/>*/}
        {/*        <CustomLoadingButton*/}
        {/*            label={"승인"} type={"largeList"}*/}
        {/*            onClick={async () => {*/}
        {/*              await AnalysisService.putEquipAnalsProcess({...putState, useSttus:StatusState.APPROVE})*/}
        {/*              setState({...state, useSttus: StatusState.APPROVE})*/}
        {/*              addModal({open: true, isDist: true, content: '승인되었습니다.'})*/}
        {/*              }}*/}
        {/*        />*/}
        {/*    </>}*/}
      </Stack>
    </Stack>
  </Stack>
}