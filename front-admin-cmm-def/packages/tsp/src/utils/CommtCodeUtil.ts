import {CodeGroup, CommonCode} from "~/service/Model";

export const CommtCodeNms = (data: CommonCode, groupName: CodeGroup) => {
  return data.codeGroup[groupName].codeGroup.map( m => m.codeNm)
}

export const CommtCodes = (data: CommonCode, groupName: CodeGroup) => {
  return data.codeGroup[groupName].codeGroup.map( m => m.code)
}

export const toCommtCode = (data: CommonCode | undefined, groupName: CodeGroup, codeNm: string) => {
  return data?.codeGroup[groupName].codeGroup.find(f => f.codeNm === codeNm)?.code || ''
}

export const toCommtCodeName = (data: CommonCode | undefined, groupName: CodeGroup, code: string) => {
  return data?.codeGroup[groupName].codeGroup.find(f => f.code === code)?.codeNm || ''
}