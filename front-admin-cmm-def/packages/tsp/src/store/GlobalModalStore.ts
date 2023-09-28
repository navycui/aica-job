import {EquipmentClassifyData, EquipmentClassifyRowData} from "~/service/Model";
import create from "zustand";
import {ModalParam} from "shared/components/ModalComponents";

interface State {
  modal: ModalParam[],
  addModal: (addParam:ModalParam) => void
  closeModal: (closeModal:ModalParam) => void
}

export const useGlobalModalStore = create<State>(set => ({
  modal: [],
  addModal: (addParam:ModalParam) => {
    set((state) => ({
      modal: [...state.modal, addParam]
    }))
  },
  closeModal: (closeModal: ModalParam) => {
    set((state) => ({
      modal: state.modal.filter(f => f != closeModal)
    }))
  },
}))