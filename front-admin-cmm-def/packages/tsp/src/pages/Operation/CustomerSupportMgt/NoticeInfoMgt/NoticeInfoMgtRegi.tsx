import React, {Fragment, useEffect, useRef, useState} from "react";
import {
  SubAttachFileContents,
  SubContents,
  TitleContents,
  VerticalInterval
} from "shared/components/LayoutComponents";
import {Box, FormControl, Stack, Table, TableBody, TableCell, TableRow, TextField} from "@mui/material";
import TableContainer from "@mui/material/TableContainer";
import {
  CustomHeadCell,
  TableComponents,
  TableRadioCell,
  TableSelectCell,
  TableTextFieldCell, TextFieldCell, WithCustomRowData,
} from "shared/components/TableComponents";
import {useNavigate} from "react-router-dom";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {AboutNoticeRegister, articleCnList, articleUrlList} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {CustomButton} from "shared/components/ButtonComponents";
import {checkValidity} from "shared/utils/validUItil";
import {NoticeInfoMgtService} from "~/pages/Operation/CustomerSupportMgt/Service/NoticeInfoMgtService";

export const NoticeInfoMgtRegi = () => {
  const [classify, setClassify] = useState(["선택"]);
  const navigate = useNavigate()
  const {addModal} = useGlobalModalStore()
  const imgRef = useRef<HTMLInputElement>(null)
  const [selectedImage, setSelectedImage] = useState<any[]>([]);
  const [isImg, setIsImg] = useState(false)

  const [fileList, setFileList] = useState<any[]>([]);
  const [fileSelect, setFileSelect] = useState<string[]>([]);

  const [noticeRowList, setNoticeRowList] = useState<WithCustomRowData<articleCnList>[]>([])
  const [noticeSelected, setNoticeSelected] = useState<string[]>([])

  const [relatedSiteRowList, setRelatedSiteRowList] = useState<WithCustomRowData<articleUrlList>[]>([])
  const [siteSelected, setSiteSelected] = useState<string[]>([])
  const information = NoticeInfoMgtService.getAttactmentExtsn('tsp-notice')
  const [str, setStr] = useState<string>('')

  const [register, setRegister] = useState<AboutNoticeRegister>({
    article: {
      notice: false,
      title: '제목',
      posting: true,
      boardId: 'tsp-notice'
    }
  })

  useEffect(() => {
    let str = ''
    if (information.data) {
      str = information.data.atchmnflExtsnSet.replaceAll('/', ',.')
      setStr(str)
    }
  }, [information])

  useEffect(() => {
    let obj: [{ articleCn: string, header: string }] = [{articleCn: '', header: ''}];
    obj.pop()
    for (let i = 0; noticeRowList.length > i; i++) {
      obj.push({articleCn: noticeRowList[i].articleCn, header: noticeRowList[i].header})
    }
    setRegister({...register, article: {...register.article, articleCnList: obj}})
  }, [noticeRowList])

  useEffect(() => {
    let obj: [{ urlNm: string, url: string }] = [{urlNm: '', url: ''}];
    obj.pop()
    for (let i = 0; relatedSiteRowList.length > i; i++) {
      obj.push({urlNm: relatedSiteRowList[i].urlNm, url: relatedSiteRowList[i].url})
    }
    setRegister({...register, article: {...register.article, articleUrlList: obj}})
  }, [relatedSiteRowList])

  useEffect(() => {
    setIsImg(false)
  }, [isImg])

  return <TitleContents title={"공지사항 등록"}>
    <Stack style={{marginTop: '40px'}} width={'100%'} id={'NoticeInfoRegi'}>
      <SubContents title={"공지정보"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableBody>
              <TableRow>
                <TableTextFieldCell
                  label={"제목"} required wordCount={30}
                  division thWidth={"13%"} tdSpan={3} onChange={(text) => {
                  setRegister({...register, article: {...register.article, title: text}})
                }}/>
              </TableRow>
              <TableRow>
                <TableRadioCell
                  label={"상단고정여부"} radioLabel={["고정안함", "고정"]} row required
                  thWidth={"13%"} tdWidth={"36%"} onClick={selected => {
                  setRegister({...register, article: {...register.article, notice: selected === '고정'}})
                }}/>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </SubContents>
      <VerticalInterval size={"40px"}/>
      <SubContents title={"이미지"} maxHeight={'100%'}
                   required
                   rightContent={<Box>
                     <CustomButton
                       label={"삭제"} type={"small"} color={"list"}
                       style={{marginRight: '10px'}}
                       onClick={() => {
                         setSelectedImage([])
                       }}
                     />

                     <CustomButton
                       label={"등록"} type={"small"} color={"list"}
                       onClick={() => {
                         if (imgRef.current) imgRef.current.click()
                       }}
                     />
                   </Box>}
      >
        <input
          hidden ref={imgRef}
          type={"file"} multiple
          accept='image/*'
          onChange={async (event: any) => {
            let upFile: any = event.target.files
            for (let i = 0; i < upFile.length; i++) {
              selectedImage.push(upFile[i]);
            }
            setSelectedImage(selectedImage)
            setIsImg(true)
          }
          }
        />
        <Stack style={{
          border: "1px solid #d7dae6",
          width: '100%',
          minHeight: "320px",
          display: 'flex',
          alignItems: "center",
          justifyContent: "center"
        }}>
          {selectedImage.length > 0 ? selectedImage.map(m => {
            return <img src={URL.createObjectURL(m)}
                        style={{width: "300px", height: "300px", margin: '10px', border: '1px solid #d7dae6'}}/>
          }) : '이미지를 등록해주세요.'
          }

        </Stack>
      </SubContents>
      <VerticalInterval size={"40px"}/>
      <SubContents title={"공지상세정보"} maxHeight={'100%'} required rightContent={
        <Stack direction={'row'} spacing={'10px'}>
          <CustomButton type={"small"} color={"list"} label={"삭제"} onClick={() => {
            setNoticeRowList(noticeRowList.filter(f => !noticeSelected.includes(f.key)))
          }}/>
          <CustomButton type={"small"} color={"list"} label={"추가"} onClick={() => {
            setNoticeRowList(noticeRowList.concat({
              key: Math.random().toString(),
              header: '', articleCn: '',
            }))
          }}/>
        </Stack>
      }>
        <TableComponents<articleCnList>
          isCheckBox hidePagination hideRowPerPage
          page={0} rowCount={noticeRowList.length} rowsPerPage={0}
          headCells={noticeHeadCells}
          bodyRows={noticeRowList}
          onSelectedKey={(keys: string[]) => {
            setNoticeSelected(keys)
          }}
          tableCell={(data) => {

            return <Fragment>
              <TableCell key={"주제-" + data.key} width={"25%"}>
                <FormControl fullWidth size={"small"}>
                  <TextField
                    value={data.header}
                    //label={props.label}
                    required
                    name={'주제'}
                    variant={"outlined"}
                    size={"small"}
                    onClick={(e) => {
                      e.stopPropagation()
                    }}
                    onChange={(e) => {
                      setNoticeRowList(noticeRowList.map(m => {
                        return {
                          ...m,
                          header: (m.key === data.key) ? e.target.value : m.header
                        }
                      }))
                    }}/>
                </FormControl>
              </TableCell>
              <TextFieldCell
                multiline
                required
                wordCount={500}
                tdWidth={"100%"}
                defaultLabel={''}
                onChange={(e) => {
                  setNoticeRowList(noticeRowList.map(m => {
                    return {
                      ...m,
                      articleCn: (m.key === data.key) ? e : m.articleCn
                    }
                  }))
                }}/>
            </Fragment>
          }}
        />
      </SubContents>
      <VerticalInterval size={'40px'}/>
      <SubContents title={"관련사이트주소"} maxHeight={'100%'} rightContent={
        <Stack direction={'row'} spacing={'10px'}>
          <CustomButton type={"small"} color={"list"} label={"삭제"} onClick={() => {
            setRelatedSiteRowList(relatedSiteRowList.filter(f => !siteSelected.includes(f.key)))
          }}/>
          <CustomButton type={"small"} color={"list"} label={"추가"} onClick={() => {
            setRelatedSiteRowList(relatedSiteRowList.concat({
              key: Math.random().toString(),
              urlNm: '', url: '',
            }))
          }}/>
        </Stack>
      }>
        <TableComponents<articleUrlList>
          isCheckBox hidePagination hideRowPerPage
          page={0} rowCount={relatedSiteRowList.length} rowsPerPage={0}
          headCells={siteHeadCells}
          bodyRows={relatedSiteRowList}
          onSelectedKey={(keys: string[]) => {
            setSiteSelected(keys)
          }}
          tableCell={(data) => {

            return <Fragment>
              <TableCell key={"사이트명-" + data.key} width={"25%"}>
                <FormControl fullWidth size={"small"}>
                  <TextField
                    value={data.urlNm}
                    //label={props.label}
                    name={'사이트명'}
                    variant={"outlined"}
                    size={"small"}

                    onClick={(e) => {
                      e.stopPropagation()
                    }}
                    onChange={(e) => {
                      setRelatedSiteRowList(relatedSiteRowList.map(m => {
                        return {
                          ...m,
                          urlNm: (m.key === data.key) ? e.target.value : m.urlNm
                        }
                      }))
                    }}/>
                </FormControl>
              </TableCell>

              <TableCell key={"사이트주소-" + data.key}>
                <FormControl fullWidth size={"small"}>
                  <TextField
                    value={data.url}
                    multiline
                    //label={props.label}
                    name={'사이트주소'}
                    variant={"outlined"}
                    size={"small"}
                    onClick={(e) => {
                      e.stopPropagation()
                    }}
                    onChange={(e) => {
                      setRelatedSiteRowList(relatedSiteRowList.map(m => {
                        return {
                          ...m,
                          url: (m.key === data.key) ? e.target.value : m.url
                        }
                      }))
                    }}/>
                </FormControl>
              </TableCell>
            </Fragment>
          }}
        />
      </SubContents>

      <VerticalInterval size={"40px"}/>

      <SubAttachFileContents
        subTitle={`가능 확장자 (${information.data?.atchmnflExtsnSet.replace(' ', '').replaceAll('/', ', ').toLowerCase() || ''})`}
        edit accept={`.${str}`} fileSelect={fileSelect} fileList={fileList} setFileSelect={setFileSelect}
        setFileList={setFileList}/>

    </Stack>
    <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%", marginTop: "40px"}}>
      <Stack>
        <CustomButton
          label={"목록"} type={"largeList"} color={"outlined"}
          onClick={() => {
            navigate('/tsp-admin/Operation/About/Notice')
          }}
        />
      </Stack>
      <Stack>
        <CustomButton
          label={"저장"}
          type={"largeList"}
          onClick={async () => {
            if (checkValidity('NoticeInfoRegi')) {
              if (noticeRowList.length === 0) {
                alert('공지상세정보를 입력해주세요.')
                return
              }
              const form = new FormData();
              form.append('article', new Blob([JSON.stringify(register.article)], {type: "application/json"}))
              for (let i = 0; i < selectedImage.length; i++) {
                form.append("image", selectedImage[i]);
              }
              for (let i = 0; i < fileList.length; i++) {
                form.append("attachment", fileList[i].fileInfo);
              }
              NoticeInfoMgtService.postNoticeData('tsp-notice', form).then(() => {
                addModal({
                  open: true, isDist: true, type: 'normal', title: '저장되었습니다.',
                  onClose: () => navigate('/tsp-admin/Operation/About/Notice'),
                  onConfirm: () => navigate('/tsp-admin/Operation/About/Notice'),
                })
              }).catch((e: any) => {
                alert(e.response.data.message)
              })
            }
          }}/>
      </Stack>
    </Stack>
  </TitleContents>
}

const noticeHeadCells: CustomHeadCell<articleCnList>[] = [
  {
    id: 'header',
    align: 'center',
    label: '주제',
  },
  {
    id: 'articleCn',
    align: 'center',
    label: '내용',
  },
];

const siteHeadCells: CustomHeadCell<articleUrlList>[] = [
  {
    id: 'urlNm',
    align: 'center',
    label: '사이트명',
  },
  {
    id: 'url',
    align: 'center',
    label: '사이트주소',
  },
];

export default NoticeInfoMgtRegi