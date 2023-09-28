import React, {Fragment, useEffect, useRef, useState} from "react";
import {
  CustomTabs, HorizontalInterval, LoadingProgress,
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
  TableTextCell,
  TableTextFieldCell, TextFieldCell, WithCustomRowData,
} from "shared/components/TableComponents";
import {useNavigate, useParams} from "react-router-dom";
import {TabPanel} from "@mui/lab";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {
  AboutNoticeRegister,
  articleCnList,
  articleUrlList,
  NoticeDetail
} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {CustomButton} from "shared/components/ButtonComponents";
import {checkValidity} from "shared/utils/validUItil";
import {NoticeInfoMgtService} from "~/pages/Operation/CustomerSupportMgt/Service/NoticeInfoMgtService";
import {dayFormat} from "shared/utils/stringUtils";

export const NoticeInfoMgtDetail = () => {
  const {id} = useParams()
  const navigate = useNavigate()
  const {addModal} = useGlobalModalStore()
  const [displaySttus, setDisplaySttus] = useState("전시");
  const imgRef = useRef<HTMLInputElement>(null)
  const [selectedImage, setSelectedImage] = useState<any[]>([]);
  const [isImg, setIsImg] = useState(false)

  const [fileList, setFileList] = useState<WithCustomRowData<any>[]>([]);
  const [fileSelect, setFileSelect] = useState<string[]>([]);

  const [noticeRowList, setNoticeRowList] = useState<WithCustomRowData<articleCnList>[]>([])
  const [noticeSelected, setNoticeSelected] = useState<string[]>([])

  const [relatedSiteRowList, setRelatedSiteRowList] = useState<WithCustomRowData<articleUrlList>[]>([])
  const [siteSelected, setSiteSelected] = useState<string[]>([])

  const information = NoticeInfoMgtService.getNoticeDetail('tsp-notice', id!.toString())
  const [register, setRegister] = useState<NoticeDetail>()
  const [postData, setPostData] = useState<AboutNoticeRegister>()

  const extsn = NoticeInfoMgtService.getAttactmentExtsn('tsp-notice')
  const [str, setStr] = useState<string>('')

  useEffect(() => {
    let str = ''
    if (extsn.data) {
      str = extsn.data.atchmnflExtsnSet.replaceAll('/', ',.')
      setStr(str)
    }
  }, [extsn])

  useEffect(() => {
    if (!information.isLoading || !information.isFetching) {
      if (!!information.data) {
        setRegister(information.data)
        setPostData({
          ...postData, article: {
            notice: information.data.notice,
            title: information.data.title,
            article: information.data.articleId,
            posting: information.data.posting,
            boardId: information.data.boardId,
            articleCnList: information.data.articleCnList,
            articleUrlList: information.data.articleUrlList
          },
          image: information.data.imageList,
        })

        if (information.data.attachmentList) {
          for (let i = 0; information.data.attachmentList.length > i; i++) {
            information.data.attachmentList[i].key = Math.random().toString();
          }
          setFileList(information.data.attachmentList)
        }

        /*if (information.data.attachmentList && information.data.attachmentList.length > 0) {
          for (let i = 0; information.data.attachmentList.length > i; i++) {
            fileList.pop()
          }
          for (let i = 0; information.data.attachmentList.length > i; i++) {
            fileList.push({
              key: Math.random().toString(),
              fileNm: information.data.attachmentList[i].fileNm,
              attachmentId: information.data.attachmentList[i].attachmentId,
              fileSize: information.data.attachmentList[i].fileSize,
              contentType: information.data.attachmentList[i].contentType
            })
          }
        }*/

        if (information.data.articleCnList.length > 0) {
          for (let i = 0; information.data.articleCnList.length > i; i++) {
            noticeRowList.pop()
          }
          for (let i = 0; information.data.articleCnList.length > i; i++) {
            noticeRowList.push({
              key: Math.random().toString(),
              header: information.data.articleCnList[i].header,
              articleCn: information.data.articleCnList[i].articleCn,
            })
          }
        }

        if (information.data.articleUrlList && information.data.articleUrlList.length > 0) {
          for (let i = 0; information.data.articleUrlList.length > i; i++) {
            relatedSiteRowList.pop()
          }
          for (let i = 0; information.data.articleUrlList.length > i; i++) {
            relatedSiteRowList.push({
              key: Math.random().toString(),
              urlNm: information.data.articleUrlList[i].urlNm,
              url: information.data.articleUrlList[i].url,
            })
          }
        }

      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  useEffect(() => {
    if (postData) {
      let obj: [{ articleCn: string, header: string }] = [{articleCn: '', header: ''}];
      obj.pop()
      for (let i = 0; noticeRowList.length > i; i++) {
        obj.push({articleCn: noticeRowList[i].articleCn, header: noticeRowList[i].header})
      }
      setPostData({...postData, article: {...postData.article, articleCnList: obj}})
    }
  }, [noticeRowList])

  useEffect(() => {
    if (postData) {
      let obj: [{ urlNm: string, url: string }] = [{urlNm: '', url: ''}];
      obj.pop()
      for (let i = 0; relatedSiteRowList.length > i; i++) {
        obj.push({urlNm: relatedSiteRowList[i].urlNm, url: relatedSiteRowList[i].url})
      }
      setPostData({...postData, article: {...postData.article, articleUrlList: obj}})
    }
  }, [relatedSiteRowList])

  useEffect(() => {
    if (isImg) setIsImg(false)
  }, [isImg])

  if (!register || !postData) return <LoadingProgress/>

  return <Stack id={'NoticeInfo'} width={'100%'}>
    <TitleContents title={"공지사항 상세"}>
      <CustomTabs tabs={["상세내용"]}>
        <TabPanel value="상세내용" sx={{padding: "0", height: "100%"}}>
          <Stack>
            <SubContents title={"기본정보"}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextCell
                        division title={"등록일"} label={dayFormat(register.createdDt)}
                        thWidth={"13%"} tdWidth={"36%"}/>
                      <TableTextCell
                        title={"전시상태"} label={register.posting ? "전시" : "전시안함"}
                        thWidth={"13%"} tdWidth={"36%"}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <Stack>
            <VerticalInterval size={"40px"}/>
            <SubContents title={"공지정보"}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextFieldCell
                        label={"제목"} defaultLabel={register.title} required wordCount={30}
                        division thWidth={"13%"} tdSpan={3} onChange={(text) => {
                        setPostData({...postData, article: {...postData?.article, title: text}})
                        //setRegister({...register, title: text})
                      }}/>
                    </TableRow>
                    <TableRow>
                      <TableRadioCell
                        label={"상단고정여부"} radioLabel={["고정안함", "고정"]} row
                        defaultLabel={postData.article?.notice ? "고정" : "고정안함"}
                        thWidth={"13%"} tdWidth={"36%"} onClick={selected => {
                        setPostData({...postData, article: {...postData?.article, notice: selected === '고정'}})
                        //setRegister({...register, notice: selected === '고정'})
                      }}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <Stack>
            <VerticalInterval size={"40px"}/>
            <SubContents title={"이미지"} maxHeight={'100%'}
                         required
                         rightContent={<Box>
                           <CustomButton
                             label={"삭제"} type={"small"} color={"list"} style={{marginRight: '10px'}}
                             onClick={() => {
                               setSelectedImage([])
                               setPostData({...postData, image: []})
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
                  // setRegister({...register, pcThumbnailFile: {MultipartFile: img}})
                }
                }
              />
              <Stack style={{
                border: "1px solid #d7dae6",
                width: '100%',
                minHeight: "320px",
                alignItems: "center",
                justifyContent: "center"
              }}>
                {
                  (postData.image && postData.image.length > 0) && postData.image.map(m => {
                    return <img
                      src={`${process.env.REACT_APP_DOMAIN}/tsp-api/api/file-dwld-contentType/${m.attachmentId}`}
                      style={{width: "300px", height: "300px", margin: '10px', border: '1px solid #d7dae6'}}/>
                  })
                }
                {
                  (selectedImage && selectedImage.length > 0) && selectedImage.map(m => {
                    return <img src={URL.createObjectURL(m)}
                                style={{width: "300px", height: "300px", margin: '10px', border: '1px solid #d7dae6'}}/>
                  })
                }
                {
                  ((selectedImage && selectedImage.length === 0) && (postData.image && postData.image!.length === 0)) &&
                  '이미지를 등록하세요.'
                }
              </Stack>
            </SubContents>
          </Stack>
          <VerticalInterval size={'40px'}/>
          <Stack>
            <SubContents title={"공지상세정보"} maxHeight={'100%'} rightContent={
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
                      defaultLabel={data.articleCn}
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
          </Stack>
          <VerticalInterval size={"40px"}/>

          <Stack>
            <SubAttachFileContents
              subTitle={`가능 확장자 (${extsn.data?.atchmnflExtsnSet.replace(' ', '').replaceAll('/', ', ').toLowerCase() || ''})`}
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
            <Stack direction={'row'}>
              <CustomButton
                label={"삭제"}
                type={"largeList"}
                onClick={async () => {
                  addModal({
                    open: true, type: 'normal', content: '삭제하시겠습니까?',
                    onConfirm: async () => {
                      await NoticeInfoMgtService.deleteNotice('tsp-notice', id!.toString()).then(() => {
                        addModal({open: true, isDist: true, content: '삭제되었습니다.'})
                        navigate('/tsp-admin/Operation/About/Notice')
                      })
                    }
                  })
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
                      NoticeInfoMgtService.putNoticePosting('tsp-notice', id!.toString(), form).then(() => {
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
              {/* {
                (displaySttus === '전시') && <>
                      <HorizontalInterval size={'10px'}/>
                      <CustomButton
                          label={"전시안함"}
                        //label={state.전시상태}
                          type={"largeList"}
                          onClick={async () => {
                            //   const result = await FrequentlyAskedQutService.change(id!)
                            //   if (result.success) {
                            addModal({
                              open: true, isDist: true, type: 'normal', content: '전시안함 처리되었습니다',
                              onConfirm: () => {
                                setDisplaySttus("전시안함")
                              }
                            })
                            //   }
                          }}
                      />
                  </>
              }
              {
                (displaySttus === '전시안함') && <>
                      <HorizontalInterval size={'10px'}/>
                      <CustomButton
                          label={"전시"}
                          type={"largeList"}
                          onClick={async () => {
                            //   const result = await FrequentlyAskedQutServiceService.change(id!)
                            //   if (result.success) {
                            addModal({
                              open: true, isDist: true, type: 'normal', content: '전시 처리되었습니다',
                              onConfirm: () => {
                                setDisplaySttus("전시")
                              }
                            })
                            //   }
                          }}
                      />
                  </>
              }*/}

              <HorizontalInterval size={'10px'}/>
              <CustomButton
                label={"저장"}
                type={"largeList"}
                onClick={async () => {
                  if (checkValidity('NoticeInfo')) {
                    const form = new FormData();
                    form.append('article', new Blob([JSON.stringify(postData.article)], {type: "application/json"}))
                    for (let i = 0; i < selectedImage.length; i++) {
                      form.append("image", selectedImage[i]);
                    }
                    if (postData!.image) {
                      for (let i = 0; i < postData.image.length; i++) {
                        form.append("image", postData.image[i].fileInfo);
                      }
                    }
                    if (register.attachmentList && register.attachmentList.filter(f => !fileList.filter(f => f.fileInfo === undefined).includes(f))) {
                      const removeFile: any = register.attachmentList.filter(f => !fileList.filter(f => f.fileInfo === undefined).includes(f))
                      for (let i = 0; removeFile.length > i; i++) {
                        await NoticeInfoMgtService.deleteNoticeBoardAttachment('tsp-notice', id!.toString(), removeFile[i].attachmentId)
                      }
                    }
                    for (let i = 0; i < fileList.length; i++) {
                      form.append("attachment", fileList[i].fileInfo);
                    }
                    NoticeInfoMgtService.putNoticeModify('tsp-notice', id!.toString(), form).then(() => {
                      information.refetch()
                      addModal({
                        open: true, isDist: true, type: 'normal', content: '저장되었습니다.',
                        onClose: () => navigate('/tsp-admin/Operation/About/Notice'),
                        onConfirm: () => navigate('/tsp-admin/Operation/About/Notice'),
                      })
                    })
                    //navigate('/OperationMgt/CustomerSupportMgt/NoticeInfoMgt/1159')
                  }

                  // const result = await NoticeInfoMgtService.delete(id!)
                  // if (result.success) {
                  //   addModal({
                  //     open: true, isDist: true, type: 'normal', content: '저장되었습니다.',
                  //     onConfirm: () => {
                  //       navigate(-1)
                  //     }
                  //   })
                  // }
                }}/>
            </Stack>
          </Stack>
        </TabPanel>
      </CustomTabs>
    </TitleContents>
  </Stack>
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

export default NoticeInfoMgtDetail