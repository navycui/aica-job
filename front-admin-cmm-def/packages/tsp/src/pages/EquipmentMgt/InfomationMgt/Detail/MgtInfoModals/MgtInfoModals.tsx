import {useParams} from "react-router-dom";
import {useEquipmentDetailStore} from "~/store/EquipmentMgt/EquipmentDetailStore";
import React, {useEffect, useState} from "react";
import {EquipmentStateChangeModalData} from "~/service/Model";
import {ModalComponents} from "shared/components/ModalComponents";
import {EquipmentInformationService} from "~/service/EquipmentMgt/EquipmentInformationService";
import {Table, TableBody, TableContainer, TableRow} from "@mui/material";
import {TableDateTermCell, TableTextFieldCell} from "shared/components/TableComponents";
import {toDayAndTimeFormat, toStringFullDayFormat} from "shared/utils/stringUtils";
import styled from "@emotion/styled";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

export const MgtInfoModals = () => {

  return <>
    <CorrectRegisterModal/>
    <CorrectFinishModal/>

    <RepairRegister/>
    <RepairEnd/>
    <RepairContentEdit/>

    <CheckFinish/>
  </>
}

const CorrectRegisterModal = () => {
  const {id} = useParams();
  const {addModal} = useGlobalModalStore()
  const mgtInfoData = useEquipmentDetailStore();

  if (!mgtInfoData.equipmentCorrectData) return <></>
  const isOpen = mgtInfoData.modalOpen.find(f => f.type === "correctionRegister")?.isOpen

  return <ModalComponents
    open={isOpen || false}
    type={"save"} title={"장비교정등록"}
    onConfirm={async () => {
      if ( (mgtInfoData.equipmentCorrectData?.manageEndDt! - mgtInfoData.equipmentCorrectData?.manageBeginDt!) < 0 ){
        addModal({isDist: true, open: true, type: 'normal', content: '기간 설정 확인 부탁드립니다.!'})
        return
      }
      const register = await EquipmentInformationService.postEquipmentsCorrect(id!, {
        ...mgtInfoData.equipmentCorrectData!,
        manageDiv: 'CORRECTION'
      })
      mgtInfoData.setModalOpen("correctionRegister", false);
      mgtInfoData.setMgtInfoData(register)
      mgtInfoData.setCorrectData(undefined);
      addModal({open: true, isDist: true, type: 'normal', content: '장비 교정이 등록되었습니다.'})
    }}
    onClose={() => {
      mgtInfoData.setModalOpen("correctionRegister", false)
      mgtInfoData.setCorrectData(undefined)
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              type={"DateTime"} label={"기간"} thWidth={"20%"}
              defaultStartTime={mgtInfoData.equipmentCorrectData.manageBeginDt}
              defaultEndTime={mgtInfoData.equipmentCorrectData.manageEndDt}
              onChange={(beginTime, endTime) => {
                mgtInfoData.setCorrectData({
                  ...mgtInfoData.equipmentCorrectData!,
                  manageBeginDt: new Date(beginTime).getTime(),
                  manageEndDt: new Date(endTime).getTime()
                })
              }}
            />
          </TableRow>
          <TableRow>
            <TableTextFieldCell
              label={"교정기관"} multiline defaultLabel={mgtInfoData.equipmentCorrectData.crrcInstt}
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, crrcInstt: text})
              }}/>
          </TableRow>
          <TableRowTextField>
            <TableTextFieldCell
              label={"교정내역"} multiline defaultLabel={mgtInfoData.equipmentCorrectData.manageResn}
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, manageResn: text})
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const CorrectFinishModal = () => {
  const {id} = useParams();
  const mgtInfoData = useEquipmentDetailStore();
  const {addModal} = useGlobalModalStore()

  if (!mgtInfoData.equipmentCorrectData) return <></>
  const isOpen = mgtInfoData.modalOpen.find(f => f.type === "correctionFinish")?.isOpen

  return <ModalComponents
    open={isOpen || false}
    type={"save"} title={"장비교정완료"}
    onConfirm={async () => {
      if ( (mgtInfoData.equipmentCorrectData?.manageEndDt! - mgtInfoData.equipmentCorrectData?.manageBeginDt!) < 0 ){
        addModal({isDist: true, open: true, type: 'normal', content: '기간 설정 확인 부탁드립니다.!'})
        return
      }
      const finish = await EquipmentInformationService.putEquipmentsCorrectFinish(id!, {
        ...mgtInfoData.equipmentCorrectData!,
        manageDiv: 'CORRECTION'
      })
      mgtInfoData.setModalOpen("correctionFinish", false);
      mgtInfoData.setMgtInfoData(finish)
      mgtInfoData.setCorrectData(undefined);
      addModal({open: true, isDist: true, type: 'normal', content: '장비 교정이 완료되었습니다.'})
    }}
    onClose={() => {
      mgtInfoData.setModalOpen("correctionFinish", false)
      mgtInfoData.setCorrectData(undefined)
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              type={"DateTime"} label={"기간"} thWidth={"20%"} disableStartTime
              defaultStartTime={mgtInfoData.equipmentCorrectData.manageBeginDt}
              defaultEndTime={mgtInfoData.equipmentCorrectData.manageEndDt}
              onChange={(beginTime, endTime) => {
                mgtInfoData.setCorrectData({
                  ...mgtInfoData.equipmentCorrectData!,
                  manageBeginDt: new Date(beginTime).getTime(),
                  manageEndDt: new Date(endTime).getTime()
                })
              }}/>
          </TableRow>
          <TableRow>
            <TableTextFieldCell
              label={"교정기관"} multiline defaultLabel={mgtInfoData.equipmentCorrectData.crrcInstt || ""}
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, crrcInstt: text})
              }}/>
          </TableRow>
          <TableRowTextField>
            <TableTextFieldCell
              label={"교정결과"} multiline defaultLabel={mgtInfoData.equipmentCorrectData.manageResult}
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, manageResult: text})
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const RepairEnd = () => {
  const {id} = useParams();
  const {addModal} = useGlobalModalStore()
  const mgtInfoData = useEquipmentDetailStore();

  if (!mgtInfoData.equipmentCorrectData) return <></>
  const isOpen = mgtInfoData.modalOpen.find(f => f.type === "repairEnd")?.isOpen
  return <ModalComponents
    open={isOpen || false}
    type={"save"} title={"장비수리완료"}
    onConfirm={async () => {
      const endRepair = await EquipmentInformationService.putEquipmentsEndRepair(id!, mgtInfoData.equipmentCorrectData!)
      mgtInfoData.setModalOpen("repairEnd", false);
      mgtInfoData.setMgtInfoData(endRepair)
      mgtInfoData.setCorrectData(undefined);
      addModal({isDist: true, open: true, type: 'normal', content: '수리 완료 처리됐습니다.'})
    }}
    onClose={() => {
      mgtInfoData.setModalOpen("repairEnd", false)
      mgtInfoData.setCorrectData(undefined)
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              type={"DateTime"} label={"기간"} thWidth={"20%"}
              disableStartTime defaultStartTime={mgtInfoData.equipmentCorrectData.manageBeginDt}
              disableEndTime defaultEndTime={mgtInfoData.equipmentCorrectData.manageEndDt}
              onChange={(beginTime, endTime) => {
                mgtInfoData.setCorrectData({
                  ...mgtInfoData.equipmentCorrectData!,
                  manageBeginDt: new Date(beginTime).getTime(),
                  manageEndDt: new Date(endTime).getTime()
                })
              }}
            />
          </TableRow>
          <TableRowTextField>
            <TableTextFieldCell
              label={"점검내역"} multiline
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              defaultLabel={mgtInfoData.equipmentCorrectData.manageResult}
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, manageResult: text})
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const RepairRegister = () => {
  const {id} = useParams();
  const mgtInfoData = useEquipmentDetailStore();
  const {addModal} = useGlobalModalStore()
  // const [correctData, setCorrectData] = useState<EquipmentStateChangeModalData | undefined>(mgtInfoData.equipmentCorrectData)

  if (!mgtInfoData.equipmentCorrectData) return <></>
  const isOpen = mgtInfoData.modalOpen.find(f => f.type === "repairRegister")?.isOpen

  return <ModalComponents
    open={isOpen || false}
    type={"save"} title={"장비수리등록"}
    onConfirm={async () => {
      if ( (mgtInfoData.equipmentCorrectData?.manageEndDt! - mgtInfoData.equipmentCorrectData?.manageBeginDt!) < 0 ){
        addModal({isDist: true, open: true, type: 'normal', content: '기간 설정 확인 부탁드립니다.!'})
        return
      }
      const repair = await EquipmentInformationService.postEquipmentsRepair(id!, mgtInfoData.equipmentCorrectData!)
      mgtInfoData.setMgtInfoData(repair)
      mgtInfoData.setCorrectData(undefined);
      mgtInfoData.setModalOpen("repairRegister", false);
      addModal({isDist: true, open: true, type: 'normal', content: '수리 등록 되었습니다.'})
    }}
    onClose={() => {
      mgtInfoData.setModalOpen("repairRegister", false);
      mgtInfoData.setCorrectData(undefined)
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              type={"DateTime"} label={"기간"} thWidth={"20%"}
              defaultStartTime={mgtInfoData.equipmentCorrectData.manageBeginDt}
              defaultEndTime={mgtInfoData.equipmentCorrectData.manageEndDt}
              onChange={(beginTime, endTime) => {
                mgtInfoData.setCorrectData({
                  ...mgtInfoData.equipmentCorrectData!,
                  manageBeginDt: new Date(beginTime).getTime(),
                  manageEndDt: new Date(endTime).getTime()
                })
              }}
            />
          </TableRow>
          <TableRowTextField>
            <TableTextFieldCell
              label={"점검내역"} multiline wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              defaultLabel={mgtInfoData.equipmentCorrectData.manageResn}
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, manageResn: text})
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const RepairContentEdit = () => {
  const {id} = useParams();
  const {addModal} = useGlobalModalStore()
  const mgtInfoData = useEquipmentDetailStore();

  if (!mgtInfoData.equipmentCorrectData) return <></>
  const isOpen = mgtInfoData.modalOpen.find(f => f.type === "repairContent")?.isOpen

  return <ModalComponents
    open={isOpen || false}
    type={"save"} title={"수리 내역 수정"}
    onConfirm={async () => {
      const result = await EquipmentInformationService.putEquipmentsRepairContent(id!, {
        ...mgtInfoData.equipmentCorrectData!,
        manageDiv: 'REPAIR_MODIY'
      })
      mgtInfoData.setModalOpen("repairContent", false);
      mgtInfoData.setMgtInfoData(result)
      mgtInfoData.setCorrectData(undefined);
      addModal({isDist: true, open: true, type: 'normal', content: '수리 내역이 수정 되었습니다.'})
    }}
    onClose={() => {
      mgtInfoData.setModalOpen("repairContent", false);
      mgtInfoData.setCorrectData(undefined);
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              type={"DateTime"} label={"기간"} thWidth={"20%"} disableStartTime
              defaultStartTime={mgtInfoData.equipmentCorrectData.manageBeginDt}
              defaultEndTime={mgtInfoData.equipmentCorrectData.manageEndDt}
              disableEndTime={mgtInfoData.mgtInfoData!.repairId ? mgtInfoData.mgtInfoData!.repairId.length > 0 : false}
              onChange={(beginTime, endTime) => {
                mgtInfoData.setCorrectData({
                  ...mgtInfoData.equipmentCorrectData!,
                  manageBeginDt: new Date(beginTime).getTime(),
                  manageEndDt: new Date(endTime).getTime()
                })
              }}
            />
          </TableRow>
          <TableRowTextField>
            <TableTextFieldCell
              label={"점검내역"} multiline
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              defaultLabel={mgtInfoData.equipmentCorrectData.manageResult}
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, manageResult: text})
              }}/>
          </TableRowTextField>
        </TableBody>
      </Table>
    </TableContainer>
  </ModalComponents>
}

const CheckFinish = () => {
  const {id} = useParams();
  const {addModal} = useGlobalModalStore()
  const mgtInfoData = useEquipmentDetailStore();

  if (!mgtInfoData.equipmentCorrectData) return <></>
  const isOpen = mgtInfoData.modalOpen.find(f => f.type === "checkFinish")?.isOpen

  return <ModalComponents
    open={isOpen || false}
    type={"save"} title={"장비점검완료"}
    onConfirm={async () => {
      const endCheck = await EquipmentInformationService.putEquipmentsEndCheck(id!, {
        ...mgtInfoData.equipmentCorrectData!,
        manageDiv: 'INSPECT'
      })
      mgtInfoData.setModalOpen("checkFinish",false);
      mgtInfoData.setMgtInfoData(endCheck)
      addModal({open: true, isDist: true, type: 'normal', content: '장비 점검이 완료되었습니다.'})
    }}
    onClose={() => {
      mgtInfoData.setModalOpen("checkFinish",false);
    }}>
    <TableContainer style={{borderTop: "1px solid #d7dae6", width: "650px"}}>
      <Table>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              type={"DateTime"} label={"기간"} thWidth={"20%"}
              disableStartTime defaultStartTime={mgtInfoData.equipmentCorrectData.manageBeginDt}
              disableEndTime defaultEndTime={mgtInfoData.equipmentCorrectData.manageEndDt}
              onChange={(beginTime, endTime) => {
                mgtInfoData.setCorrectData({
                  ...mgtInfoData.equipmentCorrectData!,
                  manageBeginDt: new Date(beginTime).getTime(),
                  manageEndDt: new Date(endTime).getTime()
                })
              }}/>
          </TableRow>
          <TableRowTextField>
            <TableTextFieldCell
              label={"점검내역"} multiline
              wordCount={100} wordLabel={'100자를 넘길 수 없습니다.'} wordCountDisabled
              defaultLabel={mgtInfoData.equipmentCorrectData.manageResult}
              onChange={(text: string) => {
                mgtInfoData.setCorrectData({...mgtInfoData.equipmentCorrectData!, manageResult: text})
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