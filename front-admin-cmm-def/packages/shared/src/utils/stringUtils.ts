import dayjs from "../libs/dayjs";

export const dayFormat = (dt: number) => {
  return dayjs(dt).format('YYYY-MM-DD')
}

export const todayTime = (_date: Date) => {
  const date = new Date(_date)
  date.setHours(0)
  date.setMinutes(0)
  date.setSeconds(0)
  return date.getTime()
}

export const todayEndTime = (_date: Date) => {
  const date = new Date(_date)
  date.setHours(23)
  date.setMinutes(59)
  date.setSeconds(59)
  return date.getTime()
}

export const tomorrowTime = (date: Date) => {
  return todayTime(date) + (1000 * 60 * 60 * 24)
}

export const toTimeFormat = (dt: number, format?: string) => {
  return dayjs(dt).format(format? format : 'YYYY-MM-DD HH시 mm분')
}

export const toDayAndTimeFormat = (day: string, time: string) => {
  const leftPad = (value:number) => {
    if (value >= 10) return value
    else return `0${value}`
  }
  const h = leftPad(Number(time))
  //.날짜 형태를 맞춰주기 위함 (YYYYMMHH => YYYY-MM-HH).//
  if(day.length == 8) {
    const daySplit = day.slice(0,4) + "-" + day.slice(4,6) + "-" + day.slice(6,8)
    return `${daySplit} ${h}:00:00`
  }
  return `${day}T${h}:00:00`
}

export const toStringFullDayFormat = (date: Date) => {
  const leftPad = (value:number) => {
    if (value >= 10) return value
    else return `0${value}`
  }
  const year = date.getFullYear();
  const month = leftPad(date.getMonth() +1)
  const day = leftPad(date.getDate())
  return [year,month,day].join('-')
}

export const getUseTime = (beginDay: string, beginHour: string) => {
  const leftPad = (value:number) => {
    if (value >= 10) return value
    else return `0${value}`
  }
  const startDate = new Date(beginDay);
  startDate.setHours(Number(beginHour));
  const date = new Date;

  const diffDate = date.getTime() - startDate.getTime();
  const dateHours = Math.floor(Math.abs(diffDate / (1000 * 3600 * 24))) * 24;

  const hour = leftPad((dateHours + date.getHours()))
  const minutes = leftPad(date.getMinutes());
  return [hour,minutes].join(':')
}

// 분단위 숫자를 시간 : 분으로 변환
export const toStringTimeForMinutes = (time: number) => {
  return `${Math.floor(time / 60)} : ${time % 60}`
}

export const toStringForMinutes = (time: number) => {
  return time % 60 === 0 ? `${Math.floor(time / 60)}시간` : `${Math.floor(time / 60)}시간 ${time % 60}분`
}

export const phoneNumberFormat = (data:any) => {
  return data.toString().replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
}

export const byteToMega = (data:number) => {
  return data / 1024 / 1024
}