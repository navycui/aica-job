import React, {Fragment, useEffect, useRef, useState} from 'react'
import {Box, Stack, Table, TableCell, TableContainer, TableRow} from "@mui/material";
import {EquipmentCategoryService} from "~/service/EquipmentMgt/EquipmentCategoryService";
import {
  DiscountData,
  EquipmentClassifyData, EquipmentRegisterData,
} from "~/service/Model";
import {HorizontalInterval, SimpleTextFiled, SubContents, TitleContents} from "shared/components/LayoutComponents";
import {
  CustomHeadCell,
  WithCustomRowData,
  TableComponents, TableDateCell, TableDateTermCell,
  TableDoubleSelectCell,
  TableTextFieldCell,
  TableRadioCell,
} from "shared/components/TableComponents";
import {CustomButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {EquipmentInformationService} from "~/service/EquipmentMgt/EquipmentInformationService";
import {checkValidity} from "shared/utils/validUItil";
import {SaleRegisterModal} from "~/pages/EquipmentMgt/SaleRegisterModal";
import {SaleSelectModal} from "~/pages/EquipmentMgt/SaleSelectModal";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 장비 등록 */
const EquipmentRegist = () => {
  const navigate = useNavigate();
  const [selectedImage, setSelectedImage] = useState<any>();
  const imgRef = useRef<HTMLInputElement>(null);
  const [req, setReq] = useState<EquipmentRegisterData>({usefulEndHour:18,usefulBeginHour:9, pchrgAt:true, tkoutAt:true})

  const {addModal} = useGlobalModalStore()
  const [isSaleRegister, setIsSaleRegister] = useState(false)
  const [isSaleSelect, setIsSaleSelect] = useState(false)
  const [disCountRowList, setDiscountRowList] = useState<WithCustomRowData<DiscountData>[]>([])
  const [selectedDiscount, setSelectedDiscount] = useState<string[]>([]);
  const {data} = EquipmentCategoryService.getRoot();
  const [lastCategoryLabel, SetLastCategoryLabel] = React.useState<string[]>([])

  const sort = (depth: number, dest: EquipmentClassifyData[], source: EquipmentClassifyData[]) => {
    source.map(m => {
      if (m.depth === depth) {
        if (m.useAt) {
          dest.push(m);
        }
      }
    })
  }
  const findId = (dest: EquipmentClassifyData[], name: string) => {
    return dest.filter(f => f.eqpmnClNm === name).flatMap(id => id.eqpmnClId)
  }
  const firstCategory: EquipmentClassifyData[] = [];
  const lastCategory: EquipmentClassifyData[] = [];

  if (data && data.list) {
    sort(1, firstCategory, data.list)
    sort(2, lastCategory, data.list)
  }

  const firstCategoryLabel: string[] = firstCategory.map((m) => {
    return m.eqpmnClNm;
  });

  const [clId, setClId] = useState<string>('')

  useEffect(() => {
    if (req && clId) {
      setReq({...req, eqpmnClId: clId})
    }

  },[clId])

  return <TitleContents title={"장비 등록"}>
    <Stack spacing={"40px"} component={"form"} id={"EquipmentRegist"}>
      <SubContents title={"기본정보"} maxHeight={'100%'}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableRow>
              <TableTextFieldCell
                division label={"자산번호"} required wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, assetsNo: text}))
              }}
              />
              <TableTextFieldCell
                division label={"장비명(국문)"} required
                wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, eqpmnNmKorean: text}))
              }}
              />
              <TableTextFieldCell
                label={"장비명(영문)"} wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} onChange={(text) => {
                setReq((state) => ({...req!, eqpmnNmEngl: text}))
              }}
              />
            </TableRow>
            <TableRow>
              <TableTextFieldCell
                division label={"모델명"} wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, modelNm: text}))
              }}
              />
              <TableDoubleSelectCell
                division
                label={'분류'} required
                firstSelectLabel={firstCategoryLabel}
                lastSelectLabel={lastCategoryLabel}
                onFirstClick={(selected: string) => {
                  const id = findId(firstCategory, selected)
                  setClId(id.at(0)!.toString())
                  // setReq((state) => ({...req!, eqpmnClfcId: id.at(0)!.toString()}))

                  firstCategory.find((f) => {
                    if (f.eqpmnClNm === selected) {
                      const eqpmnClfcNms: string[] = [];
                      lastCategory.map((m) => {
                        if (m.eqpmnLclasId === f.eqpmnClId) eqpmnClfcNms.push(m.eqpmnClNm)
                      });
                      SetLastCategoryLabel(eqpmnClfcNms)
                    }
                  })
                }}
                onLastClick={(selected: string) => {
                  const id = findId(lastCategory, selected)
                  setClId(id.at(0)!.toString())
                  // setReq((state) => ({...req!, eqpmnClId: id.at(0)!.toString()}))
                }}
              />
              <TableTextFieldCell
                label={"규격"} thWidth={"13%"}
                wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                onChange={(text) => {
                setReq((state) => ({...req!, eqpmnStndrd: text}))
              }}
              />
            </TableRow>
            <TableRow>
              <TableTextFieldCell
                label={"요약설명"}
                thWidth={"13%"} tdSpan={5} multiline
                wordCount={1000} wordLabel={'1000자를 넘길 수 없습니다.'} wordCountDisabled
                onChange={(text) => {
                setReq((state) => ({...req!, sumry: text}))
              }}
              />
            </TableRow>
          </Table>
        </TableContainer>
      </SubContents>

      <SubContents title={"제원 및 주요 구성품"}>
        <SimpleTextFiled
          row={6}
          multiline label={"내용"}
          defaultLabel={""} wordCount={1000}
          onChange={(text) => {
            setReq((state) => ({...req!, specComposition: text}));
          }}
        />
      </SubContents>

      <SubContents title={"보조기기"}>
        <SimpleTextFiled
          row={6}
          label={"내용"} wordCount={50}
          multiline defaultLabel={""}
          onChange={(text) => {
            setReq((state) => ({...req!, asstnMhrls: text}));
          }}
        />
      </SubContents>

      <SubContents title={"분야/용도"}>
        <SimpleTextFiled
          row={6}
          label={"내용"} wordCount={100}
          multiline defaultLabel={""}
          onChange={(text) => {
            setReq((state) => ({...req!, realmPrpos: text}));
          }}
        />
      </SubContents>

      <SubContents title={"이미지"}
                   required
                   rightContent={
                     <CustomButton
                       label={"등록"} type={"small"} color={"list"}
                       onClick={() => {
                         if (imgRef.current) imgRef.current.click()
                       }}
                     />
                   }>
        <input
          hidden ref={imgRef}
          type={"file"}
          accept='image/jpg,image/png,image/jpeg,image/gif'
          onChange={async (event: any) => {
            if (event.target.files && event.target.files.length > 0) {
              setSelectedImage(event.target.files[0])
              const img = new FormData();
              img.append("image", event.target.files[0])
              const response = await EquipmentInformationService.postEquipmentsUploadImage(img)

              if (!response.success) setSelectedImage(null)
              setReq((state) => ({...req!, imageId: response.value}))
            }
          }}
        />
        <Stack alignItems={"center"}>
          {selectedImage && <img src={URL.createObjectURL(selectedImage)} style={{width: "300px", height: "300px"}}/>}
        </Stack>
      </SubContents>

      <SubContents title={"상세정보"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableRow>
              <TableRadioCell
                row division label={"전원"}
                radioLabel={["220v", "110v"]}
                defaultLabel={""}
                thWidth={"13%"} tdWidth={"21%"}
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, srcelct: selected }))
                }}
              />
              <TableRadioCell
                row division label={"메뉴얼"}
                radioLabel={["있음", "없음"]}
                defaultLabel={""}
                thWidth={"13%"} tdWidth={"21%"}
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, mnlAt: selected === "있음"}))
                }}
              />
              <TableRadioCell
                row label={"소프트웨어"}
                radioLabel={["있음", "없음"]}
                defaultLabel={""}
                thWidth={"13%"}
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, swAt: selected === "있음"}))
                }}
              />
            </TableRow>

            <TableRow>
              <TableTextFieldCell
                division label={"기존설치장소"} defaultLabel={""}
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, legacyItlpc: text}))
              }}
              />
              <TableRadioCell
                division row label={"유무료"}
                radioLabel={["유료", "무료"]}
                defaultLabel={""}
                thWidth={"13%"} tdWidth={"21%"}
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, pchrgAt: selected === "유료"}))
                }}
              />
              <TableRadioCell
              row label={"반출여부"}
              radioLabel={["가능", "불가"]}
              defaultLabel={""}
              thWidth={"13%"}
              onClick={(selected: string) => {
              setReq((state) => ({...req!, tkoutAt: selected === "가능"}))
            }}
              />
            </TableRow>

            <TableRow>
              <TableTextFieldCell
                label={"특기사항"} defaultLabel={""}
                thWidth={"13%"} tdSpan={5} onChange={(text) => {
                setReq((state) => ({...req!, spcmnt: text}))
              }}
              />
            </TableRow>
          </Table>
        </TableContainer>
      </SubContents>
      <SubContents
        title={"할인조건"}
        rightContent={
          <>
            {req.pchrgAt === true &&
          <Stack flexDirection={"row"}>
            <CustomButton type={"small"} color={"list"} label={"삭제"} onClick={() => {
              setDiscountRowList(disCountRowList.filter(f => !selectedDiscount.includes(f.key)))
            }}/>
            <HorizontalInterval size={"10px"}/>
            <CustomButton type={"small"} color={"list"} label={"추가"} onClick={() => {
              setIsSaleRegister(true)
            }}
            />
            <HorizontalInterval size={"10px"}/>
            <CustomButton type={"small"} color={"list"} label={"선택"} onClick={() => {
              setIsSaleSelect(true)
            }}/>
          </Stack>}
          </>
        }>
        <TableComponents<DiscountData>
          isCheckBox hidePagination hideRowPerPage
          page={0}
          rowCount={0}
          rowsPerPage={0}
          headCells={headCells}
          bodyRows={disCountRowList}
          onSelectedKey={(keys: string[]) => {
            setSelectedDiscount(keys)
          }}
          tableCell={(data) => {

            if (!data) return <></>

            return <Fragment>
              <TableCell key={"dscntResn-" + data.key} width={'80%'}>{data?.dscntResn}</TableCell>
              <TableCell key={"dscntRate-" + data.key} width={'15%'}>{data?.dscntRate}</TableCell>
            </Fragment>
          }}
        />
      </SubContents>
      <SubContents title={"AS정보"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableRow>
              <TableTextFieldCell
                division label={"AS업체명"} defaultLabel={""} wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, asEntrprsNm: text}))
              }}
              />
              <TableTextFieldCell
                division label={"AS담당자명"} defaultLabel={""} wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, asChargerNm: text}))
              }}
              />
              <TableTextFieldCell
                label={"AS연락처"} defaultLabel={""}
                onlyNumber placeholder={'숫자만 입력해주세요.'} regexPattern={/[^0-9.]/g}
                thWidth={"13%"} onChange={(text) => {
                setReq((state) => ({...req!, asChargerCttpc: text}))
              }}
              />
            </TableRow>
          </Table>
        </TableContainer>
      </SubContents>
      <SubContents title={"구입정보"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableRow>
              <TableDateCell
                division label={"구입일자"} defaultTime={""}
                thWidth={"13%"} tdWidth={"21%"} onChange={(date) => {
                setReq((state) => ({...req!, purchsDt: date.getTime()}))
              }}
              />
              <TableTextFieldCell
                label={"구입처"} defaultLabel={""} wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, strNm: text}))
              }}
              />
            </TableRow>
            <TableRow>
              <TableTextFieldCell
                division label={"구입가격"}
                numbercount
                defaultLabel={""}
                regexPattern={/[^0-9.]/g}
                placeholder={'숫자만 입력해주세요.'}
                additionDirection={'row'}
                additionContent={<Fragment>
                  <HorizontalInterval size={'15px'}/>
                  <Box width={'50px'}>{'원'}</Box>
                </Fragment>}
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, purchsPc: Number(text)}))
              }}
              />
              <TableTextFieldCell
                label={"제조사(국)"}
                defaultLabel={""} wordCount={50} wordLabel={'50자를 넘길 수 없습니다.'} wordCountDisabled
                thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setReq((state) => ({...req!, makr: text}))
              }}
              />
            </TableRow>
          </Table>
        </TableContainer>
      </SubContents>
      <SubContents title={"상세설정"}>
        <TableContainer style={{borderTop: "1px solid #d7dae6"}}>
          <Table>
            <TableRow>
              <TableTextFieldCell
                division label={"1시간 사용료"}
                required={!!req.pchrgAt}
                disabled={!req.pchrgAt}
                numbercount
                regexPattern={/[^0-9.]/g}
                defaultLabel={req.pchrgAt ? "" : '0'}
                additionDirection={'row'}
                additionContent={<Fragment>
                  <HorizontalInterval size={'15px'}/>
                  <Box width={'50px'}>{'원'}</Box>
                </Fragment>}
                thWidth={"16%"} tdWidth={"34%"} onChange={(text) => {
                setReq((state) => ({...req!, rntfeeHour: req.pchrgAt ? Number(text) : Number(0)}))
              }}
              />
              <TableDateTermCell
                type={"Time"} label={"1일 가용시간"} required
                onChange={(beginTime: Date | string, endTime: Date | string) => {
                  setReq((state) => ({
                    ...state!,
                    usefulBeginHour: new Date(beginTime).getHours(),
                    usefulEndHour: new Date(endTime).getHours()
                  }))
                }}
                thWidth={"16%"} tdWidth={"34%"}
              />
            </TableRow>
            <TableRow>
              <TableRadioCell
                row label={"사용률 저조장비"}
                radioLabel={["설정", "설정안함"]}
                defaultLabel={"설정안함"}
                thWidth={"16%"}
                division
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, useRateInctvSetupAt: selected === "설정"}))
                }}
              />
              <TableRadioCell
                row label={"법정공휴일 휴일포함"}
                radioLabel={["포함", "포함안함"]}
                defaultLabel={"포함안함"}
                thWidth={"16%"}
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, hldyInclsAt: selected === "포함"}))
                }}
              />
            </TableRow>
            <TableRow>
              <TableRadioCell
                row label={"반출 시 휴일 포함"}
                radioLabel={["포함", "포함안함"]}
                defaultLabel={"포함안함"}
                thWidth={"16%"}
                division
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, tkoutHldyInclsAt: selected === "포함"}))
                }}
              />
              <TableRadioCell
                row label={"미반출 시 휴일포함"}
                radioLabel={["포함", "포함안함"]}
                defaultLabel={"포함안함"}
                thWidth={"16%"}
                onClick={(selected: string) => {
                  setReq((state) => ({...req!, nttkoutHldyInclsAt: selected === "포함"}))
                }}
              />
            </TableRow>
          </Table>
        </TableContainer>
      </SubContents>
    </Stack>

    <Stack flexDirection={"row"} justifyContent={"space-between"} style={{width: "100%", marginTop: "40px"}}>
      <CustomButton
        label={"목록"} type={"largeList"} color={"outlined"}
        onClick={() => {
          navigate(-1)
        }}
      />
      <Stack flexDirection={"row"}>
        <CustomButton
          label={"저장"}
          onClick={async () => {
            if (checkValidity('EquipmentRegist')) {
              if (!req?.imageId) {
                addModal({open: true, isDist: true, content: '이미지를 등록해주세요.'})
                return
              }
              if (!req.usefulBeginHour || !req.usefulEndHour){
                addModal({open: true, isDist: true, content: '1일 가용시간을 설정해주세요.'})
                return
              }
              if (req.usefulBeginHour === 12 || req.usefulEndHour === 13) {
                addModal({open: true, isDist: true, content: '점심시간(12시~13시)에 시작하거나 종료할 수 없습니다.'})
                return
              }
              const dataResponse = await EquipmentInformationService.postEquipment({
                ...req!,
                srcelct: !req.srcelct ? "220v" : req.srcelct,
                dscntId: disCountRowList.map(m => {
                  return m.dscntId
                })
              })
              if (dataResponse.success) {
                addModal({
                  open: true, isDist: true,
                  content: '등록완료.',
                  onConfirm: () => {
                    navigate(-1)
                  }
                })
              }
            }
          }}
        />
      </Stack>
    </Stack>

    {
      isSaleRegister && <SaleRegisterModal
        open onClose={() => {
        setIsSaleRegister(false)
      }}/>
    }
    {
      isSaleSelect && <SaleSelectModal
        open onSelect={(data: WithCustomRowData<DiscountData>[]) => {
        setDiscountRowList(data)
        setIsSaleSelect(false)
      }}
        onClose={() => {
          setIsSaleSelect(false)
        }}/>
    }
  </TitleContents>
}

const headCells: CustomHeadCell<DiscountData & { number: number }>[] = [
  {
    id: 'dscntResn',
    align: 'center',
    label: '할인조건',
  },
  {
    id: 'dscntRate',
    align: "center",
    label: '할인율(%)',
  },
];

export default EquipmentRegist;