import {useNavigate, useParams} from "react-router-dom";
import {Button, Stack, Table, TableBody, TableContainer, TableRow} from "@mui/material";
import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react";
import {CustomButton, CustomLoadingButton} from "~/../../shared/src/components/ButtonComponents";
import {
  HorizontalInterval,
  LoadingProgress,
  SubContents,
  VerticalInterval
} from "~/../../shared/src/components/LayoutComponents";
import {TableTextCell} from "~/../../shared/src/components/TableComponents";
import {UseResourceDetailData} from "~/service/Model";
import {dayFormat, phoneNumberFormat} from "shared/utils/stringUtils";
import {UseResourceService} from "~/service/UseMgt/Resource/UseResourceService";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";


export const UseResourceinfo = () => {
  const {id} = useParams();
  const navigate = useNavigate();
  const resourceDetail = UseResourceService.getUseResourceMgtInfo(id!.toString());
  const [state, setState] = useState<UseResourceDetailData>();
  const {addModal} = useGlobalModalStore();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_USAGE_ST", "MEMBER_TYPE",'PARTCPTN_DIV'])

  useEffect(() => {
    if (!resourceDetail.isLoading && !resourceDetail.isFetching) {
      if(!!resourceDetail.data) {
        setState(resourceDetail.data);
      }
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
                  division title={"접수번호"} label={state.rceptNo}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"신청일"} label={dayFormat(state.creatDt)}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"사용상태"} label={toCommtCodeName(commtCode, 'EQPMN_RESOURCE_USAGE_ST', state.useSttus)}
                  thWidth={"12%"} tdWidth={"21%"}
                />
              </TableRow>
              <TableRow>
                <TableTextCell
                  title={"반환요청일"} label={state.useRturnDt ? dayFormat(state.useRturnDt) : ""}
                  thWidth={"12%"} tdSpan={5}
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
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"사업자명/이름"} label={state.entrprsNm}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"직위"} label={state.ofcps}
                  thWidth={"12%"} tdWidth={"21%"}
                />
              </TableRow>
              <TableRow>
                <TableTextCell
                  division title={"연락처"} label={`${!state.cttpc ? '' : phoneNumberFormat(state.cttpc)}`}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"이메일"} label={state.email}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"AI 집적단지 사업참여 여부"} label={toCommtCodeName(commtCode, 'PARTCPTN_DIV', state.partcptnDiv)}
                  thWidth={"12%"} tdWidth={"21%"}
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
                  division title={"GPU"} label={state.gpuAt ? "사용" : "사용안함"}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"CPU"} label={state.cpuCo.toString()}
                  thWidth={"12%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"데이터 저장소"} label={state.dataStorgeAt ? "사용" : "사용안함"}
                  thWidth={"12%"} tdWidth={"21%"}
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
                  thWidth={"12%"} tdSpan={5}
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
          navigate('/tsp-admin/UseMgt/ResourceMgt/UseMgt')
        }}
      />
      <Stack flexDirection={"row"}>
        {
          (state.useSttus === 'USE') && <CustomLoadingButton
                label={"반환요청"}
                type={"largeList"}
                onClick={async () => {
                  if (resourceDetail) {
                    const UseResourceData = await UseResourceService.putUseResourceApplyReqReturn({reqstId: id!})
                    if (UseResourceData.success) {
                      addModal({
                        open: true, isDist: true,
                        content: UseResourceData.success ? '반환요청 처리되었습니다.' : '실패하였습니다.',
                      })
                      setState(UseResourceData)
                      window.scrollTo(0, 0);
                    }
                  }
                }}
            />
        }
        <HorizontalInterval size={"20px"}/>
        {
          (state.useSttus === 'USE' || state.useSttus === 'REQ_RETURN') && <CustomLoadingButton
                label={"반환완료"}
                type={"largeList"}
                onClick={async () => {
                  if (resourceDetail) {
                    const UseResourceData = await UseResourceService.putUseResourceApplyReturn({reqstId: id!})
                    if (UseResourceData.success) {
                      addModal({
                        open: true, isDist: true,
                        content: UseResourceData.success ? '반환완료 처리되었습니다.' : '실패하였습니다.',
                      })
                      setState(UseResourceData)
                      window.scrollTo(0, 0);
                    }
                  }
                }}
            />
        }
        {
          state.useSttus === 'WAITING' && <CustomLoadingButton
          label={"승인취소"}
          type={"largeList"}
          onClick={async () => {
            if (resourceDetail) {
              const UseResourceData = await UseResourceService.putUseResourceApplyCancel({reqstId:id!})
              if (UseResourceData.success) {
                addModal({
                  open: true, isDist: true,
                  content: UseResourceData.success? '승인취소 처리되었습니다.' : '실패하였습니다.',
                })
                setState(UseResourceData)
                window.scrollTo(0, 0);
              }
            }
          }}
        />
        }
      </Stack>
    </Stack>
  </Fragment>
}

