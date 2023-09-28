import {Stack, styled, Table, TableBody, TableContainer, TableRow} from "@mui/material";
import React, {Dispatch, SetStateAction, useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router-dom";
import {CustomButton, CustomLoadingButton} from "~/../../shared/src/components/ButtonComponents";
import {
  HorizontalInterval,
  LoadingProgress,
  SubAttachFileContents,
  SubContents
} from "~/../../shared/src/components/LayoutComponents";
import {ModalComponents} from "~/../../shared/src/components/ModalComponents";
import {TableSelectCell, TableTextCell, TableTextFieldCell} from "~/../../shared/src/components/TableComponents";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {DiscountData, EqpmnUseReqstDetail, StatusState} from "~/service/Model";
import {dayFormat, phoneNumberFormat, toTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {CommonService} from "~/service/CommonService";

export const ApplyEquipmentApplyInfo = () => {
  const {id} = useParams();
  const navigate = useNavigate()
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", 'EQPMN_PAYMENT', 'MEMBER_TYPE', 'PARTCPTN_DIV'])
  const [completeOpen, setcompleteOpen] = useState(false)
  const [carryOpen, setcarryOpen] = useState(false)
  const {addModal} = useGlobalModalStore()
  const information = EquipmentService.getUseEquipListApplyInfo(id!.toString())
  const [state, setState] = useState<EqpmnUseReqstDetail>();
  const [dscntId, setDscntId] = useState<string | any>('0')
  const [dataDscnt] = useState<DiscountData[]>([{dscntId: '0', dscntRate: 0, useSttus: 'END_USE', dscntResn: '없음'}])
  const [fileInfo, setFileInfo] = useState<any>()
  const [promsId, setPromsId] = useState('')
  // const fileInfo = CommonService.getAttachmentGroupIdInfo(state?.promsAtchmnflId ? state?.promsAtchmnflId : '')
  // const attachmentGroupInfo = CommonService.getAttachmentIdInfo(state?.promsAtchmnflId ? state?.promsAtchmnflId : '')

  useEffect(() => {
    if (!information.isLoading && !information.isFetching) {
      if (!!information.data) {
        setState({
          ...information.data,
          dscntList: information.data.dscntList === null ? dataDscnt : dataDscnt.concat(information.data.dscntList),
          tkoutDlbrtResult: information.data.tkoutDlbrtResult ? information.data.tkoutDlbrtResult : information.data.tkoutAt ? StatusState.APPROVE : ''
        });
        setPromsId(information.data.promsAtchmnflId)
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  useEffect(() => {
    const fileData = async () => {
      const info = await CommonService.getAttachmentGroupIdInfo1(promsId);
      setFileInfo(info)
    };
    if (promsId) fileData()
  }, [promsId])
  if (information.isLoading || !state) return <LoadingProgress/>

  return <Stack spacing={"40px"}>
    <SubContents title={"신청정보"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"신청상태"} label={toCommtCodeName(commtCode, 'EQPMN_REQST_ST', state.reqstSttus)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"신청일"} label={dayFormat(state.creatDt)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"접수번호"} label={state.rceptNo}
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
                division title={"연락처"} label={!state.cttpc ? '' : phoneNumberFormat(state.cttpc)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"이메일"} label={state.email}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell title={"AI 집적단지 사업참여 여부"}
                             label={toCommtCodeName(commtCode, 'PARTCPTN_DIV', state.partcptnDiv)} thWidth={"13%"}/>

            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"신청장비"}>
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
                title={"자산번호"} label={state.assetsNo}
                thWidth={"13%"} tdSpan={3}/>
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
                title={"활용목적"}
                label={state.useprps}
                thWidth={"13%"} tdSpan={5}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    {
      state.tkoutAt &&
      <SubContents title={"반출신청"}
                   rightContent={<>
                     {/*상태가 신청이고, 반출심의결과가 승인일때*/}
                     {state.tkoutAt && <>
                       {state.reqstSttus === StatusState.APPLY && state.tkoutDlbrtResult === StatusState.APPROVE &&
                         <CustomButton
                           label={"반출불가"} type={"small"} color={"list"}
                           onClick={() => {
                             setcarryOpen(!carryOpen)
                           }}/>}
                       {/*상태가 신청이고, 반출심의결과가 반출불가일때*/}
                       {state.reqstSttus === StatusState.APPLY && state.tkoutDlbrtResult !== StatusState.APPROVE &&
                         <CustomButton
                           label={"반출불가취소"} type={"small"} color={"list"}
                           onClick={() => {
                             addModal({
                               open: true, content: '취소하시겠습니까?', onConfirm: async () => {
                                 await EquipmentService.putUseReqstTkout({
                                   reqstId: id!.toString(),
                                   tkoutDlbrtResult: StatusState.APPROVE,
                                   tkoutDlbrtCn: '승인'
                                 })
                                 addModal({open: true, isDist: true, content: '취소되었습니다.'})
                                 setState({...state, tkoutDlbrtResult: StatusState.APPROVE, tkoutDlbrtCn: '승인'})
                               }
                             })
                           }}/>}</>}
                   </>}
      >

        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"반출여부"} label={state.tkoutAt ? '반출' : '미반출'}
                  thWidth={"13%"} tdWidth={"21%"}/>
                <TableTextCell
                  division title={"반출지 주소"} label={state.tkoutAt ? state.tkoutAdres : ''}
                  thWidth={"13%"} tdWidth={"21%"}/>
                <TableTextCell
                  title={"반출심의결과"}
                  label={!state.tkoutAt || !state.tkoutDlbrtResult ? '' : toCommtCodeName(commtCode, "EQPMN_REQST_ST", state.tkoutDlbrtResult)}
                  thWidth={"13%"} tdWidth={"21%"}/>
              </TableRow>

              <TableRow>
                <TableTextCell
                  title={"사유(용도)"}
                  label={state.tkoutAt ? state.tkoutResn : ''}
                  thWidth={"13%"} tdSpan={5}/>
              </TableRow>

              <TableRow>
                <TableTextCell
                  title={"서약서"}
                  // label={state.tkoutAt ? CommonService.getAttachmentGroupIdInfo(state.promsAtchmnflId).data && CommonService.getAttachmentGroupIdInfo(state.promsAtchmnflId).data?.fileNm : ''} // state.tkoutAt ? state.tkoutResn : ''
                  label={state.tkoutAt ? fileInfo && fileInfo.fileNm : ''} // state.tkoutAt ? state.tkoutResn : ''
                  thWidth={"13%"} tdSpan={5}
                  rightContent={
                    state.tkoutAt &&
                    <CustomButton style={{marginLeft: "10px"}}
                                  label={"다운로드"} type={"small"} color={"list"}
                                  onClick={async () => {
                                    window.open(`${process.env.REACT_APP_DOMAIN}/tsp-api/api/file-dwld-contentType/${fileInfo.attachmentId}`)
                                    // window.open(`${process.env.REACT_APP_DOMAIN}/tsp-api/api/file-dwld-contentType/${m.attachmentId}`)
                                    // window.open(`${process.env.REACT_APP_DOMAIN}/tsp-api/api/file-dwld-groupfiles/${promsId}`)
                                  }}/>
                  }
                />
              </TableRow>

              {
                (state.tkoutAt && state.tkoutDlbrtCn) &&
                <TableRow>
                  <TableTextCell
                    title={"반출심의 내용"}
                    label={state.tkoutAt ? state.tkoutDlbrtCn : ''}
                    thWidth={"13%"} tdSpan={5}/>
                </TableRow>
              }
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>
    }

    <SubContents title={"사용기간"}>

      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"시작일"} label={toTimeFormat(state.useBeginDt)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"종료일"} label={toTimeFormat(state.useEndDt)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"사용시간"} label={`${state.expectUsgtm || 0}시간`}
                thWidth={"13%"} tdWidth={"21%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents
      title={"사용금액"}
      subTitle={'사용시간 및 할인률 변경 입력시, 사용금액 재설정 버튼을 클릭해주세요.'}
      // 신청상태가 반려, 승인일때 버튼이 나오지않음
      rightContent={(state.reqstSttus === StatusState.REJECT || state.reqstSttus === StatusState.APPROVE) ? '' :
        <CustomLoadingButton
          label={"할인적용"} type={"small"} color={"list"}
          onClick={async () => {
            await EquipmentService.putUseReqstDscnt({
              dscntId: dscntId,
              reqstId: id!.toString(),
              usgtm: state.usgtm
            })
            addModal({open: true, isDist: true, content: '할인이 적용되었습니다.'})
          }}/>
      }>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"1시간 사용료"}
                label={`${state.rntfeeHour.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21%"}/>
              <TableTextCell
                division title={"1일 가용시간"}
                label={`${state.usefulHour < 0 ? state.usefulHour * -1 : state.usefulHour || 0}시간`}
                thWidth={"13%"} tdWidth={"21%"}/>
              <TableTextCell
                title={"예상 사용금액"}
                label={`${state.expectRntfee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21%"}/>

            </TableRow>
            <TableRow>
              { // 신청상태가 보완요청이거나 반려일경우 입력 불가능
                (state.reqstSttus === StatusState.REJECT || state.reqstSttus === StatusState.SPM_REQUEST || state.reqstSttus === StatusState.APPROVE) ?
                  <TableTextCell
                    title={"할인사유"}
                    label={state.dscntList === null ? '없음' :
                      `${state.dscntResn === '' ? '없음' :
                        state.dscntResn} (${state.dscntRate === null ? '0' : state.dscntRate}%)`}
                    thWidth={"13%"} tdSpan={5}/>
                  :
                  <TableSelectCell
                    label={"할인사유"} selectLabel={state.dscntList.map((m, i) => {
                    return `${m.dscntResn === null ? '없음' : m.dscntResn} (${m.dscntRate === null ? '0' : m.dscntRate}%)`
                  })}
                    defaultLabel={`${state.dscntResn === "" ? '없음' : state.dscntResn} (${state.dscntRate === null ? '0'
                      : state.dscntRate}%)`}

                    thWidth={"13%"} tdSpan={5} required onClick={(selectValue) => {
                    const findIndex = state.dscntList.findIndex(({dscntResn}) => selectValue.includes(dscntResn));
                    const updateDscnt = {
                      ...state,
                      dscntRate: state?.dscntList[findIndex].dscntRate,
                      dscntResn: state?.dscntList[findIndex].dscntResn,
                      dscntId: state?.dscntList[findIndex].dscntId || '0',
                      useSttus: findIndex === 0 ? 'END_USE' : 'USE'
                    };

                    setState(updateDscnt)
                    setDscntId(state?.dscntList[findIndex].dscntId)
                  }}/>
              }
              {/*<TableSelectCell*/}
              {/*  label={"할인사유"} selectLabel={state.dscntList.map((m, i) => {*/}
              {/*  return `${m.dscntResn === null ? '없음' : m.dscntResn} (${m.dscntRate === null ? '0' : m.dscntRate}%)`*/}
              {/*})}*/}
              {/*  defaultLabel={`${state.dscntResn === "" ? '없음' : state.dscntResn} (${state.dscntRate === null ? '0'*/}
              {/*    : state.dscntRate}%)`}*/}
              {/*  thWidth={"13%"} tdSpan={5} onClick={(selectValue) => {*/}
              {/*  const findIndex = state.dscntList.findIndex(({dscntResn}) => selectValue.includes(dscntResn));*/}
              {/*  const updateDscnt = {*/}
              {/*    ...state,*/}
              {/*    dscntRate: state?.dscntList[findIndex].dscntRate,*/}
              {/*    dscntResn: state?.dscntList[findIndex].dscntResn*/}
              {/*  };*/}
              {/*  setState(updateDscnt)*/}
              {/*  setDscntId(state?.dscntList[findIndex].dscntId)*/}
              {/*}}/>*/}
            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"지불방법"} label={toCommtCodeName(commtCode, 'EQPMN_PAYMENT', state.pymntMth)}
                thWidth={"13%"} tdWidth={"21"}/>
              <TableTextCell
                division title={"할인금액"}
                label={`${Math.round((state.rntfeeHour * state.expectUsgtm) * Number(state.dscntRate) / 100).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21"}/>
              <TableTextCell
                title={"할인적용금액"}
                label={`${Math.round(((state.rntfeeHour * state.expectUsgtm) - ((state.rntfeeHour * state.expectUsgtm) * Number(state.dscntRate) / 100))).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    {
      state.attachmentList && state.attachmentList.length > 0 &&
        <SubAttachFileContents atchmnflGroupId={state.atchmnflGroupId} atchmnfl={state.attachmentList}/>
    }

    <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%"}}>
      <CustomButton
        label={"목록"} type={"largeList"} color={"outlined"}
        onClick={() => {
          navigate('/tsp-admin/UseMgt/EquipmentMgt/ApplyMgt');
        }}
      />.
      <Stack flexDirection={"row"}>
        {
          (state.reqstSttus === StatusState.APPROVE || state.reqstSttus === StatusState.SPM_REQUEST || state.reqstSttus === StatusState.APPLY) && (!(new Date().getTime() >= state.useBeginDt) && !(new Date().getTime() <= state.useEndDt)) && <>
            <CustomLoadingButton
              label={"신청취소"}
              type={"largeList"}
              onClick={async () => {
                await EquipmentService.putUseEquipEstmtCheck({
                  reqstId: id!.toString(),
                  reqstSttus: StatusState.CANCEL,
                  rceptNo: state?.rceptNo
                }) // 사용종료
                setState({...state, reqstSttus: StatusState.CANCEL})
                addModal({open: true, isDist: true, content: '신청취소 되었습니다.'})
              }}/>
            <HorizontalInterval size={"10px"}/>
          </>
        }
        {
          (state.reqstSttus === StatusState.APPLY || state.reqstSttus === StatusState.SPM_REQUEST) && <>
            <CustomButton
              label={"보완"}
              type={"largeList"}
              onClick={() => {
                setcompleteOpen(!completeOpen)
              }}
            />
            <HorizontalInterval size={"10px"}/>
            <CustomLoadingButton
              label={"반려"}
              type={"largeList"}
              onClick={async () => {
                await EquipmentService.putUseEquipEstmtCheck({
                  reqstSttus: StatusState.REJECT,
                  reqstId: id!.toString(),
                  rceptNo: state?.rceptNo
                })
                setState({...state, reqstSttus: StatusState.REJECT})
                addModal({open: true, isDist: true, content: '반려 처리되었습니다.'})
              }}
            />
            <HorizontalInterval size={"10px"}/>
            <CustomLoadingButton
              label={"승인"}
              onClick={async () => {
                if (state.tkoutDlbrtCn === '') {
                  await EquipmentService.putUseReqstTkout({
                    reqstId: id!.toString(),
                    tkoutDlbrtResult: state?.tkoutDlbrtResult,
                    tkoutDlbrtCn: state?.tkoutDlbrtCn
                  })
                }
                await EquipmentService.putUseReqstConsent(id!.toString())
                setState({...state, reqstSttus: StatusState.APPROVE})
                /*if (state?.dscntId === "0") {
                  await EquipmentService.putUseReqstDscnt({
                    dscntId: state.dscntId,
                    reqstId: id!.toString(),
                    usgtm: state.usgtm
                  })
                }*/
                addModal({open: true, isDist: true, content: "승인되었습니다.",})
                window.scrollTo(0, 0)
              }}
            /></>}
      </Stack>
    </Stack>

    <ComplementModal open={completeOpen} setOpen={setcompleteOpen} id={id!.toString()} state={state}
                     setState={setState}/>
    <CarryModal open={carryOpen} setOpen={setcarryOpen} id={id!.toString()} state={state}
                setState={setState}/>
  </Stack>
}

const ComplementModal: React.FC<{
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
  id: string
  state: EqpmnUseReqstDetail
  setState: Dispatch<SetStateAction<any>>
}> = props => {
  const {addModal} = useGlobalModalStore()
  const [details, setDetails] = useState("")

  return <ModalComponents
    open={props.open}
    type={"transmit"} title={'보완요청'}
    onConfirm={async () => {
      await EquipmentService.putUseEquipEstmtCheck({
        rsndqf: details,
        reqstId: props.id,
        reqstSttus: StatusState.SPM_REQUEST
      })
      props.setState({...props.state, reqstSttus: StatusState.SPM_REQUEST})
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
      window.scrollTo(0, 0)
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

const CarryModal: React.FC<{
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
  id: string
  state: EqpmnUseReqstDetail
  setState: Dispatch<SetStateAction<any>>
}> = props => {
  const {addModal} = useGlobalModalStore()
  const [details, setDetails] = useState("")

  return <ModalComponents
    open={props.open}
    type={"save"} title={"반출불가사유"}
    onConfirm={async () => {
      if (!details) {
        addModal({open: true, isDist: true, content: '사유를 적어주세요.'})
        return
      }
      await EquipmentService.putUseReqstTkout({
        reqstId: props.id!.toString(),
        tkoutDlbrtResult: StatusState.REJECT,
        tkoutDlbrtCn: details
      })
      props.setState({...props.state, tkoutDlbrtResult: StatusState.REJECT, tkoutDlbrtCn: details})
      addModal({open: true, isDist: true, content: '반출불가 처리되었습니다.'})
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