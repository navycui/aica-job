import React, {Dispatch, SetStateAction, useEffect, useState, useRef, Fragment} from 'react'
import {Box, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {
  HorizontalInterval,
  LoadingProgress,
  SubAttachFileContents,
  SubContents
} from "shared/components/LayoutComponents";
import {
  TableSelectCell,
  TableTextCell,
  TableTextFieldCell
} from "shared/components/TableComponents";
import {CustomButton, CustomLoadingButton} from "shared/components/ButtonComponents";
import {useNavigate, useParams} from "react-router-dom";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {ModalComponents} from "shared/components/ModalComponents";
import styled from "@emotion/styled";
import {EstimationService} from "~/service/UseMgt/Estimation/EstimationService";
import {dayFormat, phoneNumberFormat, toTimeFormat} from "shared/utils/stringUtils";
import {
  DiscountData,
  EqpmnEstmtDetailData,
  EqpmnEstmtModifyPrice,
  EST,
  ozReportPersonInfo,
  StatusState
} from "~/service/Model";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import dayjs from "shared/libs/dayjs";
import {CommonService} from "~/service/CommonService";

export const EstimationApplyInfo = () => {
  const {id} = useParams();
  const navigate = useNavigate()
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", 'EQPMN_PAYMENT', 'MEMBER_TYPE', 'PARTCPTN_DIV'])
  const [open, setOpen] = useState(false);
  const {addModal} = useGlobalModalStore()
  const information = EstimationService.getUseEquipEstmtMgtInfo(id!.toString());
  const [state, setState] = useState<EqpmnEstmtDetailData>();
  const [dscntId, setDscntId] = useState<string | any>('0');
  const [dataDscnt] = useState<DiscountData[]>([{dscntId: '0', dscntRate: 0, useSttus: 'END_USE', dscntResn: '없음'}])
  const [est, setEst] = useState<EST>()
  useEffect(() => {
    if (!information.isLoading && !information.isFetching) {
      if (!!information.data) {
        setState({
          ...information.data,
          dscntList: information.data.dscntList === null ? dataDscnt : dataDscnt.concat(information.data.dscntList)
        });
      }
    }
  }, [information.data, information.isLoading, information.isFetching])
  const [personInfo, setPersonInfo] = useState<ozReportPersonInfo>();
  useEffect(() => {
    if (state && state.reqstSttus === StatusState.EST_APPROVE) {
      EstimationService.getOzEstmt(id!.toString()).then(value => setPersonInfo(value));
    }
  }, [state])
  useEffect(() => {
    if (personInfo && state) {
      setEst({
        ...est,
        fileName: 'Estimation',
        ozrName: 'ozreport.ozr',
        jsonData: {
          user: [{
            entrprsNm: personInfo.applcnt.entrprsNm,
            chargerNm: personInfo.applcnt.userNm,
            chargerCttpc: personInfo.applcnt.cttpc,
            adres: personInfo.applcnt.adres,
            creatDt: String(dayFormat(personInfo.creatDt)),
            validate: "발행일로부터 3일"
          }],
          admin: [{
            entrprsNm: "인공지능산업융합사업단",
            chargerNm: personInfo.mberNm,
            chargerCttpc: personInfo.telno,
            adres: "광주광역시 북구 첨단과기로 176번길 11",
            homepage: "http://aica-gj.kr",
            email: personInfo.email
          }],
          estmtDetail: [{
            assetsNo: state.assetsNo,
            eqpmnNm: state.eqpmnNmKorean,
            modelNm: state.modelNm,
            amount: "1",
            rntfee: String(`${Math.round((state.rntfeeHour * state.usgtm) - ((state.rntfeeHour * state.usgtm) * Number(state.dscntRate) / 100)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`)
          }],
          etc: [{
            place: "광주그린카진흥원 1층 기술지원동 내",
            remarks: "필요 시 시험을 위탁해 드리며, 시험금액은 다소 변동될 수 있습니다.",
            account: "우체국 502674-01-003502 인공지능산업융합사업단"
          }]
        }
      })
    }
  }, [state, personInfo])


  function post(path: string, params: any, method = 'post') {

    // The rest of this code assumes you are not using a library.
    // It can be made less verbose if you use one.
    const form = document.createElement('form');
    form.method = method;
    form.action = path;
    form.target = "_blank";

    for (const key in params) {
      if (params.hasOwnProperty(key)) {
        const hiddenField = document.createElement('input');
        hiddenField.type = 'hidden';
        hiddenField.name = key;
        if ((typeof params[key]) == 'object')
          hiddenField.value = JSON.stringify(params[key])
        else
          hiddenField.value = params[key]

        form.appendChild(hiddenField);
      }
    }
    document.body.appendChild(form);
    form.submit();
  }

  if (information.isLoading || !state) return <LoadingProgress/>

  return <Stack spacing={"40px"} component={'form'} id={'Estimateinfo'}>
    <SubContents title={"신청정보"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"신청상태"} label={toCommtCodeName(commtCode, 'EQPMN_REQST_ST', state.reqstSttus)}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"접수번호"} label={`${state.rceptNo || '접수번호'}`}
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
                division title={"직위"} label={state.ofcps}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"사업자명/이름"} label={state.entrprsNm}
                thWidth={"13%"}/>

            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"연락처"} label={`${state.cttpc ? phoneNumberFormat(state.cttpc) : ''}`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"이메일"} label={state.email}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"AI 집적단지 사업참여 여부"} thWidth={"13%"}
                label={toCommtCodeName(commtCode, 'PARTCPTN_DIV', state.partcptnDiv)}/>

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
      <SubContents title={"반출신청"} maxHeight={'100%'}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"반출여부"} label={state.tkoutAt ? '반출' : '미반출'}
                  thWidth={"13%"} tdWidth={"21%"}/>

                <TableTextCell
                  title={"반출지 주소"} label={`${state.tkoutAdres || ''}`}
                  thWidth={"13%"} tdSpan={3}/>
              </TableRow>

              <TableRow>
                <TableTextCell
                  title={"사유(용도)"}
                  label={`${state.tkoutResn || ''}`}
                  thWidth={"13%"} tdSpan={5}/>
              </TableRow>
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
                thWidth={"13%"}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents title={"신청 사용금액"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableTextCell
                division title={"1시간 사용료"}
                label={`${Math.round(state.rntfeeHour).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                division title={"1일 가용시간"} label={`${Math.round(state.usefulHour) || 0}시간`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"지불방법"} label={toCommtCodeName(commtCode, 'EQPMN_PAYMENT', state.pymntMth)}
                thWidth={"13%"}/>
            </TableRow>

            <TableRow>
              <TableTextCell
                title={"예상 사용금액"}
                label={`${!state.expectRntfee ? '0' : state.expectRntfee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdSpan={5}/>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <SubContents
      title={"조정 사용금액"}
      subTitle={'사용시간 및 할인률 변경 입력시, 사용금액 재설정 버튼을 클릭해주세요.'}
      // 신청상태가 보완요청이거나 반려일경우 버튼이 나오지않음
      rightContent={(state.reqstSttus === StatusState.REJECT || state.reqstSttus === StatusState.SPM_REQUEST || state.reqstSttus === StatusState.EST_APPROVE || state.reqstSttus === StatusState.CANCEL) ? '' :
        <CustomLoadingButton
          label={"사용금액 재설정"} type={"small"} color={"list"}
          onClick={async () => {
            await EstimationService.putEstmtModifyPrice({
              dscntId: dscntId,
              reqstId: id!.toString(),
              usgtm: state.usgtm,
            })
            addModal({open: true, content: '사용금액 재설정이 완료되었습니다.', type: 'normal', isDist: true})
          }}/>
      }>
      <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
        <Table>
          <TableBody>
            <TableRow>
              {// 신청상태가 신청일때만 가능.
                state.reqstSttus === StatusState.REJECT || state.reqstSttus === StatusState.SPM_REQUEST || state.reqstSttus === StatusState.EST_APPROVE || state.reqstSttus === StatusState.CANCEL ?
                  <TableTextCell
                    title={"사용시간"} label={`${Math.round(state.usgtm) || 0}시간`}
                    thWidth={"13%"} tdWidth={"21%"}/> :

                  <TableTextFieldCell
                    division label={"조정 사용시간"} defaultLabel={`${Math.round(state.usgtm) || 0}`}
                    additionDirection={'row'}
                    additionContent={<Fragment>
                      <HorizontalInterval size={'15px'}/>
                      <Box width={'50px'}>{'시간'}</Box>
                    </Fragment>}
                    thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                    const updateUsgtm = {...state, usgtm: Number(text)};
                    setState(updateUsgtm);
                  }}/>
              }
              <TableTextCell
                title={"사용금액"}
                label={`${Math.round(state.rntfeeHour * state.usgtm).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdSpan={3}/>
            </TableRow>

            <TableRow>
              { // 신청상태가 신청일때만 가능.
                (state.reqstSttus === StatusState.REJECT || state.reqstSttus === StatusState.SPM_REQUEST || state.reqstSttus === StatusState.EST_APPROVE || state.reqstSttus === StatusState.CANCEL) ?
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
            </TableRow>

            <TableRow>
              <TableTextCell
                division title={"할인금액"}
                label={`${Math.round((state.rntfeeHour * state.usgtm) * Number(state.dscntRate) / 100).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdWidth={"21%"}/>

              <TableTextCell
                title={"할인적용금액"}
                label={`${Math.round((state.rntfeeHour * state.usgtm) - ((state.rntfeeHour * state.usgtm) * Number(state.dscntRate) / 100)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}
                thWidth={"13%"} tdSpan={3}/>
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
          navigate(-1);
        }}/>

      <Stack flexDirection={"row"}>
        {
          (state.reqstSttus === StatusState.APPLY || state.reqstSttus === StatusState.SPM_REQUEST) && <>
            <CustomButton
              label={"보완"}
              type={"largeList"}
              onClick={() => {
                setOpen(!open)
              }}
            />
            <HorizontalInterval size={"10px"}/>
            <CustomLoadingButton
              label={"반려"}
              type={"largeList"}
              onClick={async () => {
                await EstimationService.putUseEquipEstmtCheck({
                  reqstSttus: StatusState.REJECT,
                  estmtId: id!.toString()
                })
                setState({...state, reqstSttus: StatusState.REJECT})
                addModal({open: true, isDist: true, content: '반려되었습니다.'})
                window.scrollTo(0, 0)
              }}
            />
            <HorizontalInterval size={"10px"}/>
            <CustomLoadingButton
              label={"견적서 발급"}
              type={"largeList"}
              onClick={async () => {
                await EstimationService.putUseEquipEstmtCheck({
                  reqstSttus: StatusState.EST_APPROVE,
                  estmtId: id!.toString()
                })
                setState({...state, reqstSttus: StatusState.EST_APPROVE})
                addModal({open: true, isDist: true, content: '견적서 발급처리되었습니다.'})
                /* if (dscntId === "0") {
                   EstimationService.putEstmtModifyPrice({
                     dscntId: state.dscntId,
                     reqstId: id!.toString(),
                     usgtm: state.usgtm
                   })
                 }*/
                // window.scrollTo(0, 0)
              }}
            />
          </>}
        {
          (state.reqstSttus === StatusState.EST_APPROVE) && personInfo && <>
            {!state.reqstId &&
              <CustomLoadingButton
                label={"신청취소"}
                type={"largeList"}
                onClick={async () => {
                  await EstimationService.putUseEquipEstmtCheck({
                    reqstSttus: StatusState.CANCEL,
                    estmtId: id!.toString()
                  })
                  setState({...state, reqstSttus: StatusState.CANCEL})
                  addModal({open: true, isDist: true, content: '신청취소되었습니다.'})
                }}
              />
            }
                <HorizontalInterval size={"10px"}/>
                <CustomButton // 견적서 발급
                label={"견적서 다운로드"}
                type={"largeList"}
                onClick={async () => {
                  const prdUrl = `${process.env.REACT_APP_DOMAIN}/ozreport/report.jsp`
                  const devUrl = `https://portal.atops.or.kr/ozreport/report.jsp`
                  if (!(Number(dayjs(personInfo.creatDt).format('YYYYMMDD')) > (Number(dayjs(new Date()).format('YYYYMMDD'))-3))){
                    addModal({open: true, isDist: true, content: '견적서가 폐기되었습니다..'})
                    return
                  }else {
                    post(`${process.env.REACT_APP_DOMAIN}` === "prd" ? prdUrl : devUrl, est)
                  }
                }}
            />
          </>
        }
        {/*<HorizontalInterval size={"10px"}/>*/}
        {/*<CustomButton
          label={"저장"}
          onClick={async () => {
         const result = await EquipmentInformationService.putEquipmentMgtInfo(id!, req!)
         if (result.success) {
            addModal({
              open: true,
              content: "저장 완료."
            })
          }}
          }
        />*/}
      </Stack>
    </Stack>

    <ComplementModal open={open} setOpen={setOpen} state={state} setState={setState}/>
  </Stack>
}

const ComplementModal: React.FC<{
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
  state: EqpmnEstmtDetailData
  setState: Dispatch<SetStateAction<any>>
}> = props => {
  const {id} = useParams()
  const [details, setDetails] = useState(props.state.rsndqf || '')
  const {addModal} = useGlobalModalStore()

  return <ModalComponents
    open={(props.open)}
    type={"submit"} title={'보완요청'}
    onConfirm={async () => {
      await EstimationService.putUseEquipEstmtCheck({
        rsndqf: details,
        reqstSttus: StatusState.SPM_REQUEST,
        estmtId: id!.toString()
      })
      props.setState({...props.state, reqstSttus: StatusState.SPM_REQUEST})
      addModal({open: true, isDist: true, content: '보완요청이 완료되었습니다.'})
      // const endCheck = await EquipmentInformationService.putEquipmentsEndCheck(id!, correctData)
      // mgtInfoData.setModalOpen("checkFinish",false);
      // mgtInfoData.setMgtInfoData({
      //   ...mgtInfoData.mgtInfoData!,
      //   available: endCheck.eqpmntStateInfo!.available,
      //   eqpmnSt: endCheck.eqpmntStateInfo!.eqpmnSt,
      //   inspectSt: "0",
      //   checkParam: null
      // })
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