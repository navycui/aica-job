import * as React from "react";
import {styled} from "@mui/material/styles";
import {
  DateRangePickerDay as MuiDateRangePickerDay,
  DateRangePickerDayProps
} from "@mui/x-date-pickers-pro/DateRangePickerDay";
import {DateRange, DateRangePicker, DesktopDateRangePicker, StaticDateRangePicker} from "@mui/x-date-pickers-pro";
import {Fragment, useEffect, useState} from "react";
import {Box, debounce, FormControl, Select, Stack, TextField} from "@mui/material";
import {AdapterDateFns} from '@mui/x-date-pickers/AdapterDateFns';
import {LocalizationProvider} from "@mui/x-date-pickers-pro";
import {HorizontalInterval, VerticalInterval} from "shared/components/LayoutComponents";
import {dayFormat} from "shared/utils/stringUtils";
import koLocale from 'date-fns/locale/ko'
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {random} from "shared/utils/str";
import {CustomButton} from "shared/components/ButtonComponents";
import {MuiTextFieldProps} from "@mui/x-date-pickers/internals";

export const UseEquipmentExtendPeriodView: React.FC<{
  onClickSave: () => void
  onClickList: () => void
}> = props => {
  const [value, setValue] = React.useState<DateRange<Date>>([null, null]);
  const [startTime, setStartTime] = useState<string>("")
  const [endTime, setEndTime] = useState<string>("")
  const disableTime = ["2022-06-24", "2022-06-25", "2022-06-26"]
  const splitTime = ["2022-06-01", "2022-06-06"]
  const {addModal} = useGlobalModalStore()
  const [calendarSize, setCalendarSize] = useState<{ width: number, dateSize: number, fontSize: number }>({
    width: window.innerWidth,
    dateSize: window.innerWidth / 20,
    fontSize: window.innerWidth / 1350
  })

  // 달력 사이즈 조절.
  const handlerResize = debounce(() => {
    let dateSize = window.innerWidth / 20
    dateSize = dateSize < 60 ? 60 : dateSize
    dateSize = dateSize > 80 ? 80 : dateSize

    setCalendarSize({
      width: window.innerWidth,
      dateSize: dateSize,
      fontSize: dateSize / 67.5
    })
  }, 100)

  useEffect(() => {
    window.addEventListener("resize", handlerResize)
    return () => {
      window.removeEventListener("resize", handlerResize)
    }
  })

  const renderWeekPickerDay = (
    date: Date,
    dateRangePickerDayProps: DateRangePickerDayProps<Date>
  ) => {
    const day = dayFormat(date.getTime())

    if (splitTime.includes(day) && !dateRangePickerDayProps.selected) {
      return <Box key={random().toString()} sx={{position: "relative"}}>
        <Box sx={{
          position: 'absolute',
          height: 0,
          width: 0,
          border: '2px solid',
          borderRadius: 4,
          borderColor: "red",
          right: '50%',
          transform: 'translateX(1px)',
          top: '80%'
        }}/>
        {/*@ts-ignore*/}
        <DateRangePickerDay {...dateRangePickerDayProps} disabled={disableTime.includes(day)}/>
      </Box>
    }

    //@ts-ignore
    return <DateRangePickerDay
      key={random().toString()}
      {...dateRangePickerDayProps}
      disabled={disableTime.includes(day)}/>
  }

  return <Stack alignItems={"center"}>
    <LocalizationProvider dateAdapter={AdapterDateFns} locale={koLocale}>
      <Stack flexDirection={"row"} sx={{maxWidth: 1280}}>
        <Box sx={{
          "& .css-1tape97": {
            width: "100%",
          },
          "& > div": {
            width: "100%"
          },
          "& > div > div, & > div > div > div, & .MuiCalendarPicker-root": {
            width: "100%"
          },
          "& .MuiTypography-root": {
            fontSize: `${calendarSize.fontSize}rem`
          },
          "& .MuiTypography-caption": {
            width: calendarSize.dateSize * 1.1,
            margin: 0,
            fontSize: `${calendarSize.fontSize}rem`
          },
          "& .PrivatePickersSlideTransition-root": {
            minHeight: calendarSize.dateSize * 7
          },
          '& .PrivatePickersSlideTransition-root [role="row"]': {
            margin: 0
          },
          "& .MuiPickersDay-dayWithMargin": {
            margin: 0
          },
          "& .MuiPickersDay-root": {
            width: calendarSize.dateSize,
            height: calendarSize.dateSize,
            fontSize: `${calendarSize.fontSize}rem`
          }
        }}>
          <StaticDateRangePicker
            displayStaticWrapperAs={"desktop"}
            calendars={1}
            value={value}
            showToolbar={false}
            inputFormat={"yyyy-MM-dd"}
            shouldDisableDate={(date) => {
              return false;
            }}
            onChange={(newValue) => {
              // 사용 불가 일정이 있을시 하루전 까지 이용 신청 되도록 예외 처리.
              if (newValue[0] && newValue[1]) {
                const startTime = newValue[0].getTime()
                const endTime = newValue[1].getTime()
                disableTime.some(s => {
                  const compare = new Date(s)
                  if (startTime <= compare.getTime() && compare.getTime() < endTime) {
                    newValue[1] = new Date(compare.setDate(compare.getDate() - 1))
                    addModal({open: true, content: `${s}일은 사용 불가 합니다.`})
                    return true;
                  }
                })

                splitTime.some(s => {
                  const compare = new Date(s)
                  if (startTime <= compare.getTime() && compare.getTime() < endTime) {
                    if (dayFormat(startTime) === dayFormat(compare.getTime())) return false;
                    newValue[1] = new Date(compare.setDate(compare.getDate()))
                    addModal({open: true, content: `${s}일 까지 사용 가능 합니다.`})
                    return true;
                  }
                })
              }

              setValue(newValue)
              setStartTime(newValue[0] ? dayFormat(newValue[0].getTime()) : "")
              setEndTime(newValue[1] ? dayFormat(newValue[1].getTime()) : "")
            }}
            renderDay={renderWeekPickerDay}
            renderInput={(startProps:MuiTextFieldProps, endProps: MuiTextFieldProps) => {
              return <React.Fragment>
                <TextField {...startProps} />
                <Box sx={{mx: 2}}> to </Box>
                <TextField {...endProps} />
              </React.Fragment>
            }}
          />
        </Box>
        <HorizontalInterval size={"100px"}/>
        <Box sx={{width: "100%"}}>
          <h3>사용시간을 선택해주세요.</h3>
          <Stack sx={{borderTop: "1px solid #d7dae6", paddingTop: "20px"}}>
            <DaySelectContents title={"시작일"} date={startTime} selectTime={["10시", "11시"]}/>
            <VerticalInterval size={"30px"}/>
            <DaySelectContents title={"종료일"} date={endTime} selectTime={["10시", "11시"]}/>
            <Stack flexDirection={"row"} alignItems={"center"} marginY={"15px"} justifyContent={"space-between"}>
              <span style={{minWidth: "60px"}}>사용금액</span>
              {/*<HorizontalInterval size={"82px"}/>*/}
              <Box sx={{display: "flex", alignItems: "center"}}>
                <TextField
                  sx={{pr: "5px", minWidth: "65px"}}
                  size={"small"} type={"number"}
                  inputProps={{style: {textAlign: 'right'}}}
                />
                <span>원</span>
              </Box>
            </Stack>
          </Stack>

          <Stack flexDirection={"row"} alignItems={"start"} marginY={"15px"}>
            <span style={{minWidth: "60px"}}>사유</span>
            <HorizontalInterval size={"82px"}/>
            <FormControl fullWidth>
              <TextField
                sx={{minWidth: "200px",}}
                size={"small"} multiline
                inputProps={{style: {height: "130px", overflow: "auto"}}}
              />
            </FormControl>
          </Stack>
        </Box>
      </Stack>
    </LocalizationProvider>

    <Stack flexDirection={"row"} justifyContent={"center"}>
      <CustomButton label={"목록"} color={"outlined"} onClick={() => {
        props.onClickList()
      }}/>
      <HorizontalInterval size={"20px"}/>
      <CustomButton label={"저장"} onClick={() => {
        addModal({
          open: true, isDist: true,
          content: "기간 연장 되었습니다.",
          onConfirm: props.onClickSave,
          onClose: props.onClickSave,
        })
      }}/>
    </Stack>
  </Stack>
}

const DaySelectContents: React.FC<{
  title: string,
  date: string,
  selectTime: string[]
  onChange?: (hour: string, minute: string) => void
}> = props => {
  return <Box>
    <span>{props.title}</span>
    <VerticalInterval size={"10px"}/>
    <Stack flexWrap={"wrap"} flexDirection={"row"} alignItems={"center"} justifyContent={"space-between"}>
      <span style={{marginTop: "15px"}}>{props.date}</span>
      <HorizontalInterval size={"60px"}/>
      <Stack flexDirection={"row"} alignItems={"center"} mt={"15px"}>
        <Select size={"small"} sx={{minWidth: "140px"}} value={props.selectTime}/>
        <Box sx={{display: "flex", alignItems: "center"}}>
        <TextField size={"small"} sx={{maxWidth: "100px", minWidth: "60px", paddingX: "5px"}} value={""}/>
        <span>분</span>
        </Box>
      </Stack>
    </Stack>
  </Box>
}

const DateRangePickerDay = styled(MuiDateRangePickerDay)(
  ({theme, isHighlighting, isStartOfHighlighting, isEndOfHighlighting}) => ({
    ...(isHighlighting && {
      borderRadius: 0,
      backgroundColor: "#32325",
      color: theme.palette.common.white,
      "&:hover, &:focus": {
        backgroundColor: theme.palette.primary.dark
      }
    }),
    ...(isStartOfHighlighting && {
      borderTopLeftRadius: "50%",
      borderBottomLeftRadius: "50%"
    }),
    ...(isEndOfHighlighting && {
      borderTopRightRadius: "50%",
      borderBottomRightRadius: "50%"
    })
  })
) as React.ComponentType<DateRangePickerDayProps<Date>>;