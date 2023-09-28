import create from "zustand";
import {EquipmentClassifyData, EquipmentClassifyRowData} from "~/service/Model";

interface State {
  currentCategory?: EquipmentClassifyData,
  categoryFlatten: EquipmentClassifyData[],
  tableRows: EquipmentClassifyRowData[],
  setCurrentCategory: (data: EquipmentClassifyData) => void
  setCategoryFlatten: (data: EquipmentClassifyData[]) => void
  addCategoryFlatten: (data: EquipmentClassifyData[]) => void
  setRow: (rows: EquipmentClassifyRowData[]) => void
}

export const useEquipmentClassifyStore = create<State>(set => ({
  tableRows: [],
  categoryFlatten: [],
  setCurrentCategory: (data: EquipmentClassifyData) => {
    set((state) => ({
      currentCategory: data,
      tableRows: data.child?.map((m, i) => {
        return {
          id: m.eqpmnClId,
          eqpmnClId: m.eqpmnClId,
          ordr: m.ordr,
          eqpmnClNm: m.eqpmnClNm,
          useAt: m.useAt ? "사용" : "미사용",
        }
      }).sort((a, b) => a.ordr - b.ordr),
    }))
  },
  setRow: (rows: EquipmentClassifyRowData[]) => {
    set((state) => ({
      tableRows: rows
    }))
  },
  setCategoryFlatten: (data: EquipmentClassifyData[]) => {
    set((state) => ({
      categoryFlatten: data
    }))
  },
  addCategoryFlatten: (data: EquipmentClassifyData[]) => {
    set((state) => ({
      categoryFlatten: [...state.categoryFlatten, ...data]
    }))
  }
}))