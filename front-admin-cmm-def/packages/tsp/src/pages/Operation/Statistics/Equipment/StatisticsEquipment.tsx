import React, {Dispatch, SetStateAction, useEffect, useState} from 'react'
import {LoadingProgress, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {SearchParam} from "~/service/Model";
import {Box, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {
  SearchTable,
  TableDateTermCell,
} from "shared/components/TableComponents";
import {dayFormat, todayEndTime, todayTime} from "shared/utils/stringUtils";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {Icons} from "shared/components/IconContainer";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 실증장비통계 */
const StatisticsEquipment = () => {
  const [searchParam, setSearchParam] = useState<SearchParam>()

  return <TitleContents title={"실증장비통계"}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={"30px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<SearchParam | undefined>>
}> = props => {
  const [search, setSearch] = useState(false)
  const [searchParam, setSearchParam] = useState<SearchParam>()
  const {addModal} = useGlobalModalStore()

  useEffect(() => {
    if (search) {
      props.setSearch(searchParam!)
      // if (props.onClick) props.onClick(searchParam!)
      setSearch(false)
    }
  }, [search])

  return <Box sx={{
    border: "1px solid #d7dae6",
    borderRadius: "20px",
  }}>
    <TableContainer>
      <SearchTable>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              label={"검색일"}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(beginTime, endTime) => {
                if (endTime) {
                  if (beginTime > endTime) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(beginTime))}\n종료일 : ${dayFormat(Number(endTime))}\n검색일자를 다시 입력 바랍니다.`
                    })
                  }
                }
                setSearchParam({...searchParam, BeginDt: todayTime(new Date(beginTime)), EndDt: todayEndTime(new Date(endTime))})
              }}/>
          </TableRow>
          <TableRow>
            <TableCell colSpan={4} style={{textAlign: "center", borderBottom: "none"}}>
              <CustomButton label={"검색"} onClick={() => {
                if ((searchParam?.BeginDt && !searchParam?.EndDt) || (!searchParam?.BeginDt && searchParam?.EndDt)) {
                  addModal({open: true, isDist:true, content:'신청일자를 확인해주세요.'})
                  return
                }
                setSearch(true);
              }}/>
            </TableCell>
          </TableRow>
        </TableBody>
      </SearchTable>
    </TableContainer>
  </Box>
}

const ListView: React.FC<{
  searchParam?: SearchParam
}> = props => {
  const info = EquipmentService.getStatisticsList({...props.searchParam})

  if(!info.data) return <></>

  return <Box sx={{
    border: "1px solid #d7dae6",
    borderRadius: "20px"
  }}>
    <Box sx={{textAlign: 'end', padding:'10px 30px 0px 0px'}}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          await EquipmentService.getStatisticsExcelDownload().then((res) => {
            const blob = new Blob([res]);
            const fileObjectUrl = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = fileObjectUrl;
            link.setAttribute(
              "download",
              `실증장비통계_리스트.xlsx`
            );
            document.body.appendChild(link);
            link.click();
          });
        }}
      />
    </Box>
    <Box sx={{
      display: 'flex',
      padding: '10px 30px 30px 30px',
      justifyContent: 'space-between',
    }}>
      {
        info.data.statistics.map((m) => {return <Box sx={{width: '30%', border: '1px solid #d7dae6', borderRadius: "20px",}}>
          <h2 style={{fontSize: '1.625rem', textAlign: 'center'}}>
            {m.title}
          </h2>
          <h2 style={{fontSize: '3.5rem', textAlign: 'center', marginBottom: '50px', paddingLeft: '30px'}}>
            {m.rate} %{/*<span style={{fontSize: '1.5rem', marginLeft: '30px'}}>%</span>*/}
          </h2>
        </Box>
        })
      }

      <Box sx={{width: '30%', border: '1px solid #d7dae6', borderRadius: "20px"}}>
        <h2 style={{fontSize: '1.625rem', textAlign: 'center'}}>
          장비 사용 기업수
        </h2>
        <h2 style={{fontSize: '3.5rem', textAlign: 'center', marginBottom: '50px', paddingLeft:'30px'}}>
          {info.data.entrprsCount} 건{/*<span style={{fontSize: '1.5rem', marginLeft: '30px'}}>건</span>*/}
        </h2>
      </Box>
    </Box>
  </Box>
}

export default StatisticsEquipment