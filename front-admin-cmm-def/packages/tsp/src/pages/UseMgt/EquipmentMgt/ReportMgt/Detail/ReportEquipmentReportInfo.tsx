import {useCommtCode} from "~/utils/useCommtCode";
import {useNavigate, useParams} from "react-router-dom";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import React, {Dispatch, SetStateAction, useEffect, useState} from "react";
import {EqpmnReportDetail, StatusState} from "~/service/Model";
import {
  HorizontalInterval,
  LoadingProgress,
  SubAttachFileContents,
  SubContents
} from "shared/components/LayoutComponents";
import {Stack, styled, Table, TableBody, TableContainer, TableRow} from "@mui/material";
import {TableTextCell, TableTextFieldCell} from "shared/components/TableComponents";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {dayFormat, phoneNumberFormat, toTimeFormat} from "shared/utils/stringUtils";
import {CustomButton, CustomLoadingButton} from "shared/components/ButtonComponents";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {ModalComponents} from "shared/components/ModalComponents";

export const ReportEquipmentReportInfo = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(['EQPMN_RESULT_REPORT_ST', 'EQPMN_PAYMENT', 'MEMBER_TYPE','PARTCPTN_DIV'])
  const list = EquipmentService.getEquipReprtDetail(id!.toString())
  const [state, setState] = useState<EqpmnReportDetail>();
  const {addModal} = useGlobalModalStore()
  const navigate = useNavigate()
  const [completeOpen, setCompleteOpen] = useState(false)

  useEffect(() => {
    if (!list.isLoading && !list.isFetching) {
      if (!!list.data) {
        setState(list.data);
      }
    }
  }, [list.data, list.isLoading, list.isFetching])

  if (list.isLoading || !state) return <LoadingProgress/>

  return <Stack spacing={"40px"}>
    <SubContents title={"신청정보"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"제출일"} label={dayFormat(state.creatDt)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"처리상태"} label={toCommtCodeName(commtCode, 'EQPMN_RESULT_REPORT_ST', state.reprtSttus)}
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
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"직위"} label={state.ofcps}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"연락처"} label={`${state.cttpc ? phoneNumberFormat(state.cttpc) : ''}`}
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

    <SubContents title={"활용분야"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                title={'활용분야'} label={state.prcuseRealm}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"신청장비 및 지불방법"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"장비명(국문)"} label={state.eqpmnNmKorean}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"장비명(영문)"} label={state.eqpmnNmEngl}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"모델명"} label={state.modelNm}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"장비 분야"} label={'장비분야'}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={'자산번호'} label={state.assetsNo}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"1시간 사용료"} label={`${state.rntfeeHour || 0}원`}
                thWidth={"13%"} tdWidth={"21%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"지불방법"} label={toCommtCodeName(commtCode, 'EQPMN_PAYMENT', state.pymntMth)}
                thWidth={"13%"} tdSpan={5}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"사용기간"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"시작일"} label={`${toTimeFormat(state.useBeginDt) || 0}`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"종료일"} label={`${toTimeFormat(state.useEndDt) || 0}`}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"사용금액"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"사용시간"} label={`${state.usgtm || 0}시간`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"1시간 사용료"} label={`${state.rntfeeHour || 0}시간`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"사용금액"}
                label={`${(state.usgtm * state.rntfeeHour).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                division title={'지불방법'} label={toCommtCodeName(commtCode, 'EQPMN_PAYMENT', state.pymntMth)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"할인사유"} label={state.dscntResn}
                thWidth={"13%"} tdSpan={3}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"할인금액"}
                label={`${Math.round((state.rntfeeHour * state.usgtm) * Number(state.dscntRate) / 100).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21%"}/>
              <TableTextCell
                title={"할인적용금액"}
                label={`${state.rntfee ? state.rntfee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') : '0'}원`}
                thWidth={"13%"} tdSpan={3}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"실사용금액"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"실사용날짜"} label={`${toTimeFormat(state.useBeginDt)} ~ ${toTimeFormat(state.useEndDt)}`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"실사용시간"} label={`${state.usgtm || 0}시간`}
                thWidth={"13%"}/>

              <TableTextCell
                title={"실사용금액"}
                label={`${state.rntfee ? state.rntfee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') : '0'}원`}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"장비활용내용"} maxHeight={'100%'}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                title={"활용목적"} label={state.prcusePurps}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"장비활용 필요성"} label={state.prcuseNeed}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"활용대상장비"} maxHeight={'100%'}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                title={"주관기간"} label={state.mnnst}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"대상장비"} label={state.trgetEqpmn}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"상세활용내역"} label={state.detailPrcuse}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"장비활용용도"} label={state.prcusePrpos}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"장비활용성과"} maxHeight={'100%'}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                title={"장비활용계획"} label={state.prcusePlan}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"장비활용내역"} label={state.prcuseDtls}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"계획대비 차이점"} label={state.dffrnc}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubAttachFileContents title={'장비활용성과 첨부파일'} atchmnflGroupId={state.atchmnflGroupId}/>

    <SubContents title={"제품개발(연구개발) 장비활용 성과"} maxHeight={'100%'}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                title={"장비활용 목적 달성"} label={state.achiv}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"개발 제품/기술에 본 장비활용의 기대효과"} label={state.expcEffect}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"개발 제품/기술 제품출시 예상시기"} label={state.cmtExpectEra}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"개발 제품/기술 매출 예상액"} label={state.expectSalamt}
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"장비활용 의견 수렵"} maxHeight={'100%'}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                title={"주관기간 장비활용 시 좋았던 점"} label={state.strength}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"주관기간 장비활용 시 아쉬웠던 점"} label={state.weakness}
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
        }}
      />
      <Stack flexDirection={"row"}>
        {
          state.reprtSttus !== StatusState.APPROVE && <>
                <CustomButton
                    label={"보완"}
                    type={"largeList"}
                    onClick={() => {
                      setCompleteOpen(!completeOpen)
                    }}
                />
                <HorizontalInterval size={"10px"}/>
                <CustomLoadingButton
                    label={"승인"} type={"largeList"}
                    onClick={async () => {
                      await EquipmentService.putEquipReprtProcess({
                        reprtId: id!.toString(),
                        reprtSttus: StatusState.APPROVE
                      })
                      setState({...state, reprtSttus: StatusState.APPROVE})
                      addModal({open: true, content: "승인되었습니다.", isDist: true})
                    }}
                />
            </>
        }
      </Stack>
    </Stack>
    <ComplementModal open={completeOpen} setOpen={setCompleteOpen} id={id!.toString()} state={state}
                     setState={setState}/>
  </Stack>
}

const ComplementModal: React.FC<{
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
  id: string
  state: EqpmnReportDetail
  setState: Dispatch<SetStateAction<any>>
}> = props => {
  const {addModal} = useGlobalModalStore()
  const [details, setDetails] = useState("")

  return <ModalComponents
    open={props.open}
    type={"save"} title={'보완요청'}
    onConfirm={async () => {
      await EquipmentService.putEquipReprtProcess({
        rsndqf: details,
        reprtId: props.id,
        reprtSttus: StatusState.SPM_REQUEST
      })
      props.setState({...props.state, reprtSttus: StatusState.SPM_REQUEST})
      // const endCheck = await EquipmentInformationService.putEquipmentsEndCheck(id!, correctData)
      // mgtInfoData.setModalOpen("checkFinish",false);
      // mgtInfoData.setMgtInfoData({
      //   ...mgtInfoData.mgtInfoData!,
      //   available: endCheck.eqpmntStateInfo!.available,
      //   eqpmnSt: endCheck.eqpmntStateInfo!.eqpmnSt,
      //   inspectSt: "0",
      //   checkParam: null
      // })
      addModal({open: true, isDist: true, content: '보완요청 처리되었습니다.'})
      props.setOpen(false)
    }}
    onClose={() => {
      // mgtInfoData.setModalOpen("checkFinish",false);
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