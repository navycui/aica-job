import React, {Dispatch, SetStateAction, useEffect, useState} from "react";
import {
  SubAttachFileContents,
  SubContents,
  TitleContents,
  VerticalInterval
} from "shared/components/LayoutComponents";
import {Stack, Table, TableBody, TableRow} from "@mui/material";
import TableContainer from "@mui/material/TableContainer";
import {
  TableSelectCell, TableTextFieldCell, WithCustomRowData
} from "shared/components/TableComponents";
import {useNavigate} from "react-router-dom";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {CustomButton} from "shared/components/ButtonComponents";
import {checkValidity} from "shared/utils/validUItil";
import {AskedInsParam} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode} from "~/utils/CommtCodeUtil";
import {FrequentlyAskedQutService} from "~/pages/Operation/CustomerSupportMgt/Service/FrequentlyAskedQutService";

export const FrequentlyAskedQutRegi = () => {
  const {commtCode} = useCommtCode(["CATEGORY_QNA"])
  const navigate = useNavigate()
  const {addModal} = useGlobalModalStore()
  const [category, setCategory] = useState([''])

  const [fileList, setFileList] = useState<WithCustomRowData<any>[]>([]);
  const [fileSelect, setFileSelect] = useState<string[]>([]);
  const [register, setRegister] = useState<AskedInsParam>({
    boardId: 'tsp-qna',
    notice: false,
    posting: true,
    categoryCd: "CATE-QNA-01"
  })
  const information = FrequentlyAskedQutService.getAttactmentExtsn('tsp-qna')
  const [str, setStr] = useState<string>('')

  useEffect(() => {
    let str = ''
    if (information.data) {
      str = information.data.atchmnflExtsnSet.replaceAll('/', ',.')
      setStr(str)
    }
  }, [information])

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'CATEGORY_QNA')
      if (state.length > 0) setCategory(state)
    }
  }, [commtCode])

  const insAsked = async (article: any) => {
    if (register.categoryCd === '전체') return
    const formData = new FormData()
    formData.append('article', new Blob([JSON.stringify(register)], {type: 'application/json'}))

    if (fileList) {
      for (let i = 0; fileList.length > i; i++) {
        formData.append('attachment', fileList[i].fileInfo);
      }
    }
    await FrequentlyAskedQutService.insertAsked(formData)
    navigate('/tsp-admin/Operation/About/FAQ')
  }

  return <TitleContents title={"자주묻는질문 등록"}>

    <Stack style={{marginTop: '40px'}} width={'100%'} id={'FreAskRegi'}>
      <SubContents title={"상세내용"} maxHeight={"100%"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableSelectCell
                  label={"분류"} required
                  thWidth={"20%"}
                  defaultLabel={'지원/신청'}
                  selectLabel={category}
                  onClick={(selected: string) => {
                    setRegister({...register, categoryCd: toCommtCode(commtCode, 'CATEGORY_QNA', selected)})
                  }}/>
              </TableRow>
              <TableRow>
                <TableTextFieldCell
                  label={"제목"} defaultLabel={''} required wordCount={100}
                  thWidth={"20%"} tdSpan={5} onChange={(text) => {
                  setRegister({...register, title: text})
                }}/>
              </TableRow>
              <TableRow>
                <TableTextFieldCell
                  label={"내용"} defaultLabel={''} multiline required wordCount={500}
                  thWidth={"20%"} tdSpan={5} onChange={(text) => {
                  setRegister({...register, article: text})
                }}/>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>
      <VerticalInterval size={'40px'}/>

      <SubAttachFileContents subTitle={`가능 확장자 (${information.data?.atchmnflExtsnSet.replace(' ', '').replaceAll('/', ', ').toLowerCase() || ''})`}
                             edit accept={`.${str}`}
                             fileSelect={fileSelect} fileList={fileList} setFileSelect={setFileSelect}
                             setFileList={setFileList}/>

    </Stack>
    <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%", marginTop: "40px"}}>
      <Stack>
        <CustomButton
          label={"목록"} type={"largeList"} color={"outlined"}
          onClick={() => {
            navigate('/tsp-admin/Operation/About/FAQ')
          }}
        />
      </Stack>
      <Stack>
        <CustomButton
          label={"저장"}
          type={"largeList"}
          onClick={() => {
            if (checkValidity('FreAskRegi')) {
              addModal({open: true, content: '저장하시겠습니까?', onConfirm: () => insAsked(register)})
            }
          }}/>
      </Stack>
    </Stack>
  </TitleContents>
}


export default FrequentlyAskedQutRegi