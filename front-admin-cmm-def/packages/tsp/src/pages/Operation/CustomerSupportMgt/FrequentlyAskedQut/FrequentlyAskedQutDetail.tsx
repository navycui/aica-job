import React, {useEffect, useState} from "react";
import {
  CustomTabs, HorizontalInterval, LoadingProgress,
  SubAttachFileContents,
  SubContents,
  TitleContents,
  VerticalInterval
} from "shared/components/LayoutComponents";
import {Stack, Table, TableBody, TableRow} from "@mui/material";
import TableContainer from "@mui/material/TableContainer";
import {
  TableSelectCell,
  TableTextCell,
  TableTextFieldCell,
  WithCustomRowData,
} from "shared/components/TableComponents";
import {useNavigate, useParams} from "react-router-dom";
import {TabPanel} from "@mui/lab";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {CustomButton} from "shared/components/ButtonComponents";
import {checkValidity} from "shared/utils/validUItil";
import {askedQutDetail} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useCommtCode} from "~/utils/useCommtCode";
import {FrequentlyAskedQutService} from "~/pages/Operation/CustomerSupportMgt/Service/FrequentlyAskedQutService";
import {dayFormat} from "shared/utils/stringUtils";

export const FrequentlyAskedQutDetail = () => {
  const {id} = useParams()
  const {commtCode} = useCommtCode(["CATEGORY_QNA"])
  const navigate = useNavigate()
  const {addModal} = useGlobalModalStore()
  const [displaySttus, setDisplaySttus] = useState("전시");
  const [category, setCategory] = useState([''])

  const [fileList, setFileList] = useState<WithCustomRowData<any>[]>([]);
  const [fileSelect, setFileSelect] = useState<string[]>([]);

  const information = FrequentlyAskedQutService.getDetail('tsp-qna', id!.toString())

  const [register, setRegister] = useState<askedQutDetail>()

  const extsn = FrequentlyAskedQutService.getAttactmentExtsn('tsp-qna')
  const [str, setStr] = useState<string>('')

  useEffect(() => {
    let str = ''
    if (extsn.data) {
      str = extsn.data.atchmnflExtsnSet.replaceAll('/', ',.')
      setStr(str)
    }
  }, [extsn])

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'CATEGORY_QNA')
      if (state.length > 0) setCategory(state)
    }
  }, [commtCode])

  useEffect(() => {
    if (!information.isLoading || !information.isFetching) {
      if (!!information.data) {
        setRegister(information.data)
        if (information.data.attachmentList) {
          for (let i = 0; information.data.attachmentList.length > i; i++) {
            information.data.attachmentList[i].key = Math.random().toString();
          }
          setFileList(information.data.attachmentList)
        }
        /*setFileList(information.data.attachmentList.map((m) => {
          return {
            key: m.attachmentId,
            ...m
          }
        }))*/
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  if (!register) return <LoadingProgress/>

  const updateAsked = async () => {
    const formData = new FormData()
    formData.append('article', new Blob([JSON.stringify(register)], {type: 'application/json'}))

    if (register && register.attachmentList) {
      if (register.attachmentList.filter(f => !fileList.filter(f => f.fileInfo === undefined).includes(f))) {
        const removeFile: any = register.attachmentList.filter(f => !fileList.filter(f => f.fileInfo === undefined).includes(f))
        for (let i = 0; removeFile.length > i; i++) {
          await FrequentlyAskedQutService.deleteBoardAttachment(id!.toString(), removeFile[i].attachmentId)
        }
      }
    }
    if (fileList) {
      for (let i = 0; fileList.length > i; i++) {
        // formData.append('attachment', fileList[i].fileInfo);
        formData.append('attachment', fileList[i]);
      }
    }
    FrequentlyAskedQutService.updateAsked(formData, id!.toString()).then(() => {
      information.refetch()
      addModal({
        open: true, isDist: true, type: 'normal', content: '저장되었습니다.', onConfirm: () => {
          navigate('/tsp-admin/Operation/About/FAQ')
        }
      })
    })
  }

  const deleteAskedQut = async () => {
    if (!id) {
      return;
    }
    try {
      await FrequentlyAskedQutService.deleteQNA('tsp-qna', id!.toString()).then(() => {
        addModal({open: true, isDist: true, content: '삭제되었습니다.'})
        navigate('/tsp-admin/Operation/About/FAQ');
      })
    } catch (error: any) {
      console.error(error);
    }
  }

  return <Stack id={'FreAsk'} width={'100%'}>
    <TitleContents title={"자주묻는질문 상세"}>
      <CustomTabs tabs={["상세내용"]}>
        <TabPanel value="상세내용" sx={{padding: "0", height: "100%"}}>
          <Stack>
            <SubContents title={"작성자정보"}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextCell
                        division title={"등록일"} label={dayFormat(register.createdDt)}
                        thWidth={"13%"} tdWidth={"36%"}/>
                      <TableTextCell
                        title={"전시상태"} label={register.posting ? '전시' : '미전시'}
                        thWidth={"13%"} tdWidth={"36%"}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <VerticalInterval size={'40px'}/>
          <Stack>
            <SubContents title={"상세내용"} maxHeight={'100%'}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextFieldCell
                        label={"제목"} defaultLabel={register.title} required wordCount={100}
                        division thWidth={"20%"} tdSpan={3} onChange={(text) => {
                        setRegister({...register, title: text})
                      }}/>
                      <TableSelectCell
                        label={"분류"} required
                        thWidth={"20%"}
                        defaultLabel={toCommtCodeName(commtCode, 'CATEGORY_QNA', register.categoryCd)}
                        selectLabel={category}
                        onClick={(selected: string) => {
                          setRegister({...register, categoryCd: toCommtCode(commtCode, 'CATEGORY_QNA', selected)})
                        }}/>
                    </TableRow>
                    <TableRow>
                      <TableTextFieldCell
                        label={"내용"} defaultLabel={register.article} multiline required wordCount={500}
                        thWidth={"20%"} tdSpan={5} onChange={(text) => {
                        setRegister({...register, article: text})
                      }}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <VerticalInterval size={'40px'}/>

          <Stack>
            <SubAttachFileContents
              subTitle={`가능 확장자 (${extsn.data?.atchmnflExtsnSet.replace(' ', '').replaceAll('/', ', ').toLowerCase() || ''})`}
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
            <Stack direction={'row'}>
              <CustomButton
                label={"삭제"}
                type={"largeList"}
                onClick={() => {
                  addModal({
                    open: true, type: 'normal', content: '삭제하시겠습니까?',
                    onConfirm: () => {
                      deleteAskedQut()
                    }
                  });
                }
                }
              />
              <HorizontalInterval size={'10px'}/>
              <CustomButton
                label={register.posting ? '전시안함' : '전시'}
                type={"largeList"}
                onClick={() => {
                  addModal({
                    open: true, content: `${register?.posting ? '미전시 처리하시겠습니까?' : '전시하시겠습니까?'}`,
                    onConfirm: () => {
                      const form = new FormData()
                      form.append('posting', String(!register?.posting))
                      FrequentlyAskedQutService.putQNAPosting('tsp-qna', id!.toString(), form).then(() => {
                        setRegister({...register, posting: !register.posting})
                        addModal({
                          open: true,
                          isDist: true,
                          content: `${register?.posting ? '미전시 처리되었습니다.' : '전시처리되었습니다.'}`
                        })
                      })
                    }
                  })
                }}/>
              <HorizontalInterval size={'10px'}/>
              <CustomButton
                label={"저장"}
                type={"largeList"}
                onClick={() => {
                  if (checkValidity('FreAsk')) {
                    updateAsked()
                  }
                }}/>
            </Stack>
          </Stack>
        </TabPanel>
      </CustomTabs>
    </TitleContents>
  </Stack>
}


export default FrequentlyAskedQutDetail