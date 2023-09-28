import React, {Fragment, useEffect, useLayoutEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {EquipmentInformationService} from "~/service/EquipmentMgt/EquipmentInformationService";
import {
  Box,
  Stack,
  Table,
  TableBody,
  TableContainer,
  TableRow
} from "@mui/material";
import {HorizontalInterval, LoadingProgress, SubContents} from "shared/components/LayoutComponents";
import {
  TableDateTermCell,
  TableTextFieldCell,
  TableRadioCell,
  TableTextCell
} from "shared/components/TableComponents";
import {CustomButton, CustomLoadingButton} from "shared/components/ButtonComponents";
import {useEquipmentDetailStore} from "~/store/EquipmentMgt/EquipmentDetailStore";
import {
  EquipmentCategoryData,
  EquipmentMgtInfoRequest,
} from "~/service/Model";
import {dayFormat} from "shared/utils/stringUtils";
import {MgtInfoModals} from "~/pages/EquipmentMgt/InfomationMgt/Detail/MgtInfoModals/MgtInfoModals";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {ModalParam} from "shared/components/ModalComponents";

type CategoryView = {
  depth1Nm: string
  depth1: string[]
  depth2Nm: string
  depth2: string[]
}

/* 장비 정보 관리 상세 - 관리 정보 */
export const EquipmentMgtInfo = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_ST","MEMBER_TYPE"])
  const mgt = EquipmentInformationService.getEquipmentsMgtInfo(id!.toString())
  const [req, setReq] = useState<EquipmentMgtInfoRequest | null>(null)
  const mgtInfo = useEquipmentDetailStore();
  const [category, setCategory] = useState<CategoryView>({
    depth1Nm: "", depth1: [], depth2Nm: "", depth2: []
  });
  const navigate = useNavigate();
  const {addModal} = useGlobalModalStore();
  useEffect(() => {
    if (!mgt.isLoading && !mgt.isFetching) {
      if (!!mgt.data) {
        CategorySetting(mgt.data.eqpmnClParam, mgt.data.eqpmnClId)
        mgtInfo.setMgtInfoData({
          ...mgt.data,
          usefulBeginHour: new Date().setHours(mgt.data.usefulBeginHour, 0),
          usefulEndHour: new Date().setHours(mgt.data.usefulEndHour, 0)
        })
          setReq(mgt.data)
        // if (req) {
        // }
      }
    }
  }, [mgt.data, mgt.isLoading, mgt.isFetching])

  // useEffect(() => {
  //  return () => {
  //    setReq(null);
  //  }
  // }, [])

  const CategorySetting = (category: EquipmentCategoryData[], selectId: string) => {
    const cur = category.find(f => f.eqpmnClId === selectId)
    if (cur?.eqpmnLclasId === "ROOT") {
      setCategory({
        depth1Nm: cur.eqpmnClNm,
        depth2Nm: " ",
        depth1: category.filter(f => f.depth === 1 && f.useAt).flatMap(name => name.eqpmnClNm),
        depth2: category.filter(f => f.depth === 2 && f.useAt &&
          f.eqpmnLclasId === cur.eqpmnClId).flatMap(name => name.eqpmnClNm)
      })
    } else if (cur) {
      setCategory({
        depth1Nm: category.find(f => f.eqpmnClId === cur.eqpmnLclasId)?.eqpmnClNm || "",
        depth2Nm: cur.eqpmnClNm,
        depth1: category.filter(f => f.depth === 1 && f.useAt).flatMap(name => name.eqpmnClNm),
        depth2: category.filter(f => f.depth === 2 && f.useAt &&
          f.eqpmnLclasId === cur.eqpmnLclasId).flatMap(name => name.eqpmnClNm)
      })
    }
  }

  if (!req || mgt.isLoading || !mgtInfo.mgtInfoData) return <LoadingProgress/>

  const correctParam = mgtInfo.mgtInfoData.correctParam;
  const repairParam = mgtInfo.mgtInfoData.repairParam;

  return <Fragment>
    <Stack spacing={"40px"}>
      <SubContents title={"장비사용정보"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"장비상태"} label={mgtInfo.mgtInfoData.disuseAt ? "불용" : toCommtCodeName(commtCode, 'EQPMN_ST', mgtInfo.mgtInfoData.eqpmnSttus)}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"불용여부"} label={mgtInfo.mgtInfoData.disuseAt ? "불용" : "정상"}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"등록일"} label={dayFormat(mgtInfo.mgtInfoData.creatDt)}
                  thWidth={"13%"} tdWidth={"21%"}
                />
              </TableRow>
              <TableRow>
                <TableTextCell
                  division title={"장비명"} label={mgtInfo.mgtInfoData?.eqpmnNmKorean || '장비명'}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"장비 분야"} label={category ? category.depth2Nm ? `${category.depth1Nm} - ${category.depth2Nm}` : `${category.depth1Nm}` : '장비분야가 출력됩니다.'}
                  thWidth={"13%"} tdSpan={3}
                />
                {/*<TableRadioCell*/}
                {/*  row label={"불용여부"} radioLabel={["불용", "정상"]}*/}
                {/*  thWidth={"13%"} tdWidth={"21%"} defaultLabel={mgtInfo.mgtInfoData.disuseAt ? "정상" : "불용"}*/}
                {/*  onClick={(selected) => {*/}
                {/*    setReq((state) => ({*/}
                {/*      ...state!,*/}
                {/*      disuseAt: selected === "정상"*/}
                {/*    }))*/}
                {/*  }}/>*/}
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents title={"장비반출정보"}>
        <TableContainer sx={{borderTop: "1px solid #d7dae6", width: "100%"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"반출여부"} label={mgtInfo.mgtInfoData.tkoutAt ? "반출중" : "미반출"}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"구분"} label={mgtInfo.mgtInfoData.tkoutParam ? toCommtCodeName(commtCode, 'MEMBER_TYPE', mgtInfo.mgtInfoData.tkoutParam.mberDiv) : ''}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"업체명"} label={mgtInfo.mgtInfoData.tkoutParam?.entrprsNm || ""}
                  thWidth={"13%"}
                />
              </TableRow>

              <TableRow>
                <TableTextCell
                  division title={"이름"} label={mgtInfo.mgtInfoData.tkoutParam?.userNm || ""}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  division title={"직위"} label={mgtInfo.mgtInfoData.tkoutParam?.ofcps || ""}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"연락처"} label={mgtInfo.mgtInfoData.tkoutParam?.cttpc || ""}
                  thWidth={"13%"}
                />
              </TableRow>

              <TableRow>
                <TableTextCell
                  division title={"이메일"} label={mgtInfo.mgtInfoData.tkoutParam?.email || ""}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"사업참여 여부"} label={mgtInfo.mgtInfoData.tkoutParam ?
                  mgtInfo.mgtInfoData.tkoutParam.partcptnDiv ? "참여" : "미참여" : ''}
                  thWidth={"13%"} tdSpan={3}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents
        title={"장비수리등록"}
        rightContent={<Fragment>
          <Stack flexDirection={"row"}>
            {
              mgtInfo.mgtInfoData.repairId?.length > 0 &&
              <CustomLoadingButton type={"small"} color={"list"} label={"완료내역수정"} onClick={async () => {
                if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return
                const repair = await EquipmentInformationService.getEquipmentsEndRepair(id!, 'REPAIR_MODIY')
                mgtInfo.setCorrectData(repair);
                mgtInfo.setModalOpen("repairContent", true);
              }}/>
            }
            <HorizontalInterval size={"10px"}/>
            {
              mgtInfo.mgtInfoData.repairParam && mgtInfo.mgtInfoData.eqpmnSttus === 'REPAIR' ?
                <CustomLoadingButton type={"small"} color={"list"} label={"수리완료"} onClick={async () => {
                  if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return
                  const repair = await EquipmentInformationService.getEquipmentsEndRepair(id!, 'REPAIR')
                  mgtInfo.setCorrectData(repair);
                  mgtInfo.setModalOpen("repairEnd", true);
                }}/> : <CustomButton type={"small"} color={"list"} label={"수리등록"} onClick={() => {
                  if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return
                  const curTime = new Date()
                  mgtInfo.setCorrectData({
                    manageDiv: 'REPAIR',
                    manageBeginDt: curTime.getTime(),
                    manageEndDt: curTime.getTime(),
                    crrcInstt: "",
                    manageResn: "",
                  });
                  mgtInfo.setModalOpen("repairRegister", true);
                }}/>
            }
          </Stack>
        </Fragment>
        }>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextCell
                  division title={"수리 기간"}
                  label={
                    repairParam && mgtInfo.mgtInfoData.eqpmnSttus === 'REPAIR' ?
                      `${dayFormat(repairParam.manageBeginDt)} ~ ${dayFormat(repairParam.manageEndDt)}` : ""
                  }
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"수리 내역"} label={
                  repairParam && mgtInfo.mgtInfoData.eqpmnSttus === 'REPAIR' ? repairParam.manageResn : ""
                }
                  thWidth={"13%"}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents
        title={"장비점검설정"}
        rightContent={<Fragment>
          {
            mgtInfo.mgtInfoData.eqpmnSttus === 'INSPECT' &&
            <CustomLoadingButton type={"small"} color={"list"} label={"점검완료"} onClick={async () => {
              if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return
              const endCheck = await EquipmentInformationService.getEquipmentsEndCheck(id!, 'INSPECT')
              mgtInfo.setCorrectData({
                ...endCheck,
                manageEndDt: new Date().getTime()
              });
              mgtInfo.setModalOpen("checkFinish", true);
            }}/>
          }
        </Fragment>
        }>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableRadioCell
                  row thWidth={"13%"} tdSpan={3}
                  label={"장비 점검"} defaultLabel={mgtInfo.mgtInfoData.chckTrgetAt ? "예" : "아니오"}
                  radioLabel={['예', '아니오']}
                  onClick={(selected) => {
                    setReq((state) => ({
                      ...state!,
                      chckTrgetAt: selected === "예"
                    }))
                  }}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents
        title={"장비교정등록"}
        rightContent={<Fragment>
          { !mgtInfo.mgtInfoData.crrcTrgetAt ? '' :
            mgtInfo.mgtInfoData.eqpmnSttus === 'CORRECTION' ? <Stack flexDirection={"row"}>
                <CustomButton type={"small"} color={"list"} label={"교정완료"} onClick={async () => {
                  if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return

                  const correct = await EquipmentInformationService.getEquipmentsCorrectFinish(id!, 'CORRECTION')
                  mgtInfo.setCorrectData(correct);
                  mgtInfo.setModalOpen("correctionFinish", true)
                }}/>
              </Stack> :
              <CustomButton type={"small"} color={"list"} label={"교정등록"} onClick={() => {
                if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return
                const curTime = new Date()
                mgtInfo.setCorrectData({
                  manageDiv: 'CORRECTION',
                  manageBeginDt: curTime.getTime(),
                  manageEndDt: curTime.getTime(),
                  crrcInstt: "",
                  manageResn: "",
                });
                mgtInfo.setModalOpen("correctionRegister", true)
              }}/>
          }
        </Fragment>
        }>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableRadioCell
                  row division label={"교정대상여부"}
                  defaultLabel={mgtInfo.mgtInfoData.crrcTrgetAt ? "예" : "아니오"}
                  radioLabel={["예", "아니오"]}
                  thWidth={"13%"} tdWidth={"21%"}
                  onClick={(selected: string) => {
                    setReq((state) => ({
                      ...state!,
                      crrcTrgetAt: selected === "예"
                    }))
                  }}
                />
                <TableTextFieldCell
                  division label={"교정주기"} defaultLabel={mgtInfo.mgtInfoData.crrcTrgetAt ? mgtInfo.mgtInfoData.crrcCycle.toString() : ''}
                  // division label={"교정주기"} defaultLabel={correctParam?.crrcCycle?.toString() || ''}
                  disabled={req?.crrcTrgetAt === false || false}
                  type={"number"}
                  thWidth={"13%"} tdWidth={"21%"}
                  additionDirection={'row'}
                  additionContent={<Fragment>
                    <HorizontalInterval size={'15px'}/>
                    <Box width={'50px'}>{'개월'}</Box>
                  </Fragment>}
                  onChange={(selected: string) => {
                    setReq((state) => ({
                      ...state!,
                      crrcCycle: Number(selected)
                    }))
                  }}
                />
                <TableTextCell
                  title={"마지막 교정일"}
                  label={correctParam ? dayFormat(correctParam.manageEndDt!) : ""}
                  thWidth={"13%"}
                />
              </TableRow>
              <TableRow>
                <TableTextCell
                  division title={"교정기간"}
                  label={correctParam ? `${dayFormat(correctParam!.manageBeginDt!)} ~ ${dayFormat(correctParam!.manageEndDt!)}` : ""}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableTextCell
                  title={"교정실시기관"}
                  label={correctParam ? correctParam.crrcInstt! : ""}
                  tdSpan={3} thWidth={"13%"}
                />
              </TableRow>
              <TableRow>
                <TableTextCell
                  title={"교정결과"}
                  label={correctParam ? correctParam.manageResn! : ""}
                  tdSpan={5} thWidth={"13%"}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents title={"상세설정"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextFieldCell
                  division label={"1시간 사용료"}
                  defaultLabel={mgtInfo.mgtInfoData.rntfeeHour?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                  placeholder={'숫자만 입력해주세요.'}
                  regexPattern={/[^0-9.]/g}
                  additionDirection={'row'}
                  additionContent={<Fragment>
                    <HorizontalInterval size={'15px'}/>
                    <Box width={'50px'}>{'원'}</Box>
                  </Fragment>}
                  thWidth={"13%"} tdWidth={"21%"}
                  onChange={(text: string) => {
                    setReq((state) => ({...state!, rntfeeHour: Number(text)}))
                  }}
                />
                <TableDateTermCell
                  division type={"Time"} label={"1일 가용시간"}
                  defaultStartTime={mgtInfo.mgtInfoData.usefulBeginHour}
                  defaultEndTime={mgtInfo.mgtInfoData.usefulEndHour}
                  onChange={(beginTime, endTime) => {
                    setReq((state) => ({
                      ...state!,
                      usefulBeginHour: new Date(beginTime).getHours(),
                      usefulEndHour: new Date(endTime).getHours()
                    }))
                  }}
                  thWidth={"13%"} tdWidth={"21%"}
                />
                <TableRadioCell
                  row label={"사용률 저조장비"}
                  defaultLabel={mgtInfo.mgtInfoData.useRateInctvSetupAt ? "설정" : "설정안함"}
                  radioLabel={["설정", "설정안함"]}
                  thWidth={"13%"} tdWidth={"21%"}
                  onClick={(selected: string) => {
                    setReq((state) => ({...state!, useRateInctvSetupAt: selected === "설정"}))
                  }}
                />
              </TableRow>
              <TableRow>
                <TableRadioCell
                  row division label={"법정공휴일 휴일포함"}
                  defaultLabel={mgtInfo.mgtInfoData.hldyInclsAt ? "포함" : "포함안함"}
                  radioLabel={["포함", "포함안함"]}
                  thWidth={"13%"} tdWidth={"21%"}
                  onClick={(selected: string) => {
                    setReq((state) => ({...state!, hldyInclsAt: selected === "포함"}))
                  }}
                />
                <TableRadioCell
                  row division label={"반출 시 휴일포함"}
                  defaultLabel={mgtInfo.mgtInfoData.tkoutHldyInclsAt ? "포함" : "포함안함"}
                  radioLabel={["포함", "포함안함"]}
                  thWidth={"13%"} tdWidth={"21%"}
                  onClick={(selected: string) => {
                    setReq((state) => ({...state!, tkoutHldyInclsAt: selected === "포함"}))
                  }}
                />
                <TableRadioCell
                  row label={"미반출 시 휴일포함"}
                  defaultLabel={mgtInfo.mgtInfoData.nttkoutHldyInclsAt ? "포함" : "포함안함"}
                  radioLabel={["포함", "포함안함"]}
                  thWidth={"13%"} tdWidth={"21%"}
                  onClick={(selected: string) => {
                    setReq((state) => ({...state!, nttkoutHldyInclsAt: selected === "포함"}))
                  }}
                />
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>

      <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%"}}>
        <CustomButton
          label={"목록"} type={"largeList"} color={"outlined"}
          onClick={() => {
            navigate('/tsp-admin/EquipmentMgt/InfomationMgt');
          }}
        />
        <Stack flexDirection={"row"}>
          <CustomLoadingButton
            label={mgtInfo.mgtInfoData.disuseAt ? "불용해제" : "불용설정"}
            type={"largeList"}
            onClick={async () => {
              if (mgtInfo.mgtInfoData) {
                const result = await EquipmentInformationService.putEquipmentDisuseAt(id!, !mgtInfo.mgtInfoData.disuseAt)
                if (result.success) {
                  addModal({
                    open: true, isDist: true, type: 'normal',
                    content: result.disuseAt ? '불용설정 되었습니다.' : '불용해제 되었습니다.',
                  })
                  mgtInfo.setMgtInfoData({...result, usefulBeginHour: mgtInfo.mgtInfoData.usefulBeginHour, usefulEndHour: mgtInfo.mgtInfoData.usefulEndHour})
                }
              }
            }}
          />
          <HorizontalInterval size={"10px"}/>
          <CustomLoadingButton
            label={"삭제"}
            type={"largeList"}
            onClick={async () => {
              if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return
              const result = await EquipmentInformationService.deleteEquipment(id!)
              if (result.success) {
                addModal({
                  open: true, isDist: true, type: 'normal', content: '삭제되었습니다.',
                  onConfirm: () => {
                    navigate(-1)
                  }
                })
              }
            }}
          />
          <HorizontalInterval size={"10px"}/>
          <CustomLoadingButton
            label={"저장"}
            onClick={async () => {
              if (isDisuseCheck(addModal, mgtInfo.mgtInfoData?.disuseAt)) return

              if (req.usefulBeginHour >= req.usefulEndHour) {
                addModal({open:true, isDist:true, content: '잘못된 시간입니다.\n시간을 다시 확인하세요.'})
              }
              else {
                const result = await EquipmentInformationService.putEquipmentMgtInfo(id!, req!)
                if (result.success) {
                  mgtInfo.setMgtInfoData({...result, usefulBeginHour: req.usefulBeginHour, usefulEndHour: req.usefulEndHour})
                  addModal({
                    open: true, isDist: true,
                    type: 'normal',
                    content: "저장 완료."
                  })
                }
              }

              /*if (result.success) {
                addModal({
                  open: true, isDist: true,
                  type: 'normal',
                  content: "저장 완료."
                })
                mgtInfo.setMgtInfoData(result)
              }*/
            }}
          />
        </Stack>
      </Stack>
    </Stack>

    <MgtInfoModals/>
  </Fragment>
}

const isDisuseCheck = (addModal: (addParam: ModalParam) => void, disuseAt?: boolean) => {
  // const {addModal} = useGlobalModalStore()

  if (disuseAt) {
    addModal({
      open: true, isDist: true,
      type: 'normal',
      content: "불용 상태에서는 변경이 불가능합니다."
    })
    return true
  }
  return false
}