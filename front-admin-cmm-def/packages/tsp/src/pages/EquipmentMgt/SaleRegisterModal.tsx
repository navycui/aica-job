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
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
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
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {useCommtCode} from "~/utils/useCommtCode";


export const SaleRegisterModal = (props: {
  open: boolean
  onClose: () => void
}) => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 2,
    rowCount: 0,
  });
  const [searchText, setSearchText] = useState('')
  const [rowList, setRowList] = useState<WithCustomRowData<DiscountData>[]>([])
  const dscnt = DiscountService.getDscnt({...pagination, dscntResn:searchText})
  const {commtCode} = useCommtCode(['EQPMN_DSCNT_ST'])

  useEffect(() => {
    if (!!dscnt.data) {
      setRowList(dscnt.data.list.map((m) => {
        return {
          key: m.dscntId!,
          ...m,
        }
      }));
      setPagination((state) => ({...state, rowCount: dscnt.data.totalItems}))
    }setSearchText('')
  }, [dscnt.data])

  return <ModalComponents
    isDist open={props.open}
    type={"normal"}
    title={'할인등록'}
    onClose={props.onClose}
    onConfirm={props.onClose}
  >
    <Stack spacing={'5px'} >
      <AdditionContent onAddition={async (data: DiscountData) => {
        if (data.dscntResn.length > 0 && data.dscntRate > 0) {
          const addition = await DiscountService.postDscnt(data)
          if (addition.success) {
          }
        }setSearchText(' ')
      }}/>
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

      <ListView rowList={rowList} setRowList={setRowList} pagination={pagination} setPagination={setPagination}/>
    </Stack>
  </ModalComponents>
}

const ListView = (props: {
  rowList: WithCustomRowData<DiscountData>[]
  setRowList: Dispatch<SetStateAction<WithCustomRowData<DiscountData>[]>>
  pagination: { page: number, rowsPerPage: number, rowCount: number }
  setPagination: Dispatch<SetStateAction<{ page: number, rowsPerPage: number, rowCount: number }>>
}) => {

  const PutData = async(data: WithCustomRowData<DiscountData>,value: string) => {
    const res = await DiscountService.putDscnt([{
      dscntId: data.dscntId,
      dscntRate: data.dscntRate,
      useSttus: value === '사용' ? 'USE' : 'END_USE',
      dscntResn: data.dscntResn
    }])
    if (res.success) {
      props.setRowList(props.rowList.map(m => {
        if (m.dscntId === data.key)
          m.useSttus = value === '사용' ? 'USE' : 'END_USE'

        return m
      }))
    }
  }

  return <TableComponents<DiscountData>
    hideRowPerPage
    headCells={headCells}
    bodyRows={props.rowList}
    {...props.pagination}
    onChangePagination={(page, rowPerPage) => {
      props.setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key: string) => {
      // navigation('/EquipmentMgt/InfomationMgt/' + key);
    }}
    tableCell={(data) => {

      return (
        <Fragment>
          <TableCell key={"dscntResn-" + data.key} width={'65%'}>{data?.dscntResn}</TableCell>
          <TableCell key={"dscntRate-" + data.key} width={'15%'}>{data?.dscntRate}</TableCell>
          <TableCell key={"usesttus-" + data.key} width={'20%'}>
            <FormControl fullWidth>
              <Select
                name={'사용여부'} value={data?.useSttus === 'USE' ? '사용' : '미사용'}
                size={'small'}
                onChange={(event: SelectChangeEvent) => {
                  PutData(data,event.target.value)
                }}>
                {
                  ['사용', '미사용'].map((m, i) => {
                    return <MenuItem key={i} value={m}>{m}</MenuItem>
                  })
                }
              </Select>
            </FormControl>
          </TableCell>
        </Fragment>
      )
    }}
  />
}

const AdditionContent = (props: {
  onAddition: (data: DiscountData) => void
}) => {
  const [addition, setAddition] = useState<DiscountData>({
    dscntResn: '',
    dscntRate: 0,
    useSttus: 'END_USE'
  })
  const {addModal} = useGlobalModalStore();

  return <Stack spacing={'10px'}>
    <Box display={'flex'} width={'100%'} justifyContent={'flex-end'}>
      <CustomButton label={'추가'} type={'small'} onClick={() => {

        props.onAddition(addition)
        if (addition.dscntResn != "") {
          if (addition.dscntRate != 0) {
            addModal({
              open: true, isDist: true, content: '할인 사유가 추가 되었습니다.',
            })

          }
          setAddition({dscntResn: '', dscntRate: 0, useSttus: 'END_USE'})

        }
      }
      }
      />
    </Box>
    <TableContainer sx={{borderTop: '1px solid rgb(64, 99, 236)'}}>
      <TableHead>
        <TableRow>
          <TableCell align={"center"} width={"65%"}>할인 사유</TableCell>
          <TableCell align={"center"} width={"15%"}>할인율</TableCell>
          <TableCell align={"center"} width={"20%"}>사용여부</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        <TableCell>
          <FormControl fullWidth>
            <TextField
              value={addition.dscntResn}
              name={'할인 사유'}
              variant={"outlined"}
              size={"small"}
              onChange={(e) => {
                setAddition({...addition, dscntResn: e.target.value})
              }}/>
          </FormControl>
        </TableCell>

        <TableCell>
          <FormControl fullWidth>
            <TextField
              value={addition.dscntRate}
              type={'number'}
              name={'할인율'}
              variant={"outlined"}
              size={"small"}
              onChange={(e) => {
                setAddition({...addition, dscntRate: Number(e.target.value)})
              }}/>
          </FormControl>
        </TableCell>

        <TableCell>
          <FormControl fullWidth>
            <Select name={'사용여부'} value={addition.useSttus === 'USE' ? '사용' : '미사용'} onChange={(event: SelectChangeEvent) => {
              setAddition({...addition, useSttus: event.target.value === '사용' ? 'USE' : 'END_USE'})
            }}>
              {
                ['사용', '미사용'].map((m, i) => {
                  return <MenuItem key={i} value={m}>{m}</MenuItem>
                })
              }
            </Select>
          </FormControl>
        </TableCell>
      </TableBody>
    </TableContainer>
  </Stack>
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
  },
  {
    id: 'useSttus',
    align: "center",
    label: '사용여부',
  }
]