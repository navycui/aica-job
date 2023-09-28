import {ModalComponents} from "shared/components/ModalComponents";
import {
  Box,
  FormControl, MenuItem, Select,
  Stack, Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField
} from "@mui/material";
import {CheckboxStyle, CustomButton, CustomCheckBoxs, CustomIconButton} from "shared/components/ButtonComponents";
import {DiscountData, SearchParam} from "~/service/Model";
import {DiscountService} from "~/service/EquipmentMgt/DiscountService";
import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react";
import {SelectChangeEvent} from "@mui/material/Select";
import {
  CustomHeadCell,
  TableComponents,
  TableSelectCell,
  TableTextFieldCell,
  WithCustomRowData
} from "shared/components/TableComponents";


export const SaleSelectModal = (props: {
  open: boolean
  onSelect: (data: WithCustomRowData<DiscountData>[]) => void
  onClose: () => void
}) => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [searchText, setSearchText] = useState('')
  const [selected, setSelected] = useState<WithCustomRowData<DiscountData>[]>([])
  const [rowList, setRowList] = useState<WithCustomRowData<DiscountData>[]>([])
  const dscnt = DiscountService.getDscntApply({...pagination, dscntResn:searchText})

  useEffect(() => {
    if (!!dscnt.data) {
      setRowList(dscnt.data.list.map((m) => {
        return {
          key: m.dscntId!,
          ...m,
        }
      }));
      setPagination((state) => ({...state, rowCount: dscnt.data.totalItems}))
    }
  }, [dscnt.data])

  return <ModalComponents
    isDist open={props.open}
    type={"save"}
    title={'할인검색'}
    onClose={props.onClose}
    onConfirm={() => {
      // const selectDiscout = rowList.filter(f => selected.includes(f.dscntId!))
      props.onSelect(selected)
    }}
  >
    <Stack spacing={'15px'}>
      <Stack direction={'row'} alignItems={'center'} spacing={'15px'}
             sx={{
               borderTop: '1px solid rgb(215, 218, 230)',
               borderBottom: '1px solid rgb(215, 218, 230)',
             }}>
        <TableContainer>
          <Table sx={{'> td': {borderBottom: 'none'}}}>
            <TableTextFieldCell label={'할인사유'} onChange={(text) => {
              setSearchText(text)
            }}/>
          </Table>
        </TableContainer>
        <CustomButton label={'검색'} type={'small'} onClick={() => {

        }}/>
      </Stack>

      <ListView
        rowList={rowList} selected={selected} setSelected={setSelected}
        pagination={pagination} setPagination={setPagination}
      />
    </Stack>
  </ModalComponents>
}

const ListView = (props: {
  rowList: WithCustomRowData<DiscountData>[]
  selected: WithCustomRowData<DiscountData>[]
  setSelected: Dispatch<SetStateAction<WithCustomRowData<DiscountData>[]>>
  pagination: { page: number, rowsPerPage: number, rowCount: number }
  setPagination: Dispatch<SetStateAction<{ page: number, rowsPerPage: number, rowCount: number }>>
}) => {

  return <TableComponents<DiscountData>
    hideRowPerPage isCheckBox
    headCells={headCells}
    bodyRows={props.rowList}
    {...props.pagination}
    onChangePagination={(page, rowPerPage) => {
      props.setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    onSelectedKey={(keys: string[]) => {
      const selected = props.selected.map(m => m.key)

      if (selected.length < keys.length){
        // 추가시
        const newKey = keys.find(f => !selected.includes(f))
        const data = props.rowList.find(f => f.key === newKey) as any
        props.setSelected(props.selected.concat(data))
      }else {
        // 삭제시
        const deleteKey = selected.find(f => !keys.includes(f))
        props.setSelected(props.selected.filter(f => f.key != deleteKey))
      }
      // props.setSelected(keys)
    }}
    tableCell={(data) => {

      return (
        <Fragment>
          <TableCell key={"dscntResn-" + data.key} width={'80%'}>{data?.dscntResn}</TableCell>
          <TableCell key={"dscntRate-" + data.key} width={'15%'}>{data?.dscntRate}</TableCell>
        </Fragment>
      )
    }}
  />
}

const headCells: CustomHeadCell<DiscountData>[] = [
  {
    id: 'dscntResn',
    align: "center",
    label: '할인사유',

  },
  {
    id: 'dscntRate',
    align: 'center',
    label: '할인율',
  }
]