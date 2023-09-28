import { useNavigate, useParams} from "react-router-dom";
import { Stack, Table, TableBody, TableContainer, TableRow } from "@mui/material";
import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react";
import {CustomButton, CustomLoadingButton} from "~/../../shared/src/components/ButtonComponents";
import {HorizontalInterval, LoadingProgress, SubContents} from "~/../../shared/src/components/LayoutComponents";
import {TableTextCell, TableTextFieldCell} from "~/../../shared/src/components/TableComponents";
import {ApplyResourceDetailData, StatusState} from "~/service/Model";
import {dayFormat, phoneNumberFormat} from "shared/utils/stringUtils";
import {ModalComponents} from "shared/components/ModalComponents";
import styled from "@emotion/styled";
import {ApplyResourceService} from "~/service/UseMgt/Resource/ApplyResourceService";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";

export const ApplyResourceinfo = () => {
  const {id} = useParams();
  const resourceDetail = ApplyResourceService.getApplyResourceMgtInfo(id!.toString());
  const [state, setState] = useState<ApplyResourceDetailData>();
  const navigate = useNavigate();
  const [spmopen, setSpmOpen] = useState(false);
  const [rejopen, setRejOpen] = useState(false);
  const {addModal} = useGlobalModalStore();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_REQST_ST", "MEMBER_TYPE",'PARTCPTN_DIV'])

  useEffect(() => {
    if (!resourceDetail.isLoading && !resourceDetail.isFetching) {
      setState(resourceDetail.data);
      window.scrollTo(0, 0);
    }
  }, [resourceDetail.data, !resourceDetail.isLoading, !resourceDetail.isFetching])

  if (!state) return <LoadingProgress/>
  return <Fragment>
    <Stack spacing={"40px"}>
      <SubContents title={"신청정보"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"신청상태"} label={toCommtCodeName(commtCode, 'EQPMN_RESOURCE_REQST_ST', state.reqstSttus)}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"신청일"} label={dayFormat(state.creatDt)}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"접수번호"} label={state.rceptNo}
                  thWidth={"13%"} tdWidth={"21%"}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents title={"신청자정보"}>
        <TableContainer sx={{borderTop: "1px solid #d7dae6", width: "100%"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"구분"} label={toCommtCodeName(commtCode, 'MEMBER_TYPE', state.mberDiv)}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"사업자명/이름"} label={state.entrprsNm}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"직위"} label={state.ofcps}
                  thWidth={"13%"} tdWidth={"21%"}
                />
              </TableRow>
              <TableRow>
                <TableTextCell
                  division title={"연락처"} label={`${!state.cttpc ? '' : phoneNumberFormat(state.cttpc)}`}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"이메일"} label={state.email}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"AI 집적단지 사업참여 여부"} label={toCommtCodeName(commtCode, 'PARTCPTN_DIV', state.partcptnDiv)}
                  thWidth={"13%"} tdWidth={"21%"}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>
      <SubContents title={"신청자원"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"GPU"} label={state.gpuAt ? "사용" : "미사용"}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"CPU"} label={`${state.cpuCo} core`}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"데이터 저장소"} label={state.dataStorgeAt ? "사용" : "미사용"}
                  thWidth={"13%"} tdWidth={"21%"}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>
      <SubContents title={"활용목적"} maxHeight={'100%'}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  title={"활용목적"} label={state.useprps}
                  thWidth={"13%"} tdSpan={5}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>
    </Stack>
    <br/>
    <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%"}}>
      <CustomButton
        label={"목록"} type={"largeList"} color={"outlined"}
        onClick={() => {
          navigate(-1)
        }}
      />
      <Stack flexDirection={"row"}>
        {
          (state.reqstSttus === 'APPLY' || state.reqstSttus === 'SPM_REQUEST') && <CustomButton
                label={"보완"}
                type={"largeList"}
                onClick={() => {
                  setSpmOpen(!spmopen)
                }}
            />
        }
        <HorizontalInterval size={"10px"}/>
        {
          (state.reqstSttus === 'APPLY' || state.reqstSttus === 'SPM_REQUEST') && <CustomLoadingButton
                label={"반려"}
                type={"largeList"}
                onClick={async () => {
                  const data = await ApplyResourceService.putApplyResourceRejectCheck({reqstId:id!})
                  setState({...state, reqstSttus: StatusState.REJECT})
                  addModal({open:true, isDist: true, content:'반려되었습니다.'})
                }}
            />
        }
        <HorizontalInterval size={"10px"}/>
        {
          (state.reqstSttus === 'APPLY' || state.reqstSttus === 'SPM_REQUEST') && <CustomLoadingButton
                label={"승인"}
                onClick={async () => {
                  if (resourceDetail) {
                    const ApproveData = await ApplyResourceService.putApplyResourceApproveCheck({reqstId:id!})
                    if (ApproveData.success) {
                      addModal({
                        open: true, isDist: true,
                        content: ApproveData.success? '승인 되었습니다.' : '실패하였습니다.',
                      })
                      setState({...state, reqstSttus: StatusState.APPROVE})
                      window.scrollTo(0, 0);
                    }
                  }
                }}
            />
        }
      </Stack>
    </Stack>
    {spmopen && <SpmModal open={spmopen} state={state} setOpen={setSpmOpen} setState={setState}/>}
    {rejopen && <RejectModal open={rejopen} state={state} setOpen={setRejOpen} setState={setState}/>}
  </Fragment>
}

const SpmModal: React.FC<{
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
  state: ApplyResourceDetailData
  setState: Dispatch<SetStateAction<ApplyResourceDetailData | undefined>>
}> = props => {
  const {id} = useParams()
  const [details, setDetails] = useState("")
  const {addModal} = useGlobalModalStore();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_REQST_ST"])

  return <ModalComponents
    open={(props.open)}
    type={"save"} title={"보완"}
    onConfirm={async () => {
      const SpmData = await ApplyResourceService.putApplyResourceSpmCheck({reqstId:id!, param: details})
      if (SpmData.success) {
        addModal({
          open: true, isDist: true,
          content: SpmData.success ? '보완 처리되었습니다.' : '실패하였습니다.',
        })
      }
      props.setState({...props.state, reqstSttus: StatusState.SPM_REQUEST})
      props.setOpen(false)
      window.scrollTo(0, 0);
    }}
    onClose={() => {
      props.setOpen(false)
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRowTextField>
            <TableTextFieldCell
              label={"사유"} multiline defaultLabel={details}
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              onChange={(text: string) => {
                setDetails(text)
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const RejectModal: React.FC<{
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
  state: ApplyResourceDetailData
  setState: Dispatch<SetStateAction<ApplyResourceDetailData | undefined>>
}> = props => {
  const {id} = useParams()
  const [details, setDetails] = useState("")
  const {addModal} = useGlobalModalStore();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_REQST_ST"])

  return <ModalComponents
    open={(props.open)}
    type={"save"} title={"반려"}
    onConfirm={async () => {
      const reJectData = await ApplyResourceService.putApplyResourceRejectCheck({reqstId:id!, param: details})
      if (reJectData.success) {
        addModal({
          open: true, isDist: true,
          content: reJectData.success ? '반려 처리되었습니다.' : '실패하였습니다.',
        })
      }
      props.setState({...props.state, reqstSttus: StatusState.REJECT})
      props.setOpen(false)
      window.scrollTo(0, 0);
    }}
    onClose={() => {
      props.setOpen(false)
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRowTextField>
            <TableTextFieldCell
              label={"사유"} multiline defaultLabel={details}
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              onChange={(text: string) => {
                setDetails(text)
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const TableRowTextField = styled(TableRow)`
  .MuiInputBase-root {
    height: 150px;
    align-items: baseline;
  }

  .MuiTableCell-root {
    padding-top: 15px;
    padding-bottom: 15px;
  }
`