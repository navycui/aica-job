import {
  EquipmentStateChangeModalData, EquipmentData,
  EquipmentMgtInfoData,
} from "~/service/Model";
import create from "zustand";
import {toStringFullDayFormat} from "shared/utils/stringUtils";

export type EquipmentEditState = "correctionRegister" | "correctionFinish" |
  "repairRegister" | "repairEnd" | "repairContent" | "checkRegister" | "checkFinish" | "checkContent"

interface State {
  mgtInfoData?: EquipmentMgtInfoData,
  detailInfoData?: EquipmentData,
  modalOpen: { type: EquipmentEditState, isOpen: boolean }[]
  equipmentCorrectData?: EquipmentStateChangeModalData

  setMgtInfoData: (data: EquipmentMgtInfoData) => void
  setCorrectData: (data?: EquipmentStateChangeModalData) => void
  setDetailData: (data: EquipmentData) => void
  setModalOpen: (editState: EquipmentEditState, isOpen: boolean) => void
  clear: () => void
}

export const useEquipmentDetailStore = create<State>(set => ({
  modalOpen: [
    {type: "correctionRegister", isOpen: false},
    {type: "correctionFinish", isOpen: false},
    {type: "repairRegister", isOpen: false},
    {type: "repairEnd", isOpen: false},
    {type: "repairContent", isOpen: false},
    {type: "checkRegister", isOpen: false},
    {type: "checkFinish", isOpen: false},
  ],

  setMgtInfoData: (data: EquipmentMgtInfoData) => {
    set((state) => ({
      mgtInfoData: { ...data}
    }))
  },
  setCorrectData: (data?: EquipmentStateChangeModalData) => {
    set((state) => ({
      equipmentCorrectData: data
    }))
  },
  setDetailData: (data: EquipmentData) => {
    set(state => ({
      detailInfoData: data
    }))
  },
  setModalOpen: (editState: EquipmentEditState, isOpen: boolean) => {
    set(state => ({
      modalOpen: [
        ...state.modalOpen.filter(f => f.type != editState),
        {type: editState, isOpen: isOpen}
      ]
    }))
  },
  clear: () => {
    set(state => ({
      mgtInfoData: undefined,
      equipmentCorrectData: undefined,
      modalOpen: [
        {type: "correctionRegister", isOpen: false},
        {type: "correctionFinish", isOpen: false},
        {type: "repairRegister", isOpen: false},
        {type: "repairEnd", isOpen: false},
        {type: "repairContent", isOpen: false},
        {type: "checkRegister", isOpen: false},
        {type: "checkFinish", isOpen: false},
      ]
    }))
  },
}))