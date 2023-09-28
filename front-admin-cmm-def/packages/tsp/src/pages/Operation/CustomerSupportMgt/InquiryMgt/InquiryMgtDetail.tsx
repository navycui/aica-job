import {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {
  CustomHeadCell,
  TableComponents, TableTextCell,
  TableTextFieldCell,
  WithCustomRowData
} from "~/../../shared/src/components/TableComponents";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {Stack, Table, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {dayFormat} from "shared/utils/stringUtils";
import {CustomButton} from "shared/components/ButtonComponents";
import {
  OneByOneDetail,
  questioner, SearchParam,
  UsptExpertClChargerModel,
} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {TabPanel} from "@mui/lab";
import {
  CustomTabs,
  SubAttachFileContents,
  SubContents,
  TitleContents,
  VerticalInterval
} from "shared/components/LayoutComponents";
import {InquiryMgtService} from "~/pages/Operation/CustomerSupportMgt/Service/InquiryMgtService";
import {ModalComponents} from "shared/components/ModalComponents";
import {ExpertClassificationMgtService as TService} from '../Service/ExpertCalssificationMgtService'
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";

export const InquiryMgtDetail = () => {
  const {id} = useParams()
  const navigate = useNavigate()
  const {addModal} = useGlobalModalStore()
  const [expertManagerModal, setExpertManagerModal] = useState(false)
  // const [expertManagerRowList, setExpertManagerRowList] = useState<WithCustomRowData<전문가분류관리_담당자>[]>(dummyExpertManager)
  const [expertManager, setExpertManager] = useState("김원희")

  const [state, setState] = useState<OneByOneDetail>({} as OneByOneDetail)
  const [question, setQuestion] = useState<questioner>({} as questioner)

  const [disabled, setDisabled] = useState<boolean>(false)

  const data = InquiryMgtService.getDetail('tsp-persnal', id ?? '')
  const memberDetail = InquiryMgtService.getMember('tsp-persnal', id ?? '')
  const {commtCode} = useCommtCode(['MEMBER_TYPE'])

  useEffect(() => {
    if (!data.isLoading || !data.isFetching) {
      if (!!data.data) {
        setState(data.data)
        setQuestion(data.data.questioner)
        setDisabled(!data.data.answer)
      }
    }
  }, [data.data, data.isLoading, data.isFetching])

  /*const init = () => {
    if (!!memberDetail.data) {
      console.log(memberDetail.data)
    }
  }

  useEffect(init,[])*/

  const update = async () => {
    if (state.answer) {
      const data = new FormData()
      data.append('qnaId', 'tsp-persnal')
      data.append('questId', id ?? '')
      data.append('answer', state.answer)
      const result = InquiryMgtService.updateAnswer(id ?? '', data)
        .then((response) => {
          addModal({open: true, isDist: true, content: '답변이 전송되었습니다.'})
        }).catch((e) => {
          console.log(e);
        })
    } else {
      addModal({open: true, isDist: true, content: '답변을 입력해주세요.'})
    }
  }

  return <Stack id={'UserManual'} width={'100%'}>
    <TitleContents title={"1:1문의 상세"}>
      <CustomTabs tabs={["상세내용"]}>
        <TabPanel value="상세내용" sx={{padding: "0", height: "100%"}}>
          <Stack>
            <SubContents title={"기본정보"}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextCell
                        division title={"접수일"} label={dayFormat(state.questCreatedDt)}
                        thWidth={"13%"} tdWidth={"21%"}/>
                      <TableTextCell
                        division title={"처리상태"} label={state.questStNm}
                        thWidth={"13%"} tdWidth={"21%"}/>
                      <TableTextCell
                        title={"채널"} label={"안심구역포털"}
                        thWidth={"13%"} tdWidth={"21%"}/>
                    </TableRow>
                    <TableRow>
                      <TableTextCell
                        title={"담당자"} label={state.answererNm || '-'}
                        thWidth={"13%"} tdSpan={5}
                        // rightContent={
                        // <CustomButton
                        //   label={"담당자변경"} type={"small"}
                        //   color={"list"} style={{marginLeft: '15px'}}
                        //   onClick={() => {
                        //     setExpertManagerModal(true)
                        //   }}/>
                        // }
                      />
                      {expertManagerModal && <ExpertManagerAddModal
                        open={expertManagerModal}
                        onClose={() => setExpertManagerModal(false)}
                        onSelect={(data) => {
                          // setExpertManagerRowList(expertManagerRowList.concat(data))
                          setExpertManagerModal(false)
                          data.map((item) =>
                            setExpertManager(item.memberNm!)
                          )
                        }}
                      />}


                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
            <VerticalInterval size={'40px'}/>
            <SubContents title={"작성자 정보"}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextCell
                        division title={"회원유형"} label={toCommtCodeName(commtCode, 'MEMBER_TYPE', question.memberType)}
                        thWidth={"13%"} tdWidth={"21%"}/>
                      <TableTextCell
                        division title={"사업자명/이름"} label={question.memberNm}
                        thWidth={"13%"} tdWidth={"21%"}/>
                      <TableTextCell
                        title={"담당자명"} label={state.answererNm}
                        thWidth={"13%"} tdWidth={"21%"}/>
                    </TableRow>
                    <TableRow>
                      <TableTextCell
                        division title={"직급"} label={"책임"}
                        thWidth={"13%"} tdWidth={"21%"}/>
                      <TableTextCell
                        division title={"휴대폰번호"} label={question.mobileNo}
                        thWidth={"13%"} tdWidth={"21%"}/>
                      <TableTextCell
                        title={"이메일"} label={question.email}
                        thWidth={"13%"} tdWidth={"21%"}/>
                    </TableRow>
                    <TableRow>
                      <TableTextCell
                        title={"회원ID"} label={question.loginId}
                        thWidth={"13%"} tdSpan={5}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <VerticalInterval size={'40px'}/>
          <Stack>
            <SubContents title={"문의내용"}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableTextCell
                        title={"문의구분"} label={state.categoryNm}
                        thWidth={"13%"}/>
                    </TableRow>
                    <TableRow>
                      <TableTextCell
                        title={"제목"} label={state.title}
                        thWidth={"13%"}/>
                    </TableRow>
                    <TableRow>
                      <TableTextCell
                        title={"내용"} label={state.question}
                        thWidth={"13%"}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <VerticalInterval size={'40px'}/>


          {
            state.questAttachmentList && state.questAttachmentList.length > 0 && <>
              <Stack>
                <SubAttachFileContents
                  atchmnflGroupId={state.questAttachmentGroupId}
                  atchmnfl={state.questAttachmentList}>
                </SubAttachFileContents>
              </Stack>
              <VerticalInterval size={'40px'}/>
            </>
          }

          <Stack>
            <SubContents title={"답변보내기"} maxHeight={'100%'}
                         rightContent={disabled && <CustomButton label={"답변전송"}
                                                                 type={"small"} color={"list"} onClick={() => {
                           update()
                         }}
                         />}>
              <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
                <Table>
                  <TableBody>
                    {/* <TableRow>
                      <TableTextFieldCell
                        label={"제목"} defaultLabel={""} required wordCount={50}
                        thWidth={"13%"} onChange={(text) => {
                        //setReq((state) => ({...req!, sumry: text}))
                      }}/>
                    </TableRow> */}
                    <TableRow>
                      <TableTextFieldCell
                        label={"내용"} defaultLabel={state.answer} multiline
                        required wordCount={1000} disabled={!disabled}
                        thWidth={"20%"} onChange={(text) => {
                        setState({...state, answer: text})
                      }}/>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </SubContents>
          </Stack>
          <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%", marginTop: "40px"}}>
            <Stack>
              <CustomButton
                label={"목록"} type={"largeList"} color={"outlined"}
                onClick={() => {
                  navigate('/tsp-admin/Operation/About/Inquiry')
                }}
              />
            </Stack>
          </Stack>
        </TabPanel>
      </CustomTabs>
    </TitleContents>
  </Stack>
}

const ManagerListView = (props: {
  rowList: WithCustomRowData<UsptExpertClChargerModel>[]
  selected: WithCustomRowData<UsptExpertClChargerModel>[]
  setSelected: Dispatch<SetStateAction<WithCustomRowData<UsptExpertClChargerModel>[]>>
  pagination: { page: number, rowsPerPage: number, rowCount: number }
  setPagination: Dispatch<SetStateAction<{ page: number, rowsPerPage: number, rowCount: number }>>
}) => {

  return <TableComponents<UsptExpertClChargerModel>
    hideRowPerPage showTotal isCheckBox
    headCells={managerHeadCells}
    bodyRows={props.rowList}
    {...props.pagination}
    onChangePagination={(page, rowPerPage) => {
      props.setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    onSelectedKey={(keys: string[]) => {
      const selected = props.selected.map(m => m.key)

      if (selected.length < keys.length) {
        // 추가시
        const newKey = keys.find(f => !selected.includes(f))
        const data = props.rowList.find(f => f.key === newKey) as any
        props.setSelected(props.selected.concat(data))
      } else {
        // 삭제시
        const deleteKey = selected.find(f => !keys.includes(f))
        props.setSelected(props.selected.filter(f => f.key !== deleteKey))
      }
      // props.setSelected(keys)
    }}
    tableCell={(data) => {

      return (
        <Fragment>
          <TableCell key={"부서명-" + data.key} sx={{textAlign: 'center'}}>{data.deptNm}</TableCell>
          <TableCell key={"이름-" + data.key} sx={{textAlign: 'center'}}>{data.memberNm}</TableCell>
          <TableCell key={"직급-" + data.key} sx={{textAlign: 'center'}}>{data.clsfNm}</TableCell>
        </Fragment>
      )
    }}
  />
}

const managerHeadCells: CustomHeadCell<UsptExpertClChargerModel>[] = [
  {
    id: 'deptNm',
    align: 'center',
    label: '부서명',
  },
  {
    id: 'memberNm',
    align: 'center',
    label: '이름',
  },
  {
    id: 'clsfNm',
    align: "center",
    label: '직급',
  },
];


const ExpertManagerAddModal = (props: {
  open: boolean
  onSelect: (data: WithCustomRowData<UsptExpertClChargerModel>[]) => void
  onClose: () => void
}) => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [searchText, setSearchText] = useState<SearchParam>()
  const [searchTempText, setSearchTempText] = useState<SearchParam>()

  const [selected, setSelected] = useState<WithCustomRowData<UsptExpertClChargerModel>[]>([])
  const [rowList, setRowList] = useState<WithCustomRowData<UsptExpertClChargerModel>[]>([])

  const resultData = TService.getExpertClChargerPopupList({...searchText, ...pagination})

  useEffect(() => {
    if (!resultData.isLoading && !resultData.isFetching) {
      if (!!resultData.data) {
        const list = resultData.data.list.map((m: any, index: number) => {
          return {
            key: (index + 1).toString(),
            flag: "I",
            expertClId: m.expertClId,
            memberId: m.memberId,
            memberNm: m.memberNm,
            deptNm: m.deptNm,
            clsfNm: m.clsfNm
          }
        })
        setRowList(list);

        if (pagination.rowCount !== resultData.data.totalItems) {
          setPagination((state) => ({...state, rowCount: resultData.data.totalItems}))
        }
      }
    }
  }, [resultData.data, resultData.isLoading, resultData.isFetching])

  return <ModalComponents
    isDist open={props.open}
    type={"save"}
    title={'담당자 추가'}
    onClose={props.onClose}
    onConfirm={() => {
      const addList = rowList.filter(item => selected.includes(item))
      const list = addList.map((m) => {
        return {
          flag: "I",
          ...m,
        }
      })
      props.onSelect(list)
    }}
  >
    <Stack spacing={'15px'}>
      <Stack direction={'row'} alignItems={'center'} spacing={'15px'}
             sx={{
               borderTop: '1px solid rgb(215, 218, 230)',
               borderBottom: '1px solid rgb(215, 218, 230)',
             }}>
        <TableContainer>
          <Table>
            <TableRow sx={{'> td': {borderBottom: 'none'}}}>
              <TableTextFieldCell
                division label={'담당부서'}
                wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                onChange={(text) => {
                setSearchTempText({...searchTempText!, deptNm: text})
              }}/>

              <TableTextFieldCell
                label={'담당자명'}
                wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                onChange={(text) => {
                setSearchTempText({...searchTempText!, memberNm: text})
              }}/>
            </TableRow>
          </Table>
        </TableContainer>
        <CustomButton label={'검색'} type={'small'} onClick={() => {
          setSearchText(searchTempText)
        }}/>
      </Stack>

      <ManagerListView
        rowList={rowList} selected={selected} setSelected={setSelected}
        pagination={pagination} setPagination={setPagination}
      />
    </Stack>
  </ModalComponents>
}

export default InquiryMgtDetail