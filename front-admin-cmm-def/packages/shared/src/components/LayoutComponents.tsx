import React, {ChangeEvent, Dispatch, SetStateAction, useEffect, useLayoutEffect, useRef, useState} from 'react'
import Box from "@mui/material/Box";
import {
  Checkbox,
  CircularProgress,
  Grid,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Paper,
  Stack,
  Tab, Table, TableBody, TableCell, TableHead, TableRow,
  TextField
} from "@mui/material";
import {TabContext, TabList, TabPanel} from "@mui/lab";
import {Color} from "../components/StyleUtils";
import InputLabel from "@mui/material/InputLabel";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import MUIPagination from "@mui/material/Pagination";
import styled from '@emotion/styled';
import {css} from "@emotion/react";
import {CustomButton} from "../components/ButtonComponents";
import {CommonService} from 'tsp/src/service/CommonService'
import {AttachmentParam} from "tsp/src/service/Model";
import {WithCustomRowData} from "../components/TableComponents";

export const VerticalInterval: React.FC<{
  size: number | string;
}> = (props) => <div style={{marginTop: props.size}}/>;

export const HorizontalInterval: React.FC<{
  size: number | string;
}> = (props) => <div style={{marginLeft: props.size}}/>;

export const TitleContents: React.FC<{
  title?: string;
}> = props => {
  return <Box
    style={{
      width: "100%",
      padding: "30px 20px 80px 20px",
      minWidth: '1000px',
      overflow: 'hidden'
    }}>
    <h2 style={{fontSize: '1.625rem'}}>
      {props.title}
    </h2>
    {props.children}
  </Box>
}

export const SubContents: React.FC<{
  title: string
  subTitle?: string
  hideDivision?: boolean
  required?: boolean
  maxHeight?: string
  rightContent?: React.ReactNode;
}> = (props) => {
  return (
    <Box style={{width: "100%", borderTop: props.hideDivision ? "none" : "1px solid #4063ec"}}>
      <Stack
        flexDirection={'row'}
        justifyContent={'space-between'}
        alignItems={'center'}
        marginRight={'30px'}
      >
        {props.title && (
          <h2 style={{fontSize: '1.25rem', paddingLeft: "30px"}}>
            {props.title}
            {props.required && <em style={{paddingLeft: '3px', color: 'skyblue', fontSize: '16px'}}>*</em>}
            {props.subTitle && <span style={{paddingLeft: '15px', fontSize: '16px', fontWeight:300}}>({props.subTitle})</span>}
          </h2>
        )}
        {props.rightContent}
      </Stack>
      <Box
        sx={{
          maxHeight: props.maxHeight || '300px',
          overflow: 'auto',
          '&::-webkit-scrollbar': {
            width: '8px',
          },
          '&::-webkit-scrollbar-thumb': {
            backgroundColor: '#d7dae6',
            borderRadius: '10px'
          },
          '&::-webkit-scrollbar-track': {
            backgroundColor: '#fff',
            borderRadius: '10px'
          }
        }}
      >
        {props.children}
      </Box>
    </Box>
  );
};

export const SubAttachFileContents: React.FC<{
  hideDivision?: boolean
  required?: boolean
  title?: string
  subTitle?: string
  atchmnflGroupId?: string;
  atchmnfl?: AttachmentParam[];
  accept?: string
  edit?: boolean
  fileList?: WithCustomRowData<AttachmentParam>[]
  setFileList?: any
  fileSelect?: string[]
  setFileSelect?: any
}> = (props) => {
  const [isAllCheck, setIsAllCheck] = useState(false);
  const fileRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    if (props.fileSelect && props.fileList)
      if (props.fileList.length === props.fileSelect.length && props.fileList.length > 0) setIsAllCheck(true)
      else setIsAllCheck(false)
  }, [props.fileSelect, props.fileList])

  // @ts-ignore
  const handleSelectAllClick = (event) => {
    if (props.fileList && props.setFileSelect) {
      if (event.target.checked) {
        const newSelected = props.fileList.map((m) => m.key);
        props.setFileSelect(newSelected);
        return;
      }
      props.setFileSelect([]);
    }
  };

  const handleClick = (event: any, key: string) => {
    if (props.fileSelect && props.setFileSelect) {
      const selectedIndex = props.fileSelect.indexOf(key);
      let newSelected: string[] = [];

      if (selectedIndex === -1) {
        newSelected = newSelected.concat(props.fileSelect, key);
      } else if (selectedIndex === 0) {
        newSelected = newSelected.concat(props.fileSelect.slice(1));
      } else if (selectedIndex === props.fileSelect.length - 1) {
        newSelected = newSelected.concat(props.fileSelect.slice(0, -1));
      } else if (selectedIndex > 0) {
        newSelected = newSelected.concat(
          props.fileSelect.slice(0, selectedIndex),
          props.fileSelect.slice(selectedIndex + 1)
        );
      }
      props.setFileSelect(newSelected);
    }
  }
  return (
    <Box style={{width: "100%", borderTop: props.hideDivision ? "none" : "1px solid #4063ec"}}>
      <Stack
        flexDirection={'row'}
        justifyContent={'space-between'}
        alignItems={'center'}
        marginRight={'30px'}
      >
        <h2 style={{fontSize: '1.25rem', paddingLeft: "30px"}}>
          {props.title ? props.title : '첨부파일'}
          {props.required && <em style={{paddingLeft: '3px', color: 'skyblue', fontSize: '16px'}}>*</em>}
          {props.subTitle && <span style={{paddingLeft: '15px', fontSize: '16px', fontWeight:300}}>{props.subTitle}</span>}
        </h2>
        {props.edit ? <Box>
            <CustomButton
              label={"첨부"} type={"small"} color={"list"} style={{marginRight: '10px'}}
              onClick={() => {
                if (fileRef.current) fileRef.current.click()
              }}/>
            <input
              hidden ref={fileRef}
              type={"file"} accept={props.accept}
              //onChange={props.onUpload}
              onChange={(event: any) => {
                if (event.target.files && event.target.files.length > 0) {
                  const files: WithCustomRowData<AttachmentParam>[] = {
                    ...event.target.files[0],
                    fileNm: event.target.files[0].name,
                    fileSize: event.target.files[0].size,
                    fileInfo: event.target.files[0]
                  };
                  props.setFileList(props.fileList && props.fileList.concat({...files, key: Math.random().toString()}))
                  const file = new FormData();
                  file.append("file", event.target.files[0])
                }
              }}
            />
            <CustomButton
              label={"삭제"} type={"small"} color={"list"}
              //onClick={props.onDelete}
              onClick={() => {
                if (props.fileList && props.fileSelect && props.setFileSelect) {
                  props.setFileList(props.fileList.filter(f => !props.fileSelect?.includes(f.key)))
                  props.setFileSelect([])
                }
              }}
            />
          </Box> : props.atchmnfl ?
         <CustomButton
            label={"일괄다운로드"} type={"small"} color={"list"}
            onClick={async () => {
              window.open(`${process.env.REACT_APP_DOMAIN}/tsp-api/api/file-dwld-groupfiles/${props.atchmnflGroupId}`)
            }}/> : <Box></Box>
        }
      </Stack>
      <Box
        style={{
          maxHeight: '100%',
          overflow: 'auto',
        }}
      >
        <Table>
          <TableHead>
            <TableRow style={{borderTop:'1px solid #d7dae6'}}>
              {
                props.edit && <TableCell align={"center"} width={"70px"} style={{borderRight:'1px solid #d7dae6'}}>
                      <Checkbox checked={isAllCheck} onClick={(e) => handleSelectAllClick(e)}/>
                  </TableCell>
              }
              <TableCell align={"center"} width={"70px"} style={{borderRight:'1px solid #d7dae6', }}>번호</TableCell>
              <TableCell align={"center"} style={{borderRight:'1px solid #d7dae6'}}>파일명</TableCell>
              <TableCell align={"center"} width={"150px"} style={{borderRight:!props.edit ? '1px solid #d7dae6' : 'none'}}>용량</TableCell>
              {
                !props.edit && <TableCell align={"center"} width={"150px"}>다운로드</TableCell>
              }
            </TableRow>
          </TableHead>
          <TableBody>
            {
              !props.edit && props.atchmnfl && props.atchmnfl.map((m, i) => {
                return <>
                  <TableRow>
                    <TableCell align={"center"} style={{borderRight:'1px solid #d7dae6'}}>{i + 1}</TableCell>
                    <TableCell align={"center"} style={{borderRight:'1px solid #d7dae6'}}>{`${m.fileNm}`}</TableCell>
                    <TableCell align={"center"} style={{borderRight:'1px solid #d7dae6'}}>{`${(m.fileSize / 1024 / 1024).toFixed(1)}(MB)`}</TableCell>
                    <TableCell align={"center"}>
                      <CustomButton
                        type={"small"}
                        color={"list"}
                        label={"다운로드"}
                        onClick={() => {
                          window.open(`${process.env.REACT_APP_DOMAIN}/tsp-api/api/file-dwld-contentType/${m.attachmentId}`)
                        }}/>
                    </TableCell>
                  </TableRow>
                </>
              })
            }
            {
              props.edit && props.fileList && props.fileList.map((m, i) => {
                return <>
                  <TableRow style={{borderTop:'1px solid #d7dae6'}}>
                    <TableCell align={'center'} style={{borderRight:'1px solid #d7dae6'}}>
                      <Checkbox checked={props.fileSelect && props.fileSelect.includes(m.key)}
                                onClick={(e) => handleClick(e, m.key)}/>
                    </TableCell>
                    <TableCell align={"center"} style={{borderRight:'1px solid #d7dae6'}}>{i + 1}</TableCell>
                    <TableCell align={"center"} style={{borderRight:'1px solid #d7dae6'}}>{m.fileNm}</TableCell>
                    <TableCell align={"center"}>{`${(m.fileSize / 1024 / 1024).toFixed(1)}(MB)`}</TableCell>
                  </TableRow>
                </>
              })
            }
          </TableBody>
        </Table>
      </Box>
    </Box>
  );
};

export const BlockContents: React.FC<{
  title?: string;
  title_sub?: string;
  title_must?: string;
  maxHeight?: string;
  rightContent?: React.ReactNode;
}> = (props) => {
  return (
    <Box>
      <Stack
        flexDirection={'row'}
        justifyContent={'space-between'}
        alignItems={'center'}
        marginRight={'30px'}
      >
        {props.title && (
          <h2 style={{margin: '50px 0 20px 0', fontSize: '1.625rem'}}>
            {props.title}
          </h2>
        )}
        {props.title_sub && (
          <div
            style={{
              margin: '22px 30px',
              fontSize: '1.25rem',
              fontWeight: 'bold',
            }}
            className={props.title_must && props.title_must}
          >
            {props.title_sub}
          </div>
        )}
        {props.rightContent}
      </Stack>
      <Box
        style={{
          maxHeight: props.maxHeight || '300px',
          overflow: 'auto',
        }}
      >
        {props.children}
      </Box>
    </Box>
  );
};

export const CustomTabs: React.FC<{
  tabs: string[];
  hideBottomColor?: boolean
  onClick?: (newValue: string) => void;
}> = (props) => {
  const [value, setValue] = useState<string>(props.tabs[0]);

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    if (props.onClick) props.onClick(newValue);

    setValue(newValue);
  };

  return (
    <Box sx={{width: '100%', height: '100%', typography: 'body1'}}>
      <TabContext value={value}>
        <Box sx={{borderBottom: props.hideBottomColor ? 1 : 0, borderColor: Color.primary}}>
          <TabList onChange={handleChange} variant={'scrollable'}>
            {props.tabs.map((m, i) => {
              return <TabContainer key={i} label={m} value={m}/>;
            })}
          </TabList>
        </Box>
        {props.children}
      </TabContext>
    </Box>
  );
};

export const SimpleTextFiled: React.FC<{
  label?: string
  defaultLabel?: string
  multiline?: boolean
  wordCount?: number | any
  wordLabel?: string
  wordCountDisabled?: boolean
  required?: boolean
  row?: number
  onChange?: (text: string) => void
}> = props => {
  const [value, setValue] = useState(props.defaultLabel || '')
  useLayoutEffect(() => {
    if (!!props.defaultLabel) {
      setValue(props.defaultLabel)
    }
  }, [props.defaultLabel])

  return <Box style={{borderTop: "1px solid #d7dae6", borderBottom: "1px solid #d7dae6", marginTop: "-10px"}}>
    <Box style={{
      display: "flex", flexDirection:'column',
      margin: "16px 30px 30px 30px",
    }}>
      <FormControl fullWidth>
        <TextFieldContainer
          name={props.label || "내용"}
          required={props.required}
          value={value}
          rows={props.row}
          size={"small"}
          multiline={props.multiline}
          style={{}}
          onChange={(e) => {
            if (props.onChange)
              props.onChange(e.target.value)
            setValue(e.target.value)
            if (props.wordCount <= value.length) {
              const idx = (props.wordCount - value.length) < 0 ? props.wordCount - value.length : -1;
              setValue(value.slice(0, idx));
              alert(props.wordLabel || "제한글자를 지켜주세요")
            }
          }}/>
      </FormControl>
    {props.wordCount && !props.wordCountDisabled && <WordCount curWord={value.length} maxWord={props.wordCount}/>}
    </Box>
  </Box>
}

const TextFieldContainer = styled(TextField)`
  .MuiInputBase-root {
    height: 150px;
    align-items: baseline;
  }
`

const TabContainer = styled(Tab)({
  '&.Mui-selected': {
    background: Color.primary,
    color: Color.white,
  },
  border: '1px solid #a7aec9',
  borderTopLeftRadius: 10,
  borderTopRightRadius: 10,
  color: '#a7aec9',
  minWidth: "170px",
});

export const Pagination: React.FC<{
  curPage: number;
  totalPage: number;
  rowsPerPage: number;
  handleChangeRowsPerPage: (event: any) => void;
  handleChangePage: (event: unknown, newPage: number) => void;
  hideRowPerPage?: boolean;
  style?: React.CSSProperties;
  setCurPage?: Dispatch<SetStateAction<number>>
}> = (props) => {

  useEffect(() => {
    if (!!props.rowsPerPage)
      if (!!props.setCurPage)
        props.setCurPage(0)
  },[props.rowsPerPage])

  return <Box style={{paddingTop: "15px", ...props.style}}>
    {
      !props.hideRowPerPage && <Box
            sx={{display: 'flex', color: '#666'}}
            justifyContent={'right'}
            alignItems={'center'}
        >
            <InputLabel
                variant="standard"
                htmlFor="uncontrolled-native"
                sx={{pr: 2}}
            >
                Page {props.curPage + 1} / {props.totalPage}
            </InputLabel>
            <FormControl variant="standard">
                <SelectStyle
                    className="select"
                    value={props.rowsPerPage}
                    onChange={props.handleChangeRowsPerPage}
                >
                  {[10, 15, 20].map((v: any) => (
                    <MenuItem key={v} value={v}>
                      {v}개씩
                    </MenuItem>
                  ))}
                </SelectStyle>
            </FormControl>
        </Box>
    }
    <MUIPagination
      shape="rounded"
      // color="black"
      count={props.totalPage}
      showFirstButton
      showLastButton
      page={props.curPage + 1}
      onChange={props.handleChangePage}
      style={{display: 'flex', justifyContent: 'center'}}
    />
  </Box>
}

export const LnbListItem: React.FC<{
  id: string;
  label: string;
  depth: number;
  url: string;
  isChild: boolean;
  active: boolean;
  icon?: React.ReactNode;
  onClick?: (id: string, url: string, isChild: boolean) => void;
}> = (props) => {
  const className = props.active ? "active" : undefined

  return <ListItemButton
    className={className}
    onClick={() => {
      if (props.onClick) props.onClick(props.id, props.url, props.isChild)
    }}>
    {
      props.icon && <ListItemIcon>
        {props.icon}
        </ListItemIcon>
    }
    <ListItemText primary={
      props.label
    }/>
  </ListItemButton>
}

const SelectStyle = styled(Select)`
  border: 1px solid #d7dae6;
  width: 121px;
  height: 40px;
  padding: 0;

  .MuiSelect-select {
    padding: 0 20px 0 16px;
    height: 40px;
    line-height: 40px;
  }

  .MuiSvgIcon-root {
    margin-right: 10px;
  }
`;

export interface DashboardMain {
  title: string,
  value: number,
  items: DashboardItem[]
}

export interface DashboardItem {
  itemTitle: string,
  itemSubtitle: string,
  itemValue: number
}

export const DashboardCountContents: React.FC<{
  dashboardMain: DashboardMain[]
}> = props => {
  return (
    <Grid
      container
      justifyContent="flex-start"
      spacing={2}
    >
      {props.dashboardMain.map((mainData, index) => {
        return (
          <Grid
            item
            xs={12}
            sm={6} md={4}
            key={index}
            css={DashboardStyle}
          >
            <Paper className="DashboardPaper">
              <div className="DashboardMainTitle">
                {mainData.title}
                <span className="DashboardMainValue">
                           {mainData.value}
                            </span>
                <span className="DashboardMainValueText">
                                건
                            </span>
              </div>
              {mainData.items.map((dashboardItem, index) => {
                return (
                  <DashboardItemContents
                    itemTitle={dashboardItem.itemTitle}
                    itemSubtitle={dashboardItem.itemSubtitle}
                    itemValue={dashboardItem.itemValue}
                    key={index}
                  />
                )
              })}
            </Paper>
          </Grid>
        )
      })}
    </Grid>
  );
}

export const DashboardItemContents: React.FC<{
  itemTitle: string,
  itemSubtitle: string,
  itemValue: number,
  key: number
}> = props => {
  return (<Box>
      <Box className="DashboardItemTitle">
        {props.itemTitle}
      </Box>

      <Box className="DashboardItemBox">
        <Box className="DashboardItemSubTitle">
          {props.itemSubtitle}
        </Box>
        <Box className="DashboardItemValueText">
                  <span className="DashboardItemValue">
                      {props.itemValue}
                  </span>
          건
        </Box>
      </Box>
    </Box>
  );
}

export const LoadingProgress = () => {
  return <Box display={'flex'} height={'100%'} alignItems={'center'} justifyContent={'center'}>
    <CircularProgress/>
  </Box>
}

export const WordCount: React.FC<{
  curWord: number
  maxWord: string | number
  style?: React.CSSProperties
}> = props => {
  return <Box display={"flex"} justifyContent={"right"}
              style={{...props.style}}>
    <span style={{height: '19px', minWidth: '52px', fontSize: '14px', textAlign: 'end'}}>
      {props.curWord + "/" + props.maxWord}</span>
  </Box>
}

// export const AttachFileDown: React.FC<{
//     title: string;
//     label: string;
//     fileNm?: string;
//     vols?: string;
//     onClick?: string;
//     seq?: string
//     rightContent?: string
// }> = props => {
//
//     const [rowList, setRowList] = useState<WithCustomRowData<TableData>[]>();
//     const [pagination, setPagination] = useState({
//         page: 0,
//         rowPerpage: 5,
//         rowCount: 0
//
//
//
//
// export interface TableData {
//     assetNo: string
//     seq: string
//     fileNM: string
//     vols: string
// }
//
// const headCells: CustomHeadCell<TableData>[] = [
//     {
//         id: 'seq',
//         align: 'center',
//         label: '번호',
//     },
//     {
//         id: 'fileNM',
//         align: 'center',
//         label: '신청상태',
//     },
//     {
//         id: 'vols',
//         align: "center",
//         label: '구분',
//     }
// ];


//
//     return (
//         <SubContents title={props.title}
//                      rightContent={
//                          <CustomButton
//                              label={"일괄다운로드"} type={"small"} color={"list"}
//                              onClick={() => {
//                              }}/>}>
//                 <Table>
//                 <TableRow>
//                     <TableCell align={"center"} width={"70px"}>
//                         {"번호"}
//                     </TableCell>
//                     <TableCell align={"center"}>
//                         {"파일명"}
//                     </TableCell>
//                     <TableCell align={"center"} width={"150px"}>
//                         {"용량"}
//                     </TableCell>
//                     <TableCell align={"center"} width={"150px"}>
//                         {"다운로드"}
//                     </TableCell>
//                 </TableRow>
//                 <TableRow>
//                     <TableCell align={"center"} width={"70px"}>
//                         {props.seq}
//                     </TableCell>
//                     <TableCell align={"center"}>
//                         {props.fileNm}
//                     </TableCell>
//                     <TableCell align={"center"} width={"150px"}>
//                         {props.vols}
//                     </TableCell>
//                     <TableCell align={"center"} width={"150px"}>
//                         <CustomButton label={"다운로드"} type={"small"}
//                                       color={"list"}/>
//                     </TableCell>
//                 </TableRow>
//             </Table>
//         </SubContents>
//     )
//}

const DashboardStyle = css`
  .DashboardPaper {
    height: 100%;
    border-radius: 20px;
    border: solid 1px #d7dae6;
  }

  .DashboardMainTitle {
    padding: 30px 30px 20px 30px;
    font-family: NotoSansCJKKR;
    font-size: 20px;
    font-weight: bold;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: left;
    color: #222222;
  }

  .DashboardMainValue {
    padding: 0 4px;
    font-family: Roboto;
    font-size: 20px;
    font-weight: bold;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: left;
    color: #4063ec;
  }

  .DashboardMainValueText {
    font-family: NotoSansCJKKR;
    font-size: 16px;
    font-weight: normal;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: left;
    color: #222222;
  }

  .DashboardItemTitle {
    padding: 0 30px 10px 30px;
    font-family: NotoSansCJKKR;
    font-size: 16px;
    font-weight: 500;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: left;
    color: #222222
  }

  .DashboardItemBox {
    align-items: center;
    padding: 20px 30px;
    margin: 0 20px 20px 20px;
    border-radius: 10px;
    background-color: #f5f5f5;
    display: flex;
    justify-content: space-between
  }

  .DashboardItemSubTitle {
    font-family: NotoSansCJKKR;
    font-size: 16px;
    font-weight: normal;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: left;
    color: #707070
  }

  .DashboardItemValueText {
    font-family: NotoSansCJKKR;
    font-size: 18px;
    font-weight: normal;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: right;
    color: #222222
  }

  .DashboardItemValue {
    padding: 0 4px;
    font-family: NotoSansCJKKR;
    font-size: 30px;
    font-weight: normal;
    font-stretch: normal;
    font-style: normal;
    line-height: normal;
    letter-spacing: normal;
    text-align: right;
    color: #222222
  }
`;